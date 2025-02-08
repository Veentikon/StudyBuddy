package com.example.studyboddy;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;

public class FlashcardMainModel {
    private ObservableList<FlashcardSetModel> setsList = FXCollections.observableArrayList();
    private ArrayList<FlashcardModelSubscriber> subscribers;

    private FlashcardSetModel selected;
    private FlashcardMainController controller;

    public FlashcardMainModel() {
        subscribers = new ArrayList<>();
        selected = null;
    }

    /***
     * Add new subscriber to the list if subscribers
     * @param subscriber class implementing FlashcardModelSubscriber interface
     */
    public void addSubscriber(FlashcardModelSubscriber subscriber){
        subscribers.add(subscriber);
    }

    /***
     * Store a reference to controller, for the purposes of linking action listeners to events need to store a reference
     * those events can only be set up at run time.
     * @param controller that will wire action events
     */
    public void setController(FlashcardMainController controller){
        this.controller = controller;
    }

    /**
     * Create a new flash card set
     * @param name name of the subject/topic for the set
     */
    public FlashcardSetModel createFlashcardSet(String name) {
        // create and add a flashcard set
        FlashcardSetModel flashcardSetModel = new FlashcardSetModel(name, controller);
        this.setsList.add(flashcardSetModel);
        notifySubscribers();
        return flashcardSetModel;
    }

    // This method needed to set up ListView instance in the main view
    public ObservableList<FlashcardSetModel> getSetsList(){
        return this.setsList;
    }

    // Notify all subscribers to the TaskModel about changes made to the TaskModel
    private void notifySubscribers(){
        subscribers.forEach(FlashcardModelSubscriber::modelChanged);
    }

    /**
     * Remove a FlashcardSetModel from the list of sets "setsList"
     * @param set a reference to a specific FlashcardSetModel that is to be removed from the list
     */
    public void removeSet(FlashcardSetModel set){
        this.setsList.remove(set);
        // notify all subscribers that they need to update
        notifySubscribers();
    }

    // Set selected flashcard set, is called when a user wants to modify or view a specific set of flashcards
    public void setSelected(FlashcardSetModel model){
        this.selected = model;
    }

    // return current selection of Flashcard Set
    public FlashcardSetModel getSelected(){
        return selected;
    }

    // Create a test set of flashcards
    public void testFlashcardSet() {
        // TODO: implement
    }
}
