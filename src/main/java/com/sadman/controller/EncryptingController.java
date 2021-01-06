package com.sadman.controller;

import com.itextpdf.signatures.DigestAlgorithms;
import com.itextpdf.signatures.PdfSigner;
import com.sadman.model.CertificateInfo;
import com.sadman.service.SigningService;
import com.sadman.util.JavaKeyStore;
import com.sadman.util.Util;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ResourceBundle;

/**
 * @author Sadman
 */
public class EncryptingController implements Initializable {

    @FXML
    ChoiceBox<String> cbType;

    @FXML
    ChoiceBox<CertificateInfo> cbKey;

    @FXML
    private Label lblStatus;

    @FXML
    private Label lblKeyType;

    private String SRC;
    private String DES;
    private String FILE_NAME;
    private String ALIAS_NAME;
    private String TYPE;
    JavaKeyStore javaKeyStore;
    BouncyCastleProvider provider = new BouncyCastleProvider();


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Security.addProvider(provider);
        ObservableList<String> types = FXCollections.observableArrayList(
                "Encrypt", "Decrypt");
        cbType.setItems(types);
        lblKeyType.setText("Select Key");
        try {
            cbKey.setItems(Util.getCertificates());
            javaKeyStore = new JavaKeyStore("PKCS12", "123456", "JavaKeyStore.jks");
            javaKeyStore.loadKeyStore();
        } catch (CertificateException | IOException | KeyStoreException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        cbKey.valueProperty().addListener((obs, oldval, newval) -> {
            if(newval != null)
                ALIAS_NAME = newval.getAliasName();
        });
        cbType.valueProperty().addListener((obs, oldval, newval) -> {
            if(newval != null) {
                TYPE = newval;
                if(TYPE == "Encrypt")
                    lblKeyType.setText("Select Certificate");
                else
                    lblKeyType.setText("Select Private Key");
            }
        });
    }

    @FXML
    public void doSelectFile(ActionEvent actionEvent){
        FileChooser fileChooser = new FileChooser();
        File selectedFile = fileChooser.showOpenDialog(null);
        SRC = selectedFile.toString();
        if(TYPE.equals("Encrypt")) {
            FILE_NAME = selectedFile.getName();
        }

    }

    @FXML
    public void doGenerate(ActionEvent actionEvent) throws GeneralSecurityException, IOException {
        String name = "";
        String extension = "";
        int i = FILE_NAME.lastIndexOf('.');
        System.out.println(i);
        if (i >= 0) {
            name = FILE_NAME.substring(0,i);
            extension = FILE_NAME.substring(i+1);
        }
        if(TYPE.equals("Encrypt")) {
            encryptFile(SRC, "encrypted" + name + "." + extension);
        }
        else {
            decryptedFile(SRC,  "decrypted" + name + "." + extension);
        }
    }

    public void encryptFile(String fileInputPath, String fileOutPath)
            throws NoSuchAlgorithmException, NoSuchPaddingException, IOException, BadPaddingException, IllegalBlockSizeException, InvalidKeyException, KeyStoreException {
        X509Certificate x509Certificate = (X509Certificate)javaKeyStore.getCertificate(ALIAS_NAME);
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, x509Certificate);

        File fileInput = new File(fileInputPath);
        FileInputStream inputStream = new FileInputStream(fileInput);
        byte[] inputBytes = new byte[(int) fileInput.length()];
        inputStream.read(inputBytes);

        byte[] outputBytes = cipher.doFinal(inputBytes);

        File fileEncryptOut = new File(fileOutPath);
        FileOutputStream outputStream = new FileOutputStream(fileEncryptOut);
        outputStream.write(outputBytes);

        inputStream.close();
        outputStream.close();

        System.out.println("File successfully encrypted!");
        System.out.println("New File: " + fileOutPath);
        lblStatus.setText("File Encrypted");
        lblStatus.setTextFill(Color.GREEN);
    }

    public void decryptedFile(String fileInputPath, String fileOutPath)
            throws NoSuchAlgorithmException, NoSuchPaddingException, IOException,
            IllegalBlockSizeException, BadPaddingException, KeyStoreException, UnrecoverableKeyException, InvalidKeyException {
        PrivateKey privateKey = javaKeyStore.getPrivateKey(ALIAS_NAME,"123456");
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);

        File fileInput = new File(fileInputPath);
        FileInputStream inputStream = new FileInputStream(fileInput);
        byte[] inputBytes = new byte[(int) fileInput.length()];
        inputStream.read(inputBytes);

        byte[] outputBytes = cipher.doFinal(inputBytes);

        File fileEncryptOut = new File(fileOutPath);
        FileOutputStream outputStream = new FileOutputStream(fileEncryptOut);
        outputStream.write(outputBytes);

        inputStream.close();
        outputStream.close();

        System.out.println("File successfully decrypted!");
        System.out.println("New File: " + fileOutPath);
        lblStatus.setText("File Decrypted");
        lblStatus.setTextFill(Color.GREEN);
    }

    @FXML
    public void closeAction(ActionEvent event) {
        Platform.exit();
    }
}
