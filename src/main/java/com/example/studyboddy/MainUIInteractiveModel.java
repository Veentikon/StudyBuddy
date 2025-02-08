package com.example.studyboddy;

import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.ArrayList;

public class MainUIInteractiveModel {

    ArrayList<iModelListener> subscribers;
    public enum views {CALENDAR ,PQ, FlashCards, INCENTIVE}
    private views currentView;

    public MainUIInteractiveModel() {
        subscribers = new ArrayList<>();
        currentView = views.CALENDAR;

    }

    public void setCurrentView(views newView) {
        currentView = newView;
        notifySubscribers();
    }

    public views getCurrentView() {
        return currentView;
    }

    public void addSubscriber(iModelListener sub) {
        subscribers.add(sub);
    }

    private void notifySubscribers() {
        subscribers.forEach(iModelListener::iModelChanged);
    }


}
