package com.example.studyboddy;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

// View in which the user has ability to review cards. This includes turning them over to back and front.
public class FlashcardsReviewView extends StackPane {
    private VBox container;
    private Text text;
    private TextField textFront;
    private TextField textBack;
    private Button next;
    private Button show;
    private Button prev;
    private Button exit;
    private Button edit;
    private Button save;
    private VBox buttons;
    private HBox buttons1;
    private HBox buttons2;
    private FlashcardModel flashcard;
    private FlashcardSetModel flashcardSet;
    private int side;

    // Constructor
    public FlashcardsReviewView(){
        container = new VBox();
        text = new Text();
        next = new Button("next");
        show = new Button("turn over");
        prev = new Button("previous");
        exit = new Button("exit");
        edit = new Button("edit");
        save = new Button("save");
        buttons = new VBox();
        buttons1 = new HBox();
        buttons2 = new HBox();
        side = 0;

        // UI settings
        next.setMinSize(60, 45);
        show.setMinSize(128, 45);
        prev.setMinSize(60, 45);
        exit.setMinSize(60, 45);
        edit.setMinSize(60, 45);
        save.setMinSize(60, 45);
        container.setAlignment(Pos.BASELINE_CENTER);
        buttons.setAlignment(Pos.BASELINE_CENTER);
        buttons.setSpacing(8);
        buttons1.setAlignment(Pos.BASELINE_CENTER);
        buttons1.setSpacing(8);
        buttons2.setAlignment(Pos.BASELINE_CENTER);
        buttons2.setSpacing(8);

        buttons1.getChildren().addAll(prev, next);
        buttons2.getChildren().addAll(exit, edit);
        buttons.getChildren().addAll(show, buttons1, buttons2);
        container.getChildren().addAll(text, buttons);
        this.getChildren().addAll(container);
    }

    /**
     * Set the flashcard set of the view, the set of cards to iterate through
     * @param set FlashcardSetModel, model of a selected flashcard set
     */
    public void setFlashcardSet(FlashcardSetModel set){
        flashcardSet = set;
        flashcard = set.getFirst();
        text.setText(flashcard.getFront());
    }

    // no actual controller needed, establish actions of the buttons
    public void setControls(FlashcardMainController controller){
        next.setOnAction(e -> {
            // manage when return value of getNext()
            FlashcardModel card = flashcard;
            flashcard = flashcardSet.getNext();
            if (flashcard != null) {
                side = 0;
                text.setText(flashcard.getFront());
            }
            else {
                flashcard = card;
            }
        }); // get the next card, set text to front of the next card
        show.setOnAction(e -> {
            if (side == 0) {
                text.setText(flashcard.getBack());
                side = 1;
                controller.incrementViewed();
            }
            else if (side == 1){
                text.setText(flashcard.getFront());
                side = 0;
            }
        });
        exit.setOnAction(e -> {
            controller.handleBack();
            redrawWithText();
        });
        edit.setOnAction(e -> redrawWithTextField());
        save.setOnAction(e -> {
            flashcard.updateFront(textFront.getText());
            flashcard.updateBack(textBack.getText());
            redrawWithText();
        });
    }

    // redraw the set view, with TextField in place of Text
    public void redrawWithTextField(){
        textFront = new TextField(flashcard.getFront());
        textBack = new TextField(flashcard.getBack());

        container.getChildren().clear();
        container.getChildren().addAll(textFront, textBack, save, buttons);
    }

    // redraw the set view, with Text in place of TextField
    public void redrawWithText(){
        text.setText(flashcard.getFront());
        container.getChildren().clear();
        container.getChildren().addAll(text, buttons);
    }
}
