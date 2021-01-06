package com.sadman.controller;

import com.sadman.model.CertificateInfo;
import com.sadman.util.JavaKeyStore;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import sun.security.x509.X500Name;

import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import org.bouncycastle.openssl.jcajce.JcaPEMWriter;
import java.util.*;

/**
 * @author Sadman
 */
public class CertificateController implements Initializable {

    @FXML
    private TableView<CertificateInfo> certificateTable;
    @FXML
    private TableColumn<CertificateInfo, String> nameColumn, companyColumn, validityColumn;

    private double xOffset = 0;
    private double yOffset = 0;
    private JavaKeyStore javaKeyStore;

    public CertificateController() throws CertificateException, NoSuchAlgorithmException, IOException, KeyStoreException {
        javaKeyStore = new JavaKeyStore("PKCS12", "123456", "JavaKeyStore.jks");
        javaKeyStore.loadKeyStore();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("userName"));
        companyColumn.setCellValueFactory(new PropertyValueFactory<>("companyName"));
        validityColumn.setCellValueFactory(new PropertyValueFactory<>("validDate"));
        certificateTable.setItems(getCertificates());
    }

    public ObservableList<CertificateInfo> getCertificates() {
        ObservableList<CertificateInfo> certificates = FXCollections.<CertificateInfo>observableArrayList();
        try {
            Map<String, Certificate> certificateMap = javaKeyStore.getCertificateMap();
            certificateMap.forEach((k, v) -> {
                X509Certificate x509Certificate = (X509Certificate) v;
                String cn = null;
                String on = null;
                try {
                    cn = ((X500Name) x509Certificate.getSubjectDN()).getCommonName();
                    on = ((X500Name) x509Certificate.getSubjectDN()).getOrganization();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                String date = x509Certificate.getNotAfter().toString();
                System.out.println(cn + "     " + on + "     " + date);
                List<String> x = new ArrayList<String>();
                CertificateInfo certificateInfo = new CertificateInfo();
                certificateInfo.setAliasName(k);
                certificateInfo.setUserName(cn);
                certificateInfo.setCompanyName(on);
                certificateInfo.setValidDate(date);
                certificates.add(certificateInfo);
            });

        } catch (KeyStoreException e) {
            e.printStackTrace();
        }
        return certificates;
    }

    @FXML
    public void addAction(ActionEvent event) throws Exception {

        Parent root = FXMLLoader.load(getClass().getResource("/view/certificate/add.fxml"));
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        root.setOnMousePressed((MouseEvent e) -> {
            xOffset = e.getSceneX();
            yOffset = e.getSceneY();
        });
        root.setOnMouseDragged((MouseEvent e) -> {
            stage.setX(e.getScreenX() - xOffset);
            stage.setY(e.getScreenY() - yOffset);
        });
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("New Certificate");
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void deleteAction(ActionEvent event) throws Exception {

        if(certificateTable.getSelectionModel().getSelectedItem() == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Select Certificate");
            alert.setContentText("Please select a certificate");
            Optional<ButtonType> result = alert.showAndWait();
        }
        else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete");
            alert.setHeaderText("Delete Certificate");
            alert.setContentText("Are you sure?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                CertificateInfo selectedCertificate = certificateTable.getSelectionModel().getSelectedItem();
                javaKeyStore.deleteEntry(selectedCertificate.getAliasName());
                javaKeyStore.saveKeyStore();
                certificateTable.getSelectionModel().clearSelection();
            }
        }
    }

    public void downloadCertificate(ActionEvent event) throws Exception {
        if(certificateTable.getSelectionModel().getSelectedItem() == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Select Certificate");
            alert.setContentText("Please select a certificate");
            Optional<ButtonType> result = alert.showAndWait();
        }
        else {
            CertificateInfo selectedCertificate = certificateTable.getSelectionModel().getSelectedItem();
            X509Certificate x509Certificate = (X509Certificate) javaKeyStore.getCertificate(selectedCertificate.getAliasName());
            try(JcaPEMWriter pemWriter = new JcaPEMWriter(new FileWriter("Certificate.pem"))) {
                pemWriter.writeObject(x509Certificate);
            }
        }
    }

    public void downloadPrivateKey(ActionEvent event) throws Exception {
        if(certificateTable.getSelectionModel().getSelectedItem() == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Select Certificate");
            alert.setContentText("Please select a certificate");
            Optional<ButtonType> result = alert.showAndWait();
        }
        else {
            CertificateInfo selectedCertificate = certificateTable.getSelectionModel().getSelectedItem();
            PrivateKey privateKey = javaKeyStore.getPrivateKey(selectedCertificate.getAliasName(),"123456");
            try(JcaPEMWriter pemWriter = new JcaPEMWriter(new FileWriter("PrivateKey.pem"))) {
                pemWriter.writeObject(privateKey);
            }
        }
    }

    public void refreshAction(ActionEvent event) {
        certificateTable.refresh();
    }

    @FXML
    public void closeAction(ActionEvent event) {
        Platform.exit();
    }
}
