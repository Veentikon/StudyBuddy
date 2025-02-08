package com.example.studyboddy;

import javafx.event.EventHandler;
import javafx.scene.paint.Color;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class CalendarEvent implements Serializable {
    public String eventName;

    public Task task;

    public Date startTime;

    public Date endTime;

    // Color is not serializable (bruh)
    public SerializableColor displayColor;

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

    public boolean intersects(CalendarEvent other) {
        Date start = this.startTime;
        Date end = this.endTime;
        // they intersect if the start or end is in range
        // OR the start is before and end is after
        return (other.startTime.before(start) && other.endTime.after(end)) ||
                (other.startTime.after(start) && other.startTime.before(end)) ||
                (other.endTime.after(start) && other.endTime.before(end)) ||
                // VERY IMPORTANT TO CONSIDER PERFECT OVERLAP
                (other.endTime.equals(end) && other.startTime.equals(start));
    }

    /**
     * Determines if given time is within this event
     * @param date the date / time to check
     * @return true if given date is within this event
     */
    public boolean containsTime(Date date) {
        //return !this.startTime.after(date) && !this.endTime.before(date);
        return this.startTime.before(date) && this.endTime.after(date);
    }

    /**
     * Slices this event into two or more pieces so that it doesnt intersect with other
     * @param other other event we are slicing this up so that it does not intersect
     * @return List of slices that do not intersect with other, while maintaining as much of this time slot as possible
     */
    public List<CalendarEvent> sliceAroundEvent(CalendarEvent other) {
        List<CalendarEvent> sliced = new ArrayList<>();

        // if no intersection, then there is no need to slice
        if (!this.intersects(other)) {
            sliced.add(this);
            return sliced;
        }

        // whether the start and end times of other are within this event
        boolean otherStartWithin = this.containsTime(other.startTime);
        boolean otherEndWithin = this.containsTime(other.endTime);

        // other entirely covers this, so return nothing
        if (!otherStartWithin && !otherEndWithin) {
            return sliced;
        }
        // this entirely contains other
        else if (otherStartWithin && otherEndWithin) {
            // add left and right slices
            CalendarEvent left = this.getClone();
            left.endTime = other.startTime;
            sliced.add(left);

            CalendarEvent right = this.getClone();
            right.startTime = other.endTime;
            sliced.add(right);
            return sliced;
        }

        // keep left
        else if (otherStartWithin) {
            CalendarEvent left = this.getClone();
            left.endTime = other.startTime;
            sliced.add(left);
            return sliced;
        }

        // keep right
        else if (otherEndWithin) {
            CalendarEvent right = this.getClone();
            right.startTime = other.endTime;
            sliced.add(right);
            return sliced;
        }

        return null;
    }

    // getter and setter for color
    public Color getDisplayColor() {
        return displayColor.getFXColor();
    }

    public void setDisplayColor(Color color) {
        this.displayColor = new SerializableColor(color);
    }

    public String toString() {
        return eventName + ", " + task + " from " + startTime + "-" + endTime;
    }

    public CalendarEvent getClone() {
        CalendarEvent clone = new CalendarEvent(this.eventName, this.task, this.startTime, this.endTime);
        clone.displayColor = this.displayColor;
        return clone;
    }
}
