package com.example.studyboddy;

import javafx.event.EventHandler;

import java.util.Date;

public class CalendarEvent {
    public String eventName;

    public Task task;

    public Date startTime;

    public Date endTime;

    /**
     * Event to be displayed to calendar
     * @param eventName Name of the event; not null
     * @param task Possibly null task
     * @param startTime Time to start event
     * @param endTime Time to finish event
     */
    public CalendarEvent(String eventName, Task task, Date startTime, Date endTime) {
        this.eventName = eventName;
        this.task = task;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public String toString() {
        return eventName + ", " + task + " from " + startTime + "-" + endTime;
    }
}
