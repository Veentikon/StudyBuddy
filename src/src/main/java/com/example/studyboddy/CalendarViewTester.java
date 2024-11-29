package com.example.studyboddy;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class CalendarViewTester extends Application {
    @Override
    public void start(Stage stage) {
        CalendarView root = new CalendarView();
        Scene scene = new Scene(root);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }


    public static void main(String[] args) {
        launch();
    }
}
