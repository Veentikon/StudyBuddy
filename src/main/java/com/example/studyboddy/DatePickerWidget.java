package com.example.studyboddy;

import javafx.collections.FXCollections;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.HBox;

public class DatePickerWidget extends HBox {

    private String[] months = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};


    public  DatePickerWidget() {
        this.setSpacing(5);

        ComboBox<String> monthBox = new ComboBox<>(FXCollections.observableArrayList(months));


        Integer[] days = {1, 2, 3};
        ComboBox<Integer> dayBox = new ComboBox<>(FXCollections.observableArrayList(days));

        Integer[] years = {2022, 2023};
        ComboBox<Integer> yearBox = new ComboBox<>(FXCollections.observableArrayList(years));
        yearBox.getSelectionModel().selectFirst();


        this.getChildren().addAll(monthBox, dayBox, yearBox);



    }

}
