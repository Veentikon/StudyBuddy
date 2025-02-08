package com.example.studyboddy;

import javafx.event.ActionEvent;
import javafx.scene.control.TextArea;

public class PriorityQueueController {
    TaskManager pqm;
    MainUIController controller;
    public void setModel(TaskManager pqm) {
        this.pqm = pqm;
    }

    public void setUIController(MainUIController controller) {
        this.controller = controller;
    }

    public void addColumn(String string) {
        pqm.addCategoryColumn(string);
    }

    public void newTaskClicked(String text, String description,String category) {
        pqm.addTask(new Task(text,description,category));
    }

    public void moveAllMarked(String columnDestination) {
        pqm.changeCategoryOfMoveForMarked(columnDestination);
    }

    public void editTask(int ID) {
        controller.editTask(ID);
    }


//    public void deleteALlThatIsMArked() {
//        pqm.deleteAllThatIsMArked();
//    }
}
