package com.sadman.controller.algorithm;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
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

    @FXML
    private Label lblAsymmteric;

    @FXML
    private Button btnGenerateKey;

    @FXML
    private Button btnEncrypt;

    @FXML
    private Button btnDecrypt;

    private String header;

    public AsymmetricController(String header) {
        this.header = header;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        lblAsymmteric.setText(header);
    }

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
    }

    public void doEncrypt(ActionEvent actionEvent) throws IOException, NoSuchPaddingException, NoSuchAlgorithmException, BadPaddingException, IllegalBlockSizeException, InvalidKeyException, InvalidKeySpecException {
        String plainString = inputText.getText();
        String publicKeyString = publicKeyText.getText();
        byte[] publicBytes = Base64.getDecoder().decode(publicKeyString);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(header);
        PublicKey publickey = keyFactory.generatePublic(keySpec);

        Cipher cipher = Cipher.getInstance(header);
        cipher.init(Cipher.ENCRYPT_MODE, publickey);
        String encryptedString = Base64.getEncoder().encodeToString(cipher.doFinal(plainString.getBytes("UTF-8")));
        outputText.setText(encryptedString);
    }

    public void doDecrypt(ActionEvent actionEvent) throws IOException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, InvalidKeySpecException {
        String encryptedString = inputText.getText();
        String privateKeyString = privateKeyText.getText();
        byte[] privateBytes = Base64.getDecoder().decode(privateKeyString);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(header);
        PrivateKey privateKey = keyFactory.generatePrivate(keySpec);

        Cipher cipher = Cipher.getInstance(header);
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        String decryptedString = new String(cipher.doFinal(Base64.getDecoder().decode(encryptedString)));
        outputText.setText(decryptedString);
    }

}
