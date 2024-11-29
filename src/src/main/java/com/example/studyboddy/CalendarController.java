package com.example.studyboddy;

import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;

import java.time.Month;

public class CalendarController {
    private CalendarModel model;

    public CalendarController() {

    }

    public void setModel(CalendarModel calendarModel) {
        this.model = calendarModel;
    }

    public void handleAddButton(ActionEvent event) {
        // handle an Add button which adds a task
    }

    public void handleMView(ActionEvent event, ToggleButton b1, ToggleButton b2) {
        b1.setSelected(false);
        b2.setSelected(false);
    }

    public void handleWView(ActionEvent event, ToggleButton b1, ToggleButton b2) {
        b1.setSelected(false);
        b2.setSelected(false);
    }

    public void handleDView(ActionEvent event, ToggleButton b1, ToggleButton b2) {
        b1.setSelected(false);
        b2.setSelected(false);
    }


    public void handlePrevMonthBtn(ActionEvent event, Integer current, Label l) {
        current -= 1;
        l.setText(Month.of(current).toString());
    }

    public void handleNextMonthBtn(ActionEvent event, Integer current, Label l) {
        current += 1;
        l.setText(Month.of(current).toString());
    }
}
