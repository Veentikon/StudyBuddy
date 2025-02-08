package com.example.studyboddy;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Pattern;

public class NewTaskView extends VBox {

    TextField nameField;
    TextArea descField;

    TextField durationHours;
    TextField durationMinutes;

    ComboBox<String> categories;
    ComboBox<String> status;

    Label warning = new Label();
    Button saveButton;
    Button cancelButton;

    // due date selector
    TimeSelectorWidget timeSelector;

    Integer ID;

    CheckBox setEventCheckBox;
    Pane calendarEventPane;     // contains calendar event
    TimeSelectorWidget calendarStartTime;
    TextField calendarEventDurationHours;
    TextField calendarEventDurationMinutes;
    // set color of the event
    ColorPicker colorPicker;

    public NewTaskView() {
        createView();
        this.ID = null;

        // initialize to current time
        timeSelector.setTimeSelected(new Date());
        calendarStartTime.setTimeSelected(new Date());
        colorPicker.setValue(Color.DARKRED);
    }

    public NewTaskView(Task task) {
        createView();

        //Populate the calendar fields and due date fields
        nameField.setText(task.taskName);
        descField.setText(task.taskDescription);
        durationHours.setText(String.valueOf(task.duration / 60));
        durationMinutes.setText(String.valueOf(task.duration % 60));

        timeSelector.setTimeSelected(task.dueDate);
        colorPicker.setValue(Color.DARKRED);    // will be overwritten if applicable; this is just default


        String selection = null;
        switch (task.status) {
            case UNSTARTED -> selection = "Not Started";
            case WORKING -> selection = "In Progress";
            case FINISHED -> selection = "Completed";
            case AUTO_SCHEDULER -> selection = "Auto-Scheduler";
        }
         status.getSelectionModel().select(selection);

        // if it has event, check box
        if (task.calendarEvent != null) {
            setEventCheckBox.setSelected(true);
            calendarStartTime.setTimeSelected(task.calendarEvent.startTime);

            // Drumcorps - For The Living; set the hour and minute duration fields
            long eventDurationMS = task.calendarEvent.endTime.getTime() - task.calendarEvent.startTime.getTime();
            long eventDurationMinutes = eventDurationMS / (1000 * 60);
            calendarEventDurationHours.setText(Long.toString(eventDurationMinutes / 60));
            calendarEventDurationMinutes.setText(Long.toString(eventDurationMinutes % 60));
            // set color if there is one
            if (task.calendarEvent.displayColor != null) {
                colorPicker.setValue(task.calendarEvent.getDisplayColor());
            }
        }
        else {
            setEventCheckBox.setSelected(false);
        }

        this.ID = task.ID;
    }

