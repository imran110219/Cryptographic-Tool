package com.sadman.controller.algorithm;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
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

    public SymmetricController(String header) {
        this.header = header;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
//        lblSymmteric.setText(header);
    }

    public void generateKey(ActionEvent actionEvent) throws NoSuchAlgorithmException {
        SecretKey secretKey = KeyGenerator.getInstance("AES").generateKey();
        String key = Base64.getEncoder().encodeToString(secretKey.getEncoded());
        keyText.setText(key);
    }

    public void doEncrypt(ActionEvent actionEvent) throws IOException, InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException, BadPaddingException, IllegalBlockSizeException {
        String originalString = inputText.getText();
        String keyString = keyText.getText();
        byte[] decodedKey = Base64.getDecoder().decode(keyString);
        SecretKey secretKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, header);
        Cipher cipher = Cipher.getInstance(header);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        String encryptedString = Base64.getEncoder().encodeToString(cipher.doFinal(originalString.getBytes("UTF-8")));
        outputText.setText(encryptedString);
    }

    public void doDecrypt(ActionEvent actionEvent) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        String encryptedString = inputText.getText();
        String keyString = keyText.getText();
        byte[] decodedKey = Base64.getDecoder().decode(keyString);
        SecretKey secretKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, header);
        Cipher cipher = Cipher.getInstance(header);
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        String decryptedString = new String(cipher.doFinal(Base64.getDecoder().decode(encryptedString)));
        outputText.setText(decryptedString);
    }

    public void doUploadKey(ActionEvent actionEvent) throws IOException {
        FileChooser fileChooser = new FileChooser();
        File selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null) {

            lblStatus.setText("File selected: " + selectedFile.getName());

            StringBuilder resultStringBuilder = new StringBuilder();
            try (BufferedReader br = new BufferedReader(new FileReader(selectedFile))) {
                String line;
                while ((line = br.readLine()) != null) {
                    resultStringBuilder.append(line).append("\n");
                }
            }

            keyText.setText(resultStringBuilder.toString());
        }
        else {
            lblStatus.setText("File selection cancelled.");
        }
    }
}
