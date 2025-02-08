package com.example.studyboddy;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * A view for a week of events in the calendar
 */
public class CalendarWeekView extends Pane implements iModelListener, ModelListener {

    private CalendarModel calendarModel;
    private CalendarIModel calendarIModel;

    private String[] weekDays = {"Sunday","Monday","Tuesday","Wednesday","Thursday","Friday","Saturday"};
    private String[] months = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};

    private static final double COLHEIGHT = 650;    // height of a column to display events
    private static final double WEEKWIDTH = 700;    // width of the weeks

    private CalendarColumn[] columns;

    Button prevWeekButton, nextWeekButton;
    Label weekTitle;

    // ONLY FOR SETTING ACTIONS ON LABELS
    private CalendarController controller;

    public CalendarWeekView() {
        VBox root = new VBox();
        this.getChildren().add(root);

        // label for switching weeks
        // Create the header for the calendar for month
        HBox weekHead = new HBox();
        weekHead.setMinSize(700,100);
        weekHead.setMaxSize(700,100);
        weekHead.setPrefSize(700,100);
        weekHead.setAlignment(Pos.TOP_CENTER);
        weekHead.setSpacing(50);
        weekHead.setPadding(new Insets(20,20,20,20));
        prevWeekButton = new Button("<");
        prevWeekButton.setPrefSize(50,50);
        weekTitle = new Label();
        weekTitle.setFont(new Font("Regular", 30));
        weekTitle.setPrefWidth(500);
        nextWeekButton = new Button(">");
        weekTitle.setAlignment(Pos.CENTER);
        nextWeekButton.setPrefSize(50,50);

        weekHead.getChildren().add(prevWeekButton);
        weekHead.getChildren().add(weekTitle);
        weekHead.getChildren().add(nextWeekButton);

        root.getChildren().add(weekHead);

        // weeks
        // TODO get rid of this entirely unused view section
        HBox weeksBox = new HBox();
        //root.getChildren().add(weeksBox);

        GridPane weeksGrid = new GridPane();
        root.getChildren().add(weeksGrid);

        for (int i = 0; i < 24; i++) {
            // add a grey alternating background
            if (i % 2 == 0) {
                Pane greyPane = new Pane();
                greyPane.setPrefSize(40 + WEEKWIDTH, COLHEIGHT / 24);
                greyPane.setStyle("-fx-background-color: #bbbbbb;");
                weeksGrid.add(greyPane, 0, i + 1, 25, 1);
            }
        }
        weeksGrid.setGridLinesVisible(true);

        columns = new CalendarColumn[7];

        // add a column for each day of the week
        for (int i = 0; i<7; i++) {
            VBox dayCol = new VBox();
            weeksBox.getChildren().add(dayCol);

            Label dayLabel = new Label(weekDays[i]);
            dayLabel.setPrefWidth(WEEKWIDTH/7);
            dayLabel.setAlignment(Pos.BOTTOM_CENTER);
            dayCol.getChildren().add(dayLabel);

            weeksGrid.add(dayLabel, i+1, 0, 1, 1);

            columns[i] = new CalendarColumn();
            columns[i].setPrefHeight(COLHEIGHT);    // need to communicate to VBox holding it that it is large
            columns[i].setHeightDupe(COLHEIGHT);
            columns[i].setStyle("-fx-border-color: #999999;");
            columns[i].setPrefWidth(WEEKWIDTH/7);
            dayCol.getChildren().add(columns[i]);

            weeksGrid.add(columns[i], i+1, 1, 1, 24);
        }

        // TODO: noon should be pm not am
        for (int i = 0; i < 24; i++) {
            // get time from 0-11
            int time = (i + 1) % 12;
            // 0 is actually 12
            if (time == 0) {time = 12;}

            // get if its am or pm; end of the day midnight is am
            String ampm = i < 12 || i == 23 ? "am" : "pm";

            // make the actual label for that time
            String timeString = time + ampm;
            Label timeLabel = new Label(timeString);
            timeLabel.setPrefHeight(COLHEIGHT / 24);
            timeLabel.setAlignment(Pos.BOTTOM_RIGHT);
            timeLabel.setPrefWidth(40);
            weeksGrid.add(timeLabel, 0, i+1);
        }
    }

    /** populates columns based on the start of the week in IModel */
    // TODO: fix bug where positioning this before week view has appeared causes events to be positioned incorrectly
    private void updateColumns() {
        // get the start of the day
        Calendar c = Calendar.getInstance();
        c.setTime(calendarIModel.getWeekViewing());
        Date colStartDate = calendarIModel.getWeekViewing();

        for (int i = 0; i < 7; i++) {
            // get the Date one day later
            c.add(Calendar.DATE, 1);
            Date colEndDate = c.getTime();

            // get all events today
            List<CalendarEvent> eventsToday = calendarModel.getEventsInRange(colStartDate, colEndDate,calendarIModel.getCategories());

            // make labels for each event
            List<Label> eventLabels = new ArrayList<>();
            for (CalendarEvent event : eventsToday) {
                Label eventLabel = new Label(event.eventName);

                // Probably set color based on category?
                // for now just make it some color
                eventLabel.setStyle("-fx-background-color: red;");
                eventLabel.setPrefWidth(WEEKWIDTH/7);

                eventLabels.add(eventLabel);

                // Right-clicking will give you an option to modify or delete
                CalendarMainView.createContextMenu(event, eventLabel, controller);
            }

            // position the labels in the column
            columns[i].getChildren().clear();
            columns[i].positionNodes(colStartDate, colEndDate, eventLabels, eventsToday);

            // iterate to next day
            colStartDate = colEndDate;
        }
    }

    // update display of which week we are viewing
    private void updateHeader() {
        String title = "";
        // TODO: display year here

        // add start time
        Date weekStart = calendarIModel.getWeekViewing();
        title += months[weekStart.getMonth()];
        title += " " + weekStart.getDate() + " - ";

        // get time of end of week
        Calendar c = Calendar.getInstance();
        c.setTime(weekStart);
        c.add(Calendar.DATE, 6);
        Date weekEnd = c.getTime();

        // add end time
        title += months[weekEnd.getMonth()];
        title += " " + weekEnd.getDate();
        weekTitle.setText(title);
    }

    @Override
    public void iModelChanged() {
        updateColumns();
        updateHeader();
    }

    @Override
    public void modelChanged() {
        updateColumns();
        updateHeader();
    }

    public void setCalendarIModel(CalendarIModel iModel) {
        this.calendarIModel = iModel;
    }

    public void setCalendarModel(CalendarModel model) {
        this.calendarModel = model;
    }

    public void setController(CalendarController controller) {
        nextWeekButton.setOnAction(controller::viewNextWeek);
        prevWeekButton.setOnAction(controller::viewPreviousWeek);

        this.controller = controller;   // for using for labels
    }
}
