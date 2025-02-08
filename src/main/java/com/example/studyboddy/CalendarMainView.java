package com.example.studyboddy;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.time.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/** Calendar Month View */
public class CalendarMainView extends GridPane implements ModelListener,iModelListener{
    private CalendarModel model;
    private CalendarIModel iModel;
    private CalendarController controller;

    // Month headers
    private Label title;
    private Button prevMonth,nextMonth;

    // Information about a day
    private ArrayList<VBox> days;
    private ArrayList<Label> dayTitleNumbers;
    private ArrayList<VBox> dayContents;

    // To cap the events that can appear in a day
    // and prevent overlapping other vboxes
    private ArrayList<ArrayList<Label>> dayContentCapped;

    private ArrayList<CalendarEvent> inCalendarView;

    private String[] weekDays = {"Sunday","Monday","Tuesday","Wednesday","Thursday","Friday","Saturday"};

    private static final double WIDTH = 700;    // width of the whole calendar

    private static final double VBOX_DAY_SIDES = 100;   // sides for vboxes

    /**
     * Constructor for the month view
     */
    public CalendarMainView() {
        this.setPrefWidth(WIDTH);
        this.setMaxWidth(WIDTH);
        this.setMinWidth(WIDTH);

        // Create the header for the calendar for month
        HBox monthHead = new HBox();
        monthHead.setMinSize(WIDTH, VBOX_DAY_SIDES);
        monthHead.setMaxSize(WIDTH, VBOX_DAY_SIDES);
        monthHead.setPrefSize(WIDTH, VBOX_DAY_SIDES);
        monthHead.setAlignment(Pos.TOP_CENTER);
        monthHead.setSpacing(100);
        monthHead.setPadding(new Insets(20,20,20,20));
        prevMonth = new Button("<");
        prevMonth.setPrefSize(50,50);
        Date now = new Date();
        title = new Label(Month.of(now.getMonth()+1) + " " + (1900+now.getYear()));
        title.setFont(new Font("Regular", 30));
        nextMonth = new Button(">");
        nextMonth.setPrefSize(50,50);
        monthHead.getChildren().addAll(prevMonth,title,nextMonth);
        this.add(monthHead,0,0,7,1);



        // Create subheader for the weekdays
        for (int i = 0; i < 7; i++) {
            VBox weekSubheader = new VBox();
            weekSubheader.setMinSize(VBOX_DAY_SIDES,50);
            weekSubheader.setMaxSize(VBOX_DAY_SIDES,50);
            weekSubheader.setPrefSize(VBOX_DAY_SIDES,50);
            weekSubheader.setStyle("-fx-border-color: black");
            Label sub = new Label(weekDays[i]);
            sub.setFont(new Font("Arial",15));
            weekSubheader.getChildren().add(sub);
            this.add(weekSubheader,i,1,1,1);
        }

        // Creates the individual Vboxes for each days in the month
        days = new ArrayList<>();
        for (int i = 2; i < 8; i++) {
            for (int j = 0; j < 7; j++) {
                VBox day = new VBox();
                day.setMinSize(VBOX_DAY_SIDES,VBOX_DAY_SIDES);
                day.setPrefSize(VBOX_DAY_SIDES,VBOX_DAY_SIDES);
                day.setMaxSize(VBOX_DAY_SIDES,VBOX_DAY_SIDES);
                day.setStyle("-fx-border-color: black");
                this.days.add(day);
                this.add(day,j,i,1,1);
            }
        }

        // Creates a reference to day numbers, day content container, and cap in events
        // that appears in the Vbox
        dayTitleNumbers = new ArrayList<>();
        dayContents = new ArrayList<>();
        dayContentCapped = new ArrayList<>();
        for (int i = 0; i < days.size(); i++) {
            dayContentCapped.add(new ArrayList<>());
            dayTitleNumbers.add(new Label(""));
            days.get(i).getChildren().add(dayTitleNumbers.get(i));
            dayContents.add(new VBox());
            days.get(i).getChildren().add(dayContents.get(i));
        }


        // Sets up the current date
        Calendar c = Calendar.getInstance();
        Date date = new Date(c.getTime().getYear(),c.getTime().getMonth(),1);

        // Calculates total days of each month
        int monthTotalDays = c.getActualMaximum(Calendar.DAY_OF_MONTH);
        int day_of_week = date.getDay();
        for (int i = 0; i < monthTotalDays; i++) {
            dayTitleNumbers.get(i+day_of_week).setText(i+1+"");
        }

        inCalendarView = new ArrayList<>();
    }

    /**
     * Changes the header (month, year)
     */
    private void updateHeader() {
        title.setText(Month.of(iModel.getMonthViewing().getMonth()+1) + " " + (1900+iModel.getMonthViewing().getYear()));
    }

