package com.sadman.controller.certificate;

import com.sadman.model.CertificateInfo;
import com.sadman.util.CertificateGenerator;
import com.sadman.util.JavaKeyStore;
import com.sadman.util.Util;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.Stage;
import sun.security.tools.keytool.CertAndKeyGen;
import sun.security.x509.X500Name;

import java.io.IOException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

public class CrudCertificateController {

    @FXML
    private TextField nameField;
    @FXML
    private TextField unitField;
    @FXML
    private TextField organizationField;
    @FXML
    private TextField cityField;
    @FXML
    private TextField stateField;
    @FXML
    private TextField countryField;
    @FXML
    private DatePicker expiredDatePicker;
    @FXML
    private TextField aliasField;
    @FXML
    private PasswordField passwordField;

    @FXML
    private Button saveButton;

    @FXML
    private TableView<CertificateInfo> certificateTable;

    JavaKeyStore javaKeyStore;

    public CrudCertificateController() throws CertificateException, NoSuchAlgorithmException, KeyStoreException, IOException {
        javaKeyStore = new JavaKeyStore(Util.readApplicationProperty("java.keystore.type"),
                Util.readApplicationProperty("java.keystore.password"),
                Util.readApplicationProperty("java.keystore.name"));
        javaKeyStore.loadKeyStore();
    }

    @FXML
    public void saveAction(ActionEvent event) throws Exception {

        if (validateInput()) {
            X500Name x500Name = new X500Name(nameField.getText(),
                    unitField.getText(),
                    organizationField.getText(),
                    cityField.getText(),
                    stateField.getText(),
                    countryField.getText());
            CertificateGenerator certificateGenerator = new CertificateGenerator();
            CertAndKeyGen keypair = certificateGenerator.generateCertAndKeyGen();
            LocalDate valid = expiredDatePicker.getValue();
            long validity = ChronoUnit.DAYS.between(LocalDate.now(),valid);
            X509Certificate certificate = certificateGenerator.generateCertificate(keypair, x500Name, validity);
            PrivateKey privateKey = certificateGenerator.generatePrivateKey(keypair);
            javaKeyStore.setKeyEntry(aliasField.getText(), privateKey, passwordField.getText(), new Certificate[]{certificate});
            javaKeyStore.saveKeyStore();

            ((Stage) saveButton.getScene().getWindow()).close();

            certificateTable.refresh();

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Successful");
            alert.setHeaderText("Certificated Generated!");
            alert.setContentText("Certificated is generated successfully");
            alert.showAndWait();
        }
    }

    private boolean validateInput() {

        String errorMessage = "";

        if (nameField.getText() == null || nameField.getText().length() == 0) {
            errorMessage += "No valid name!\n";
        }

        if (unitField.getText() == null || unitField.getText().length() == 0) {
            errorMessage += "No Organization Unit Name!\n";
        }

        if (organizationField.getText() == null || organizationField.getText().length() == 0) {
            errorMessage += "No Organization Name!\n";
        }

        if (cityField.getText() == null || cityField.getText().length() == 0) {
            errorMessage += "No City Name!\n";
        }

        if (stateField.getText() == null || stateField.getText().length() == 0) {
            errorMessage += "No State Name!\n";
        }

        if (countryField.getText() == null || countryField.getText().length() == 0) {
            errorMessage += "No Country Name!\n";
        }

        if (expiredDatePicker.getValue() == null) {
            errorMessage += "No Valid Date time\n";
        }

        if (LocalDate.now().isAfter(expiredDatePicker.getValue())) {
            errorMessage += "Date should be after present time\n";
        }

        if (aliasField.getText() == null || aliasField.getText().length() == 0) {
            errorMessage += "No Alias Name!\n";
        }

        if (passwordField.getText() == null || passwordField.getText().length() == 0) {
            errorMessage += "No Alias Name!\n";
        }

        if (errorMessage.length() == 0) {
            return true;
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid Fields");
            alert.setHeaderText("Please correct invalid fields");
            alert.setContentText(errorMessage);
            alert.showAndWait();

            return false;
        }
    }

    @FXML
    public void cancelAction(ActionEvent event) {
        ((Node) (event.getSource())).getScene().getWindow().hide();
    }
}
