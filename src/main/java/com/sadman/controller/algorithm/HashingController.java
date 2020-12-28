package com.sadman.controller.algorithm;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

import java.io.IOException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ResourceBundle;

/**
 * @author Sadman
 */
public class HashingController implements Initializable {
    @FXML
    private TextArea inputText;

    @FXML
    private TextArea outputText;

    @FXML
    private Label lblHashing;

    @FXML
    private Button btnHashing;

    private String header;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        lblHashing.setText(header);
    }

    public HashingController(String header) {
        this.header = header;
    }

    public void doHash(ActionEvent actionEvent) throws IOException {
        String plainString = inputText.getText();
        String hashString = null;
        try {
            MessageDigest md = MessageDigest.getInstance(header);
            byte[] bytes = md.digest(plainString.getBytes());
            StringBuilder sb = new StringBuilder();
            for(int i=0; i< bytes.length ;i++)
            {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            hashString = sb.toString();
        }
        catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
        outputText.setText(hashString);
    }
}
