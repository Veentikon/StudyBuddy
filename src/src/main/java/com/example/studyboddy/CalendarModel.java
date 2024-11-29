package com.example.studyboddy;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;

import java.util.*;

/**
 * Model to contain events scheduled in calendar
 * IT HAS NOT BEEN TESTED
 */
public class CalendarModel {
    private ObservableList<CalendarEvent> eventList;

    // unused
    private SortedList<CalendarEvent> sortedList;

    // time before and after anything scheduled
    public static final Date universalStart = new Date(0);
    public static final Date universalEnd = new Date(Long.MAX_VALUE);

    public CalendarModel() {
        eventList = FXCollections.observableArrayList();
        sortedList = new SortedList<>(eventList);
    }

    public void addCalendarEvent(CalendarEvent newEvent) {
        this.eventList.add(newEvent);
    }

    /**
     * Gets event at given index; or null if out of range
     * @param index hopefully valid index
     * @return Returns calendar event at given index, or null if index is invalid
     */
    public CalendarEvent getCalendarEvent(int index) {
        try {
            return this.eventList.get(index);
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
    }

    public int getEventAmount() {
        return this.eventList.size();
    }

    /**
     * Gets list of categories in given time range
     * @param start start time to look at tasks
     * @param end end time to look at tasks
     * @return List of strings, where a string is in the list if that category was present in the given time range
     */
    public List<String> getCategoriesList(Date start, Date end) {
        // set of categories for speed of contains
        Set<String> categories = new HashSet<>();

        // get the events in range
        List<CalendarEvent> inRangeEvents = this.getEventsInRange(start, end);

        // get all the category strings in a list
        for (CalendarEvent event : inRangeEvents) {
            categories.add(event.task.category);
        }

        return categories.stream().toList();
    }

    /**
     * Gets events that intersect with given range
     * @param start the start of the range
     * @param end the end of the range
     * @return A list of events that take place during given range
     */
    public List<CalendarEvent> getEventsInRange(Date start, Date end) {
        List<CalendarEvent> inRangeEvents = new ArrayList<>();

        for (CalendarEvent event : eventList) {
            // they intersect if the start or end is in range
            // OR the start is before and end is after
            if ( (event.startTime.before(start) && event.endTime.after(end)) ||
                    (event.startTime.after(start) && event.startTime.before(end)) ||
                    (event.endTime.after(start) && event.endTime.before(end))) {
                inRangeEvents.add(event);
            }
        }
        return inRangeEvents;
    }

    /**
     * Gets events that intersect with given range, AND are within given categories
     * @param start the start of the range
     * @param end the end of the range
     * @param categories A list of the categores we want to include
     * @return A list of events that take place during given range
     *         AND are in the given list of categories
     */
    public List<CalendarEvent> getEventsInRange(Date start, Date end, List<String> categories) {
        List<CalendarEvent> inRangeEvents = this.getEventsInRange(start, end);
        ArrayList<CalendarEvent> filteredEvents = new ArrayList<>();

        for (CalendarEvent event : inRangeEvents) {
            if (categories.contains(event.task.category)) {
                filteredEvents.add(event);
            }
        }

        return filteredEvents;
    }

    /** TEST SCRIPT */
    public static void main(String[] args) {
        CalendarModel calendarModel = new CalendarModel();

        Task task1 = new Task("Test script", "Test this script");
        //CalendarEvent event1 = new CalendarEvent("Scheduled testing", task1, Calendar.getInstance().set(2022, 10, 19, 11, 15, 45 ), Calendar.getInstance().set(2022, 10, 19, 12, 0));
        CalendarEvent event1 = new CalendarEvent("Scheduled testing", task1, new Date(2022 - 1900, Calendar.OCTOBER, 19, 11, 15), new Date(2022 - 1900, Calendar.OCTOBER, 19, 12, 0));
        calendarModel.addCalendarEvent(event1);

        Task task2 = new Task("Bike", "Bike to school");
        task2.category = "Transit";
        //CalendarEvent event1 = new CalendarEvent("Scheduled testing", task1, Calendar.getInstance().set(2022, 10, 19, 11, 15, 45 ), Calendar.getInstance().set(2022, 10, 19, 12, 0));
        CalendarEvent event2 = new CalendarEvent("Bike to school", task2, new Date(2022 - 1900, Calendar.OCTOBER, 19, 12, 0), new Date(2022 - 1900, Calendar.OCTOBER, 19, 12, 20));
        calendarModel.addCalendarEvent(event2);

        // get all events on oct 19
        List<CalendarEvent> events19th = calendarModel.getEventsInRange(new Date(2022 - 1900, Calendar.OCTOBER, 19), new Date(2022 - 1900, Calendar.OCTOBER, 19, 23, 59, 59));
        System.out.println("Events on the 19th");
        System.out.println(events19th);

        // get all events on oct 20
        List<CalendarEvent> events20th = calendarModel.getEventsInRange(new Date(2022 - 1900, Calendar.OCTOBER, 20), new Date(2022 - 1900, Calendar.OCTOBER, 20, 23, 59, 59));
        System.out.println("Events ont the 20th");
        System.out.println(events20th);

        // get categories thus far
        List<String> events = calendarModel.getCategoriesList(universalStart, universalEnd);
        System.out.println("All categories:");
        System.out.println(events);

        // get all transit events on the 19th
        List<CalendarEvent> events19thTransit = calendarModel.getEventsInRange(new Date(2022 - 1900, Calendar.OCTOBER, 19), new Date(2022 - 1900, Calendar.OCTOBER, 19, 23, 59, 59), List.of(new String[]{"Transit"}));
        System.out.println("Transit only Events on the 19th");
        System.out.println(events19thTransit);
    }
}
