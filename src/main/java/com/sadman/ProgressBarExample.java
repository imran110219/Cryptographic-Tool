package com.sadman;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 * @author Sadman
 */
public class ProgressBarExample  extends Application {
    public void start(Stage stage) {
        //Creating a progress bar
        ProgressBar progress = new ProgressBar();
        //Setting value to the progress bar
        progress.setProgress(0.5);
        //Creating a progress indicator
        ProgressIndicator indicator = new ProgressIndicator(0.6);
        //Setting the size of the progress bar
        progress.setPrefSize(300, 30);
        //Creating a hbox to hold the progress bar and progress indicator
        HBox hbox = new HBox(20);
        hbox.setSpacing(5);
        hbox.setPadding(new Insets(75, 150, 50, 60));
        hbox.getChildren().addAll(progress, indicator);
        //Setting the stage
        Group root = new Group(hbox);
        Scene scene = new Scene(root, 595, 200, Color.BEIGE);
        stage.setTitle("Progress Bar Example");
        stage.setScene(scene);
        stage.show();
    }
    public static void main(String args[]){
        launch(args);
    }
}