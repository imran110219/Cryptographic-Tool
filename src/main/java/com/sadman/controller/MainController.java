package com.sadman.controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author Sadman
 */
public class MainController implements Initializable {

//    @FXML
//    private VBox pnItems = null;

    @FXML
    private Button btnDashboard;

    @FXML
    private Button btnAlgorithm;

    @FXML
    private Button btnCerficate;

    @FXML
    private Button btnSigning;

    @FXML
    private Button btnEncrypt;

    @FXML
    private Button btnMail;

    @FXML
    private Pane pnlDashboard;

    @FXML
    private Pane pnlAlgorithm;

    @FXML
    private Pane pnlCerficate;

    @FXML
    private Pane pnlSigning;

    @FXML
    private Pane pnlEncrypt;

    @FXML
    private Pane pnlMail;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("Initialize");
        Pane dashboardPane = new Pane();
        try {
            dashboardPane = FXMLLoader.load(getClass().getResource("/view/dashboard.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        pnlDashboard.getChildren().setAll(dashboardPane);
        pnlDashboard.setStyle("-fx-background-color : #1620A1");
        pnlDashboard.toFront();
    }


    public void handleClicks(ActionEvent actionEvent) throws IOException {
        if (actionEvent.getSource() == btnDashboard) {
            Pane dashboardPane =  FXMLLoader.load(getClass().getResource("/view/dashboard.fxml"));
            pnlDashboard.getChildren().setAll(dashboardPane);
            pnlDashboard.setStyle("-fx-background-color : #1620A1");
            pnlDashboard.toFront();
        }
        if (actionEvent.getSource() == btnAlgorithm) {
            Pane algorithmPane =  FXMLLoader.load(getClass().getResource("/view/algorithm.fxml"));
            pnlAlgorithm.getChildren().setAll(algorithmPane);
            pnlAlgorithm.setStyle("-fx-background-color : #F1F0F6");
            pnlAlgorithm.toFront();
        }
        if (actionEvent.getSource() == btnCerficate) {
            Pane certificatePane =  FXMLLoader.load(getClass().getResource("/view/certificate.fxml"));
            pnlCerficate.getChildren().setAll(certificatePane);
            pnlCerficate.setStyle("-fx-background-color : #F1F0F6");
            pnlCerficate.toFront();
        }
        if(actionEvent.getSource()==btnSigning)
        {
            pnlSigning.setStyle("-fx-background-color : #896d6c");
            pnlSigning.toFront();
        }
        if(actionEvent.getSource()==btnEncrypt)
        {
            pnlEncrypt.setStyle("-fx-background-color : #d9cd0f");
            pnlEncrypt.toFront();
        }
        if(actionEvent.getSource()==btnMail)
        {
            pnlMail.setStyle("-fx-background-color : #f11426");
            pnlMail.toFront();
        }
    }



    @FXML
    public void closeAction(ActionEvent event) {
        Platform.exit();
    }
}
