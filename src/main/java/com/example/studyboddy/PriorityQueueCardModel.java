package com.example.studyboddy;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class PriorityQueueCardModel {

    public String columnHeader;
    public ObservableList<QueueStatus> observableList;

    public PriorityQueueCardModel(String columnHeader){
        this.columnHeader = columnHeader;
        QueueStatus queueStatus = new QueueStatus("Test LMAO");
        this.observableList = FXCollections.observableArrayList(queueStatus);
    }

}
