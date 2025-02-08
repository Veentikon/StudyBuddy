package com.example.studyboddy;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/***
 * Runnable program for testing Flashcard part of the system
 */
public class FlashcardViewTest extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        FlashcardMainView root = new FlashcardMainView();
        FlashcardMainModel fModel = new FlashcardMainModel();
        root.setModel(fModel);
        FlashcardMainController fMController = new FlashcardMainController();
        fModel.setController(fMController);
        fModel.addSubscriber(root);
        fMController.setModel(fModel);
        root.setController(fMController);
        Scene scene = new Scene(root);
        stage.setTitle("Flashcards");
        stage.setScene(scene);
        stage.show();
    }
}
