package controllers;

import classes.Student;
import classes.StudentDataStore;
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

public class RegisterStudent {
    @FXML
    private TextField studentName;
    @FXML
    private Label nameErrorMessage;

    @FXML
    private TextField studentUsername;
    @FXML
    private Label usernameErrorMessage;

    @FXML
    private PasswordField studentPassword;
    @FXML
    private Label passwordErrorMessage;

    @FXML
    public void handleStudentRegister() {
        StudentDataStore studentDataStore = new StudentDataStore();
        try {
            String name = studentName.getText();
            String username = studentUsername.getText();
            String password = studentPassword.getText();

            Student student = new Student(name, username, password);
            boolean studentValid = validateStudentDetails(student.getName(), student.getUsername(), student.getPassword());

            if (studentValid) {
                studentDataStore.open();
                usernameErrorMessage.setText(studentDataStore.insertStudent(student.getName(), student.getUsername(), student.getPassword()));
                if (!usernameErrorMessage.getText().isEmpty()) {
                    CustomAlert.showAlert(Alert.AlertType.WARNING, "CAN'T CREATE STUDENT", "Student Already Exists!", "The username provided already exists");
                    studentUsername.requestFocus();
                } else {
                    CustomAlert.showAlert(Alert.AlertType.CONFIRMATION, "STUDENT CREATED", "Student Created Successfully", "");
                    studentName.setText(null);
                    studentUsername.setText(null);
                    studentPassword.setText(null);
                }
            }
        } catch (NullPointerException ex) {
            ex.printStackTrace();
            CustomAlert.showAlert(Alert.AlertType.WARNING, "INVALID STUDENT DETAILS", "Student Registration Incomplete!", "Please ensure you complete the form");
        }
    }

    private boolean validateStudentDetails(String studentName, String studentUsername, String studentPassword) {
        boolean isStudentValid = false;
        if (studentName.isEmpty()) {
            nameErrorMessage.setText("Student Name is required!");
            isStudentValid = true;
        } else {
            nameErrorMessage.setText("");
            isStudentValid = !isStudentValid;
        }

        if (studentUsername.isEmpty()) {
            usernameErrorMessage.setText("Student username is required!");
            isStudentValid = true;
        } else {
            usernameErrorMessage.setText("");
            isStudentValid = !isStudentValid;
        }

        if (studentPassword.isEmpty()) {
            passwordErrorMessage.setText("Student password is required!");
            isStudentValid = false;
        } else {
            passwordErrorMessage.setText("");
            isStudentValid = !isStudentValid;
        }

        if (!isStudentValid) return false;
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
