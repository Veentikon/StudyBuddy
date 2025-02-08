package com.example.studyboddy;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class CalendarIModel {
    private List<iModelListener> subscribers;

    private Date dayViewing;    // start for the day we are viewing

    private Date weekViewing;   // start of the week we are viewing

    private Date monthViewing;  // start of the month we are viewing

    private CalendarViewSelected selected;

    private ArrayList<String> selectedCategories;   // selected categories

    /**
     * Constructor for the calendar interaction model
     */
    public CalendarIModel() {
        subscribers = new ArrayList<>();
        selectedCategories = new ArrayList<>();
        selectedCategories.add("Assignment");
        selectedCategories.add("Transit");
        selectedCategories.add("Exam");
        selectedCategories.add("Uncategorized");
        selectedCategories.add("Auto-schedule");

        selected = CalendarViewSelected.MONTH;

        Date currentTime = new Date();  // initialized to current time

        // for the month viewing
        Date startOfDay = new Date(currentTime.getYear(), currentTime.getMonth(), currentTime.getDate());   // prevent conflicts
        monthViewing = startOfDay;

        // for the week viewing
        startOfDay = new Date(currentTime.getYear(), currentTime.getMonth(), currentTime.getDate());
        int dayOfWeek = startOfDay.getDay();    // get which day we are (0-6)
        Calendar cal = Calendar.getInstance();  // make the calendar subtract that
        cal.setTime(startOfDay);
        cal.add(Calendar.DATE, -dayOfWeek);
        weekViewing = cal.getTime();            // this is the beginning of sunday of the current week

        // for the day viewing
        startOfDay = new Date(currentTime.getYear(),currentTime.getMonth(),currentTime.getDate()-1,24,0);
        dayViewing = startOfDay;

     }


    /**
     * Adds a category to the selected list of category that will show up
     * @param category name of category
     */
    public void addCategorySelected(String category) {
        for (String c:selectedCategories) {
            if (c.equals(category)) {
                return;                         // The category is already in the selected list
            }
        }
        selectedCategories.add(category);
        notifySubscribers();
    }

    /**
     * Removes a category to the selected list of category that will show up
     * @param category name of category
     */
    public void removeCategorySelected(String category) {
        selectedCategories.removeIf(c -> c.equals(category));
        notifySubscribers();
    }

    /**
     * Returns the current date of the month view
     * @return date
     */
    public Date getMonthViewing() {
        return monthViewing;
    }

    /**
     * Increment the month viewing by 1
     */
    public void viewNextMonth() {
        Calendar c = Calendar.getInstance();
        c.setTime(monthViewing);
        c.roll(Calendar.MONTH,true);
        if (c.getTime().getMonth() == Calendar.JANUARY) {
            c.add(Calendar.YEAR,1);
        }
        monthViewing = c.getTime();
        notifySubscribers();
    }

    /**
     * Decrement the month viewing by 1
     */
    public void viewPrevMonth() {
        Calendar c = Calendar.getInstance();
        c.setTime(monthViewing);
        c.roll(Calendar.MONTH,false);
        if (c.getTime().getMonth() == Calendar.DECEMBER) {
            c.add(Calendar.YEAR,-1);
        }
        monthViewing = c.getTime();
        notifySubscribers();
    }

    /**
     * Sets the month viewing
     * @param d date
     */
    public void setMonthViewing(Date d) {
        monthViewing = d;
        notifySubscribers();
    }

    /**
     * Returns the current date of the day view
     * @return date
     */
    public Date getDayViewing() {
        return dayViewing;
    }

    /**
     * Increment the day viewing by 1
     */
    public void viewNextDay() {
        Calendar c = Calendar.getInstance();
        c.setTime(dayViewing);
        c.add(Calendar.DATE, 1);
        dayViewing = c.getTime();
        notifySubscribers();
    }

    /**
     * Increment day multiple times
     */
    public void viewNextDayTimes(int number) {
        for (int i = 0; i < number; i += 1) {
            viewNextDay();
        }
    }

    /**
     * Decrement the day viewing by 1
     */
    public void viewPrevDay() {
        Calendar c = Calendar.getInstance();
        c.setTime(dayViewing);
        c.add(Calendar.DATE, -1);
        dayViewing = c.getTime();
        notifySubscribers();
    }

    /**
     * Decrement day multiple times
     */
    public void viewPrevDayTimes(int number) {
        for (int i = 0; i < number; i += 1) {
            viewPrevDay();
        }
    }

    /**
     * Sets the day viewing
     * @param d date
     */
    public void setDayViewing(Date d) {
        dayViewing = d;
        notifySubscribers();
    }

    /**
     * Get the start of the current week being viewed
     */
    public Date getWeekViewing() {
        return weekViewing;
    }

    /**
     * Increment the week viewing by 1
     */
    public void viewNextWeek() {
        // increment week viewing by 7 days
        Calendar c = Calendar.getInstance();
        c.setTime(weekViewing);
        c.add(Calendar.DATE, 7);
        weekViewing = c.getTime();
        notifySubscribers();
    }

    /**
     * Decrement the week viewing by 1
     */
    public void viewPreviousWeek() {
        // increment week viewing by 7 days
        Calendar c = Calendar.getInstance();
        c.setTime(weekViewing);
        c.add(Calendar.DATE, -7);
        weekViewing = c.getTime();
        notifySubscribers();
    }

    /**
     * Sets the week viewing
     * @param d date
     */
    public void setWeekViewing(Date d) {
        weekViewing = d;
        notifySubscribers();
    }


    /**
     * Switches the view selected from month,day, or view
     * @param selection selected view
     */
    public void setViewSelected(CalendarViewSelected selection) {
        this.selected = selection;
        notifySubscribers();
    }

    /**
     * Get the current view selected
     * @return view selected
     */
    public CalendarViewSelected getViewSelected() {
        return selected;
    }


    /**
     * Add subscriber to the calendar iModel
     * @param sub subscriber
     */
    public void addSubscriber(iModelListener sub) {
        subscribers.add(sub);
    }

    /**
     * Notify the subscribers of the iModel
     */
    private void notifySubscribers() {
        subscribers.forEach(iModelListener::iModelChanged);
    }

    /**
     * Return the selected categories that will show up in the view
     * @return list of categories
     */
    public ArrayList<String> getCategories() {
        return selectedCategories;
    }
}
