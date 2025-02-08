package com.example.studyboddy;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.util.ArrayList;

/**
 * Creates the left sidebar of the calendar
 */
public class CalendarSidebarView extends StackPane {
    private Button add;
    private Button autoScheduleButton;
    private Label autoScheduleLabel;
    private Button addAutoAvailableButton;
    private Label addAutoAvailableLabel;
    private ArrayList<ToggleButton> viewButtons;
    private ToggleButton mViewBtn, wViewBtn, dViewBtn;
    private Label addTitle, viewTitle, categoryTitle;
    private ArrayList<CheckBox> checkBoxes;

    /**
     * Constructor for the calendar sidebar
     */
    public CalendarSidebarView(){
        VBox root = new VBox();
        root.setSpacing(75);
        root.setStyle("-fx-border-color: black");
        root.setAlignment(Pos.CENTER);

        VBox tasks = new VBox();
        tasks.setAlignment(Pos.CENTER);
        tasks.setPadding(new Insets(10,10,10,10));
        tasks.setSpacing(5);
        add = new Button("+");
        addTitle = new Label("Create Task");

        // create add auto available button
        addAutoAvailableButton = new Button("+");
        addAutoAvailableLabel = new Label("Add Availability\nfor Auto-Scheduler");

        // create auto schedule button
        autoScheduleButton = new Button("+");
        autoScheduleLabel = new Label("Perform Automatic\nScheduling");

        VBox viewMode = new VBox();
        viewMode.setAlignment(Pos.CENTER);
        viewMode.setPadding(new Insets(10,10,10,10));
        viewMode.setSpacing(5);
        viewTitle = new Label("View");
        viewButtons = new ArrayList<>();
        mViewBtn = new ToggleButton("Month");
        mViewBtn.setPrefWidth(55);
        wViewBtn = new ToggleButton("Week");
        wViewBtn.setPrefWidth(55);
        dViewBtn = new ToggleButton("Day");
        dViewBtn.setPrefWidth(55);
        viewButtons.add(mViewBtn);
        viewButtons.add(wViewBtn);
        viewButtons.add(dViewBtn);

        VBox categories = new VBox();
        categories.setAlignment(Pos.TOP_LEFT);
        categories.setPadding(new Insets(10,10,10,10));
        categories.setSpacing(5);
        categoryTitle = new Label("Categories");
        checkBoxes = new ArrayList<>();
        CheckBox cb1 = new CheckBox("Assignment");
        CheckBox cb2 = new CheckBox("Transit");
        CheckBox cb3 = new CheckBox("Exam");
        CheckBox cb4 = new CheckBox("Uncategorized");
        CheckBox cb5 = new CheckBox("Auto-schedule");
        checkBoxes.add(cb1);
        checkBoxes.add(cb2);
        checkBoxes.add(cb3);
        checkBoxes.add(cb4);
        checkBoxes.add(cb5);

        tasks.getChildren().addAll(add,addTitle);
        tasks.getChildren().addAll(addAutoAvailableButton, addAutoAvailableLabel);
        tasks.getChildren().addAll(autoScheduleButton, autoScheduleLabel);
        viewMode.getChildren().addAll(viewTitle, mViewBtn, wViewBtn, dViewBtn);
        categories.getChildren().addAll(categoryTitle,cb1,cb2,cb3,cb4,cb5);
        root.getChildren().addAll(tasks,viewMode,categories);
        this.getChildren().add(root);
    }


    /**
     * Add task is bind to the button
     * @param handler handler
     */
    public void bindAddTaskButton(EventHandler<ActionEvent> handler) {
        add.setOnAction(handler);
        //add.setOnAction(e -> System.out.println("Hello"));
    }

    /**
     * Add auto task is bound to the button
     * @param handler handler
     */
    public void bindAddAutoTaskButton(EventHandler<ActionEvent> handler) {
        addAutoAvailableButton.setOnAction(handler);
    }

    /**
     * Sets the controller
     * @param controller controller
     */
    public void setController(CalendarController controller) {
        //add.setOnAction(controller::handleAddButton);
        viewButtons.forEach(b -> b.setOnAction(e -> controller.handleViewButton(e,b,viewButtons)));
        checkBoxes.forEach(c -> c.setOnAction(e -> {
            if (c.isSelected())
                controller.handleAddCategory(c.getText());
            else
                controller.handleRemoveCategory(c.getText());
        }));

        autoScheduleButton.setOnAction(e -> controller.handleAutoScheduleButtonPressed());
    }

    /**
     * Sets up the initial state of categories selected
     */
    public void init() {
        for (CheckBox c:checkBoxes) {
            c.fire();
        }
        viewButtons.get(0).fire();
    }
}
