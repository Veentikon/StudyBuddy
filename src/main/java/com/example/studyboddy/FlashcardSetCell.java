package com.example.studyboddy;

import javafx.scene.control.ListCell;

// ListCell, ListView in main flashcard view
public class FlashcardSetCell extends ListCell<FlashcardSetModel> {

    public void updateItem(FlashcardSetModel item, boolean empty){
        super.updateItem(item, empty);
        if (empty){
            setGraphic(null);
        }
        else {
            FlashcardSetCellView fv = new FlashcardSetCellView(item);
            item.setView(fv);
            fv.setController(item.getMController());
            setGraphic(fv);
        }
    }

    public FlashcardSetCell(){
    }
}
