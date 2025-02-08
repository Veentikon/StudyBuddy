package com.example.studyboddy;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;

import java.io.FileInputStream;
import java.text.NumberFormat;

// View of a flashcard set, this is how it will look in list of the main flashcard view
public class FlashcardSetCellView extends Pane {
    private HBox container;
    private MenuBar menuBar;
    private Menu menu;
    private MenuItem newCardOption;
    private MenuItem review;
    private MenuItem delete;
    private MenuItem viewSet;
    private FlashcardSetModel model;
    private Label name;
    private Label nCards;
    private HBox menuStats;

    /***
     * Constructor method
     * @param item: new FlashcardSetModel instance, in case it is null return, otherwise do proper set up of view
     */
    public FlashcardSetCellView(FlashcardSetModel item){
        if (item == null){
            // When no new item, all cells in ListView are initialized to null, in other words, empty cells.
            return;
        }
        else {
            this.model = item;
        }
        // instantiation
        container = new HBox();
        menuBar = new MenuBar();
        menu = new Menu("menu");
        newCardOption = new MenuItem("New card");
        review = new MenuItem("Review cards");
        delete = new MenuItem("Delete set");
        viewSet = new MenuItem("View Set");
        name = new Label(this.model.getName());
        nCards = new Label("Cards: " + this.model.getNOfCards());
        menuStats = new HBox();

        // container/layout settings
        name.setMinWidth(250);
        name.setFont(Font.font("Verdana", FontWeight.SEMI_BOLD, FontPosture.REGULAR, 18));
        name.setAlignment(Pos.CENTER_LEFT);

        container.setBorder(new Border(new BorderStroke(Color.GREEN, BorderStrokeStyle.SOLID, null , null)));
        container.setMinHeight(35);
        container.setMinWidth(490);
        container.setPadding(new Insets(8));

        // wire it all together
        menu.getItems().addAll(newCardOption, review, delete, viewSet);
        menuBar.getMenus().add(menu);
        menuStats.getChildren().addAll(menuBar, nCards);
        container.getChildren().addAll(name, menuStats);

        this.getChildren().add(container);
    }

    /**
     * Add action handlers to view of FlashcardSetCell
     * @param controller reference to an instance of FlashcardMController class
     */
    public void setController(FlashcardMainController controller){
        newCardOption.setOnAction(e -> controller.handleAddCard(model));

        // create and set up confirmation/warning pop up
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm delete Set");
        alert.setContentText("Are you sure you want to delete this set?");
        Button btOk = (Button) alert.getDialogPane().lookupButton(ButtonType.OK);

        // create and set up warning pop up
        Alert alert1 = new Alert(Alert.AlertType.WARNING);
        alert1.setContentText("The set is empty");

        // add action listeners for warning pop up
        btOk.setOnAction(e -> controller.handleRemoveSet(this.model));
        delete.setOnAction(e -> alert.showAndWait());
        viewSet.setOnAction(e -> controller.handleViewSet(alert1, model));
        review.setOnAction(e -> controller.handleReviewSet(alert1, model));
    }

    public void modelUpdated(){
        this.nCards.setText("Cards: " + this.model.getNOfCards());
    }
}
