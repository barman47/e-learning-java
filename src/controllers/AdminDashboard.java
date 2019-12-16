package controllers;

import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyCode;
import models.Course;
import models.CourseDataStore;
import models.Student;
import models.StudentDataStore;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import sample.CustomAlert;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class AdminDashboard {
    // Course Table
    @FXML
    private TableView<Course> courseTable;

    @FXML
    private TableColumn<Course, Integer> id;

    @FXML
    private TableColumn<Course, String> title;

    @FXML
    private TableColumn<Course, String> courseUrl;

    @FXML
    private TableColumn<Course, String> students;

    // Students Table
    @FXML
    private TableView<Student> studentTable;

    @FXML
    private TableColumn<Student, Integer> studentId;

    @FXML
    private TableColumn<Student, String> studentName;

    @FXML
    private TableColumn<Student, String> studentUsername;

    @FXML
    private TableColumn<Student, String> studentPassword;

    //Observable List of Courses
    private static ObservableList<Course> courses = FXCollections.observableArrayList();

    // Observable list of students
    private static ObservableList<Student> storedStudents = FXCollections.observableArrayList();

    @FXML
    private TextField addCourseField;

    @FXML
    private Label fileName;

    @FXML
    private Label addCourseErrorMessage;

    @FXML
    private Button addCourseButton;

    @FXML
    private ComboBox<String> courseComboBox;

    @FXML
    private ComboBox<String> studentComboBox;

    @FXML
    private Button addStudentToCourse;

    @FXML
    public void initialize () {
        loadCourseTable();
        loadStudentTable();

        // Making the tables editable
        setStudentTableEditable();
        setCourseTableEditable();
    }

    @FXML
    public void handleOkayButton () {
        if (addCourseField.getText().isEmpty() || fileName.getText().isEmpty()) {
            addCourseErrorMessage.setText("Please enter course title and select a file to upload");
            CustomAlert.showAlert(Alert.AlertType.ERROR, "Invalid Course", "INCOMPLETE FORM INFORMATION", "Please provide a course title and a file to upload");
            addCourseField.requestFocus();
        } else {
            Course course = new Course(addCourseField.getText());

            CourseDataStore courseDataStore = new CourseDataStore();
            courseDataStore.open();
            addCourseErrorMessage.setText(courseDataStore.insertCourse(course.getTitle(), fileName.getText()));

            if (!addCourseErrorMessage.getText().isEmpty()) {
                CustomAlert.showAlert(Alert.AlertType.WARNING, "CAN'T ADD COURSE", "Course Already Exists!", "Course title already exists");
                addCourseField.requestFocus();
            } else {
                CustomAlert.showAlert(Alert.AlertType.CONFIRMATION, "COURSE ADDED", "Course Created Successfully", "");
                addCourseField.setText(null);
                addCourseErrorMessage.setText(null);
                fileName.setText(null);
                clearCourseTable();
                loadCourseTable();
            }
        }
    }

    private ObservableList<Course> getCourses () {
        CourseDataStore courseDataStore = new CourseDataStore();
        courseDataStore.open();
        ArrayList<Course> returnedCourses = courseDataStore.getCourses();
        returnedCourses.forEach((course) -> {
            courses.add(new Course(course.getId(), course.getStudents(), course.getCourseUrl(), course.getTitle()));
        });
        return courses;
    }

    private void loadCourseTable () {
        id.setCellValueFactory(new PropertyValueFactory<>("id"));
        students.setCellValueFactory(new PropertyValueFactory<>("students"));
        courseUrl.setCellValueFactory(new PropertyValueFactory<>("courseUrl"));
        title.setCellValueFactory(new PropertyValueFactory<>("title"));

        courseTable.setItems(getCourses());
    }

    private ObservableList<Student> getStudents () {
        StudentDataStore studentDataStore = new StudentDataStore();
        studentDataStore.open();
        ArrayList<Student> returnedStudents = studentDataStore.getStudents();
        returnedStudents.forEach((student) -> {
            storedStudents.add(new Student(student.getId(), student.getName(), student.getUsername(), student.getPassword(), student.getCourses()));
        });
        return storedStudents;
    }

    private void loadStudentTable () {
        studentId.setCellValueFactory(new PropertyValueFactory<>("id"));
        studentName.setCellValueFactory(new PropertyValueFactory<>("name"));
        studentUsername.setCellValueFactory(new PropertyValueFactory<>("username"));
        studentPassword.setCellValueFactory(new PropertyValueFactory<>("password"));

        studentTable.setItems(getStudents());
    }

    @FXML
    public void handleUpdateStudent () {
        try {
            Student student = new Student();
            student.setId(studentTable.getSelectionModel().getSelectedItem().getId());
            student.setName(studentTable.getSelectionModel().getSelectedItem().getName());
            student.setPasword(studentTable.getSelectionModel().getSelectedItem().getPassword());
            student.setUsername(studentTable.getSelectionModel().getSelectedItem().getUsername());

            StudentDataStore studentDataStore = new StudentDataStore();
            studentDataStore.open();
            boolean isStudentUpdated = studentDataStore.updateStudent(student.getId(), student.getName(), student.getUsername(), student.getPassword());

            if (isStudentUpdated) {
                CustomAlert.showAlert(Alert.AlertType.CONFIRMATION, "Update Successful", "Student Successfully Updated", "");
            }
        } catch (NullPointerException ex) {
            CustomAlert.showAlert(Alert.AlertType.ERROR, "INVALID STUDENT", "NO STUDENT SELECTED!", "Please Select a Student to Update");
        } finally {
            loadStudentTable();
        }
    }

    private void setStudentTableEditable() {
        studentTable.setEditable(true);
        studentId.setEditable(false);
        studentName.setCellFactory(TextFieldTableCell.forTableColumn());
        studentUsername.setCellFactory(TextFieldTableCell.forTableColumn());
        studentPassword.setCellFactory(TextFieldTableCell.forTableColumn());
    }

    public void onStudentEditChange(TableColumn.CellEditEvent<Student, String> studentStringCellEditEvent) {
        Student student = studentTable.getSelectionModel().getSelectedItem();
        student.setName(studentStringCellEditEvent.getNewValue());
        student.setPasword(studentStringCellEditEvent.getNewValue());
        student.setUsername(studentStringCellEditEvent.getNewValue());
    }

    private void setCourseTableEditable() {
        courseTable.setEditable(true);
        id.setEditable(false);
        courseUrl.setCellFactory(TextFieldTableCell.forTableColumn());
        title.setCellFactory(TextFieldTableCell.forTableColumn());
        students.setCellFactory(TextFieldTableCell.forTableColumn());
    }

    public void onCourseEditChange(TableColumn.CellEditEvent<Course, String> courseStringCellEditEvent) {
        Course course = courseTable.getSelectionModel().getSelectedItem();
        course.setTitle(courseStringCellEditEvent.getNewValue());
        course.setCourseUrl(courseStringCellEditEvent.getNewValue());
        course.setStudentId(courseStringCellEditEvent.getNewValue());
    }

    private void clearCourseTable () {
        courses.clear();
    }


    @FXML
    public void handleLogoutButton(ActionEvent e){
        Stage stage = new Stage();
        Label logoutText = new Label("Confirm Logout. You will be returned to the login screen.");
        logoutText.setPadding(new Insets(20, 10, 20, 10));

        Button okayButton = new Button("Logout");
        okayButton.setDefaultButton(true);
        okayButton.setOnAction((event -> {
            Stage parentWindow = (Stage) ((Node)e.getSource()).getScene().getWindow().getScene().getWindow();
            Stage window = new Stage();
            Parent root = null;
            try {
                root = FXMLLoader.load(getClass().getResource("../sample/home.fxml"));
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            window.setTitle("Admin Login");
            Scene loginScene = new Scene(root, 430, 490);
            window.setResizable(false);
            window.setScene(loginScene);
            stage.close();
            parentWindow.close();
            window.show();
        }));

        Button cancelButton = new Button("Cancel");
        cancelButton.setCancelButton(true);
        cancelButton.setOnAction((event -> stage.close()));


        HBox hBox = new HBox();
        hBox.setSpacing(20);
        hBox.getChildren().addAll(okayButton, cancelButton);
        hBox.setPadding(new Insets(0, 0, 10, 150));

        VBox container = new VBox();
        container.setSpacing(20);
        container.getChildren().addAll(logoutText, hBox);

        Scene scene = new Scene(container, 400, 120);

        stage.setTitle("Confirm Logout");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.centerOnScreen();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();
    }
}
