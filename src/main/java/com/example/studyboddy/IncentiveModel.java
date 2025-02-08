package com.example.studyboddy;

import javafx.event.ActionEvent;
import javafx.scene.control.CheckBox;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class IncentiveModel implements iModelListener, ModelListener {

    Date today;

    ArrayList<DailyGoal> goalList;
    String[] goals = {"Study for 30 minutes", "Go over 3 flash card sets", "Complete 1 task", "Complete a flash card set without making a mistake", "Study with a friend", "Create a task", "Work on an assignment for 30 minutes", "Practice your oldest flashcard set", "Take a 15 minute break"};

    ArrayList<ModelListener> subscribers;
    TaskManager taskModel;

    int numOfCompletedTasks;
    int numOfCompletedDailyGoals;
    int numOfFlashCardSets;
    int numOfFlashCardTotal;
    Boolean[] dailyGoalCompletion = {false, false, false};

    public IncentiveModel() {
        subscribers = new ArrayList<>();

        goalList = new ArrayList<>();

        for(String goal : goals) {
            DailyGoal newGoal = new DailyGoal(goal);
            newGoal.setOnAction(this::checkBoxHandler);
            goalList.add(newGoal);
        }

        today = new Date();

        //Reads the persistent data from file
        try {
            readFile("savedata/incentive.txt");
        }
        catch(RuntimeException e) {
            System.out.println("Fatal Error reading from file");
        }

    }


    //Updates checkbox state in model
    private void checkBoxHandler(ActionEvent actionEvent) {
        CheckBox cb = (CheckBox) actionEvent.getSource();
        dailyGoalCompletion[((DailyGoal)cb.getParent()).goalNumber] = cb.isSelected();
        addSubDailyGoal(cb.isSelected());
    }


    /**
     * Chooses three daily goals in a pseudo-random fashion based on what day it is
     * In it formatted so that the same day will always give the same daily goals
     * @return A DailyGoal[3] array
     */
    public DailyGoal[] getDailyGoals() {

        int lengthOfGoals = goalList.size();
        int n = 1;
        int firstIndex;
        int secondIndex;
        int thirdIndex;

        Date today = new Date();

        firstIndex = (today.getDate() + today.getDay() + today.getMonth())% lengthOfGoals;

        do {
            secondIndex = (today.getDate() + ((today.getDate() + today.getDay() + n) % 5) * n) % lengthOfGoals;
            n += 1;
        } while(secondIndex == firstIndex); //Ensures that there is no duplicates

        n = 1;

        do {
            thirdIndex = (today.getDate() + ((today.getDate() + today.getMonth() + n) % 7) * n) % lengthOfGoals;
            n += 1;
        } while(thirdIndex == firstIndex || thirdIndex == secondIndex); //Ensures there is no duplicates


        goalList.get(firstIndex).setGoalNumber(0);
        goalList.get(secondIndex).setGoalNumber(1);
        goalList.get(thirdIndex).setGoalNumber(2);

        return new DailyGoal[]{goalList.get(firstIndex), goalList.get(secondIndex), goalList.get(thirdIndex)};
    }


    @Override
    public void iModelChanged() {
        Date now = new Date();
        if (today.getYear() != now.getYear() ||today.getMonth() != now.getMonth() || today.getDay() != now.getDay()) {
            today = now;
            notifySubscribers();
        }
    }


    /**
     * Saves data so that it is persistent between launches of the program
     * @param path the name and path of the desired save location
     * @throws IOException
     */
    public void writeAsFile(String path) throws IOException {
        try(PrintWriter p = new PrintWriter(path)){
            p.write(today.getDate() + "\n");
            p.write(today.getMonth() + "\n");
            p.write(today.getYear() + "\n");
            p.write(numOfCompletedDailyGoals + "\n");
            p.write(numOfFlashCardSets + "\n");
            p.write(numOfFlashCardTotal + "\n");
            p.write(dailyGoalCompletion[0] + ":" + dailyGoalCompletion[1] + ":" + dailyGoalCompletion[2]);

            p.close();
        }catch (IOException error){
            error.printStackTrace();
        }
    }


    /**
     * Gets the persistent data from file
     * @param path the name and path of the desired file
     */
    public void readFile(String path) {
        try(FileReader r = new FileReader(path)) {
            BufferedReader br = new BufferedReader(r);

            Queue<String> lines = new ArrayDeque<>();
            String line;
            while((line=br.readLine())!=null)
            {
                lines.add(line);
            }

            int fileDay = Integer.parseInt(lines.remove());
            int fileMonth = Integer.parseInt(lines.remove());
            int fileYear = Integer.parseInt(lines.remove());
            boolean flag = false;

            //If it is currently a different day than when the program was last saved, we don't need to keep track of which daily goals were completed
            if (today.getDate() != fileDay || today.getMonth() != fileMonth || today.getYear() != fileYear) {
                flag = true;
            }

            numOfCompletedDailyGoals = Integer.parseInt(lines.remove());
            numOfFlashCardSets = Integer.parseInt(lines.remove());
            numOfFlashCardTotal = Integer.parseInt(lines.remove());

            String dgline = lines.remove();

            if (!flag) {
                String[] dailyGoalComplete = dgline.split(":");
                dailyGoalCompletion[0] = Boolean.parseBoolean(dailyGoalComplete[0]);
                dailyGoalCompletion[1] = Boolean.parseBoolean(dailyGoalComplete[1]);
                dailyGoalCompletion[2] = Boolean.parseBoolean(dailyGoalComplete[2]);
            }
            br.close();
            notifySubscribers();

        }
        catch (FileNotFoundException e) {
            //This happens on the very first load of the program.
            //When the program is closed it will create the file.
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void notifySubscribers() {
        subscribers.forEach(s -> s.modelChanged());
    }

    public void addSubscriber(ModelListener sub) {
        this.subscribers.add(sub);
    }

    public void setTaskModel(TaskManager tm) {
        taskModel = tm;
    };

    @Override
    public void modelChanged() {
        numOfCompletedTasks = 0;

        for (Task task : taskModel.taskList) {
            if (task.status == TaskStatus.FINISHED) {
                numOfCompletedTasks++;
            }
        }
        notifySubscribers();

    }

    //Adds or subtracts depending on if true or false
    public void addSubDailyGoal(boolean addSub) {

        if (addSub) {
            numOfCompletedDailyGoals++;
        }
        else {
            numOfCompletedDailyGoals--;
        }

        notifySubscribers();
    }


    public void addFlashcardView() {
        numOfFlashCardTotal++;
        notifySubscribers();
    }

    public void addFlashCardSet() {
        numOfFlashCardSets++;
        notifySubscribers();
    }


    //This is a duplication of the getDailyGoals() method modified slightly to work in a test suite
    public static Integer[] getDailyGoalsForTestingPurposesOnly(Date today) {

        int lengthOfGoals = 10;
        int n = 1;
        int firstIndex;
        int secondIndex;
        int thirdIndex;

        firstIndex = (today.getDate() + today.getDay() + today.getMonth())% lengthOfGoals;

        do {
            secondIndex = (today.getDate() + ((today.getDate() + today.getDay() + n) % 5) * n) % lengthOfGoals;
            n += 1;
        } while(secondIndex == firstIndex); //Ensures that there is no duplicates

        n = 1;

        do {
            thirdIndex = (today.getDate() + ((today.getDate() + today.getMonth() + n) % 7) * n) % lengthOfGoals;
            n += 1;
        } while(thirdIndex == firstIndex || thirdIndex == secondIndex); //Ensures there is no duplicates


        return new Integer[]{firstIndex, secondIndex, thirdIndex};
    }

    public static void main(String[] args) {

        Integer[] totals = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

        //Tests for duplicate goals
        for (int i = 0; i < 10000; i ++) {

            //Due to how overflow works with dates, increasing the day works to also increase the month and year effectively
            Integer[] goals = IncentiveModel.getDailyGoalsForTestingPurposesOnly(new Date(2022, 11, i));

            if(goals[0].equals(goals[1]) || goals[0].equals(goals[2]) || goals[1].equals(goals[2])) {
                System.out.println("Duplicate goal found on input " + i);
            }

            totals[goals[0]]++;
            totals[goals[1]]++;
            totals[goals[2]]++;

        }

        System.out.println("Distribution of which goal is chosen");
        System.out.println("1: " + totals[0]);
        System.out.println("2: " + totals[1]);
        System.out.println("3: " + totals[2]);
        System.out.println("4: " + totals[3]);
        System.out.println("5: " + totals[4]);
        System.out.println("6: " + totals[5]);
        System.out.println("7: " + totals[6]);
        System.out.println("8: " + totals[7]);
        System.out.println("9: " + totals[8]);
        System.out.println("10: " + totals[9]);

        System.out.println("Test Suite complete");

        System.out.println();
    }
}
