package com.example.studyboddy;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

import java.util.Calendar;
import java.util.Date;

public class AutoScheduleView extends VBox{

    Label warning = new Label();
    Button autoScheduleButton;
    Button cancelButton;

    Integer ID;

    Pane calendarEventPane;     // contains calendar event
    TimeSelectorWidget calendarStartTime;
    TextField calendarEventDurationHours;
    TextField calendarEventDurationMinutes;

    /**
     * Constructor for creating an auto-schedule event
     */
    public AutoScheduleView() {
        createView();
        this.ID = null;

        // initialize to current time
        calendarStartTime.setTimeSelected(new Date());
    }


    /**
     * Constructor for editing an auto-scheduled event
     * @param task task
     */
    public AutoScheduleView(Task task) {
        createView();

        //Populate the calendar fields and due date fields
        calendarStartTime.setTimeSelected(task.calendarEvent.startTime);

        // set the hour and minute duration fields
        long eventDurationMS = task.calendarEvent.endTime.getTime() - task.calendarEvent.startTime.getTime();
        long eventDurationMinutes = eventDurationMS / (1000 * 60);
        calendarEventDurationHours.setText(Long.toString(eventDurationMinutes / 60));
        calendarEventDurationMinutes.setText(Long.toString(eventDurationMinutes % 60));

        this.ID = task.ID;
    }

    /**
     * Creates the view for the auto-schedule
     */
    private void createView() {
        this.setPadding(new Insets(10));
        this.setSpacing(10);

        // vbox to set event time
        calendarEventPane = new Pane();
        VBox calendarEventBox = new VBox();
        calendarEventPane.getChildren().add(calendarEventBox);

        // make the start time selector
        calendarStartTime = new TimeSelectorWidget();
        calendarEventBox.getChildren().add(calendarStartTime);

        // text fields for duration
        calendarEventDurationHours = new TextField();
        calendarEventDurationMinutes = new TextField();
        calendarEventDurationHours.setPrefWidth(60);
        calendarEventDurationMinutes.setPrefWidth(60);

        // add the duration boxes to event pane
        Label eventDurationLabel = new Label("Availability Duration: ");
        Label colonLabel = new Label(" : ");
        HBox calendarDurationBox = new HBox();
        calendarDurationBox.setAlignment(Pos.CENTER_LEFT);
        calendarDurationBox.getChildren().addAll(eventDurationLabel, calendarEventDurationHours, colonLabel, calendarEventDurationMinutes);
        calendarEventBox.getChildren().add(calendarDurationBox);

        //Warning
        warning.setTextFill(Paint.valueOf("#FF0000"));

        //Buttons
        HBox buttonBox = new HBox(5);
        buttonBox.setAlignment(Pos.BOTTOM_RIGHT);
        autoScheduleButton = new Button("Auto-schedule");
        cancelButton = new Button("Cancel");

        buttonBox.getChildren().addAll(autoScheduleButton, cancelButton);

        this.getChildren().addAll(calendarEventPane, warning, buttonBox);
    }

    public void setController(MainUIController controller) {

        autoScheduleButton.setOnAction(e -> {
            warning.setText("");

            // get the start time for event
            Date eventStartTime = calendarStartTime.getTimeSelected();
            if (eventStartTime == null) {
                warning.setText("The scheduled calendar date must be valid");
                return;
            }

            if (!(calendarEventDurationHours.getText().matches("[0-9]+") && calendarEventDurationHours.getText().length() > 0)) {
                warning.setText("Error saving Calendar Duration (hours)");
                return;
            }

            if (!(calendarEventDurationMinutes.getText().matches("[0-9]+") && calendarEventDurationMinutes.getText().length() > 0)) {
                warning.setText("Error saving Calendar Duration (Minutes)");
                return;
            }

            int calendarDurationHours = Integer.parseInt(calendarEventDurationHours.getText());
            int calendarDurationMinutes = Integer.parseInt(calendarEventDurationMinutes.getText());

            // add the duration to get end time
            Calendar c = Calendar.getInstance();
            c.setTime(eventStartTime);
            c.add(Calendar.HOUR, calendarDurationHours);
            c.add(Calendar.MINUTE, calendarDurationMinutes);
            Date eventEndTime = c.getTime();

            controller.saveTask("Auto-Schedule", "Available for automatic scheduling", eventEndTime, 0,0, "Auto-schedule", this.ID, true, eventStartTime, eventEndTime, Color.AQUA, TaskStatus.AUTO_SCHEDULER);
        });

        cancelButton.setOnAction(e -> controller.close());
    }
}
