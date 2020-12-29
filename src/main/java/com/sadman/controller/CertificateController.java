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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import sun.security.x509.X500Name;

import java.io.IOException;
import java.net.URL;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("userName"));
        companyColumn.setCellValueFactory(new PropertyValueFactory<>("companyName"));
        validityColumn.setCellValueFactory(new PropertyValueFactory<>("validDate"));
        certificateTable.setItems(getCertificates());
    }

    private ObservableList<CertificateInfo> getCertificates() {
        ObservableList<CertificateInfo> certificates = FXCollections.<CertificateInfo> observableArrayList();
        JavaKeyStore javaKeyStore = null;
        try {
            javaKeyStore = new JavaKeyStore("PKCS12","123456", "JavaKeyStore.jks");
            javaKeyStore.loadKeyStore();

            Map<String, Certificate> certificateMap = javaKeyStore.getCertificateMap();
            certificateMap.forEach((k,v)-> {
                X509Certificate x509Certificate = (X509Certificate) v;
                String cn = null;
                String on = null;
                try {
                    cn = ((X500Name)x509Certificate.getSubjectDN()).getCommonName();
                    on = ((X500Name) x509Certificate.getSubjectDN()).getOrganization();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                String date = x509Certificate.getNotAfter().toString();
                System.out.println(cn + "     " + on + "     " +date);
                List<String> x = new ArrayList<String>();
                CertificateInfo certificateInfo = new CertificateInfo();
                certificateInfo.setAliasName(k);
                certificateInfo.setUserName(cn);
                certificateInfo.setCompanyName(on);
                certificateInfo.setValidDate(date);
                certificates.add(certificateInfo);
            });

//            for(Certificate cert : javaKeyStore.getCertificateList()){
//                X509Certificate x509Certificate = (X509Certificate) cert;
////            System.out.println(cert);
//                String an = ((X500Name)x509Certificate.getSubjectDN()).getCommonName();
//                String cn = ((X500Name)x509Certificate.getSubjectDN()).getCommonName();
//                String on = ((X500Name) x509Certificate.getSubjectDN()).getOrganization();
//                String date = x509Certificate.getNotAfter().toString();
//                System.out.println(cn + "     " + on + "     " +date);
//                List<String> x = new ArrayList<String>();
//                CertificateInfo certificateInfo = new CertificateInfo();
//                certificateInfo.setUserName(cn);
//                certificateInfo.setCompanyName(on);
//                certificateInfo.setValidDate(date);
//                certificates.add(certificateInfo);
//            }
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (IOException e) {
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
    public void closeAction(ActionEvent event) {
        Platform.exit();
    }
}
