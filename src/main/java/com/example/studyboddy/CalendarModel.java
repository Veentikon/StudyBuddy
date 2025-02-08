package com.example.studyboddy;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;

import java.io.*;
import java.util.*;

/**
 * Model to contain events scheduled in calendar
 * IT HAS NOT BEEN TESTED
 */
public class CalendarModel {
    private List<CalendarEvent> eventList;

    // time before and after anything scheduled
    public static final Date universalStart = new Date(0);
    public static final Date universalEnd = new Date(Long.MAX_VALUE);

    private List<ModelListener> subscribers;

    String path = "savedata/CalendarEvents.bin"; //the save directory for Calendar Manager along with the name

    public CalendarModel() {
        eventList = new ArrayList<>();
        subscribers = new ArrayList<>();
    }

    public void addCalendarEvent(CalendarEvent newEvent) {
        this.eventList.add(newEvent);

        notifySubscribers();
        writeAsFile();
    }

    public void changeEventTime(CalendarEvent calendarEvent, Date eventStartTime, Date eventEndTime) {
        calendarEvent.startTime = eventStartTime;
        calendarEvent.endTime = eventEndTime;
        notifySubscribers();
        writeAsFile();
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
     * Removes calendar event
     * @param toBeRemoved Event to be removed
     */
    public void removeEvent(CalendarEvent toBeRemoved) {
        if (toBeRemoved == null) {
            return;
        }
        eventList.remove(toBeRemoved);
        notifySubscribers();
        writeAsFile();
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
                    (event.endTime.after(start) && event.endTime.before(end)) ||
                    // VERY IMPORTANT TO CONSIDER PERFECT OVERLAP
                    (event.endTime.equals(end) && event.startTime.equals(start))) {
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

    /**
     * Gets all events
     * @return A list of all calendar events
     */
    public List<CalendarEvent> getAllEvents() {
        return getEventsInRange(CalendarModel.universalStart, CalendarModel.universalEnd);
    }

    /**
     * Gets all CalendarEvents for a given task
     * @param task some task to find associated events
     * @return A list of CalendarEvents with that task
     */
    public List<CalendarEvent> getEventsForTask(Task task) {
        List<CalendarEvent> list = new ArrayList<>();

        for (CalendarEvent event : eventList) {
            if (event.task == task) {
                list.add(event);
            }
        }

        return list;
    }

    public void addSubscriber(ModelListener sub) {
        subscribers.add(sub);
    }

    private void notifySubscribers() {
        subscribers.forEach(ModelListener::modelChanged);
    }

    public void writeAsFile() {
        System.out.println("writeAsFile() in CalendarModel");

        try(FileOutputStream f = new FileOutputStream(path)) {
            ObjectOutputStream oos = new ObjectOutputStream(f);

            oos.writeObject(eventList);
            oos.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<CalendarEvent> readFile() {
        System.out.println("readFile() in CalendarModel");

        List<CalendarEvent> art = null;
        try (FileInputStream in = new FileInputStream(path);
        ObjectInputStream s = new ObjectInputStream(in)) {
            art = (List<CalendarEvent>) s.readObject();

        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }

        return art;
    }

    public void setCalendarModelAsReadData(TaskManager taskManager) {
        System.out.println("setCalendarModelAsReadData() in CalendarModel");

        List<CalendarEvent> temp = readFile();
        if (temp != null) {
            this.eventList = temp;

            // probably have to set the task to be a correct pointer
            eventList.forEach(c -> {
                //System.out.println(c.task.calendarEvent == c);
                c.task = taskManager.getTaskByID(c.task.ID);
            });
        }
    }

    /** TEST SCRIPT */
    public static void main(String[] args) {
        CalendarModel calendarModel = new CalendarModel();

        // TC01
        // get all events, should get no events
        List<CalendarEvent> noEvents = calendarModel.getAllEvents();
        if (noEvents.size() != 0) {
            System.out.println("There should be no events, but there were events: ");
            System.out.println(noEvents);
        }

        Task task1 = new Task("Test script", "Test this script");
        CalendarEvent testEvent = new CalendarEvent("Scheduled testing", task1, new Date(2022 - 1900, Calendar.OCTOBER, 19, 11, 15), new Date(2022 - 1900, Calendar.OCTOBER, 19, 12, 0));
        calendarModel.addCalendarEvent(testEvent);

        Task task2 = new Task("Bike", "Bike to school");
        task2.category = "Transit";
        CalendarEvent bikeEvent = new CalendarEvent("Bike to school", task2, new Date(2022 - 1900, Calendar.OCTOBER, 19, 12, 0), new Date(2022 - 1900, Calendar.OCTOBER, 19, 12, 20));
        calendarModel.addCalendarEvent(bikeEvent);

        Task task3 = new Task("Bus", "Bus to school");
        task2.category = "Transit";
        CalendarEvent busEvent = new CalendarEvent("Bus to school", task3, new Date(2022-1900, Calendar.NOVEMBER, 4, 11, 40), new Date(2022-1900, Calendar.NOVEMBER, 4, 12, 10));
        calendarModel.addCalendarEvent(busEvent);

        // TC02
        // get all events on oct 19
        // should have test and bike
        List<CalendarEvent> events19th = calendarModel.getEventsInRange(new Date(2022 - 1900, Calendar.OCTOBER, 19), new Date(2022 - 1900, Calendar.OCTOBER, 19, 23, 59, 59));
        if (!(events19th.contains(testEvent) && events19th.contains(bikeEvent) && events19th.size() == 2)) {
            System.out.println("Error: There should be Test and Bike on the 19th, but there were more or less" );
            System.out.println(events19th);
        }

        // TC 03
        // get all events on oct 20
        // should have no events on 20th
        List<CalendarEvent> events20th = calendarModel.getEventsInRange(new Date(2022 - 1900, Calendar.OCTOBER, 20), new Date(2022 - 1900, Calendar.OCTOBER, 20, 23, 59, 59));
        if (events20th.size() != 0) {
            System.out.println("Error: There should be no events on the 20th, but events were scheduled:");
            System.out.println(events20th);
        }

        // TC 04
        // get categories thus far
        // should have only transit and uncategorized
        List<String> events = calendarModel.getCategoriesList(universalStart, universalEnd);
        if (!(events.contains("Transit") && events.contains("Uncategorized") && events.size() == 2)) {
            System.out.println("Error: should be Transit and Uncategorized categories, but there were:");
            System.out.println(events);
        }

        // TC 05
        // get all transit events on the 19th
        // should have only bike
        List<CalendarEvent> events19thTransit = calendarModel.getEventsInRange(new Date(2022 - 1900, Calendar.OCTOBER, 19), new Date(2022 - 1900, Calendar.OCTOBER, 19, 23, 59, 59), List.of(new String[]{"Transit"}));
        if (!(events19thTransit.contains(bikeEvent) && events19thTransit.size()==1)) {
            System.out.println("Erorr: Transit events on the 19th should only be bike to school, but contained: ");
            System.out.println(events19thTransit);
        }

        // TC 06
        // try to get events where start is after end
        // should get no events
        // TODO fix this bug
        List<CalendarEvent> incorrectTimesList = calendarModel.getEventsInRange(testEvent.endTime, testEvent.startTime);
        if (incorrectTimesList.size() != 0) {
            System.out.println("Error: no events should be in range, because start is after end in range, but got:");
            System.out.println(incorrectTimesList);
        }



        // CREATE NEW MODEL
        CalendarModel model2 = new CalendarModel();

        // TC 07
        // add test event;
        // should only have test event
        model2.addCalendarEvent(testEvent);
        List<CalendarEvent> eventsOneOnly = model2.getAllEvents();
        if (!(eventsOneOnly.contains(testEvent) && eventsOneOnly.size() == 1)) {
            System.out.println("Error: calendar should only contain test event, but contained: ");
            System.out.println(eventsOneOnly);
        }

        // TC 08
        // remove wrong event
        // should still have test event
        model2.removeEvent(bikeEvent);
        List<CalendarEvent> fakeBikeRemovalEvents = model2.getAllEvents();
        if (!(fakeBikeRemovalEvents.contains(testEvent) && fakeBikeRemovalEvents.size() == 1)) {
            System.out.println("Error: calendar should only contain test event, as the wrong event was removed but contained: ");
            System.out.println(fakeBikeRemovalEvents);
        }

        // TC 09
        // remove test event, get events
        // should have no events
        model2.removeEvent(testEvent);
        List<CalendarEvent> noEvents2 = model2.getAllEvents();
        if (!(noEvents2.size()==0)) {
            System.out.println("Error: Calendar should contain no events, as all were removed, but contained: ");
            System.out.println(noEvents2);
        }

        model2.addCalendarEvent(testEvent);

        // TC 10
        // get events, where end of query range is the same as start of an event
        // expected: don't get any event
        List<CalendarEvent> endOfRangeEvents = model2.getEventsInRange(CalendarModel.universalStart, testEvent.startTime);
        if (endOfRangeEvents.size() != 0) {
            System.out.println("Error: Event starting at end of range should not be included, but got:");
            System.out.println(endOfRangeEvents);
        }

        // TC 11
        // get events, where end of query range is just after start of event
        // expected: get that event
        Date startDatePlus1 = new Date(testEvent.startTime.getTime() + 1L);
        List<CalendarEvent> justAfterEndOfRange = model2.getEventsInRange(CalendarModel.universalStart, startDatePlus1);
        if (!(justAfterEndOfRange.contains(testEvent) && justAfterEndOfRange.size() == 1)) {
            System.out.println("Error: end of range is barely after event, so it should include event, but got:");
            System.out.println(justAfterEndOfRange);
        }

        // TC 12
        // get events where end of query range is just before start of event
        // expected: don't get that event
        Date startDateMinus1 = new Date(testEvent.startTime.getTime() - 1L);
        List<CalendarEvent> justBeforeEndOfRange = model2.getEventsInRange(CalendarModel.universalStart, startDateMinus1);
        if (justBeforeEndOfRange.size()!=0) {
            System.out.println("Error: end of range is barely before event, so it should not include event, but got:");
            System.out.println(justBeforeEndOfRange);
        }

        // TC 13
        // get events where start of query range is end of event
        // expected: dont get event
        List<CalendarEvent> startRangeAtEnd = model2.getEventsInRange(testEvent.endTime, CalendarModel.universalEnd);
        if (startRangeAtEnd.size()!=0) {
            System.out.println("Error: Range starting at end of event should not include event, but got:");
            System.out.println(startRangeAtEnd);
        }

        // TC 14
        // get events where start of query range is just before end of event
        // expected: get that event
        Date endDateMinus1 = new Date(testEvent.endTime.getTime() - 1L);
        List<CalendarEvent> startBeforeEnd = model2.getEventsInRange(endDateMinus1, CalendarModel.universalEnd);
        if (!(startBeforeEnd.contains(testEvent) && startBeforeEnd.size()==1)) {
            System.out.println("Error: Range start before end of event, so should contain that event, but got:");
            System.out.println(startBeforeEnd);
        }

        // TC 15
        // get events where start of query range is just after end of event
        // expected: get that event
        Date endDatePlus1 = new Date(testEvent.endTime.getTime() + 1L);
        List<CalendarEvent> startAfterEnd = model2.getEventsInRange(endDatePlus1, CalendarModel.universalEnd);
        if (startAfterEnd.size()!= 0) {
            System.out.println("Error: Range starts after event end, should contain nothing, but got:");
            System.out.println(startAfterEnd);
        }

        // get events where range is inside of events
        // should get that event
        List<CalendarEvent> rangeWithinEvent = model2.getEventsInRange(startDatePlus1, endDateMinus1);
        if (!(rangeWithinEvent.contains(testEvent) && rangeWithinEvent.size()==1)) {
            System.out.println("Error: Range within event should return that event, but got:");
            System.out.println(rangeWithinEvent);
        }

        System.out.println("Calendar Model Test Script Completed!");
    }


}