    private void createView() {
        this.setPadding(new Insets(10));
        this.setSpacing(10);

        //Task Name
        AnchorPane nameBox = new AnchorPane();
        Label taskName = new Label("Task Name: ");

        nameField = new TextField();
        nameBox.getChildren().addAll(taskName, nameField);
        nameField.setPrefWidth(250);
        AnchorPane.setRightAnchor(nameField, 10.0);

        //Description
        AnchorPane descBox = new AnchorPane();
        Label descLabel = new Label("Description: ");

        descField = new TextArea();
        AnchorPane.setRightAnchor(descField, 10.0);
        descField.setPrefWidth(250);
        descField.setPrefRowCount(3);
        descBox.getChildren().addAll(descLabel, descField);

        timeSelector = new TimeSelectorWidget();
        AnchorPane dateBox = new AnchorPane();
        dateBox.getChildren().addAll(new Label("Due Date:"), timeSelector);

        AnchorPane.setRightAnchor(timeSelector, 10.0);

        HBox durationBox = new HBox(10);
        durationBox.setAlignment(Pos.CENTER_LEFT);
        durationBox.getChildren().add(new Label("Task Duration:"));

        durationHours = new TextField();
        durationHours.setPromptText("hrs");
        durationHours.setPrefColumnCount(2);

        durationMinutes = new TextField();
        durationMinutes.setPromptText("min");
        durationMinutes.setPrefColumnCount(2);

        durationBox.getChildren().addAll(durationHours, new Label(":"),durationMinutes);

        //Categories
        HBox categoryBox = new HBox();
        /*
        categoryBox.setAlignment(Pos.CENTER_LEFT);
        categoryBox.getChildren().add(new Label("Category: "));

        categories = new ComboBox<>();
        categoryBox.getChildren().add(categories);
         */

        HBox statusBox = new HBox(10);
        status = new ComboBox<String>(FXCollections.observableArrayList("Not Started", "In Progress", "Completed"));
        status.getSelectionModel().selectFirst();
        statusBox.getChildren().addAll(new Label("Status: "),status);


        // checkbox for whether to schedule calendar event
        setEventCheckBox = new CheckBox();
        HBox haveEventBox = new HBox();
        haveEventBox.setAlignment(Pos.CENTER_LEFT);
        haveEventBox.getChildren().addAll(new Label("Schedule in calendar: "), setEventCheckBox);

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
        Label eventDurationLabel = new Label("Calendar Event Duration: ");
        Label colonLabel = new Label(" : ");
        HBox calendarDurationBox = new HBox();
        calendarDurationBox.setAlignment(Pos.CENTER_LEFT);
        calendarDurationBox.getChildren().addAll(eventDurationLabel, calendarEventDurationHours, colonLabel, calendarEventDurationMinutes);
        calendarEventBox.getChildren().add(calendarDurationBox);

        colorPicker = new ColorPicker();
        Label colorPickerLabel = new Label("Scheduled Color: ");
        HBox colorHBox = new HBox(colorPickerLabel, colorPicker);
        calendarEventBox.getChildren().add(colorHBox);

        //Warning
        warning.setTextFill(Paint.valueOf("#FF0000"));

        //Buttons
        HBox buttonBox = new HBox(5);
        buttonBox.setAlignment(Pos.BOTTOM_RIGHT);
        saveButton = new Button("Save");
        cancelButton = new Button("Cancel");

        buttonBox.getChildren().addAll(saveButton, cancelButton);

        this.getChildren().addAll(nameBox, descBox, dateBox, durationBox, categoryBox, statusBox, haveEventBox, calendarEventPane, warning, buttonBox);
    }

    public void setController(MainUIController controller) {

        saveButton.setOnAction(e -> {
            warning.setText("");

            if (nameField.getText().length() == 0) {
                warning.setText("The task must have a name");
                return;
            }

            if (!durationHours.getText().matches("[0-9]+") && durationHours.getText().length() > 0) {
                warning.setText("Error saving Duration (hours)");
                return;
            }

            if (!durationMinutes.getText().matches("[0-9]+") && durationMinutes.getText().length() > 0) {
                warning.setText("Error saving Duration (minutes)");
                return;
            }

            int durationInMinutes = ((durationHours.getText().length() > 0 ? Integer.parseInt(durationHours.getText()) : 0) *60 ) + (durationMinutes.getText().length() > 0 ? Integer.parseInt(durationMinutes.getText()) : 0);

            Date dueDate = timeSelector.getTimeSelected();
            if (dueDate == null) {
                warning.setText("The due date must be valid");
                return;
            }


            TaskStatus status = null;
            switch(this.status.getValue()) {
                case "Not Started" -> status = TaskStatus.UNSTARTED;
                case "In Progress" -> status = TaskStatus.WORKING;
                case "Completed" -> status = TaskStatus.FINISHED;
                case "Auto-Scheduler" -> status = TaskStatus.AUTO_SCHEDULER;
            }


            // start and end times to be set if we are updating event time
            Date eventStartTime = null;
            Date eventEndTime = null;

            boolean setEvent = setEventCheckBox.isSelected();
            if (setEvent) {
                // get the start time for event
                eventStartTime = calendarStartTime.getTimeSelected();
                if (eventStartTime == null) {
                    warning.setText("The scheduled calendar date must be valid");
                    return;
                }

                if (!calendarEventDurationHours.getText().matches("[0-9]+") && calendarEventDurationHours.getText().length() > 0) {
                    warning.setText("Error saving Calendar Duration (hours)");
                    return;
                }

                if (!calendarEventDurationMinutes.getText().matches("[0-9]+") && calendarEventDurationMinutes.getText().length() > 0) {
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
                eventEndTime = c.getTime();
            }

            controller.saveTask(nameField.getText(), descField.getText(), dueDate, durationInMinutes, 0, "Uncategorized", this.ID, setEvent, eventStartTime, eventEndTime, colorPicker.getValue(), status);

        });

        cancelButton.setOnAction(e -> controller.close());
    }
}
