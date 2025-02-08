package com.example.studyboddy;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.Iterator;


public class FlashcardSetModel {
    public ObservableList<FlashcardModel> flashcardList = FXCollections.observableArrayList();
    private FlashcardMainController controller;

    private String name;
    private int nOfCards;
    private Iterator<FlashcardModel> iterator;

    private FlashcardSetCellView setView;
    //TODO maybe move setView somewhere else, or leave it, undecided yet.

    public FlashcardSetModel(String name, FlashcardMainController controller) {
        this.controller = controller;
        this.nOfCards = 0;
        this.name = name;
    }

    // save reference to the flashcard main controller
    public FlashcardMainController getMController(){
        return this.controller;
    }

    /***
     * Add new flashcard test to the list
     * @param front text of flashcard
     * @param back text of flashcard
     */
    public void addCard(String front, String back){
        this.flashcardList.add(new FlashcardModel(front, back));
        this.nOfCards += 1;
        setView.modelUpdated();
    }
    // return the name of the set
    public String getName(){
        return name;
    }

    // return the reference to the flashcard main controller
    public void setController(FlashcardMainController controller){
        this.controller = controller;
    }

    // return current number of cards in the set
    public int getNOfCards(){
        return nOfCards;
    }

    // save reference to the corresponding set view for this set model
    public void setView(FlashcardSetCellView view){
        setView = view;
    }

    public String getId(){
        return this.name; // name of the set is also id, corresponding FlashcardSetView has the same id
    }

    public FlashcardModel getFirst(){
        this.iterator = flashcardList.listIterator();
        return iterator.next(); // the first return
    }
    public FlashcardModel getNext(){
        if (iterator.hasNext()){
            return iterator.next();
        }
        else {
            return null;
        }
    }

    public ObservableList<FlashcardModel> getFlashcardList(){
        return flashcardList;
    }

    /* Remove the item(s) from the list */
    public void removeCards(ObservableList<FlashcardModel> list){
        // currently deletes just one element
        flashcardList.remove(list.get(0));
//        list.forEach(item -> flashcardList.remove(item));
    }

    public void decrCount(int decrement){
        this.nOfCards -= decrement;
        this.setView.modelUpdated();
    }
}
