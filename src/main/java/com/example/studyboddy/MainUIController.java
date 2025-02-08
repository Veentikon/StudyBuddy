package com.example.studyboddy;

import javafx.scene.paint.Color;

import java.util.Date;

public class MainUIController {

    MainUIInteractiveModel iModel;
    TaskManager TaskModel;
    CalendarModel calendarModel;

    public void changeView(MainUIInteractiveModel.views newView) {
        iModel.setCurrentView(newView);
    }

    public void setiModel(MainUIInteractiveModel iModel) {
        this.iModel = iModel;
    }

    public void setTaskModel(TaskManager taskModel) {
        this.TaskModel = taskModel;
    }

    public void setCalendarModel(CalendarModel calendarModel) {
        this.calendarModel = calendarModel;
    }

    public void saveTask(String name, String description, Date dueDate, int duration, int priorityLevel, String category, Integer ID, boolean isCalendarEvent, Date eventStartTime, Date eventEndTime, Color scheduledColor, TaskStatus status) {


        if (ID == null) {
            // we are creating a new task
            Task newTask = new Task(name, description, dueDate, duration, priorityLevel, category, status);
            TaskModel.addTask(newTask);

            // update calendar event
            if(isCalendarEvent) {
                CalendarEvent calendarEvent = new CalendarEvent(name, newTask, eventStartTime, eventEndTime);
                calendarEvent.setDisplayColor(scheduledColor);
                newTask.setCalendarEvent(calendarEvent);
                calendarModel.addCalendarEvent(calendarEvent);
            }
        }
        else {
            // we are modifying an existing task
            TaskModel.updateTask(name, description, dueDate, duration, priorityLevel, category, ID, status);
            if(isCalendarEvent) {
                // get the calendar event
                Task currentTask = TaskModel.getTaskByID(ID);
                CalendarEvent calendarEvent = currentTask.calendarEvent;

                // in case there was originally no event, make one
                if (calendarEvent == null) {
                    calendarEvent = new CalendarEvent(name, currentTask, eventStartTime, eventEndTime);
                    currentTask.calendarEvent = calendarEvent;

                    calendarModel.addCalendarEvent(calendarEvent);
                }

                calendarEvent.eventName = name;
                calendarEvent.setDisplayColor(scheduledColor);
                calendarModel.changeEventTime(calendarEvent, eventStartTime, eventEndTime);
            }
            else {
                calendarModel.removeEvent(TaskModel.getTaskByID(ID).calendarEvent);
            }
        }
    }

    public void editTask(int id) {
        TaskModel.editTask(id);
    }

    public void close() {
        TaskModel.closeNewTask();
    }
}
