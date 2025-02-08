package com.example.studyboddy;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class MainUI extends BorderPane implements iModelListener, ModelListener, CloseTask, EditTask {

    MainUIController controller;
    MainUIInteractiveModel iModel;
    StackPane flashcardMainView;
    CalendarView calendarView;
    PriorityQueueView PQ;
    Stage newTask;
    IncentiveView incentiveView;

    public MainUI() {

        VBox navBar = new VBox(5);
        navBar.setPadding(new Insets(5));
        navBar.setStyle("-fx-background-color: #00FF00");

        Button calendarButton = new Button("Cale\nndar");
        calendarButton.setOnAction(e -> controller.changeView(MainUIInteractiveModel.views.CALENDAR));
        calendarButton.setPrefSize(50, 50);

        //ImageView calendarImage = new ImageView(new Image(new getResourceAsStream("date.png")));
        //calendarButton.setGraphic(calendarImage);

        //PRIORITY QUEUE
        Button PQButton = new Button("PQ");
        PQButton.setOnAction(e -> controller.changeView(MainUIInteractiveModel.views.PQ));
        PQButton.setPrefSize(50, 50);

        Button FlashCardButton = new Button("Flash\nCards");
        FlashCardButton.setOnAction(e -> controller.changeView(MainUIInteractiveModel.views.FlashCards));
        FlashCardButton.setPrefSize(50, 50);

        Button IncentiveButton = new Button("Goals");
        IncentiveButton.setOnAction(e -> controller.changeView(MainUIInteractiveModel.views.INCENTIVE));
        IncentiveButton.setPrefSize(50, 50);

        Button newTaskButton = new Button("+");
        newTaskButton.setOnAction(e -> this.newTask());
        newTaskButton.setPrefSize(50, 50);

        navBar.getChildren().addAll(calendarButton, PQButton, FlashCardButton, IncentiveButton, newTaskButton);

        this.setLeft(navBar);

    }

    public void setController(MainUIController controller) {
        this.controller = controller;
    }

    public void setInteractiveModel(MainUIInteractiveModel iModel) {
        this.iModel = iModel;
    }


    public void setFlashcardMainView(StackPane flashcardMainView) {
        this.flashcardMainView = flashcardMainView;
    }

    public void setCalendarView(CalendarView calendarView) {
        this.calendarView = calendarView;
        this.calendarView.bindAddTaskButton(e -> newTask());
        this.calendarView.bindAddAutoTaskButton(e -> newAutoTask());
        this.setCenter(calendarView);
    }

    public void setPriorityQueue(PriorityQueueView pq) {
        this.PQ = pq;
    }

    public void setIncentiveView(IncentiveView incentiveView) {this.incentiveView = incentiveView; }

    @Override
    public void iModelChanged() {
        switch (iModel.getCurrentView()) {
            //case MAIN -> this.setCenter(null);
            case PQ -> this.setCenter(PQ);
            case CALENDAR -> this.setCenter(calendarView);
            case FlashCards -> this.setCenter(flashcardMainView);
            case INCENTIVE -> this.setCenter(incentiveView);
        }
    }

    public void newTask() {
        if (newTask != null) {
            return;
        }

        NewTaskView root = new NewTaskView();
        root.setController(controller);

        newTask = new Stage();
        newTask.setResizable(false);
        newTask.setOnCloseRequest(windowEvent -> newTask = null);

        Scene scene = new Scene(root, 350, 450);
        newTask.setTitle("New Task");
        newTask.setScene(scene);
        newTask.show();
    }

    /**
     * Adds calendar event available to auto scheduler
     */
    public void newAutoTask() {
        if (newTask != null) {
            return;
        }

        AutoScheduleView root = new AutoScheduleView();
        root.setController(controller);

        newTask = new Stage();
        newTask.setResizable(false);
        newTask.setOnCloseRequest(windowEvent -> newTask = null);

        Scene scene = new Scene(root, 350, 450);
        newTask.setTitle("New Auto-Schedule Availability");
        newTask.setScene(scene);
        newTask.show();
    }

    public void editTask(Task task) {
        if (newTask != null) {
            return;
        }

        if (task.category.equals("Auto-schedule")) {
            AutoScheduleView root = new AutoScheduleView(task);
            root.setController(controller);

            newTask = new Stage();
            newTask.setResizable(false);
            newTask.setOnCloseRequest(windowEvent -> newTask = null);

            Scene scene = new Scene(root, 350, 450);
            newTask.setTitle("Edit Auto-Scheduled Availability");
            newTask.setScene(scene);
        } else {
            NewTaskView root = new NewTaskView(task);
            root.setController(controller);

            newTask = new Stage();
            newTask.setResizable(false);
            newTask.setOnCloseRequest(windowEvent -> newTask = null);

            Scene scene = new Scene(root, 350, 450);
            newTask.setTitle("Edit Task");
            newTask.setScene(scene);
        }
        newTask.show();
    }

    @Override
    public void modelChanged() {

    }

    @Override
    public void close() {
        newTask.close();
        newTask = null;
    }
}
