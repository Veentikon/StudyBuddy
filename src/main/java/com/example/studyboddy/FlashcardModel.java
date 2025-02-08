package com.example.studyboddy;

public class FlashcardModel {
    private FlashcardMainController controller;
    private String front;
    private String back;

    public FlashcardModel(String front, String back) {
        this.front = front;
        this.back = back;
    }

    /***
     * Return the front text of the card
     * @return String
     */
    public String getFront(){
        return front;
    }

    /***
     * Return the back text of the card
     * @return String
     */
    public String getBack(){
        return back;
    }

    public void setController(FlashcardMainController controller){
        this.controller = controller;
    }

    public FlashcardMainController getController(){
        return controller;
    }

    public void updateFront(String front){
        this.front = front;
    }

    public void updateBack(String back){
        this.back = back;
    }
}
