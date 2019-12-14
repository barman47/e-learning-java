package sample;

import classes.*;
import controllers.StudentDashboard;
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
    public void handleAdminLogin (ActionEvent e) throws IOException {
        handleLogin(adminUsername, adminPassword, "admin", e);
    }

    @FXML
    public void handleTeacherLogin (ActionEvent e) throws IOException {
        handleLogin(teacherUsername, teacherPassword, "teacher", e);
    }

    @FXML
    public void handleStudentLogin (ActionEvent e) throws IOException {
        handleLogin(studentUsername, studentPassword, "student", e);
    }

    // Handle user login
    private void handleLogin (TextField username, PasswordField password, String useCase, ActionEvent e) throws IOException {
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
                        if (admin.getPassword().equals(password.getText())) {
                            adminUsernameError.setText("");
                            adminPasswordError.setText("");

                            // Show Admin Dashboard
                            showDashboardPage(e, "admin");
                        } else {
                            adminPasswordError.setText("Password Incorrect!");
                            adminPassword.requestFocus();
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
                    studentUsernameError.setText("");
                    isValid = true;
                }

                if (password.getText().isEmpty()) {
                    studentPasswordError.setText("Student Password is required!");
                    isValid = false;
                } else {
                    studentPasswordError.setText("");
                    isValid = true;
                }

                if (!isValid) {
                    CustomAlert.showAlert(Alert.AlertType.ERROR, errorTitle, errorHeader, errorMessage);
                } else {
                    StudentDataStore studentDataStore = new StudentDataStore();
                    studentDataStore.open();
                    // Query and login teacher
                    Student student = studentDataStore.loginStudent(username.getText(), password.getText());
                    if (student != null) {
                        if (student.getPassword().equals(password.getText())) {
                            studentUsernameError.setText("");
                            studentPasswordError.setText("");

                            // Show Student Dashboard
                            showDashboardPage(e, "student");
                        } else {
                            studentPasswordError.setText("Password Incorrect!");
                            studentPassword.requestFocus();
                        }
                    } else {
                        studentUsernameError.setText("Student not found!");
                        studentUsername.requestFocus();
                    }
                }
                break;

            // teacher useCase
            case "teacher": {
                if (username.getText().isEmpty()) {
                    teacherUsernameError.setText("Teacher Username is required!");
                    isValid = false;
                } else {
                    teacherUsernameError.setText("");
                    isValid = true;
                }

                if (password.getText().isEmpty()) {
                    teacherPasswordError.setText("Teacher Password is required!");
                    isValid = false;
                } else {
                    teacherPasswordError.setText("");
                    isValid = true;
                }

                if (!isValid) {
                    CustomAlert.showAlert(Alert.AlertType.ERROR, errorTitle, errorHeader, errorMessage);
                } else {
                    TeacherDataStore teacherDataStore = new TeacherDataStore();
                    teacherDataStore.open();
                    // Query and login teacher
                    Teacher teacher = teacherDataStore.loginTeacher(username.getText(), password.getText());
                    if (teacher != null) {
                        if (teacher.getPassword().equals(password.getText())) {
                            teacherUsernameError.setText("");
                            teacherPasswordError.setText("");

                            // Show Teacher Dashboard
                            showDashboardPage(e, "teacher");
                        } else {
                            teacherPasswordError.setText("Password Incorrect!");
                            teacherPassword.requestFocus();
                        }
                    } else {
                        teacherUsernameError.setText("Teacher not found!");
                        teacherUsername.requestFocus();
                    }
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
                adminRegisterStage.centerOnScreen();
                adminRegisterStage.show();
                break;

            case "student":
                root = FXMLLoader.load(getClass().getResource("../views/registerStudent.fxml"));
                registerScene = new Scene(root, 750, 467);
                registerStage = (Stage) ((Node)e.getSource()).getScene().getWindow();
                registerStage.hide();
                Stage studentRegisterStage = registerStage;
                studentRegisterStage.setScene(registerScene);
                studentRegisterStage.setTitle("Register New Student");
                studentRegisterStage.setResizable(false);
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
                teacherRegisterStage.centerOnScreen();
                teacherRegisterStage.show();
                break;
            default:
                break;
        }
    }

    private void showDashboardPage (ActionEvent e, String useCase) throws IOException {
        Parent root;
        Scene loginScene;
        Stage loginStage;
        switch (useCase) {
            case "admin":
                root = FXMLLoader.load(getClass().getResource("../views/adminDashboard.fxml"));
                loginScene = new Scene(root, 663, 467);
                loginStage = (Stage) ((Node)e.getSource()).getScene().getWindow();
                loginStage.hide();
                Stage adminRegisterStage = loginStage;
                adminRegisterStage.setScene(loginScene);
                adminRegisterStage.setTitle("Admin Dashboard");
                adminRegisterStage.setResizable(false);
                adminRegisterStage.centerOnScreen();
                adminRegisterStage.show();
                break;

            case "student":
                root = FXMLLoader.load(getClass().getResource("../views/studentDashboard.fxml"));
                loginScene = new Scene(root, 663, 467);
                loginStage = (Stage) ((Node)e.getSource()).getScene().getWindow();
                loginStage.hide();
                Stage studentRegisterStage = loginStage;
                studentRegisterStage.setScene(loginScene);
                studentRegisterStage.setTitle("Student Dashboard");
                studentRegisterStage.setResizable(false);
                studentRegisterStage.centerOnScreen();
                studentRegisterStage.show();
                break;

            case "teacher":
                root = FXMLLoader.load(getClass().getResource("../views/teacherDashboard.fxml"));
                loginScene = new Scene(root, 800, 467);
                loginStage = (Stage) ((Node)e.getSource()).getScene().getWindow();
                loginStage.hide();
                Stage teacherRegisterStage = loginStage;
                teacherRegisterStage.setScene(loginScene);
                teacherRegisterStage.setTitle("Teacher Dashboard");
                teacherRegisterStage.setResizable(false);
                teacherRegisterStage.centerOnScreen();
                teacherRegisterStage.show();
                break;

            default:
                break;
        }
    }
}
