package com.sadman.controller.algorithm;

import com.sadman.util.Util;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.net.URL;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.ResourceBundle;

/**
 * @author Sadman
 */
public class SymmetricController implements Initializable {

    @FXML
    private TextArea inputText;

    @FXML
    private TextArea outputText;

    @FXML
    private TextField keyText;

//    @FXML
//    private Label lblSymmteric;

    @FXML
    private Button btnEncrypt;

    @FXML
    private Button btnDecrypt;

    @FXML
    private Button btnGenerateKey;

    @FXML
    private Button btnUploadKey;

    @FXML
    private Label lblStatus;

    private String header;
    private Cipher cipher;
    private FileChooser fileChooser;
    private File selectedFile;

    public SymmetricController(String header) throws NoSuchPaddingException, NoSuchAlgorithmException {
        this.header = header;
        this.cipher = Cipher.getInstance(header);
        this.fileChooser = new FileChooser();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
//        lblSymmteric.setText(header);
    }

    public void generateKey(ActionEvent actionEvent) throws NoSuchAlgorithmException {
        SecretKey secretKey = KeyGenerator.getInstance(header).generateKey();
        String key = Base64.getEncoder().encodeToString(secretKey.getEncoded());
        keyText.setText(key);
        lblStatus.setText(header + " Key generated.");
        lblStatus.setTextFill(Color.GREEN);
    }

    public void doEncrypt(ActionEvent actionEvent) throws IOException, InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException, BadPaddingException, IllegalBlockSizeException {
        String originalString = inputText.getText();
        String keyString = keyText.getText();
        byte[] decodedKey = Base64.getDecoder().decode(keyString);
        SecretKey secretKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, header);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        String encryptedString = Base64.getEncoder().encodeToString(cipher.doFinal(originalString.getBytes("UTF-8")));
        outputText.setText(encryptedString);
        lblStatus.setText("Text encrypted.");
        lblStatus.setTextFill(Color.GREEN);
    }

    public void doDecrypt(ActionEvent actionEvent) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        String encryptedString = inputText.getText();
        String keyString = keyText.getText();
        byte[] decodedKey = Base64.getDecoder().decode(keyString);
        SecretKey secretKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, header);
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        String decryptedString = new String(cipher.doFinal(Base64.getDecoder().decode(encryptedString)));
        outputText.setText(decryptedString);
        lblStatus.setText("Text decrypted.");
        lblStatus.setTextFill(Color.GREEN);
    }

    public void doUploadText(ActionEvent actionEvent) throws IOException {
        selectedFile = fileChooser.showOpenDialog(null);

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
        } else {
            lblStatus.setText("File selection cancelled.");
            lblStatus.setTextFill(Color.RED);
        }
    }

    public void doDownloadText(ActionEvent actionEvent) throws IOException {
        String str = outputText.getText();
        BufferedWriter writer = new BufferedWriter(new FileWriter(header + "Text.txt"));
        writer.write(str);
        lblStatus.setText("File downloaded: " + header + "Text.txt");
        lblStatus.setTextFill(Color.GREEN);
        writer.close();
    }

    public void doUploadKey(ActionEvent actionEvent) throws IOException {
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
            keyText.setText(resultStringBuilder.toString());
        } else {
            lblStatus.setText("File selection cancelled.");
            lblStatus.setTextFill(Color.RED);
        }
    }

    public void doDownloadKey(ActionEvent actionEvent) throws IOException {
        String str = keyText.getText().trim();
        BufferedWriter writer = new BufferedWriter(new FileWriter(header + "Key.txt"));
        writer.write(str);
        lblStatus.setText("File downloaded: " + header + "Key.txt");
        lblStatus.setTextFill(Color.GREEN);
        writer.close();
    }
}
