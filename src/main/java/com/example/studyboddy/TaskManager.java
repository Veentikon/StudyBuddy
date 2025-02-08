package com.example.studyboddy;

import java.io.*;
import java.util.*;

/**
 * Model to contain all the tasks
 * Can be saved to and read from file
 */
public class TaskManager {
    public List<Task> taskList;
    private ArrayList<ModelListener> subscribers;
    private CloseTask closer;
    private EditTask editor;

    public ArrayList<String> categories;

    String path = "savedata/Tasks.bin"; //the save directory for task manager along with the name


    public TaskManager() {
        taskList = new ArrayList<>();
        subscribers = new ArrayList<>();
        categories = new ArrayList<>(List.of(new String[]{"Uncategorized"}));
        /*
        for (int i = 0; i < 10;i++){
            taskList.add(new Task(Integer.toString(i),Integer.toString(i)));
        }
        //
        //The design is used to create a test set of tasks that you can manipulate to test the data
         */
    }

    public void writeAsFile() {
        //FORMERLY getAsFile
        // return or save the list of tasks as a file
        try(FileOutputStream f = new FileOutputStream(path)){
            ObjectOutputStream oos = new ObjectOutputStream(f);
            oos.writeObject(taskList);
            oos.close();
        }catch (IOException error){
            error.printStackTrace();
        }
    }
    @SuppressWarnings("unchecked") //SHHHHHHH Know one has to know
    public ArrayList<Task> readFile() {
        ArrayList<Task> art = null;
        try(FileInputStream in = new FileInputStream(path);
            ObjectInputStream s = new ObjectInputStream(in)) {
            art = (ArrayList<Task>) s.readObject();
            s.close();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }catch (FileNotFoundException ignored){
            return null;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return art;
    }

    public void addTask(Task newTask) {
        taskList.add(newTask);
        newTask.setID(taskList.indexOf(newTask));
        writeAsFile();
        notifySubscribers();
        closeNewTask();
    }

    public void updateTask(String name, String description, Date dueDate, int duration, int priorityLevel, String category, Integer ID, TaskStatus status) {
        Task task = taskList.get(ID);
        task.taskName = name;
        task.taskDescription = description;
        task.dueDate = dueDate;
        task.duration = duration;
        task.priorityLevel = priorityLevel;
        task.category = category;
        task.status = status;
        writeAsFile();
        notifySubscribers();
        closeNewTask();
    }

    public Task getTaskByID(int id) {
        return taskList.get(id);
    }

    public void editTask(int id) {
        editor.editTask(taskList.get(id));
        this.writeAsFile();
    }

    /**
     * Gets map of String category keys to List of Tasks with that category
     * @return HashMap of String category keys to List of Tasks with that category
     */
    public Map<String, List<Task>> getTasksByCategory() {
        // add each task to the map's corresponding list
        HashMap<String, List<Task>> map = new HashMap<>();
        for (int i = 0; i < taskList.size(); i++) {
            Task task = taskList.get(i);

            List<Task> categoryList = map.get(task.category);
            if (categoryList == null) {
                // if there is no list, make one
                categoryList = new ArrayList<>();
                categoryList.add(task);
                map.put(task.category, categoryList);
            }
            else {
                // add it to the existing list
                categoryList.add(task);
            }
        }

        return map;
    }

    /**
     * Gets a duplicate list of the tasks
     * @return A duplicated list of the tasks in the model
     */
    public List<Task> getTaskList() {
        return (List<Task>) ((ArrayList<Task>)taskList).clone();
    }

    public ArrayList<String> getCategories(){
        return  this.categories;
    }


    public void notifySubscribers() {
        subscribers.forEach(ModelListener::modelChanged);
    }

    public void addSubscriber(ModelListener sub) {
        subscribers.add(sub);
    }

    public void closeNewTask() {
        closer.close();
    }

    public void addCloser(CloseTask closer) {
        this.closer = closer;
    }

    public void addEditor(EditTask editor) {
        this.editor = editor;
    }

    public void addCategoryColumn(String string) {
        this.categories.add(string);
        writeAsFile();
        notifySubscribers();
    }

    /** TEST SCRIPT */
    public static void main(String[] args) {
        // to run, need to ignore closing new task
        TaskManager taskManager = new TaskManager();
        Task task1 = new Task("task1", "desc1");
        taskManager.addTask(task1);

        Task task2 = new Task("Task2", "desc2");
        task2.category = "Transit";
        taskManager.addTask(task2);

        Task task3 = new Task("Task3", "desc3");
        taskManager.addTask(task3);

        System.out.println(taskManager.getTasksByCategory());
    }

    public void changeCategoryOfMoveForMarked(String columnDestination) {
        this.taskList.forEach(tsk -> {
            if(tsk.markedForMovement){
                tsk.category = columnDestination;
            }
        });
        notifySubscribers();
        this.writeAsFile();

    }

    public void setTaskModelAsReadData() {
        ArrayList<Task> temp = readFile();
        if(temp != null){
            this.taskList = temp;
            temp.forEach(c -> {
                if(!categories.contains(c.category)){
                    categories.add(c.category);
                }
            });
        }
        notifySubscribers();
    }
}
