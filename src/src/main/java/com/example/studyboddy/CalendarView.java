package com.example.studyboddy;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;


public class CalendarView extends StackPane {
    public CalendarView() {
        BorderPane root = new BorderPane();
        CalendarMainView mainView = new CalendarMainView();
        CalendarSidebarView sidebarView = new CalendarSidebarView();
        CalendarController controller = new CalendarController();
        mainView.setController(controller);
        sidebarView.setController(controller);
        root.setLeft(sidebarView);
        root.setCenter(mainView);
        this.getChildren().add(root);
    }
}
