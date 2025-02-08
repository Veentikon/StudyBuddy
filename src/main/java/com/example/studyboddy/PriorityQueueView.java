package com.example.studyboddy;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.util.Callback;

import java.util.ArrayList;

import static javafx.collections.FXCollections.observableArrayList;


public class PriorityQueueView extends BorderPane implements ModelListener   {
    private TaskManager pqm;
    private PriorityQueueController pqc;

    ArrayList<VBox> columns;


    HBox root;

    TextArea columnName;

    Button addColumn;

    ComboBox<Label> lblComboBox;

    VBox leftSideVBox;

    Button moveButton;





    public PriorityQueueView(){
        /*
            This is where we put the persistent aspects of PQ
         */
        columns = new ArrayList<>(); //I use this to make sure the JVM GC is persisitent and keeps things
        leftSideVBox = new VBox();
        this.setLeft(leftSideVBox);
        addColumn = new Button("Add a column with Name");

        columnName = new TextArea();
        columnName.setMaxSize(200,12);
        addColumn = new Button("Add a column with Name");

        leftSideVBox.getChildren().addAll(columnName, addColumn);
        moveButton = new Button("Select all of the marked and move them");

    }


    public void setModel(TaskManager pqm) {
        //Used to link the model to TaskManager
        this.pqm = pqm;
        modelChanged();
    }

    public void setController(PriorityQueueController pqc) {
        this.pqc = pqc; //sets tihe action of setController
        addColumn.setOnAction(e -> {
            pqc.addColumn(columnName.getText());
        });
        moveButton.setOnAction(e -> {
            pqc.moveAllMarked(lblComboBox.getSelectionModel().getSelectedItem().getText());
        });
    }


    @Override
    public void modelChanged() {
        //This is where we generate the columns based on the information in task manager
        VBox tempVbox; //create a variable temp to store the vbox
        root = new HBox(); //set the root to a fresh instance of HBOX. Just easier to ask for forgiveness than permsission
        root.setAlignment(Pos.CENTER);

        tempVbox = new VBox();
        root.getChildren().add(tempVbox);

        columns = new ArrayList<>();

        columnName.setText("");

        //we now generate each individual section of the queue
        for (int i = 0; i < pqm.getCategories().size(); i++) {
            tempVbox = new VBox();
            tempVbox.getChildren().add(new Label(pqm.getCategories().get(i)));

            //set sizing of each vatalogue
            tempVbox.setMinSize(80,80);
            tempVbox.setMaxSize(800,800);
            tempVbox.setPrefSize(225,1000);
            tempVbox.setAlignment(Pos.TOP_CENTER);

            columns.add(tempVbox); //add the temp reference to be a persistent reference
            ObservableList<Task> observableList;

            if (pqm.getTasksByCategory().get(pqm.getCategories().get(i)) == null) {
                observableList = FXCollections.emptyObservableList(); //if there is a category but there is no collection to associate with the category

            } else{
                observableList = FXCollections.observableList(pqm.getTasksByCategory().get(pqm.getCategories().get(i)));
                //set the collection to the observable list if the map isn't empty
            }

            ListView<Task> columnOfPriorityCards = new ListView<>(observableList);
            columnOfPriorityCards.setItems(observableList);
            columnOfPriorityCards.setCellFactory(new Callback<ListView<Task>, ListCell<Task>>() {
                     @Override public ListCell<Task> call(ListView<Task> list) {
                         return new PriorityQueueCardView(pqc);

                     }
                 }
            );
            root.getChildren().add(tempVbox); //add it to the root
            tempVbox.getChildren().add(columnOfPriorityCards);
            HBox.setHgrow(tempVbox, Priority.ALWAYS);
        }
        this.setCenter(root);


        ArrayList<String> arrayList = pqm.getCategories();
        ObservableList<Label> observableListLabels = FXCollections.observableArrayList();
        arrayList.forEach(t -> {
            observableListLabels.add(new Label(t));
        });

        lblComboBox = new ComboBox<>(observableListLabels);
        lblComboBox.setItems(observableListLabels);
        if(observableListLabels.size() >= 1) {
            lblComboBox.setValue(observableListLabels.get(0));
        }

//        HBox bottomBox = new HBox();
        this.leftSideVBox.getChildren().clear();
        this.leftSideVBox.getChildren().addAll(columnName, addColumn,lblComboBox,moveButton);
    }

}
