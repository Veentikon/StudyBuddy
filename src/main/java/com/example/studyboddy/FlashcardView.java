package com.example.studyboddy;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;

/*** View of a flashcard when reviewing it */
public class FlashcardView extends StackPane {
    private HBox root;
    private TextField front;
    private TextField back;

    private Button save;
    private Button exit;
    private Button next;

    private FlashcardModel model;

    // Constructor
    public FlashcardView(){
        // initializations
        root = new HBox();
        front = new TextField();
        back = new TextField();
        save = new Button("save changes");
        exit = new Button("exit");
        next = new Button("next");

        // set up
        root.getChildren().addAll(front, back);
        this.getChildren().add(root);
    }

    // Model corresponding to this view
    public void setModel(FlashcardModel model){
        this.model = model;
        front.setText(model.getFront());
        back.setText(model.getBack());
    }

    public void modelUpdated(){
        front.setText(model.getFront());
        back.setText(model.getBack());
    }
}
