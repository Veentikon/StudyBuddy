package com.example.studyboddy;

import javafx.scene.layout.StackPane;

import java.util.ArrayList;

// create all views, models, controllers and wire then together
public class FlashcardHub {
    private FlashcardMainView mainView;
    private FlashcardMainModel mainModel;
    private FlashcardMainController mainController;
    private FlashcardNewCardView newCardView;
    private FlashcardsReviewView reviewView;
    private ArrayList<FlashcardSetView> setViews;
    public enum windows {mainView, newCardView, setView, review}

    // the view that is visible
    private FlashcardSetView currentSetView;
    private StackPane root; // the root, change child to swap to different window

    public FlashcardHub(){
        mainView = new FlashcardMainView();
        mainModel = new FlashcardMainModel();
        mainController = new FlashcardMainController();
        newCardView = new FlashcardNewCardView();
        reviewView = new FlashcardsReviewView();
        root = new StackPane();
        setViews = new ArrayList<>();

        // wire them together
        mainView.setModel(mainModel);
        mainView.setController(mainController);
        mainModel.setController(mainController);
        mainController.setModel(mainModel);
        mainController.setHub(this);
        newCardView.setController(mainController);
        reviewView.setControls(mainController);

        // set up initial window to FlashcardMainView
        root = new StackPane();
        root.getChildren().add(mainView);
    }

    public StackPane getFlashcardView(){
        return root;
    }

    public void switchWindow(windows window){
        this.root.getChildren().clear();
        if (window == windows.mainView){
            this.root.getChildren().add(mainView);
        }
        else if (window == windows.newCardView){
            this.root.getChildren().add(newCardView);
        }
        else if (window == windows.setView){
            // find setView by id and set it as the view
            String Id = mainModel.getSelected().getId();
            for (FlashcardSetView setView : setViews) {
                if (setView.getId().equals(Id)) {
                    this.root.getChildren().add(setView);
                    break;
                }
            }
        }
        else if (window == windows.review){
            reviewView.setFlashcardSet(mainModel.getSelected());
            this.root.getChildren().add(reviewView);
        }
    }

    public void addSetView(FlashcardSetModel set){
        FlashcardSetView newSet = new FlashcardSetView();
        newSet.setController(mainController);
        newSet.setModel(set);
        setViews.add(newSet);
    }

    public void removeSetView(String id){
        setViews.removeIf(setView -> setView.getId().equals(id));
    }

    public void setReviewSet(FlashcardSetModel set){
        reviewView.setFlashcardSet(set);
    }

    public void addIncentiveModel(IncentiveModel incentiveModel) {
        mainController.addIncentiveModel(incentiveModel);
    }

}
