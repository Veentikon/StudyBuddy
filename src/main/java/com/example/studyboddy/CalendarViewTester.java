package com.example.studyboddy;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Calendar;
import java.util.Date;

public class CalendarViewTester extends Application{

    @Override
    public void start(Stage stage) {
        MainUI root = new MainUI();
        MainUIController mainUIController = new MainUIController();
        MainUIInteractiveModel mainUIInteractiveModel = new MainUIInteractiveModel();
        TaskManager taskModel = new TaskManager();


        root.setController(mainUIController);
        root.setInteractiveModel(mainUIInteractiveModel);
        mainUIController.setiModel(mainUIInteractiveModel);
        mainUIController.setTaskModel(taskModel);

        taskModel.addSubscriber(root);
        taskModel.addCloser(root);
        taskModel.addEditor(root);

        taskModel.addSubscriber(root);
        taskModel.addCloser(root);
        mainUIInteractiveModel.addSubscriber(root);


        Scene scene = new Scene(root, 1000, 800);
        stage.setTitle("Study Buddy!");
        stage.setScene(scene);
        stage.show();

        //PQ
        PriorityQueueView pqv = new PriorityQueueView();
        root.setPriorityQueue(pqv);
        PriorityQueueModel pqm = new PriorityQueueModel();
        PriorityQueueController pqc = new PriorityQueueController();
        pqv.setModel(taskModel);
        pqv.setController(pqc);
        pqc.setModel(taskModel);
        pqm.addSubscriber(pqv);

        //Calendar
        CalendarView calendarView = new CalendarView();
        root.setCalendarView(calendarView);
        // make models
        CalendarIModel calendarIModel = new CalendarIModel();
        CalendarModel calendarModel = new CalendarModel();
        // make controller
        CalendarController calendarController = new CalendarController();
        calendarController.setModel(calendarModel);
        calendarController.setIModel(calendarIModel);
        // link up view
        calendarView.setIModel(calendarIModel);
        calendarIModel.addSubscriber(calendarView);
        // link up month view
        calendarView.calendarMonthView.setModel(calendarModel);
        calendarView.calendarMonthView.setIModel(calendarIModel);
        calendarView.calendarMonthView.setController(calendarController);
        calendarModel.addSubscriber(calendarView.calendarMonthView);
        calendarIModel.addSubscriber(calendarView.calendarMonthView);
        // link up sidebar view
        calendarView.calendarSidebarView.setController(calendarController);
        // link up the week view
        calendarView.calendarWeekView.setCalendarModel(calendarModel);
        calendarView.calendarWeekView.setCalendarIModel(calendarIModel);
        calendarView.calendarWeekView.setController(calendarController);
        calendarIModel.addSubscriber(calendarView.calendarWeekView);
        calendarModel.addSubscriber(calendarView.calendarWeekView);
        // link up the day view
        calendarView.calendarDayView.setModel(calendarModel);
        calendarView.calendarDayView.setIModel(calendarIModel);
        calendarView.calendarDayView.setController(calendarController);
        calendarModel.addSubscriber(calendarView.calendarDayView);
        calendarIModel.addSubscriber(calendarView.calendarDayView);
        // link up model to MainUIController
        mainUIController.setCalendarModel(calendarModel);
        // give controller simple access to MainUI edit task/calendarEvent
        calendarController.setTaskEditor(root);



        // Set up Flashcards
        FlashcardHub flashcardHub = new FlashcardHub();
        root.setFlashcardMainView(flashcardHub.getFlashcardView());


        /**
         * Testing for the Calendar Month View, Calendar Day View
         */
        calendarIModel.setMonthViewing(new Date(2022-1900, Calendar.NOVEMBER, 4, 0, 0));
        CalendarEvent event = new CalendarEvent("Nap",
                new Task("Name", "Description"),
                new Date(2022-1900, Calendar.OCTOBER, 1, 20, 0),
                new Date(2022-1900, Calendar.OCTOBER, 1, 21, 0));
        calendarModel.addCalendarEvent(event);
        calendarIModel.viewPrevMonth();
        if (calendarView.calendarMonthView.getTitle().getText().contains("OCTOBER")) {
            if (calendarView.calendarMonthView.getDayContents().get(6) == null) {
                System.out.println("Input: " + event.eventName + "\n" +
                                    "Expected Output: Some VBox containing the events\n" +
                                    "Output: "+ calendarView.calendarMonthView.getDayContents().get(6));
            }
        }

        event = new CalendarEvent("Work",
                new Task("Name", "Description"),
                new Date(2022-1900, Calendar.OCTOBER, 31, 8,0),
                new Date(2022-1900, Calendar.OCTOBER, 31, 16,0));
        calendarModel.addCalendarEvent(event);
        if (calendarView.calendarMonthView.getTitle().getText().contains("OCTOBER")) {
            if (calendarView.calendarMonthView.getDayContents().get(33) == null) {
                System.out.println("Input: " + event.eventName + "\n" +
                        "Expected Output: Some VBox containing the events\n" +
                        "Output: "+ calendarView.calendarMonthView.getDayContents().get(33));
            }
        }

        event = new CalendarEvent("Read Manga",
                new Task("Name", "Description"),
                new Date(2022-1900, Calendar.OCTOBER, 12, 4,0),
                new Date(2022-1900, Calendar.OCTOBER, 12, 8,30));
        calendarModel.addCalendarEvent(event);
        if (calendarView.calendarMonthView.getTitle().getText().contains("OCTOBER")) {
            if (calendarView.calendarMonthView.getDayContents().get(17) == null) {
                System.out.println("Input: " + event.eventName + "\n" +
                        "Expected Output: Some VBox containing the events\n" +
                        "Output: "+ calendarView.calendarMonthView.getDayContents().get(17));
            }
        }

        // Automatically increment the month by 1
        calendarIModel.viewNextMonth();

        event = new CalendarEvent("Exercise",
                new Task("Name", "Description"),
                new Date(2022-1900, Calendar.NOVEMBER, 1, 8, 30),
                new Date(2022-1900, Calendar.NOVEMBER, 1, 9, 0));
        calendarModel.addCalendarEvent(event);
        if (calendarView.calendarMonthView.getTitle().getText().contains("NOVEMBER")) {
            if (calendarView.calendarMonthView.getDayContents().get(2) == null) {
                System.out.println("Input: " + event.eventName + "\n" +
                        "Expected Output: Some VBox containing the events\n" +
                        "Output: "+ calendarView.calendarMonthView.getDayContents().get(2));
            }
        }

        event = new CalendarEvent("Work",
                new Task("Name", "Description"),
                new Date(2022-1900, Calendar.NOVEMBER, 30, 14,0),
                new Date(2022-1900, Calendar.NOVEMBER, 30, 22,0));
        calendarModel.addCalendarEvent(event);
        if (calendarView.calendarMonthView.getTitle().getText().contains("NOVEMBER")) {
            if (calendarView.calendarMonthView.getDayContents().get(31) == null) {
                System.out.println("Input: " + event.eventName + "\n" +
                        "Expected Output: Some VBox containing the events\n" +
                        "Output: "+ calendarView.calendarMonthView.getDayContents().get(31));
            }
        }

        event = new CalendarEvent("Watch Anime",
                new Task("Name", "Description"),
                new Date(2022-1900, Calendar.NOVEMBER, 20, 4,0),
                new Date(2022-1900, Calendar.NOVEMBER, 20, 8,30));
        calendarModel.addCalendarEvent(event);
        if (calendarView.calendarMonthView.getTitle().getText().contains("NOVEMBER")) {
            if (calendarView.calendarMonthView.getDayContents().get(25) == null) {
                System.out.println("Input: " + event.eventName + "\n" +
                        "Expected Output: Some VBox containing the events\n" +
                        "Output: "+ calendarView.calendarMonthView.getDayContents().get(25));
            }
        }

        // Automatically decrement the month by 2
        calendarIModel.viewPrevMonth();
        calendarIModel.viewPrevMonth();

        event = new CalendarEvent("Exercise",
                new Task("Name", "Description"),
                new Date(2022-1900, Calendar.SEPTEMBER, 1, 8, 30),
                new Date(2022-1900, Calendar.SEPTEMBER, 1, 9, 0));
        calendarModel.addCalendarEvent(event);
        if (calendarView.calendarMonthView.getTitle().getText().contains("SEPTEMBER")) {
            if (calendarView.calendarMonthView.getDayContents().get(4) == null) {
                System.out.println("Input: " + event.eventName + "\n" +
                        "Expected Output: Some VBox containing the events\n" +
                        "Output: "+ calendarView.calendarMonthView.getDayContents().get(4));
            }
        }

        event = new CalendarEvent("Work",
                new Task("Name", "Description"),
                new Date(2022-1900, Calendar.SEPTEMBER, 30, 14,0),
                new Date(2022-1900, Calendar.SEPTEMBER, 30, 22,0));
        calendarModel.addCalendarEvent(event);
        if (calendarView.calendarMonthView.getTitle().getText().contains("SEPTEMBER")) {
            if (calendarView.calendarMonthView.getDayContents().get(33) == null) {
                System.out.println("Input: " + event.eventName + "\n" +
                        "Expected Output: Some VBox containing the events\n" +
                        "Output: "+ calendarView.calendarMonthView.getDayContents().get(33));
            }
        }

        event = new CalendarEvent("Play League of Legends",
                new Task("Name", "Description"),
                new Date(2022-1900, Calendar.SEPTEMBER, 13, 4,0),
                new Date(2022-1900, Calendar.SEPTEMBER, 13, 8,30));
        calendarModel.addCalendarEvent(event);
        if (calendarView.calendarMonthView.getTitle().getText().contains("SEPTEMBER")) {
            if (calendarView.calendarMonthView.getDayContents().get(18) == null) {
                System.out.println("Input: " + event.eventName + "\n" +
                        "Expected Output: Some VBox containing the events\n" +
                        "Output: "+ calendarView.calendarMonthView.getDayContents().get(18));
            }
        }

        /**
         * Testing for Calendar Week View
         */
        calendarIModel.setWeekViewing(new Date(2022-1900, Calendar.OCTOBER, 30, 0, 0));

        // Initial state
        if (!calendarView.calendarWeekView.weekTitle.getText().contains("October 30 - November 5")) {
            System.out.println("Input: Current week" + "\n" +
                    "Expected Output: October 30 - November 5\n" +
                    "Output: "+ calendarView.calendarWeekView.weekTitle.getText());
        }

        calendarIModel.viewNextWeek();
        if (!calendarView.calendarWeekView.weekTitle.getText().contains("November 6 - November 12")) {
            System.out.println("Input: Calendar Week view current week incremented by 1" + "\n" +
                    "Expected Output: November 6 - November 12\n" +
                    "Output: "+ calendarView.calendarWeekView.weekTitle.getText());
        }

        calendarIModel.viewPreviousWeek();
        if (!calendarView.calendarWeekView.weekTitle.getText().contains("October 30 - November 5")) {
            System.out.println("Input: Calendar Week view current week decremented by 1" + "\n" +
                    "Expected Output: October 30 - November 5\n" +
                    "Output: "+ calendarView.calendarWeekView.weekTitle.getText());
        }

        calendarIModel.viewNextWeek();
        calendarIModel.viewNextWeek();
        calendarIModel.viewNextWeek();
        if (!calendarView.calendarWeekView.weekTitle.getText().contains("November 20 - November 26")) {
            System.out.println("Input: Calendar Week view current week incremented by 3" + "\n" +
                    "Expected Output: November 20 - November 26\n" +
                    "Output: "+ calendarView.calendarWeekView.weekTitle.getText());
        }

        calendarIModel.viewPreviousWeek();
        calendarIModel.viewPreviousWeek();
        calendarIModel.viewPreviousWeek();
        if (!calendarView.calendarWeekView.weekTitle.getText().contains("October 30 - November 5")) {
            System.out.println("Input: Calendar Week view current week decremented by 3" + "\n" +
                    "Expected Output: October 30 - November 5\n" +
                    "Output: "+ calendarView.calendarWeekView.weekTitle.getText());
        }

        /**
         * Testing for the Calendar Day View
         */
        calendarIModel.setDayViewing(new Date(2022-1900, Calendar.NOVEMBER, 4, 0, 0));

        // Initial state
        if (!calendarView.calendarDayView.getTitleDay().getText().contains("NOVEMBER 4")) {
            System.out.println("Input: Current day" + "\n" +
                    "Expected Output: NOVEMBER 4\n" +
                    "Output: "+ calendarView.calendarDayView.getTitleDay().getText());
        }

        calendarIModel.viewNextDayTimes(1);
        if (!calendarView.calendarDayView.getTitleDay().getText().contains("NOVEMBER 5")) {
            System.out.println("Input: Calendar Day views current day incremented" + "\n" +
                    "Expected Output: NOVEMBER 5\n" +
                    "Output: "+ calendarView.calendarDayView.getTitleDay().getText());
        }

        calendarIModel.viewPrevDayTimes(1);
        if (!calendarView.calendarDayView.getTitleDay().getText().contains("NOVEMBER 4")) {
            System.out.println("Input: Calendar Day views current day decremented" + "\n" +
                    "Expected Output: NOVEMBER 4\n" +
                    "Output: "+ calendarView.calendarDayView.getTitleDay().getText());
        }

        calendarIModel.viewNextDayTimes(30);
        if (!calendarView.calendarDayView.getTitleDay().getText().contains("DECEMBER 4")) {
            System.out.println("Input: Calendar Day views current day incremented by 30 days" + "\n" +
                    "Expected Output: DECEMBER 4\n" +
                    "Output: "+ calendarView.calendarDayView.getTitleDay().getText());
        }

        calendarIModel.viewPrevDayTimes(30);
        if (!calendarView.calendarDayView.getTitleDay().getText().contains("NOVEMBER 4")) {
            System.out.println("Input: Calendar Day views current day decremented by 30 days" + "\n" +
                    "Expected Output: NOVEMBER 4\n" +
                    "Output: "+ calendarView.calendarDayView.getTitleDay().getText());
        }

        System.out.println("Successful Tests");

    }

    public static void main(String[] args) {
        launch();
    }
}
