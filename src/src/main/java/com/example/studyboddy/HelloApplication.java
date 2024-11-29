package com.example.studyboddy;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) {

        MainUI root = new MainUI();
        MainUIController mainUIController = new MainUIController();
        MainUIInteractiveModel mainUIInteractiveModel = new MainUIInteractiveModel();

        root.setController(mainUIController);
        root.setInteractiveModel(mainUIInteractiveModel);
        mainUIController.setiModel(mainUIInteractiveModel);

        mainUIInteractiveModel.addSubscriber(root);


        Scene scene = new Scene(root, 1000, 800);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();

        //PQ
        PriorityQueue pq = new PriorityQueue();
        root.setPriorityQueue(pq);

        //Calendar
        CalendarView calendarView = new CalendarView();
        root.setCalendarView(calendarView);


        // Testing flash card view(s)
        FlashcardMModel fModel = new FlashcardMModel();
        FlashcardMView fMView = new FlashcardMView();
        FlashcardMController fMController = new FlashcardMController();
        fModel.setController(fMController);
        fMView.setModel(fModel);
        fMView.setController(fMController);
        fMController.setModel(fModel);
        fModel.addSubscriber(fMView);

        root.setFlashcardMainView(fMView);

        /*
        Scene flashCards = new Scene(fMView, 320, 240);
        stage.setScene(flashCards);
        stage.show();
        */
    }





    public static void main(String[] args) {
        launch();
    }
}