package com.example.studyboddy;

import java.util.Date;

public class Task {
    public String taskName;

    public String taskDescription;

    public Date dueDate;

    public Date completionDate;

    public int duration;

    public int timeWorkedOn;

    public TaskStatus status;

    public int priorityLevel;

    public String category;

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
}
