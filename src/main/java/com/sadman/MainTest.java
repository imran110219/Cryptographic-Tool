package com.sadman;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.layout.TilePane;
import javafx.stage.Stage;

import java.time.Duration;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class MainTest extends Application {

    // launch the application
    public void start(Stage s)
    {
        // set title for the stage
        s.setTitle("creating date picker");

        // create a tile pane
        TilePane r = new TilePane();

        // label to show the date
        Label l1 = new Label("no date selected");
        Label l2 = new Label();

        // create a date picker
        DatePicker d = new DatePicker();

        // action event
        EventHandler<ActionEvent> event = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e)
            {
                // get the date picker value
                LocalDate i = d.getValue();
                long validity = ChronoUnit.DAYS.between(LocalDate.now(),i);
//                long validity = d.toMinutes();
                // get the selected date
                l1.setText("Date :" + i);
                l2.setText(String.valueOf(validity));
            }
        };

        // show week numbers
        d.setShowWeekNumbers(true);

        // when datePicker is pressed
        d.setOnAction(event);

        // add button and label
        r.getChildren().add(d);
        r.getChildren().add(l1);
        r.getChildren().add(l2);

        // create a scene
        Scene sc = new Scene(r, 200, 200);

        // set the scene
        s.setScene(sc);

        s.show();
    }

    public static void main(String args[])
    {
        // launch the application
        launch(args);
    }
}