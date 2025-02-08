package com.example.studyboddy;

import java.io.Serializable;
import java.util.Date;

public class Task implements Serializable {
    public String taskName;

    public String taskDescription;

    public Date dueDate;

    public Date completionDate;

    // total time it would to take to complete in minutes
    public int duration;

    // total time already worked on in minutes
    public int timeWorkedOn;

    public TaskStatus status;

    public int priorityLevel;

    public String category;

    public boolean markedForMovement;

    public int ID;

    /** If there is no calendar even scheduled, this is null */
    public CalendarEvent calendarEvent;

    public Task(String name) {
        taskName = name;
        taskDescription = null;
        dueDate = null;
        completionDate = null;
        duration = 0;
        timeWorkedOn = 0;
        status = TaskStatus.UNSTARTED;
        priorityLevel = 0;
        category = "Uncategorized";
    }


    public Task(String name, String description) {
        taskName = name;
        taskDescription = description;
        dueDate = null;
        completionDate = null;
        duration = 0;
        timeWorkedOn = 0;
        status = TaskStatus.UNSTARTED;
        priorityLevel = 0;
        category = "Uncategorized";
    }

    public Task(String name, String description, String category) {
        taskName = name;
        taskDescription = description;
        dueDate = null;
        completionDate = null;
        duration = 0;
        timeWorkedOn = 0;
        status = TaskStatus.UNSTARTED;
        priorityLevel = 0;
        this.category = category;
    }

    public Task(String name, String description, Date dueDate, int duration, int priorityLevel, String category, TaskStatus status) {
        taskName = name;
        taskDescription = description;
        this.dueDate = dueDate;
        completionDate = null;
        this.duration = duration;
        timeWorkedOn = 0;
        this.status = status;
        this.priorityLevel = priorityLevel;
        this.category = category;

    }

    public void setID(int id) {
        this.ID = id;
    }

    public void setCalendarEvent(CalendarEvent ce) {
        this.calendarEvent = ce;
    }

}
