package com.sadman.controller.algorithm;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;

import java.io.*;
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

//    @FXML
//    private Label lblHashing;

    @FXML
    private Button btnHashing;

    @FXML
    private Label lblStatus;

    private String header;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
//        lblHashing.setText(header);
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
        lblStatus.setText("Hash generated.");
        lblStatus.setTextFill(Color.GREEN);
    }

    public void doUploadText(ActionEvent actionEvent) throws IOException {
        FileChooser fileChooser = new FileChooser();
        File selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null) {

            lblStatus.setText("File selected: " + selectedFile.getName());
            lblStatus.setTextFill(Color.GREEN);
            StringBuilder resultStringBuilder = new StringBuilder();
            try (BufferedReader br = new BufferedReader(new FileReader(selectedFile))) {
                String line;
                while ((line = br.readLine()) != null) {
                    resultStringBuilder.append(line).append("\n");
                }
            }

            inputText.setText(resultStringBuilder.toString());
        }
        else {
            lblStatus.setText("File selection cancelled.");
            lblStatus.setTextFill(Color.RED);
        }
    }

    public void doDownloadText(ActionEvent actionEvent) throws IOException {
        String str = outputText.getText();
        BufferedWriter writer = new BufferedWriter(new FileWriter(header + "Text.txt"));
        writer.write(str);
        lblStatus.setText("File downloaded: "  + header + "Text.txt");
        lblStatus.setTextFill(Color.GREEN);
        writer.close();
    }
}
