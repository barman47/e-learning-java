package sample;

import classes.Admin;
import classes.AdminDataStore;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import javafx.event.ActionEvent;
import java.io.IOException;

public class HomeController {
    // Admin login controls
    @FXML
    private TextField adminUsername;

    @FXML
    private Label adminUsernameError;

    @FXML
    private PasswordField adminPassword;

    @FXML
    private Label adminPasswordError;

    @FXML
    private Hyperlink registerAdminLink;

    @FXML
    private Button adminLoginButton;

    // Teacher login controls
    @FXML
    private TextField teacherUsername;

    @FXML
    private Label teacherUsernameError;

    @FXML
    private PasswordField teacherPassword;

    @FXML
    private Label teacherPasswordError;

    @FXML
    private Hyperlink registerTeacherLink;

    @FXML
    private Button teacherLoginButton;

    // Student login controls
    @FXML
    private TextField studentUsername;

    @FXML
    private Label studentUsernameError;

    @FXML
    private PasswordField studentPassword;

    @FXML
    private Label studentPasswordError;

    @FXML
    private Hyperlink registerStudentLink;

    @FXML
    private Button studentLoginButton;

    @FXML
    public void intialize () {

    }

    @FXML
    public void handleAdminLogin () {
        handleLogin(adminUsername, adminPassword, "admin");
    }

    @FXML
    public void handleTeacherLogin () {
        handleLogin(teacherUsername, teacherPassword, "teacher");
    }

    @FXML
    public void handleStudentLogin () {
        handleLogin(studentUsername, studentPassword, "student");
    }

    // Handle user login
    private void handleLogin (TextField username, PasswordField password, String useCase) {
        String errorTitle = "LOGIN ERROR";
        String errorHeader = "Invalid Login Details";
        String errorMessage= "Username and Password are required!";

        boolean isValid;
        // login case
        switch (useCase) {
            // admin useCase
            case "admin":
                if (username.getText().isEmpty()) {
                    adminUsernameError.setText("Admin Username is required!");
                    isValid = false;
                } else {
                    adminUsernameError.setText("");
                    isValid = true;
                }

                if (password.getText().isEmpty()) {
                    adminPasswordError.setText("Admin Password is required!");
                    isValid = false;
                } else {
                    adminPasswordError.setText("");
                    isValid = true;
                }

                if (!isValid) {
                    CustomAlert.showAlert(Alert.AlertType.ERROR, errorTitle, errorHeader, errorMessage);
                } else {
                    AdminDataStore adminDataStore = new AdminDataStore();
                    adminDataStore.open();
                    // Query and login admin
                    Admin admin = adminDataStore.loginAdmin(username.getText(), password.getText());
                    if (admin != null) {
                        if (admin.getPassword() == password.getText()) {
                            adminUsernameError.setText("");
                            adminPasswordError.setText("");
                            System.out.println("Passwords match");
                        } else {
                            adminPasswordError.setText("Password Incorrect!");
                            adminPassword.requestFocus();

                            // Show Admin Dashboard
                        }
                    } else {
                        adminUsernameError.setText("Admin not found!");
                        adminUsername.requestFocus();
                    }
                }
                break;

            // Student useCase
            case "student":
                if (username.getText().isEmpty()) {
                    studentUsernameError.setText("Student Username is required!");
                    isValid = false;
                } else {
                    isValid = true;
                }

                if (password.getText().isEmpty()) {
                    studentPasswordError.setText("Student Password is required!");
                    isValid = false;
                } else {
                    isValid = true;
                }

                if (!isValid) {
                    CustomAlert.showAlert(Alert.AlertType.ERROR, errorTitle, errorHeader, errorMessage);
                } else {
                    studentUsernameError.setText("");
                    studentPasswordError.setText("");
                    CustomAlert.showAlert(Alert.AlertType.CONFIRMATION, "LOGIN SUCCESSFUL", "Login Succeeded", "Admin Logged In");
                }
                break;

            // teacher useCase
            case "teacher": {
                if (username.getText().isEmpty()) {
                    teacherUsernameError.setText("Teacher Username is required!");
                    isValid = false;
                } else {
                    isValid = true;
                }

                if (password.getText().isEmpty()) {
                    teacherPasswordError.setText("Teacher Password is required!");
                    isValid = false;
                } else {
                    isValid = true;
                }

                if (!isValid) {
                    CustomAlert.showAlert(Alert.AlertType.ERROR, errorTitle, errorHeader, errorMessage);
                } else {
                    teacherUsernameError.setText("");
                    teacherPasswordError.setText("");
                    CustomAlert.showAlert(Alert.AlertType.CONFIRMATION, "LOGIN SUCCESSFUL", "Login Succeeded", "Admin Logged In");
                }
                break;
            }

            default:
                break;
        }
    }

    @FXML
    public void showRegisterAdminScene (ActionEvent event) throws IOException {
        showRegisterPage(event, "admin");
    }

    @FXML
    public void showRegisterStudentScene (ActionEvent event) throws IOException {
        showRegisterPage(event, "student");
    }

    @FXML
    public void showRegisterTeacherScene (ActionEvent event) throws IOException {
        showRegisterPage(event, "teacher");
    }

    private void showRegisterPage (ActionEvent e, String useCase) throws IOException {
        Parent root;
        Scene registerScene;
        Stage registerStage;
        switch (useCase) {
            case "admin":
                root = FXMLLoader.load(getClass().getResource("../views/registerAdmin.fxml"));
                registerScene = new Scene(root, 663, 467);
                registerStage = (Stage) ((Node)e.getSource()).getScene().getWindow();
                registerStage.hide();
                Stage adminRegisterStage = registerStage;
                adminRegisterStage.setScene(registerScene);
                adminRegisterStage.setTitle("Register New Admin");
                adminRegisterStage.setResizable(false);
//                adminRegisterStage.setMaximized(true);
                adminRegisterStage.centerOnScreen();
                adminRegisterStage.show();
                break;

            case "student":
                root = FXMLLoader.load(getClass().getResource("../views/registerStudent.fxml"));
                registerScene = new Scene(root, 663, 467);
                registerStage = (Stage) ((Node)e.getSource()).getScene().getWindow();
                registerStage.hide();
                Stage studentRegisterStage = registerStage;
                studentRegisterStage.setScene(registerScene);
                studentRegisterStage.setTitle("Register New Student");
                studentRegisterStage.setResizable(false);
//                adminRegisterStage.setMaximized(true);
                studentRegisterStage.centerOnScreen();
                studentRegisterStage.show();
                break;

            case "teacher":
                root = FXMLLoader.load(getClass().getResource("../views/registerTeacher.fxml"));
                registerScene = new Scene(root, 663, 467);
                registerStage = (Stage) ((Node)e.getSource()).getScene().getWindow();
                registerStage.hide();
                Stage teacherRegisterStage = registerStage;
                teacherRegisterStage.setScene(registerScene);
                teacherRegisterStage.setTitle("Register New Teacher");
                teacherRegisterStage.setResizable(false);
//                adminRegisterStage.setMaximized(true);
                teacherRegisterStage.centerOnScreen();
                teacherRegisterStage.show();
                break;
            default:
                break;
        }
    }
}
