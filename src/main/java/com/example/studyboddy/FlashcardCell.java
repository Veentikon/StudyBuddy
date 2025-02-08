package com.example.studyboddy;

import javafx.scene.control.ListCell;

// Helps populate ListView of FlashcardSetView
public class FlashcardCell extends ListCell<FlashcardModel> {
    public void updateItem(FlashcardModel item, boolean empty){
        super.updateItem(item, empty);
        if (empty){
            setGraphic(null);
        }
        else {
            FlashcardCellView fv = new FlashcardCellView(item);
            fv.setController(item.getController());
            setGraphic(fv);
        }
    }
    public FlashcardCell(){
    }
}
