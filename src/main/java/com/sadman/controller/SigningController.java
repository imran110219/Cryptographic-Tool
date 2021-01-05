package com.sadman.controller;

import com.sadman.model.CertificateInfo;
import com.sadman.service.SigningService;
import com.sadman.util.JavaKeyStore;
import com.sadman.util.Util;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.jcajce.JcaPEMWriter;
import sun.security.x509.X500Name;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.*;

import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.signatures.BouncyCastleDigest;
import com.itextpdf.signatures.DigestAlgorithms;
import com.itextpdf.signatures.IExternalDigest;
import com.itextpdf.signatures.IExternalSignature;
import com.itextpdf.signatures.PdfSignatureAppearance;
import com.itextpdf.signatures.PdfSigner;
import com.itextpdf.signatures.PrivateKeySignature;

/**
 * @author Sadman
 */
public class SigningController implements Initializable {

    @FXML
    ChoiceBox<String> cbFileType;

    @FXML
    ChoiceBox<CertificateInfo> cbSigner;

    @FXML
    private Label lblStatus;

    private String SRC;
    private String DES;
    private String ALIAS_NAME;
    private String DOCUMENT_TYPE;
    JavaKeyStore javaKeyStore;
    BouncyCastleProvider provider = new BouncyCastleProvider();


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Security.addProvider(provider);
        ObservableList<String> filetypes = FXCollections.observableArrayList(
                "PDF", "Word", "JPG");
        cbFileType.setItems(filetypes);
        try {
            cbSigner.setItems(Util.getCertificates());
            javaKeyStore = new JavaKeyStore("PKCS12", "123456", "JavaKeyStore.jks");
            javaKeyStore.loadKeyStore();
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        cbSigner.valueProperty().addListener((obs, oldval, newval) -> {
            if(newval != null)
                ALIAS_NAME = newval.getAliasName();
        });
        cbFileType.valueProperty().addListener((obs, oldval, newval) -> {
            if(newval != null)
                DOCUMENT_TYPE = newval;
        });
    }

    @FXML
    public void doSelectFile(ActionEvent actionEvent){
        FileChooser fileChooser = new FileChooser();
        File selectedFile = fileChooser.showOpenDialog(null);
        SRC = selectedFile.toString();
    }

    @FXML
    public void doSign(ActionEvent actionEvent) throws GeneralSecurityException, IOException {
        DES = "E:/Digidoc/signed.pdf";
        Certificate[] chain = javaKeyStore.getCertificateChain(ALIAS_NAME);
        PrivateKey privateKey = Util.getPrivateKey(ALIAS_NAME,"123456");
        if(DOCUMENT_TYPE.equals("PDF")){
            SigningService.signPDF(SRC, DES, chain, privateKey, DigestAlgorithms.SHA256, provider.getName(), PdfSigner.CryptoStandard.CMS, "Test", "Dhaka");
            lblStatus.setText("PDF Signed");
            lblStatus.setTextFill(Color.GREEN);
        }
        else {
            lblStatus.setText("Not Available");
            lblStatus.setTextFill(Color.RED);
        }
    }

    @FXML
    public void closeAction(ActionEvent event) {
        Platform.exit();
    }
}
