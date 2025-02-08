package com.example.studyboddy;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.util.Callback;

public class PriorityQueueCardView extends ListCell<Task> {
    HBox root;
    Button editButton;
    PriorityQueueController pqc;

    public PriorityQueueCardView(PriorityQueueController pqc){
        this.pqc = pqc;
    }

    @Override
    public void updateItem(Task item, boolean empty) {
        super.updateItem(item, empty);
        if (item != null) {
            root = new HBox();
            CheckBox checkBox = new CheckBox();
            Label labelDescriber = new Label(item.taskName);
            editButton = new Button("Edit");
            editButton.setOnAction(e -> pqc.editTask(item.ID));

            root.getChildren().addAll(labelDescriber, checkBox, editButton );
            checkBox.setOnAction(actionEvent -> {
                item.markedForMovement = checkBox.selectedProperty().get();
            });


            setGraphic(root);
        }
    }

}
