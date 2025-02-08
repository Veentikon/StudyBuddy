package com.example.studyboddy;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

/** Main view class for the calendar
 *  Has the month/week/day view as children
 *  And sidebar view as child
 */
public class CalendarView extends StackPane implements iModelListener {
    // various views for month week and day
    public CalendarDayView calendarDayView;
    public CalendarWeekView calendarWeekView;
    public CalendarMainView calendarMonthView;
    // sidebar view
    public CalendarSidebarView calendarSidebarView;

    // pane to contain current main view; i.e. month view would be put here if selected
    private Pane currentViewPane;

    private CalendarIModel iModel;

    public CalendarView() {
        BorderPane root = new BorderPane();

        calendarMonthView = new CalendarMainView();
        calendarSidebarView = new CalendarSidebarView();

        calendarDayView = new CalendarDayView();
        calendarWeekView = new CalendarWeekView();

        currentViewPane = new Pane();
        currentViewPane.getChildren().add(calendarMonthView);
        calendarSidebarView.init();                             // set month selected

        root.setLeft(calendarSidebarView);
        root.setCenter(currentViewPane);
        this.getChildren().add(root);
    }


    @Override
    public void iModelChanged() {
        // update which view we use
        currentViewPane.getChildren().clear();
        switch(iModel.getViewSelected()) {
            case DAY -> currentViewPane.getChildren().add(calendarDayView);
            case WEEK -> currentViewPane.getChildren().add(calendarWeekView);
            case MONTH -> currentViewPane.getChildren().add(calendarMonthView);
        }
    }

    /**
     * Sets the iModel
     * @param iModel interaction model
     */
    public void setIModel(CalendarIModel iModel) {
        this.iModel = iModel;
    }

    /**
     * Add task is bind to the button
     * @param handler handler
     */
    public void bindAddTaskButton(EventHandler<ActionEvent> handler) {
        calendarSidebarView.bindAddTaskButton(handler);
    }

    /**
     * Add auto task is binded to the button
     * @param handler handler
     */
    public void bindAddAutoTaskButton(EventHandler<ActionEvent> handler) {
        calendarSidebarView.bindAddAutoTaskButton(handler);
    }

}
