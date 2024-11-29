package com.example.studyboddy;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

import java.time.Month;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class CalendarMainView extends GridPane {
    private Label title;
    private Button prevMonth,nextMonth;
    private Integer month;
    private ArrayList<VBox> days;

    private String[] weekDays = {"Sunday","Monday","Tuesday","Wednesday","Thursday","Friday","Saturday"};

    public CalendarMainView() {
        // Sets the size for main view
        this.setPrefWidth(700);
        this.setMaxWidth(700);
        this.setMinWidth(700);

        // Create the header for the calendar for month
        HBox monthHead = new HBox();
        monthHead.setMinSize(700,100);
        monthHead.setMaxSize(700,100);
        monthHead.setPrefSize(700,100);
        monthHead.setAlignment(Pos.TOP_CENTER);
        monthHead.setSpacing(100);
        monthHead.setPadding(new Insets(20,20,20,20));
        prevMonth = new Button("<");
        title = new Label("OCTOBER");
        title.setFont(new Font("Regular", 30));
        nextMonth = new Button(">");
        month = Month.OCTOBER.getValue();
        monthHead.getChildren().addAll(prevMonth,title,nextMonth);
        this.add(monthHead,0,0,7,1);

        // Create subheader for the weekdays
        for (int i = 0; i < 7; i++) {
            VBox weekSubheader = new VBox();
            weekSubheader.setMinSize(100,50);
            weekSubheader.setMaxSize(100,50);
            weekSubheader.setPrefSize(100,50);
            weekSubheader.setStyle("-fx-border-color: black");
            Label sub = new Label(weekDays[i]);
            sub.setFont(new Font("Arial",15));
            weekSubheader.getChildren().add(sub);
            this.add(weekSubheader,i,1,1,1);
        }


        // Creates the vboxes of days
        days = new ArrayList<>();
        for (int i = 2; i < 8; i++) {
            for (int j = 0; j < 7; j++) {
                VBox day = new VBox();
                day.setMinSize(100,100);
                day.setPrefSize(100,100);
                day.setMaxSize(100,100);
                day.setStyle("-fx-border-color: black");
                this.days.add(day);
                this.add(day,j,i,1,1);
            }
        }



        int[] days_in_month = new int[12];
        for (int i = 0; i < 12; i++) {
            YearMonth yearMonth = YearMonth.of(2022,i+1);
            days_in_month[i] = yearMonth.lengthOfMonth();
        }
        for (int i = 0; i < days_in_month[9]; i++) {

            days.get(i).getChildren().add(new Label(i+1+""));
        }

//        doStuff();
    }

    /**
     * Positions labels, so they take up vertical space corresponding to the events they are for
     *      FOR WHATEVER REASON DOESN'T WORK UNLESS YOU MANUALLY DO <CalendarColumn>.setHeight(whatever height)
     * @param startTime the start time of this column (i.e. if the column is for friday this would be midnight at the start of friday)
     * @param endTime the end time of this column     (i.e. if the column is for friday this would be midnight at the start of the next day)
     * @param labels Nodes and events are parallel lists of the node to position, and the event itself
     * @param labels List of labels parallel to list of events          (i.e. labels.get(i) is the label for the event events.get(i))
     * @param events List of CalendarEvents parallel to list of labels
     */
    public void positionNodes(Date startTime, Date endTime, List<Label> labels, List<CalendarEvent> events) {
        // how long this entire column is in milliseconds
        long columnDuration = endTime.getTime() - startTime.getTime();

        double columnHeight = this.getHeight();
        System.out.println("Column height: " + columnHeight);
        //System.out.println(((Pane)this.getParent()).getHeight());

        for (int i = 0; i < labels.size(); i++) {
            Label node = labels.get(i);

            // how many ms after the start of this column
            long relEventStart = events.get(i).startTime.getTime() - startTime.getTime();
            // the y coordinate of the top of the event; (relEventStart / columnDuration) is double from 0-1 on where it is in the column
            double startY = columnHeight * relEventStart / columnDuration;

            long relEventEnd = events.get(i).endTime.getTime() - startTime.getTime();
            double endY = columnHeight * relEventEnd / columnDuration;

            // print its top and bottom y values
            System.out.println("StartY, endY coords: " +startY + ", " + endY);

            // add it with startY top and endY-startY height
            this.add(node,1,2,1,1);
//            this.getChildren().add(node);
            double height = endY - startY;
            node.setPrefHeight(height);
            node.setMinHeight(height);
            node.setMaxHeight(height);

            // manually set its top position
            node.setLayoutY(startY);

            // this would be if we are relatively moving it, but we are not
            //node.setLayoutY(startY - node.getLayoutBounds().getMinY());
        }
    }

    public void doStuff() {
        // if the height is not manually set, the labels will not be properly positioned
        this.setHeight(120);

        // this doesnt work for some reason
        //this.setPrefHeight(Double.MAX_VALUE);
        //VBox.setVgrow(this, Priority.ALWAYS);

        this.getChildren().add(new Label("BAIC"));  // random reference label

        // the start and end time of our column; column is for October 20th
        Date startTime = new Date(2022-1900, Calendar.OCTOBER, 20);
        Date endTime = new Date(2022-1900, Calendar.OCTOBER, 21);

        // make some calendar event from 2:30am - 10:00am
        Task task = new Task("Test layout", "make sure vert");
        CalendarEvent event = new CalendarEvent("Test", task, new Date(2022-1900, Calendar.OCTOBER, 20, 2, 30), new Date(2022-1900, Calendar.OCTOBER, 20, 10, 0));

        // add that calendar event to a list
        List<CalendarEvent> events = new ArrayList<>();
        events.add(event);

        // make a label to correspond to the above calendar event, and add it to a list
        List<Label> nodes = new ArrayList<>();
        Label eventLabel = new Label("WOW");
        eventLabel.setPrefWidth(100);
        eventLabel.setStyle("-fx-background-color: #ff0000");
        nodes.add(eventLabel);

        // position the nodes
        this.positionNodes(startTime, endTime, nodes, events);
    }

    public void setController(CalendarController controller) {
        prevMonth.setOnAction(e -> {
            controller.handlePrevMonthBtn(e,month,title);
        });
        nextMonth.setOnAction(e -> {
            controller.handleNextMonthBtn(e,month,title);
        });
    }

}
