package com.example.studyboddy;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

public class IncentiveView extends BorderPane implements ModelListener {

    IncentiveModel model;

    VBox dailyGoalBox;

    Label tasksCompleted;
    Label dailiesCompleted;
    Label flashcardsViewed;
    Label flashcardSets;

    VBox achievementBox;

    public IncentiveView() {
        this.setPadding(new Insets(10));

        dailyGoalBox = new VBox(10);
        dailyGoalBox.setPadding(new Insets(10));
        dailyGoalBox.setPrefWidth(450);
        dailyGoalBox.setBorder(new Border(new BorderStroke(Color.valueOf("#AAAAAA"), BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(2))));

        this.setLeft(dailyGoalBox);

        VBox allTimeGoalBox = new VBox(10);
        allTimeGoalBox.setPadding(new Insets(10));
        allTimeGoalBox.setBorder(new Border(new BorderStroke(Color.valueOf("#AAAAAA"), BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(2))));
        allTimeGoalBox.setPrefWidth(450);

        Label allTimeLabel = new Label("Stats");
        allTimeLabel.setStyle("-fx-font-size: 24");
        allTimeGoalBox.getChildren().add(allTimeLabel);

        tasksCompleted = new Label("Tasks Completed: 0");
        tasksCompleted.setStyle("-fx-font-size: 18");
        allTimeGoalBox.getChildren().add(tasksCompleted);

        dailiesCompleted = new Label("Daily Goals Completed: 0");
        dailiesCompleted.setStyle("-fx-font-size: 18");
        allTimeGoalBox.getChildren().add(dailiesCompleted);

        flashcardsViewed = new Label("Total Flashcards Reviewed: 0");
        flashcardsViewed.setStyle("-fx-font-size: 18");
        allTimeGoalBox.getChildren().add(flashcardsViewed);

        flashcardSets = new Label("Total Flashcards Sets Practiced: 0");
        flashcardSets.setStyle("-fx-font-size: 18");
        allTimeGoalBox.getChildren().add(flashcardSets);

        allTimeGoalBox.getChildren().add(new Label(""));

        Label achievementLabel = new Label("Achievements:");
        achievementLabel.setStyle("-fx-font-size: 18");
        allTimeGoalBox.getChildren().add(achievementLabel);

        ScrollPane achievementScroll = new ScrollPane();
        achievementBox = new VBox(5);
        achievementScroll.setContent(achievementBox);

        achievementBox.getChildren().add(new Achievement("Complete 10 Tasks", 10, AchievementType.TASK));
        achievementBox.getChildren().add(new Achievement("Complete 50 Tasks", 50, AchievementType.TASK));
        achievementBox.getChildren().add(new Achievement("Complete 100 Tasks", 100, AchievementType.TASK));
        achievementBox.getChildren().add(new Achievement("Complete 500 Tasks", 500, AchievementType.TASK));

        achievementBox.getChildren().add(new Achievement("Complete 10 Daily Goals", 10, AchievementType.DAILY_GOAL));
        achievementBox.getChildren().add(new Achievement("Complete 50 Daily Goals", 50, AchievementType.DAILY_GOAL));
        achievementBox.getChildren().add(new Achievement("Complete 100 Daily Goals", 100, AchievementType.DAILY_GOAL));
        achievementBox.getChildren().add(new Achievement("Complete 500 Daily Goals", 500, AchievementType.DAILY_GOAL));

        achievementBox.getChildren().add(new Achievement("Practice 10 Flashcard Sets", 10, AchievementType.FLASHCARD_SET));
        achievementBox.getChildren().add(new Achievement("Practice 50 Flashcard Sets", 50, AchievementType.FLASHCARD_SET));
        achievementBox.getChildren().add(new Achievement("Practice 250 Flashcard Sets", 250, AchievementType.FLASHCARD_SET));
        achievementBox.getChildren().add(new Achievement("Practice 1000 Flashcard Sets", 1000, AchievementType.FLASHCARD_SET));

        achievementBox.getChildren().add(new Achievement("Practice 100 Flashcards", 100, AchievementType.FLASHCARD_TOTAL));
        achievementBox.getChildren().add(new Achievement("Practice 250 Flashcards", 250, AchievementType.FLASHCARD_TOTAL));
        achievementBox.getChildren().add(new Achievement("Practice 1000 Flashcards", 1000, AchievementType.FLASHCARD_TOTAL));
        achievementBox.getChildren().add(new Achievement("Practice 2500 Flashcards", 2500, AchievementType.FLASHCARD_TOTAL));

        allTimeGoalBox.getChildren().add(achievementScroll);

        this.setRight(allTimeGoalBox);
    }

    public void addModel(IncentiveModel model) {
        this.model = model;
        modelChanged();

    }

    @Override
    public void modelChanged() {
        dailyGoalBox.getChildren().clear();

        Label dailyGoalLabel = new Label("Daily Goals");
        dailyGoalLabel.setStyle("-fx-font-size: 24");
        dailyGoalBox.getChildren().add(dailyGoalLabel);

        //Gets the daily goals and checks whether they should be checked or not
        DailyGoal[] goals = model.getDailyGoals();
        for(int i = 0; i < 3; i++) {
            DailyGoal goal = goals[i];

            if(model.dailyGoalCompletion[i]) {
                goal.checkbox.setSelected(true);
            }
            else {
                goal.checkbox.setSelected(false);
            }

            dailyGoalBox.getChildren().add(goal);
        }

        tasksCompleted.setText("Tasks Completed: " + model.numOfCompletedTasks);
        dailiesCompleted.setText("Daily Goals Completed: " + model.numOfCompletedDailyGoals);
        flashcardsViewed.setText("Total Flashcards Reviewed: " + model.numOfFlashCardTotal);
        flashcardSets.setText("Total Flashcard Sets Practiced: " + model.numOfFlashCardSets);


        achievementBox.getChildren().forEach(c -> {
            switch (((Achievement) c).getType()) {
                case TASK -> ((Achievement) c).setCurrent(model.numOfCompletedTasks);
                case DAILY_GOAL -> ((Achievement) c).setCurrent(model.numOfCompletedDailyGoals);
                case FLASHCARD_SET -> ((Achievement) c).setCurrent(model.numOfFlashCardSets);
                case FLASHCARD_TOTAL -> ((Achievement) c).setCurrent(model.numOfFlashCardTotal);
            }
        });

    }
}
