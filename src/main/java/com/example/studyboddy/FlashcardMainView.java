package com.example.studyboddy;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

/** Flashcards main menu/window */
public class FlashcardMainView extends StackPane implements FlashcardModelSubscriber {
    // main container for the components
    private HBox root;
    // buttons container
    private VBox buttons;

    private Button newSet;
    private Button exportCards;
    private Button importCards;
    private Button testSet;

    private ListView<FlashcardSetModel> sets;
    private FlashcardMainModel model;

    private TextInputDialog inputDialog;

    /** Constructor */
    public FlashcardMainView(){
        // initializations
        root = new HBox();
        buttons = new VBox();
        newSet = new Button("New Set");
        testSet = new Button("Test Set");
        importCards = new Button("Import");
        exportCards = new Button("Export");
        buttons.getChildren().addAll(newSet);
        // removed import export and test set buttons

        buttons.setPadding(new Insets(9));
        buttons.setSpacing(9);
        buttons.setMinWidth(70);
        buttons.setAlignment(Pos.TOP_CENTER);
        buttons.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, null , null)));
        newSet.setMinSize(60, 45);
        testSet.setMinSize(60, 45);
        importCards.setMinSize(60, 45);
        exportCards.setMinSize(60, 45);
    }

    /**
     * Set the flashcard main TaskModel/ save reference to FlashcardMModel into local variable
     * @param model FlashcardMModel
     */
    public void setModel(FlashcardMainModel model){
        this.model = model;
        sets = new ListView<>(model.getSetsList());
        sets.setCellFactory(listItem -> new FlashcardSetCell());
        sets.setMinWidth(500);
        // set up main panel
        root.getChildren().addAll(buttons, sets);
        this.getChildren().add(root);
    }

    /***
     * Add handlers to interactive elements of the View
     * @param controller Flashcard main controller
     */
    public void setController(FlashcardMainController controller){
        sets = new ListView<>(model.getSetsList());

        exportCards.setOnAction(e -> controller.handleExport());
        importCards.setOnAction(e -> controller.handleImport());
        testSet.setOnAction(e -> controller.handleTest());

        // set up input dialog
        inputDialog = new TextInputDialog();
        inputDialog.getEditor().setText("");
        inputDialog.setContentText("Text: ");
        inputDialog.setHeaderText("Creating new Flashcard Set");
        // set action listeners to cancel/ok buttons in input dialog
        Button btOk = (Button) inputDialog.getDialogPane().lookupButton(ButtonType.OK);
        btOk.setOnAction(e -> {
            controller.handleAddSet(inputDialog.getEditor().getText());
            // clear input dialog value
            inputDialog.getEditor().clear();
        });
        newSet.setOnAction(e -> inputDialog.showAndWait());
    }

    @Override
    public void modelChanged() {
        // refresh the ListView so it reflects the changes made to the list of flashcard sets
        System.out.println("TaskModel changed");
        sets.refresh();
    }
}
