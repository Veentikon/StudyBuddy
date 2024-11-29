package com.example.studyboddy;

public class MainUIController {

    MainUIInteractiveModel iModel;

    public void changeView(MainUIInteractiveModel.views newView) {
        iModel.setCurrentView(newView);
    }



    public void setiModel(MainUIInteractiveModel iModel) {
        this.iModel = iModel;
    }

}
