package com.example.studyboddy;

import javafx.scene.paint.Color;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

/** Automatically schedules events in CalendarModel */
public class AutoScheduler {
    private CalendarModel calendarModel;
    private TaskManager taskManager;

    public AutoScheduler() {

    }

    public void setCalendarModel(CalendarModel model) {
        calendarModel = model;
    }

    public void setTaskManager(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    /**
     * Automatically schedules tasks
     */
    public void autoSchedule() {
        // what category we use to look for times available to the auto scheduler
        String autoScheduleString = "Auto-schedule";

        List<String> autoScheduleCategoryList = new ArrayList<>();
        autoScheduleCategoryList.add(autoScheduleString);
        // get list of all times we have to work with
        List<CalendarEvent> designatedTimes = calendarModel.getEventsInRange(CalendarModel.universalStart, CalendarModel.universalEnd, autoScheduleCategoryList);

        // get list of all categories minus auto scheduler
        List<String> categoriesImmutable = calendarModel.getCategoriesList(CalendarModel.universalStart, CalendarModel.universalEnd);
        List<String> categories = new ArrayList<>(categoriesImmutable);
        // remove Auto-schedule from categories
        int scheduleStringFoundIndex = categories.indexOf(autoScheduleString);
        if (scheduleStringFoundIndex != -1) {
            categories.remove(scheduleStringFoundIndex);
        }

        // force designated times to not overlap
        List<CalendarEvent> nonOverlappingTimes = new ArrayList<>();
        while (designatedTimes.size() > 0) {
            // get the times overlapping
            CalendarEvent calendarEvent = designatedTimes.get(0);
            List<CalendarEvent> overlappingTimes = calendarModel.getEventsInRange(calendarEvent.startTime, calendarEvent.endTime, categories);

            if (overlappingTimes.size() == 0) {
                // if no overlap, move them over
                nonOverlappingTimes.add(calendarEvent);
                designatedTimes.remove(0);
            }
            else {
                // if there is overlap,
                nonOverlappingTimes.addAll(sliceCalendarEvent(calendarEvent, overlappingTimes));
                designatedTimes.remove(0);
            }
        }
        designatedTimes.addAll(nonOverlappingTimes);

        // TODO: make designated times not be allowed to overlap (probably just remove themself from intersect instead of entire category)
        // order designated times by start time
        Collections.sort(designatedTimes, new Comparator<CalendarEvent>() {
            @Override
            public int compare(CalendarEvent o1, CalendarEvent o2) {
                return o1.startTime.compareTo(o2.startTime);
            }
        });

        // get list of tasks ordered by due date
        List<Task> tasks = taskManager.getTaskList();
        Collections.sort(tasks, new Comparator<Task>() {
            @Override
            public int compare(Task o1, Task o2) {
                return o1.dueDate.compareTo(o2.dueDate);
            }
        });

        Date currentTime = new Date();

        // list of ints parallel to tasks, indicating how much more time needs to be scheduled for the given task
        List<Integer> taskDurationToAdd = new ArrayList<>();
        for (Task task : tasks) {
            // in minutes
            int timeToSchedule = task.duration - task.timeWorkedOn;

            // also account for all other scheduled times
            List<CalendarEvent> eventsForTask = calendarModel.getEventsForTask(task);
            for (CalendarEvent event : eventsForTask) {
                if (!event.endTime.before(currentTime)) {
                    // only consider this if the event is in the future
                    long eventDurationInMinutes = (event.endTime.getTime() - event.startTime.getTime()) / (1000 * 60);
                    timeToSchedule -= eventDurationInMinutes;
                }
            }

            taskDurationToAdd.add(timeToSchedule);
        }

        // parallel list of colors for those tasks
        List<Color> taskColors = new ArrayList<>();
        for (int i = 0 ; i < tasks.size(); i++) {
            taskColors.add(getRandomColor());
        }

        // index of which task we are currently adding
        int taskBeingAdded = 0;

        // fill the designated time slots with tasks
        for (CalendarEvent designatedTime : designatedTimes) {
            if (!designatedTime.endTime.before(currentTime)) {
                // if the designated time is over, skip it
                Date timeScheduling = designatedTime.startTime;
                Date endTimeScheduling = designatedTime.endTime;

                // as long as we have time to schedule, and tasks to schedule
                while (timeScheduling.before(endTimeScheduling) && taskBeingAdded < tasks.size()) {
                    // how long we want to schedule
                    int minutesToSchedule = taskDurationToAdd.get(taskBeingAdded);

                    // what the end time would be if we scheduled the entire event
                    Calendar c = Calendar.getInstance();
                    c.setTime(timeScheduling);
                    c.add(Calendar.MINUTE, minutesToSchedule);

                    // if the task is already fully scheduled, just increment what task we are working on and move on
                    if (minutesToSchedule <=0) {
                        taskBeingAdded += 1;
                    }

                    // the entire event can be scheduled in this time slot
                    else if (c.getTime().before(endTimeScheduling)) {
                        // schedule the calendar event
                        CalendarEvent newEvent = new CalendarEvent(tasks.get(taskBeingAdded).taskName, tasks.get(taskBeingAdded), timeScheduling, c.getTime());
                        newEvent.setDisplayColor(taskColors.get(taskBeingAdded));
                        calendarModel.addCalendarEvent(newEvent);

                        // increment the time scheduling, and task added
                        timeScheduling = c.getTime();
                        taskBeingAdded += 1;

                    }
                    // the event could not all fit in this time slot
                    else {
                        // schedule the calendar event here
                        CalendarEvent newEvent = new CalendarEvent(tasks.get(taskBeingAdded).taskName, tasks.get(taskBeingAdded), timeScheduling, endTimeScheduling);
                        newEvent.setDisplayColor(taskColors.get(taskBeingAdded));
                        calendarModel.addCalendarEvent(newEvent);

                        // subtract time to schedule
                        long minutesScheduled = (endTimeScheduling.getTime() - timeScheduling.getTime()) / (1000 * 60);
                        minutesToSchedule -= minutesScheduled;
                        taskDurationToAdd.set(taskBeingAdded, minutesToSchedule);

                        // increment the time scheduling
                        timeScheduling = endTimeScheduling;
                    }
                }
            }
        }
    }

    /**
     * Get a list of events that populate eventToSlice 's time slot without intersecting with intersectingEvents
     * @param eventToSlice The event to be sliced up so that it does not intersect
     * @param intersectingEvents List of events to slice the event around
     * @return A list of events that do not intersect with intersectingEvents, and fill eventToSlice's time slot
     */
    private List<CalendarEvent> sliceCalendarEvent(CalendarEvent eventToSlice, List<CalendarEvent> intersectingEvents) {
        List<CalendarEvent> undetermined = new ArrayList<>();
        undetermined.add(eventToSlice);
        List<CalendarEvent> nonIntersecting = new ArrayList<>();

        while(undetermined.size() !=0) {
            CalendarEvent eventToCheck = undetermined.get(0);

            // for this calendar event, check against possible intersections
            boolean intersected = false;
            for (int i = 0; i < intersectingEvents.size() && !intersected; i++) {

                // if it intersects, remove it from undetermined, and add the sliced children back
                CalendarEvent possibleIntersection = intersectingEvents.get(i);
                if (eventToCheck.intersects(possibleIntersection)) {
                    // slice it up
                    List<CalendarEvent> sliced = eventToCheck.sliceAroundEvent(possibleIntersection);
                    undetermined.addAll(sliced);
                    undetermined.remove(0);

                    intersected = true;
                }
            }

            // if there was no intersection, move it to non-intersecting
            if (!intersected) {
                nonIntersecting.add(eventToCheck);
                undetermined.remove(0);
            }
        }

        return nonIntersecting;
    }

    private Color getRandomColor() {
        Random rand = new Random();
        // Java 'Color' class takes 3 floats, from 0 to 1.
        float r = rand.nextFloat();
        float g = rand.nextFloat();
        float b = rand.nextFloat();
        return Color.color(r, g, b);
    }
}
