package com.example.studyboddy;

import javafx.collections.FXCollections;
import javafx.geometry.Pos;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import javax.crypto.spec.PSource;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class TimeSelectorWidget extends Pane {
    DatePicker datePicker;
    TextField hourField;
    TextField minuteField;

    ComboBox<String> ampmSelector;

    public TimeSelectorWidget() {
        datePicker = new DatePicker();
        hourField = new TextField();
        minuteField = new TextField();

        //Disables the textfield, because it is useless
        datePicker.getEditor().setDisable(true);

        hourField.setPrefWidth(60);
        minuteField.setPrefWidth(60);

        VBox root = new VBox();
        this.getChildren().add(root);

        String[] ampmList = new String[] {"am", "pm"};
        ampmSelector = new ComboBox<String>(FXCollections.observableArrayList(ampmList));
        ampmSelector.getSelectionModel().selectFirst();

        Label timeLabel = new Label("Time: ");
        Label colonLabel = new Label(" : ");

        Label dateLabel = new Label("Date: ");

        // add date picker
        HBox dateBox = new HBox();
        dateBox.setAlignment(Pos.CENTER_LEFT);
        dateBox.getChildren().addAll(dateLabel, datePicker);
        root.getChildren().add(dateBox);

        // add time of day picker as hbox
        HBox timeBox = new HBox();
        timeBox.setAlignment(Pos.CENTER_LEFT);
        timeBox.getChildren().addAll(timeLabel, hourField, colonLabel, minuteField, ampmSelector);
        root.getChildren().add(timeBox);
    }

    /**
     * Set time displayed on widget to given time
     * @param selected Date for what time is to be selected
     */
    public void setTimeSelected(Date selected) {
        datePicker.setValue(selected.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        int hour = selected.getHours();
        int minute = selected.getMinutes();
        //System.out.println(hour + " " + minute);

        boolean pm = hour >= 12;
        ampmSelector.getSelectionModel().select(pm ? 1 : 0);

        // get which hour it is
        int hourSelected;
        if (hour == 0) {
            hourSelected = 12;
        } else if (hour <= 12) {
            hourSelected = hour;
        } else {
            hourSelected = hour - 12;
        }
        hourField.setText(hourSelected + "");

        minuteField.setText(minute + "");
    }

    /**
     * Gets date selected, or null if invalid
     * @return Date for what is selected; or null if anything is invalid
     */
    public Date getTimeSelected() {
        // get date at start of day
        LocalDate localDate = datePicker.getValue();
        if (localDate == null) {return null;}
        Instant instant = Instant.from(localDate.atStartOfDay(ZoneId.systemDefault()));
        Date date = Date.from(instant);

        int hourSelected;
        int minuteSelected;
        boolean amSelected;

        // if invalid hour, minute, or am/pm unselected, return null
        try {
            hourSelected = Integer.parseInt(hourField.getText());
            minuteSelected = Integer.parseInt(minuteField.getText());
            amSelected = "am".equals(ampmSelector.getValue());
        } catch (NumberFormatException e) {
            return null;
        }

        // if invalid hour or minute, return null
        if (hourSelected <= 0 || hourSelected > 12) {return null;}
        if (minuteSelected < 0 || minuteSelected >= 60) {return null;}

        // set the hour we selected
        int hours;
        if (amSelected) {
            // midnight is hour 0
            if (hourSelected == 12) {
                hours = 0;
            }
            else {
                // 1 am is hour 1 (second hour); and so on...
                hours = hourSelected;
            }
        }
        else {
            // 12pm AKA Noon is hour 12 (the 13th hour)
            if (hourSelected == 12) {
                hours = 12;
            }
            else {
                // 1pm is hour 13 (the 14th hour); and so on...
                hours = hourSelected + 12;
            }
        }
        date.setHours(hours);

        // set the minute we selected
        date.setMinutes(minuteSelected);
        return date;
    }
}
