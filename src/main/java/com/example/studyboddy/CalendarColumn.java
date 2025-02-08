package com.example.studyboddy;

import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

import java.util.Date;
import java.util.List;

public class CalendarColumn extends Pane {
    /**
     * Positions labels, so they take up vertical space corresponding to the events they are for
     *      FOR WHATEVER REASON DOESN'T WORK UNLESS YOU MANUALLY DO <CalendarColumn>.setHeight(whatever height)
     * @param startTime the start time of this column (i.e. if the column is for friday this would be midnight at the start of friday)
     * @param endTime the end time of this column     (i.e. if the column is for friday this would be midnight at the start of the next day)
     * @param labels Nodes and events are parallel lists of the node to position, and the event itself
     * @param labels List of labels parallel to list of events          (i.e. labels.get(i) is the label for the event events.get(i))
     * @param events List of CalendarEvents parallel to list of labels
     */
    public void positionNodes(Date startTime, Date endTime, List<Label> labels, List<CalendarEvent> events) {
        // how long this entire column is in milliseconds
        long columnDuration = endTime.getTime() - startTime.getTime();

        double columnHeight = this.getHeight();

        for (int i = 0; i < labels.size(); i++) {
            Label node = labels.get(i);
            CalendarEvent event = events.get(i);
            if (events.get(i).displayColor != null) {
                // TODO: if this is middle of grey, there is no visiblity
                node.setStyle("-fx-background-color: " + toHexString( event.getDisplayColor()) + "; -fx-text-fill: " + toHexString(event.getDisplayColor().invert()));
            }

            // how many ms after the start of this column
            long relEventStart = event.startTime.getTime() - startTime.getTime();
            // the y coordinate of the top of the event; (relEventStart / columnDuration) is double from 0-1 on where it is in the column
            double startY = columnHeight * relEventStart / columnDuration;
            startY = Math.max(startY, 0);

            long relEventEnd = event.endTime.getTime() - startTime.getTime();
            double endY = columnHeight * relEventEnd / columnDuration;
            endY = Math.min(endY, columnHeight);

            // add it with startY top and endY-startY height
            this.getChildren().add(node);
            double height = endY - startY;
            node.setPrefHeight(height);
            node.setMinHeight(height);
            node.setMaxHeight(height);

            // manually set its top position
            node.setLayoutY(startY);

            // this would be if we are relatively moving it, but we are not
            //node.setLayoutY(startY - node.getLayoutBounds().getMinY());
        }
    }

    // https://stackoverflow.com/questions/60441144/how-to-convert-color-from-colorpicker-to-string-value-in-javafx
    private static String toHexString(Color color) {
        int r = ((int) Math.round(color.getRed()     * 255)) << 24;
        int g = ((int) Math.round(color.getGreen()   * 255)) << 16;
        int b = ((int) Math.round(color.getBlue()    * 255)) << 8;
        int a = ((int) Math.round(color.getOpacity() * 255));
        return String.format("#%08X", (r + g + b + a));
    }

    /** sets height of this column */
    public void setHeightDupe(double v) {
        this.setHeight(v);
    }
}

