package com.example.studyboddy;

import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class PriorityQueue extends Pane {
    //set up the layout

    public PriorityQueue(){
        //This is just for testing purposes, don't bank on this being the final model
        //Don't judge, need to figure out and brainstorm what would look the best for PQ
        HBox mainHBOX = new HBox();
        VBox pastTasks = new VBox();
        VBox presentTasks = new VBox();
        this.getChildren().add(mainHBOX);
        mainHBOX.getChildren().addAll(pastTasks, presentTasks);
        pastTasks.getChildren().add(new Label("Past tasks lmao"));
        presentTasks.getChildren().add(new Label("present tasks lmao"));
    }
}
