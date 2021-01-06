package com.sadman.controller.algorithm;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.*;
import java.net.URL;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.ResourceBundle;

/**
 * @author Sadman
 */
public class AsymmetricController implements Initializable {

    @FXML
    private TextArea inputText;

    @FXML
    private TextArea outputText;

    @FXML
    private TextField privateKeyText;

    @FXML
    private TextField publicKeyText;

//    @FXML
//    private Label lblAsymmteric;

    @FXML
    private Button btnGenerateKey;

    @FXML
    private Button btnEncrypt;

    @FXML
    private Button btnDecrypt;

    @FXML
    private Label lblStatus;

    private String header;
    private Cipher cipher;
    private FileChooser fileChooser;
    private File selectedFile;

    public AsymmetricController(String header) throws NoSuchPaddingException, NoSuchAlgorithmException {
        this.header = header;
        this.cipher = Cipher.getInstance(header);
        this.fileChooser = new FileChooser();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
//        lblAsymmteric.setText(header);
    }

    @FXML
    public void generateKey(ActionEvent actionEvent) throws NoSuchAlgorithmException {
        KeyPairGenerator kpg = KeyPairGenerator.getInstance(header);
        SecureRandom secureRandom = new SecureRandom();
        kpg.initialize(2048,secureRandom);
        KeyPair kp = kpg.generateKeyPair();
        PublicKey publicKey = kp.getPublic();
        PrivateKey privateKey = kp.getPrivate();

        String strPublicKey = Base64.getEncoder().encodeToString(publicKey.getEncoded());
        String strPrivateKey = Base64.getEncoder().encodeToString(privateKey.getEncoded());

        publicKeyText.setText(strPublicKey);
        privateKeyText.setText(strPrivateKey);
        lblStatus.setText("Key Pair generated.");
        lblStatus.setTextFill(Color.GREEN);
    }

    @FXML
    public void doEncrypt(ActionEvent actionEvent) throws IOException, NoSuchPaddingException, NoSuchAlgorithmException, BadPaddingException, IllegalBlockSizeException, InvalidKeyException, InvalidKeySpecException {
        String plainString = inputText.getText();
        String publicKeyString = publicKeyText.getText();
        byte[] publicBytes = Base64.getDecoder().decode(publicKeyString);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(header);
        PublicKey publickey = keyFactory.generatePublic(keySpec);

        cipher.init(Cipher.ENCRYPT_MODE, publickey);
        String encryptedString = Base64.getEncoder().encodeToString(cipher.doFinal(plainString.getBytes("UTF-8")));
        outputText.setText(encryptedString);
        lblStatus.setText("Text encrypted.");
        lblStatus.setTextFill(Color.GREEN);
    }

    @FXML
    public void doDecrypt(ActionEvent actionEvent) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, InvalidKeySpecException {
        String encryptedString = inputText.getText();
        String privateKeyString = privateKeyText.getText();
        byte[] privateBytes = Base64.getDecoder().decode(privateKeyString);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(header);
        PrivateKey privateKey = keyFactory.generatePrivate(keySpec);

        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        String decryptedString = new String(cipher.doFinal(Base64.getDecoder().decode(encryptedString)));
        outputText.setText(decryptedString);
        lblStatus.setText("Text decrypted.");
        lblStatus.setTextFill(Color.GREEN);
    }

    @FXML
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
        }
        else {
            lblStatus.setText("File selection cancelled.");
            lblStatus.setTextFill(Color.RED);

        }
    }

    @FXML
    public void doDownloadText(ActionEvent actionEvent) throws IOException {
        String str = outputText.getText();
        BufferedWriter writer = new BufferedWriter(new FileWriter(header + "Text.txt"));
        writer.write(str);
        lblStatus.setText("File downloaded: "  + header + "Text.txt");
        lblStatus.setTextFill(Color.GREEN);
        writer.close();
    }

    public void doUploadPrivateKey(ActionEvent actionEvent) throws IOException {
        selectedFile = new FileChooser().showOpenDialog(null);

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
            privateKeyText.setText(resultStringBuilder.toString());
        }
        else {
            lblStatus.setText("File selection cancelled.");
            lblStatus.setTextFill(Color.RED);
        }
    }

    @FXML
    public void doDownloadPrivateKey(ActionEvent actionEvent) throws IOException {
        String str = publicKeyText.getText().trim();
        BufferedWriter writer = new BufferedWriter(new FileWriter(header + "Privatekey.txt"));
        writer.write(str);
        lblStatus.setText("File downloaded: " + header + "Privatekey.txt");
        lblStatus.setTextFill(Color.GREEN);
        writer.close();
    }

    public void doUploadPublicKey(ActionEvent actionEvent) throws IOException {
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
            publicKeyText.setText(resultStringBuilder.toString());
        }
        else {
            lblStatus.setText("File selection cancelled.");
            lblStatus.setTextFill(Color.RED);
        }
    }

    public void doDownloadPublicKey(ActionEvent actionEvent) throws IOException {
        String str = privateKeyText.getText().trim();
        BufferedWriter writer = new BufferedWriter(new FileWriter(header + "PublicKey.txt"));
        writer.write(str);
        lblStatus.setText("File downloaded: " + header + "PublicKey.txt");
        lblStatus.setTextFill(Color.GREEN);
        writer.close();
    }
}
