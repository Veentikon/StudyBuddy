package com.example.studyboddy;

import javafx.geometry.Insets;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

/*** Representation of Flashcard in FlashcardSetView */
public class FlashcardCellView extends StackPane {
    private HBox cardContainer;
    private FlashcardModel model;
    private FlashcardMainController controller;

    private HBox front;
    private VBox back;
    private Text textFront;
    private Text textBack;

    public FlashcardCellView(FlashcardModel card) {
        if (card == null) {
            return;
        } else {
            this.model = card;
        }

        front = new HBox();
        back = new VBox();
        textFront = new Text(card.getFront());
        textBack = new Text(card.getBack());
        front.setBorder(new Border(new BorderStroke(Color.GREEN, BorderStrokeStyle.SOLID, null , null)));
        back.setBorder(new Border(new BorderStroke(Color.GREEN, BorderStrokeStyle.SOLID, null , null)));

        cardContainer = new HBox();

        front.setMinWidth(360);
        front.setMinHeight(50);
        front.setMaxWidth(360);
        front.setMaxHeight(200);
        back.setMinWidth(360);
        back.setMinHeight(50);
        back.setMaxWidth(360);
        back.setMaxHeight(200);

        front.getChildren().add(textFront);
        back.getChildren().add(textBack);
        textFront.setWrappingWidth(310);
        textBack.setWrappingWidth(310);

        cardContainer.setPadding(new Insets(10));

        cardContainer.getChildren().addAll(front, back);
        this.getChildren().add(cardContainer);
    }

    public void setController(FlashcardMainController controller){
        this.controller = controller;
    }
}
