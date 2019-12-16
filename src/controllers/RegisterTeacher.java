package controllers;

import models.Teacher;
import models.TeacherDataStore;
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

public class RegisterTeacher {
    @FXML
    private TextField teacherName;
    @FXML
    private Label nameErrorMessage;

    @FXML
    private TextField teacherUsername;
    @FXML
    private Label usernameErrorMessage;

    @FXML
    private PasswordField teacherPassword;
    @FXML
    private Label passwordErrorMessage;

    @FXML
    public void handleTeacherRegister() {
        TeacherDataStore teacherDataStore = new TeacherDataStore();
        try {
            String name = teacherName.getText();
            String username = teacherUsername.getText();
            String password = teacherPassword.getText();

            Teacher teacher = new Teacher(name, username, password);
            boolean teacherValid = validateTeacherDetails(teacher.getName(), teacher.getUsername(), teacher.getPassword());

            if (teacherValid) {
                teacherDataStore.open();
                usernameErrorMessage.setText(teacherDataStore.insertTeacher(teacher.getName(), teacher.getUsername(), teacher.getPassword()));
                if (!usernameErrorMessage.getText().isEmpty()) {
                    CustomAlert.showAlert(Alert.AlertType.WARNING, "CAN'T CREATE TEACHER", "Teacher Already Exists!", "The username provided already exists");
                    teacherUsername.requestFocus();
                } else {
                    CustomAlert.showAlert(Alert.AlertType.CONFIRMATION, "TEACHER CREATED", "Teacher Created Successfully", "");
                    teacherName.setText(null);
                    teacherUsername.setText(null);
                    teacherPassword.setText(null);
                }
            }
        } catch (NullPointerException ex) {
            ex.printStackTrace();
            CustomAlert.showAlert(Alert.AlertType.WARNING, "INVALID TEACHER DETAILS", "Teacher Registration Incomplete!", "Please ensure you complete the form");
        }
    }

    private boolean validateTeacherDetails(String teacherName, String teacherUsername, String teacherPassword) {
        boolean isTeacherValid = false;
        if (teacherName.isEmpty()) {
            nameErrorMessage.setText("Teacher Name is required!");
            isTeacherValid = true;
        } else {
            nameErrorMessage.setText("");
            isTeacherValid = !isTeacherValid;
        }

        if (teacherUsername.isEmpty()) {
            usernameErrorMessage.setText("Teacher username is required!");
            isTeacherValid = true;
        } else {
            usernameErrorMessage.setText("");
            isTeacherValid = !isTeacherValid;
        }

        if (teacherPassword.isEmpty()) {
            passwordErrorMessage.setText("Teacher password is required!");
            isTeacherValid = false;
        } else {
            passwordErrorMessage.setText("");
            isTeacherValid = !isTeacherValid;
        }

        if (!isTeacherValid) return false;
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
        Stage teacherRegisterStage = registerStage;
        teacherRegisterStage.setScene(registerScene);
        teacherRegisterStage.setTitle("Register New Teacher");
        teacherRegisterStage.setResizable(false);
        teacherRegisterStage.centerOnScreen();
        teacherRegisterStage.show();
    }
}
