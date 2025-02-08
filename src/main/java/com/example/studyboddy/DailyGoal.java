package com.example.studyboddy;

import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

public class DailyGoal extends HBox {

    String goal;
    CheckBox checkbox;
    int goalNumber;

    public DailyGoal(String goal) {

        this.goal = goal;

        this.setSpacing(5);
        this.setPadding(new Insets(10));
        this.setAlignment(Pos.CENTER_LEFT);
        this.setStyle("-fx-background-color: #EEEEEE");
        this.setBorder(new Border(new BorderStroke(Color.valueOf("#AAAAAA"), BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(2))));
        this.setPrefSize(400, 75);


        checkbox = new CheckBox();
        this.getChildren().add(checkbox);

        Label label = new Label(goal);
        label.setStyle("-fx-text-overrun: none");
        this.getChildren().add(new Label(goal));



    }

    public void setGoalNumber(int goalNumber) {
        this.goalNumber = goalNumber;
    }

    public void setOnAction(EventHandler<ActionEvent> checkBoxHandler) {
        checkbox.setOnAction(checkBoxHandler);
    }
}
