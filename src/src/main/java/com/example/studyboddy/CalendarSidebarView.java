package com.example.studyboddy;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

/**
 * Creates the left sidebar of the calendar
 */
public class CalendarSidebarView extends StackPane {
    private Button add;
    private ToggleButton mViewBtn, wViewBtn, dViewBtn;
    private Label addTitle, viewTitle, categoryTitle;
    private CheckBox cb1,cb2,cb3,cb4;

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

        VBox viewMode = new VBox();
        viewMode.setAlignment(Pos.CENTER);
        viewMode.setPadding(new Insets(10,10,10,10));
        viewMode.setSpacing(5);
        viewTitle = new Label("View");
        mViewBtn = new ToggleButton("Month");
        mViewBtn.setPrefWidth(55);
        wViewBtn = new ToggleButton("Week");
        wViewBtn.setPrefWidth(55);
        dViewBtn = new ToggleButton("Day");
        dViewBtn.setPrefWidth(55);


        VBox categories = new VBox();
        categories.setAlignment(Pos.CENTER);
        categories.setPadding(new Insets(10,10,10,10));
        categories.setSpacing(5);
        categoryTitle = new Label("Categories");
        cb1 = new CheckBox("category1");
        cb2 = new CheckBox("category2");
        cb3 = new CheckBox("category3");
        cb4 = new CheckBox("category4");

        tasks.getChildren().addAll(add,addTitle);
        viewMode.getChildren().addAll(viewTitle, mViewBtn, wViewBtn, dViewBtn);
        categories.getChildren().addAll(categoryTitle,cb1,cb2,cb3,cb4);
        root.getChildren().addAll(tasks,viewMode,categories);
        this.getChildren().add(root);
    }

    public ToggleButton getmViewBtn() {
        return mViewBtn;
    }

    public ToggleButton getwViewBtn() {
        return wViewBtn;
    }

    public ToggleButton getdViewBtn() {
        return dViewBtn;
    }

    public void setController(CalendarController controller) {
        add.setOnAction(controller::handleAddButton);
        mViewBtn.setOnAction(e -> {
            wViewBtn.setSelected(false);
            dViewBtn.setSelected(false);
            controller.handleMView(e,wViewBtn,dViewBtn);
        });
        wViewBtn.setOnAction(e -> {
            mViewBtn.setSelected(false);
            dViewBtn.setSelected(false);
            controller.handleWView(e,mViewBtn,dViewBtn);
        });
        dViewBtn.setOnAction(e -> {
            mViewBtn.setSelected(false);
            wViewBtn.setSelected(false);
            controller.handleDView(e,wViewBtn,mViewBtn);
        });
    }
}
