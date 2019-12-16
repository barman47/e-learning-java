package controllers;

import models.AdminDataStore;
import models.Admin;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import sample.CustomAlert;

import java.io.IOException;

public class RegisterAdmin {
    @FXML
    private TextField adminName;
    @FXML
    private Label nameErrorMessage;

    @FXML
    private TextField adminUsername;
    @FXML
    private Label usernameErrorMessage;

    @FXML
    private PasswordField adminPassword;
    @FXML
    private Label passwordErrorMessage;

    @FXML
    public void handleAdminRegister() {
        AdminDataStore adminDataStore = new AdminDataStore();
        try {
            String name = adminName.getText();
            String username = adminUsername.getText();
            String password = adminPassword.getText();

            Admin admin = new Admin(name, username, password);
            boolean adminValid = validateAdminDetails(admin.getName(), admin.getUsername(), admin.getPassword());

            if (adminValid) {
                adminDataStore.open();
                usernameErrorMessage.setText(adminDataStore.insertAdmin(admin.getName(), admin.getUsername(), admin.getPassword()));
                if (!usernameErrorMessage.getText().isEmpty()) {
                    CustomAlert.showAlert(Alert.AlertType.WARNING, "CAN'T CREATE ADMIN", "Admin Already Exists!", "The username provided already exists");
                    adminUsername.requestFocus();
                } else {
                    CustomAlert.showAlert(Alert.AlertType.CONFIRMATION, "ADMIN CREATED", "Admin Created Successfully", "");
                    adminName.setText(null);
                    adminUsername.setText(null);
                    adminPassword.setText(null);
                }
            }
        } catch (NullPointerException ex) {
            ex.printStackTrace();
            CustomAlert.showAlert(Alert.AlertType.WARNING, "INVALID ADMIN DETAILS", "Admin Registration Incomplete!", "Please ensure you complete the form");
        }
    }

    private boolean validateAdminDetails (String adminName, String adminUsername, String adminPassword) {
        boolean isAdminValid = false;
        if (adminName.isEmpty()) {
            nameErrorMessage.setText("Admin Name is required!");
            isAdminValid = true;
        } else {
            nameErrorMessage.setText("");
            isAdminValid = !isAdminValid;
        }

        if (adminUsername.isEmpty()) {
            usernameErrorMessage.setText("Admin username is required!");
            isAdminValid = true;
        } else {
            usernameErrorMessage.setText("");
            isAdminValid = !isAdminValid;
        }

        if (adminPassword.isEmpty()) {
            passwordErrorMessage.setText("Admin password is required!");
            isAdminValid = false;
        } else {
            passwordErrorMessage.setText("");
            isAdminValid = !isAdminValid;
        }

        if (!isAdminValid) return false;
        else {
            return true;
        }
    }

    @FXML
    public void handleBackButtonClick (ActionEvent e) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("../sample/home.fxml"));
        Scene registerScene = new Scene(root, 430, 490);
        Stage registerStage = (Stage) ((Node)e.getSource()).getScene().getWindow();
        registerStage.hide();
        Stage studentRegisterStage = registerStage;
        studentRegisterStage.setScene(registerScene);
        studentRegisterStage.setTitle("Register New Student");
        studentRegisterStage.setResizable(false);
        studentRegisterStage.centerOnScreen();
        studentRegisterStage.show();
    }
}
