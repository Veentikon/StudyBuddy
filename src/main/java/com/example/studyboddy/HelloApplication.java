package com.example.studyboddy;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

public class HelloApplication extends Application {

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
        PriorityQueueController pqc = new PriorityQueueController();
        pqv.setModel(taskModel);
        pqv.setController(pqc);
        pqc.setModel(taskModel);
        pqc.setUIController(mainUIController);

        //PQ INTERACTS WITH TASK MODEL HERE TO LINK
        taskModel.addSubscriber(pqv);
        taskModel.setTaskModelAsReadData();

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

        // READ IN CALENDAR EVENTS AFTER TASK MODEL
        calendarModel.setCalendarModelAsReadData(taskModel);

        // auto scheduler
        AutoScheduler autoScheduler = new AutoScheduler();
        autoScheduler.setTaskManager(taskModel);
        autoScheduler.setCalendarModel(calendarModel);
        calendarController.setAutoScheduler(autoScheduler);

        //Incentive System
        IncentiveView incentiveView = new IncentiveView();
        IncentiveModel incentiveModel = new IncentiveModel();

        incentiveView.addModel(incentiveModel);
        root.setIncentiveView(incentiveView);

        incentiveModel.addSubscriber(incentiveView);
        incentiveModel.setTaskModel(taskModel);
        mainUIInteractiveModel.addSubscriber(incentiveModel);
        taskModel.addSubscriber(incentiveModel);



        // Set up Flashcards
//        FlashcardMainModel fModel = new FlashcardMainModel();
//        FlashcardMainView fMView = new FlashcardMainView();
//        FlashcardMainController fMController = new FlashcardMainController();
//        FlashcardNewCardView newCardView = new FlashcardNewCardView();
//        // settings for MainView
//        fMView.setModel(fModel);
//        fMView.setController(fMController);
//        // setting for FlashcardMainModel
//        fModel.setController(fMController);
//        // settings for FlashcardMainController
//        fMController.setModel(fModel);
//        fMController.setUIController(mainUIController);
//        // settings for New card view
//        newCardView.setController(fMController);
//        // add Flashcard views to the main UI
//        root.setFlashcardMainView(fMView);
//        root.setNewCardView(newCardView);
        FlashcardHub flashcardHub = new FlashcardHub();
        root.setFlashcardMainView(flashcardHub.getFlashcardView());
        flashcardHub.addIncentiveModel(incentiveModel);


        stage.setOnCloseRequest(r -> {
            try {
                incentiveModel.writeAsFile("savedata/incentive.txt");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });



        // add some auto schedule events
        /*
        Task autoTask = new Task("Auto", "");
        autoTask.category = "Auto-schedule";
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.HOUR, 2);
        CalendarEvent autoEvent1 = new CalendarEvent(autoTask.taskName, autoTask, new Date(), c.getTime());
        autoEvent1.displayColor = Color.AQUA;
        calendarModel.addCalendarEvent(autoEvent1);

        c.add(Calendar.HOUR, 3);

        Date startTime = c.getTime();
        c.add(Calendar.HOUR, 3);
        CalendarEvent autoEvent2 = new CalendarEvent(autoTask.taskName, autoTask, startTime, c.getTime());
        autoEvent1.displayColor = Color.AQUA;
        calendarModel.addCalendarEvent(autoEvent2);
        */


    }

    public static void main(String[] args) {
        launch();
    }
}