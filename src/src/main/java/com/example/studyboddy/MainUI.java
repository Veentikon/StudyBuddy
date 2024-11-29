package com.example.studyboddy;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.io.FileInputStream;

public class MainUI extends BorderPane implements iModelListener {

    MainUIController controller;
    MainUIInteractiveModel iModel;
    FlashcardMView flashcardMainView;
    CalendarView calendarView;
    PriorityQueue PQ;

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


        Button clearButton = new Button("Clear");
        clearButton.setOnAction(e -> this.setCenter(null));
        clearButton.setPrefSize(50, 50);

        navBar.getChildren().addAll(calendarButton, PQButton, FlashCardButton,clearButton);

        this.setLeft(navBar);

        this.getChildren().add(new Label("Testing"));


    }


    public void setController(MainUIController controller) {
        this.controller = controller;
    }

    public void setInteractiveModel(MainUIInteractiveModel iModel) {
        this.iModel = iModel;
    }


    public void setFlashcardMainView(FlashcardMView flashcardMainView) {
        this.flashcardMainView = flashcardMainView;
    }

    public void setCalendarView(CalendarView calendarView) {
        this.calendarView = calendarView;
    }

    public void setPriorityQueue(PriorityQueue pq) {
        this.PQ = pq;
    }

    @Override
    public void iModelChanged() {

        switch (iModel.getCurrentView()) {
            case MAIN -> this.setCenter(null);
            case PQ -> this.setCenter(PQ);
            case CALENDAR -> this.setCenter(calendarView);
            case FlashCards -> this.setCenter(flashcardMainView);
        }

    }
}
