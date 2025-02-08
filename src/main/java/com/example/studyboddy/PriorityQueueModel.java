package com.example.studyboddy;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.Objects;

public class PriorityQueueModel {
    private ArrayList<ModelListener> subscribers;
    private ArrayList<PriorityQueueCardModel> pqcm;

    ArrayList<String> headerList;


    public PriorityQueueModel() {
        subscribers = new ArrayList<>();
        pqcm = new ArrayList<>();
        headerList = new ArrayList<>();

    }

    public ArrayList<PriorityQueueCardModel> getPQCM(){
        return pqcm;
    }
    public void addPQMStack(String string){
        PriorityQueueCardModel priorityQueueCardModel = new PriorityQueueCardModel(string);
        pqcm.add(priorityQueueCardModel);
        headerList.add(string);
        notifySubscribers();
    }

    public void addSubscriber(ModelListener sub) {
        subscribers.add(sub);
    }

    private void notifySubscribers() {
        subscribers.forEach(ModelListener::modelChanged);
    }

    public void addToColumn(String text, String column) {
        pqcm.forEach(qc -> {
            if (Objects.equals(qc.columnHeader, column)){
                qc.observableList.add(new QueueStatus(text));
            }
        });
        notifySubscribers();
    }

    public void deleteAllThatIsMArked() {
        for (int i = 0; i < pqcm.size(); i++) {
            PriorityQueueCardModel pqcmGet = pqcm.get(i);
            ObservableList<QueueStatus> olqs = FXCollections.observableArrayList();
            for (int j = 0; j < pqcmGet.observableList.size(); j++) {
                if(pqcmGet.observableList.get(j).taskStatus.equals("Marked For Deletion")){
                    olqs.add(pqcmGet.observableList.get(j));
                }
            }
            pqcmGet.observableList.removeAll(olqs);

        }
        notifySubscribers();
    }
}
