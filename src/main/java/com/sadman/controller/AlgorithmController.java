package com.sadman.controller;

import com.sadman.controller.algorithm.AsymmetricController;
import com.sadman.controller.algorithm.HashingController;
import com.sadman.controller.algorithm.SymmetricController;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author Sadman
 */
public class AlgorithmController implements Initializable {

    @FXML
    private ComboBox<String> nameCBX;

    @FXML
    private Pane pnlHashing;

    @FXML
    private Pane pnlAsymmetric;

    @FXML
    private Pane pnlSymmetric;

    @FXML
    private ToggleButton toggleButtonSymmetric;

    @FXML
    private ToggleButton toggleButtonAsymmetric;

    @FXML
    private ToggleButton toggleButtonHashing;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        ToggleGroup algorithmGroup = new ToggleGroup();
        toggleButtonSymmetric.setToggleGroup(algorithmGroup);
        toggleButtonAsymmetric.setToggleGroup(algorithmGroup);
        toggleButtonHashing.setToggleGroup(algorithmGroup);
    }

    public void loadSymmetric(){
        nameCBX.setItems(FXCollections.observableArrayList("AES", "DES"));
        nameCBX.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            public void changed(ObservableValue<? extends String> ov, final String oldvalue, final String newvalue) {
                try {
                    nameChangedSymmetric(ov, oldvalue, newvalue);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void loadAsymmetric(){
        nameCBX.setItems(FXCollections.observableArrayList("RSA", "ECC"));
        nameCBX.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            public void changed(ObservableValue<? extends String> ov, final String oldvalue, final String newvalue) {
                try {
                    nameChangedAsymmetric(ov, oldvalue, newvalue);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void loadHashing(){
        nameCBX.setItems(FXCollections.observableArrayList("MD5", "SHA-1", "SHA-256", "SHA-512"));
        nameCBX.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            public void changed(ObservableValue<? extends String> ov, final String oldvalue, final String newvalue) {
                try {
                    nameChangedHashing(ov, oldvalue, newvalue);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void nameChangedSymmetric(ObservableValue<? extends String> observable, String oldValue, String newValue) throws IOException {
//        String oldText = oldValue == null ? "null" : oldValue.toString();
        String newText = newValue == null ? "null" : newValue.toString();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/symmetric.fxml"));
        SymmetricController symmetricController = new SymmetricController(newText);
        loader.setController(symmetricController);
        Pane symmetricPane = loader.load();
        pnlSymmetric.getChildren().setAll(symmetricPane);
        pnlSymmetric.setStyle("-fx-background-color : #F1F0F6");
        pnlSymmetric.toFront();
    }

    public void nameChangedAsymmetric(ObservableValue<? extends String> observable, String oldValue, String newValue) throws IOException {
//        String oldText = oldValue == null ? "null" : oldValue.toString();
        String newText = newValue == null ? "null" : newValue.toString();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/asymmetric.fxml"));
        AsymmetricController asymmetricController = new AsymmetricController(newText);
        loader.setController(asymmetricController);
        Pane asymmetricPane = loader.load();
        pnlAsymmetric.getChildren().setAll(asymmetricPane);
        pnlAsymmetric.setStyle("-fx-background-color : #F1F0F6");
        pnlAsymmetric.toFront();
    }

    public void nameChangedHashing(ObservableValue<? extends String> observable, String oldValue, String newValue) throws IOException {
//        String oldText = oldValue == null ? "null" : oldValue.toString();
        String newText = newValue == null ? "null" : newValue.toString();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/hashing.fxml"));
        HashingController hashingController = new HashingController(newText);
        loader.setController(hashingController);
        Pane hashingPane = loader.load();
        pnlHashing.getChildren().setAll(hashingPane);
        pnlHashing.setStyle("-fx-background-color : #F1F0F6");
        pnlHashing.toFront();
    }

    @FXML
    public void closeAction(ActionEvent event) {
        Platform.exit();
    }
}
