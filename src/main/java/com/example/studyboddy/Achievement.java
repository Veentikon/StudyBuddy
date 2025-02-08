package com.example.studyboddy;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Slider;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

public class Achievement extends VBox {

    String goal;
    int max;
    int current;
    private AchievementType type;

    Label progressLabel;
    ProgressBar progressBar;
    Label completeLabel;



    public Achievement(String goal, int max, AchievementType type) {
        this.goal = goal;
        this.max = max;
        this.current = 0;
        this.type = type;
        this.setMinWidth(400);
        this.setMaxWidth(400);

        this.setAlignment(Pos.CENTER_LEFT);
        this.setPadding(new Insets(5));
        this.setSpacing(5);
        this.setBorder(new Border(new BorderStroke(Color.valueOf("#AAAAAA"), BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(2))));

        BorderPane titleBox = new BorderPane();
        titleBox.setLeft(new Label(goal));
        progressLabel = new Label();
        titleBox.setRight(progressLabel);
        this.getChildren().add(titleBox);

        progressBar = new ProgressBar();
        progressBar.setProgress(0);
        progressBar.setPrefWidth(380);

        progressLabel.setLabelFor(progressBar);

        this.getChildren().add(progressBar);

        completeLabel = new Label();
        this.getChildren().add(completeLabel);

    }

    public  void setCurrent(int newCurrent) {
        this.current = newCurrent;

        if (newCurrent >= max) {
            current = max;
            completeLabel.setText("Complete!");
        }
        this.progressLabel.setText(current + "/" + max);
        this.progressBar.setProgress((float)current / (float)max);
    }

    public AchievementType getType() {
        return type;
    }


}
