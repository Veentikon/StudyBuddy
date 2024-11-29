package com.example.studyboddy;

import java.util.ArrayList;
import java.util.List;

/**
 * Model to contain all the tasks
 * Can be saved to and read from file
 */
public class TaskManager {
    public List<Task> taskList;

    public TaskManager() {
        taskList = new ArrayList<>();
    }

    public void getAsFile() {
        // return or save the list of tasks as a file
    }

    public void readFile() {
        // reads in task list from file
    }
}
