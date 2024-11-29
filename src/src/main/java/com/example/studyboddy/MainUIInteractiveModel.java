package com.example.studyboddy;

import java.util.ArrayList;

public class MainUIInteractiveModel {

    ArrayList<iModelListener> subscribers;
    public enum views {MAIN, CALENDAR ,PQ, FlashCards}
    private views currentView;

    public MainUIInteractiveModel() {
        subscribers = new ArrayList<>();
        currentView = views.MAIN;

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
