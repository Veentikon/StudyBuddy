package com.example.studyboddy;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;

/*** View of a flashcard set, each model of FlashcardSet has corresponding FlashcardSetView */
public class FlashcardSetView extends StackPane {
    private ListView<FlashcardModel> cards;
    private FlashcardSetModel model;
    private Button back;
    private Button delete;
    private Button edit;
    private VBox labelsCards;
    private HBox container;
    private VBox buttons;
    private HBox labels;
    private Label lFront;
    private Label lBack;

    public void setModel(FlashcardSetModel model){
        this.model = model;
        cards = new ListView<>(model.getFlashcardList());
        cards.setCellFactory(listItem -> new FlashcardCell());
        setId(model.getId());

        cards.setMinWidth(757);
        cards.setMinHeight(770);

        labelsCards.getChildren().add(cards);
        container.getChildren().add(labelsCards);
        this.getChildren().add(container);
    }

    public void setController(FlashcardMainController controller){
        back.setOnAction(e -> controller.handleBack());
        delete.setOnAction(e -> controller.handleDeleteCard(cards.getSelectionModel().getSelectedItems()));
    }

    public FlashcardSetView(){
        labelsCards = new VBox();
        back = new Button("back");
        delete = new Button("delete");
        edit = new Button("edit");
        buttons = new VBox();
        labels = new HBox();
        container = new HBox();
        lFront = new Label("      Front");
        lBack = new Label("      Back");

        // settings, alignment/size
        back.setMinSize(60, 45);
        delete.setMinSize(60, 45);
        edit.setMinSize(60, 45); // currently non functional, remove/implement
        buttons.setPadding(new Insets(8));
        buttons.setSpacing(8);
        buttons.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, null , null)));
        lFront.setMinWidth(360);
        lBack.setMinWidth(360);
        lFront.setFont(Font.font("Verdana", FontWeight.SEMI_BOLD, FontPosture.REGULAR, 15));
        lBack.setFont(Font.font("Verdana", FontWeight.SEMI_BOLD, FontPosture.REGULAR, 15));

        // linking
        buttons.getChildren().addAll(back, delete);
        container.getChildren().add(buttons);
        labels.getChildren().addAll(lFront, lBack);
        labelsCards.getChildren().add(labels);
    }
}