    /**
     * Draw the calendar with the events included
     */
    private void redrawCalendar() {
        for (ArrayList<Label> labelArrayList:dayContentCapped) {
            labelArrayList.clear();
        }
        if (model != null && model.getEventsInRange(CalendarModel.universalStart,CalendarModel.universalEnd,iModel.getCategories()) != null) {
            for (CalendarEvent c : model.getEventsInRange(CalendarModel.universalStart, CalendarModel.universalEnd,iModel.getCategories())) {
                // If already in the view do not draw it again
                if (inCalendarView.contains(c))
                    continue;
                inCalendarView.add(c);
                for (int i = 0; i < days.size(); i++) {
                    if (iModel.getMonthViewing().getYear() == c.startTime.getYear() && iModel.getMonthViewing().getMonth() == c.startTime.getMonth() &&
                            dayTitleNumbers.get(i).getText().equals("" + c.startTime.getDate()) &&
                            dayContentCapped.get(i).size() < 4) {
                        Label label = new Label(c.eventName);
                        if (c.displayColor != null) {
                            label.setStyle("-fx-background-color: " + toHexString(c.getDisplayColor()) + "; -fx-text-fill: " + toHexString(c.getDisplayColor().invert()));
                        }
                        label.setMaxWidth(VBOX_DAY_SIDES-5);
                        label.setPrefWidth(VBOX_DAY_SIDES-5);
                        label.setMinWidth(VBOX_DAY_SIDES-5);

                        createContextMenu(c, label, controller);

                        dayContentCapped.get(i).add(label);
                        dayContents.get(i).getChildren().addAll(label);
                    }
                }
            }
        }
    }

    /**
     * Creates a context menu for a label
     * @param c event
     * @param label label
     * @param controller controller
     */
    static void createContextMenu(CalendarEvent c, Label label, CalendarController controller) {
        ContextMenu menu = new ContextMenu();
        label.setContextMenu(menu);
        MenuItem modifyAction = new MenuItem("Modify");
        MenuItem deleteAction = new MenuItem("Delete");
        menu.getItems().addAll(modifyAction,deleteAction);
        modifyAction.setOnAction(e -> controller.handleEditCalendarEvent(c));
        deleteAction.setOnAction(e -> controller.handleRemoveCalendarEvent(c));
    }

    /**
     * Labels all the days with the correct day labels
     */
    private void adjustDayNumbers() {
        Calendar c = Calendar.getInstance();
        c.setTime(iModel.getMonthViewing());
        Date d = new Date(c.getTime().getYear(),c.getTime().getMonth(),1);

        // Clears the previous day title numbers and events
        for (VBox day:dayContents) {
            day.getChildren().clear();
        }
        for (Label label:dayTitleNumbers) {
            label.setText("");
        }

        // Sets up the new title numbers
        int monthTotalDays = c.getActualMaximum(Calendar.DAY_OF_MONTH);
        int day_of_week = d.getDay();
        for (int i = 0; i < monthTotalDays; i++) {
            dayTitleNumbers.get(i+day_of_week).setText(i+1+"");
        }
    }

    /**
     * Sets the model for the month view
     * @param calendarModel model
     */
    public void setModel(CalendarModel calendarModel) {
        model = calendarModel;
    }

    /**
     * Sets the iModel for the month view
     * @param interactionModel iModel
     */
    public void setIModel(CalendarIModel interactionModel) {
        iModel = interactionModel;
    }

    /**
     * Sets up the controller once for the month view
     * @param controller controller
     */
    public void setController(CalendarController controller) {
        prevMonth.setOnAction(controller::handlePrevMonthBtn);
        nextMonth.setOnAction(controller::handleNextMonthBtn);
        this.controller = controller;
    }

    @Override
    public void modelChanged() {
        updateHeader();
        adjustDayNumbers();
        inCalendarView.clear();
        redrawCalendar();
    }

    @Override
    public void iModelChanged() {
        updateHeader();
        adjustDayNumbers();
        inCalendarView.clear();
        redrawCalendar();
    }

    // For testing purposes

    /**
     * Returns the current label
     * @return title
     */
    public Label getTitle() {
        return title;
    }

    /**
     * Returns the list of day containing the tasks
     * @return list of days
     */
    public ArrayList<VBox> getDayContents() {
        return dayContents;
    }

    private static String toHexString(Color color) {
        int r = ((int) Math.round(color.getRed()     * 255)) << 24;
        int g = ((int) Math.round(color.getGreen()   * 255)) << 16;
        int b = ((int) Math.round(color.getBlue()    * 255)) << 8;
        int a = ((int) Math.round(color.getOpacity() * 255));
        return String.format("#%08X", (r + g + b + a));
    }
}
