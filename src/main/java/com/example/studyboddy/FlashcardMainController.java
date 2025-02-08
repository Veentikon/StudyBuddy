package com.example.studyboddy;

import javafx.collections.ObservableList;
import javafx.scene.control.Alert;

// handle events in the views
public class FlashcardMainController {
    private IncentiveModel incentiveModel;
    private FlashcardMainModel model;
    private FlashcardHub hub;

    public void setModel(FlashcardMainModel model){
        this.model = model;
    }

    // handle click on Add set button in FlashcardMainView
    public void handleAddSet(String name){
        // do not allow no-name set or " " name for a set
        if (name.length() != 0 && !name.equals(" ")) {
            hub.addSetView(model.createFlashcardSet(name));
        }
    }

    public void setHub(FlashcardHub hub){
        this.hub = hub;
    }

    // Handle Remove set button press in FlashcardSetCellView
    public void handleRemoveSet(FlashcardSetModel set){
        hub.removeSetView(set.getId()); // remove view from hub
        model.removeSet(set); // remove model from main model
    }

    // handle click on Import set button
    public void handleImport(){
        System.out.println("import handler called");
    }

    // handle click on import set button
    public void handleExport(){
        System.out.println("export handler called");
    }

    // handle click on test button in main flashcard view
    public void handleTest(){
        System.out.println("test handler called");
    }

    // handle click on add Card button
    public void handleAddCard(FlashcardSetModel item){
        model.setSelected(item);
        hub.switchWindow(FlashcardHub.windows.newCardView);
    }

    // handle click on "finish/back" button(s) - go back to main view of flashcards
    public void handleBack(){
        hub.switchWindow(FlashcardHub.windows.mainView);
    }

    // handle click/press on save button in FlashcardNewSetView
    public void handleSave(Alert alert, String front, String back) {
        if (front.equals("") || front.equals(" ") || front.equals("\n") ||
                back.equals("") || back.equals(" ") || back.equals("\n")) {
            alert.showAndWait();
        } else {
            model.getSelected().addCard(front, back);
        }
    }
    // Handle click/press on new card in FlashcardMainView
    public void handleViewSet(Alert alert, FlashcardSetModel set){
        if (set.getNOfCards() == 0){
            alert.showAndWait();
        }
        else {
            model.setSelected(set);
            hub.switchWindow(FlashcardHub.windows.setView);
        }
    }

    // handle click on Review set button in FlashcardSetCellView
    public void handleReviewSet(Alert alert, FlashcardSetModel set) {
        if (set.getNOfCards() == 0){
            alert.showAndWait();
        }
        else {
            model.setSelected(set);
            hub.switchWindow(FlashcardHub.windows.review);
            incentiveModel.addFlashCardSet();
        }
    }


    public void addIncentiveModel(IncentiveModel incentiveModel) {
        this.incentiveModel = incentiveModel;
    }

    public void incrementViewed() {
        incentiveModel.addFlashcardView();
    }

    public void handleDeleteCard(ObservableList<FlashcardModel> list){
        FlashcardSetModel selected = model.getSelected();
        // delegate deletion of flashcards to the selected set
        selected.decrCount(list.size());
        selected.removeCards(list);
        // if the last card was deleted, switch to main view
        if (selected.getNOfCards() == 0) {
            hub.switchWindow(FlashcardHub.windows.mainView);
        }
    }
}
