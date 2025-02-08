package com.example.studyboddy;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

import java.time.Month;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class CalendarDayView extends Pane implements iModelListener,ModelListener {
    private CalendarModel model;
    private CalendarIModel iModel;

    private Button prevDay,nextDay;
    private Label titleDay;

    private CalendarColumn column;
    private CalendarController controller;

    private static final String[] hourTitleList = {"1am","2am","3am","4am","5am","6am","7am","8am","9am","10am","11am"
    ,"12pm","1pm","2pm","3pm","4pm","5pm","6pm","7pm","8pm","9pm","10pm","11pm", "12am"};

    private static final double WINDOW_WIDTH = 750,WINDOW_HEIGHT = 700; // Only viewable part from scroll pane
    private static final double COLUMN_WIDTH = 650,COLUMN_HEIGHT = 2400; // Only viewable part from scroll pane
    private static final double VBOX_DAY_SIDES = 100;   // sides for vboxes

    /**
     * Constructor for the day view
     */
    public CalendarDayView() {
        // Make the day view scrollable
        ScrollPane root = new ScrollPane();
        root.setMaxSize(WINDOW_WIDTH,WINDOW_HEIGHT);
        root.setMinSize(WINDOW_WIDTH,WINDOW_HEIGHT);
        root.setPrefSize(WINDOW_WIDTH,WINDOW_HEIGHT);
        VBox dayContainer = new VBox();

        // day headers
        HBox dayHead = new HBox();
        dayHead.setMinSize(WINDOW_HEIGHT,VBOX_DAY_SIDES);
        dayHead.setMaxSize(WINDOW_HEIGHT,VBOX_DAY_SIDES);
        dayHead.setPrefSize(WINDOW_HEIGHT,VBOX_DAY_SIDES);
        dayHead.setAlignment(Pos.TOP_CENTER);
        dayHead.setSpacing(100);
        dayHead.setPadding(new Insets(20,20,20,20));
        prevDay = new Button("<");
        prevDay.setPrefSize(50,50);
        nextDay = new Button(">");
        nextDay.setPrefSize(50,50);
        Date now = new Date();
        titleDay = new Label(Month.of(now.getMonth()+1) + " " + now.getDate());
        titleDay.setFont(new Font("Regular", 30));

        // day contents
        HBox contents = new HBox();

        GridPane dayGrid = new GridPane();
        for (int i = 0; i < 24; i++) {
            // add a grey alternating background
            Pane greyPane = new Pane();
            greyPane.setPrefSize(COLUMN_WIDTH, COLUMN_HEIGHT / 24);
            if (i % 2 == 0) {
                greyPane.setStyle("-fx-background-color: #bbbbbb;");
            }
            dayGrid.add(greyPane, 0, i + 1, 25, 1);
        }
        dayGrid.setGridLinesVisible(true);
        column = new CalendarColumn();
        column.setMaxWidth(COLUMN_WIDTH);
        column.setMinWidth(COLUMN_WIDTH);
        column.setPrefWidth(COLUMN_WIDTH);
        column.setPrefHeight(COLUMN_HEIGHT);
        column.setHeightDupe(COLUMN_HEIGHT);
        column.setStyle("-fx-border-color: lightgray");

        GridPane timeGrid = new GridPane();
        for (int i = 0; i < 24; i += 1) {
            Pane timeBox = new Pane();
            // add a grey alternating background
            timeBox.setPrefSize(50, VBOX_DAY_SIDES);
            if (i % 2 == 0) {
                timeBox.setStyle("-fx-background-color: #bbbbbb;");
            }
            timeGrid.add(timeBox, 0, i + 1, 25, 1);

            Label time = new Label(hourTitleList[i]);
            time.setMinWidth(50);
            time.setMinHeight(VBOX_DAY_SIDES);
            time.setAlignment(Pos.BOTTOM_RIGHT);
            time.setFont(new Font("Calibri",15));
            timeBox.getChildren().add(time);
        }
        timeGrid.setGridLinesVisible(true);

        dayHead.getChildren().addAll(prevDay,titleDay,nextDay);
        dayGrid.add(column,0,0,(int)COLUMN_WIDTH,(int)COLUMN_HEIGHT);
        contents.getChildren().addAll(timeGrid,dayGrid);
        dayContainer.getChildren().addAll(dayHead,contents);
        root.setContent(dayContainer);
        this.getChildren().add(root);
    }

    /**
     * Changes the header to the appropriate day
     */
    private void updateHeader() {
        titleDay.setText(Month.of(iModel.getDayViewing().getMonth()+1) + " " + iModel.getDayViewing().getDate());
    }

    /**
     * Changes the column's content to appropriate day
     */
    private void updateDay() {
        column.getChildren().clear();
        Calendar calendar = Calendar.getInstance();
        Date start = iModel.getDayViewing();
        calendar.setTime(iModel.getDayViewing());
        calendar.add(Calendar.DATE,1);
        Date end = calendar.getTime();
        ArrayList<Label> labels = new ArrayList<>();
        for (CalendarEvent c:model.getEventsInRange(start,end,iModel.getCategories())) {
            if (c.startTime.getMonth() == iModel.getDayViewing().getMonth() && c.startTime.getDate() == iModel.getDayViewing().getDate()) {
                Label label = new Label(c.eventName);

                CalendarMainView.createContextMenu(c, label, controller);

                label.setMaxWidth(COLUMN_WIDTH);
                label.setPrefWidth(COLUMN_WIDTH);
                label.setMinWidth(COLUMN_WIDTH);
                label.setStyle("-fx-background-color: red");
                labels.add(label);
            }
        }
        column.positionNodes(start,end,labels,model.getEventsInRange(start,end,iModel.getCategories()));

    }


    @Override
    public void modelChanged() {
        updateHeader();
        updateDay();
    }

    @Override
    public void iModelChanged() {
        updateHeader();
        updateDay();
    }

    /**
     * Sets the model
     * @param model model
     */
    public void setModel(CalendarModel model) {
        this.model = model;
    }

    /**
     * Sets the interaction model
     * @param iModel interaction model
     */
    public void setIModel(CalendarIModel iModel) {
        this.iModel = iModel;
    }

    /**
     * Sets up the controller once
     * @param controller controller
     */
    public void setController(CalendarController controller) {
        prevDay.setOnAction(controller::handlePrevDay);
        nextDay.setOnAction(controller::handleNextDay);
        this.controller = controller;
    }

    // For testing purposes


    /**
     * Returns the current label
     * @return label
     */
    public Label getTitleDay() {
        return titleDay;
    }

}
