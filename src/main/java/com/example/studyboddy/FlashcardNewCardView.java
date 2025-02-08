package com.example.studyboddy;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;

// A view of the UI window that opens when a user wants to add new card to selected flashcard set
public class FlashcardNewCardView extends StackPane {
    private TextArea front;
    private TextArea back;
    private Button save;
    private Button done;
    private Label frontLabel;
    private Label backLabel;
    private HBox buttons;
    private VBox textFields;
    private VBox root;
    private Alert alert1;
    private Alert alert2;
    private Button Ok;

    public FlashcardNewCardView(){
        front = new TextArea();
        back = new TextArea();
        save = new Button("save");
        done = new Button("finish");
        frontLabel = new Label("Front");
        backLabel = new Label("Back");
        buttons = new HBox();
        root = new VBox();
        textFields = new VBox();

        this.alert1 = new Alert(Alert.AlertType.WARNING);
        this.alert1.setContentText("One of the fields is empty");
        this.alert2 = new Alert(Alert.AlertType.WARNING);
        this.alert2.setContentText("Are you sure you want to procceed? progress will be lost");
        Ok = (Button) alert2.getDialogPane().lookupButton(ButtonType.OK);

        // UI elements settings
        frontLabel.setFont(Font.font("Verdana", FontWeight.SEMI_BOLD, FontPosture.REGULAR, 11));
        backLabel.setFont(Font.font("Verdana", FontWeight.SEMI_BOLD, FontPosture.REGULAR, 11));
        front.setWrapText(true); // when typing, text wraps around
        back.setWrapText(true);
        // button settings
//        save.setFont(Font.font("Verdana", FontWeight.SEMI_BOLD, FontPosture.REGULAR, 15));
//        done.setFont(Font.font("Verdana", FontWeight.SEMI_BOLD, FontPosture.REGULAR, 15));
        save.setMinSize(60, 45);
        done.setMinSize(60, 45);
        buttons.setSpacing(8);
        buttons.setMinHeight(50);
        buttons.setAlignment(Pos.CENTER_LEFT);
//        buttons.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, null , null)));
        root.setSpacing(8);
        this.setPadding(new Insets(8));
//        BackgroundFill backgroundFill = new BackgroundFill(Color.GREEN, CornerRadii.EMPTY , Insets.EMPTY);
//        Background background = new Background(backgroundFill);
//        buttons.setBackground(background);


        buttons.getChildren().addAll(save, done);
        textFields.getChildren().addAll(front, back);
        root.getChildren().addAll(frontLabel, front, backLabel, back, buttons);
        this.getChildren().add(root);
    }

    // do not store refers of the controller but add action listeners to UI elements
    public void setController(FlashcardMainController controller){
        Ok.setOnAction(event -> {
            front.setText("");
            back.setText("");
            controller.handleBack();
        });

        this.save.setOnAction(e -> {
            controller.handleSave(alert1, front.getText(), back.getText());
            front.clear();
            back.clear();
        });
        this.done.setOnAction(e -> {
            if (front.getText().length()!=0 || back.getText().length()!=0){
                alert2.showAndWait();
            }
            else {
                controller.handleBack();
            }
        });
    }
}
