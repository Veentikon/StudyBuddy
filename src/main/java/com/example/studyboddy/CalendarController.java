package com.example.studyboddy;

import javafx.event.ActionEvent;
import javafx.scene.control.ToggleButton;

import java.util.ArrayList;

public class CalendarController {
    private CalendarModel model;
    private CalendarIModel iModel;

    private MainUI taskEditor;
    private AutoScheduler autoScheduler;

    public CalendarController() {

    }

    /**
     * Sets the model
     * @param calendarModel model
     */
    public void setModel(CalendarModel calendarModel) {
        this.model = calendarModel;
    }

    /**
     * Sets the interaction model
     * @param calendarIModel interaction model
     */
    public void setIModel(CalendarIModel calendarIModel) {
        this.iModel = calendarIModel;
    }

    /**
     * Switches the view to the previous month in month view
     * @param event event
     */
    public void handlePrevMonthBtn(ActionEvent event) {
        iModel.viewPrevMonth();
    }

    /**
     * Switches the view to the next month in month view
     * @param event event
     */
    public void handleNextMonthBtn(ActionEvent event) {
        iModel.viewNextMonth();
    }

    /**
     * Switches the view to the next week in week view
     * @param event event
     */
    public void viewNextWeek(ActionEvent event) {
        iModel.viewNextWeek();
    }

    /**
     * Switches the view to the previous week in week view
     * @param event event
     */
    public void viewPreviousWeek(ActionEvent event) {
        iModel.viewPreviousWeek();
    }

    // day operations
    /**
     * Switches the view to the previous day in day view
     * @param event event
     */
    public void handlePrevDay(ActionEvent event) {
        iModel.viewPrevDay();
    }

    /**
     * Switches the view to the next day in day view
     * @param event event
     */
    public void handleNextDay(ActionEvent event) {
        iModel.viewNextDay();
    }


    /**
     * Switches the view based on which view is selected
     * @param event event
     * @param b button selected
     * @param buttons list of buttons
     */
    public void handleViewButton(ActionEvent event, ToggleButton b, ArrayList<ToggleButton> buttons) {
        String selected;
        for (ToggleButton button:buttons) {
            button.setSelected(false);
        }
        b.setSelected(true);
        selected = b.getText();

        switch (selected) {
            case "Month" -> {
                iModel.setViewSelected(CalendarViewSelected.MONTH);
            }
            case "Week" -> {
                iModel.setViewSelected(CalendarViewSelected.WEEK);
            }
            case "Day" -> {
                iModel.setViewSelected(CalendarViewSelected.DAY);;
            }
        }

    }

    /**
     * Adds a category to the tasks that will show up in the view
     * @param s name of category
     */
    public void handleAddCategory(String s) {
        iModel.addCategorySelected(s);
    }

    /**
     * Removes a category to the tasks that will show up in the view
     * @param s name of category
     */
    public void handleRemoveCategory(String s) {
        iModel.removeCategorySelected(s);
    }

    /**
     * On clicking an even in the calendarWeekView, this is called
     * Could also be used for day view and potentially month view
     * @param calendarEvent The event which we are editing the task/event for
     */
    public void handleEditCalendarEvent(CalendarEvent calendarEvent) {
        // link the task to event being modified
        calendarEvent.task.calendarEvent = calendarEvent;

        taskEditor.editTask(calendarEvent.task);
    }

    /**
     * Uses mainUI's editTask method
     */
    public void setTaskEditor(MainUI mainUI) {
        taskEditor = mainUI;
    }

    /**
     * Object to be called to auto schedule
     */
    public void setAutoScheduler(AutoScheduler autoScheduler) {
        this.autoScheduler = autoScheduler;
    }

    /**
     * Removes a calendar event in the list
     * @param c event
     */
    public void handleRemoveCalendarEvent(CalendarEvent c) {
        model.removeEvent(c);
    }

    /**
     * Calls auto scheduler
     */
    public void handleAutoScheduleButtonPressed() {
        this.autoScheduler.autoSchedule();
    }
}
