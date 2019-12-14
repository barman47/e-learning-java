package controllers;

import classes.Course;
import classes.CourseDataStore;
import javafx.application.Application;
import javafx.application.HostServices;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import sample.CustomAlert;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class StudentDashboard {
    @FXML
    private ListView courseList;

    @FXML
    private ObservableList<String> courses = FXCollections.observableArrayList();

    @FXML
    public void initialize () {
        setListViewCourses();
    }

    private ObservableList<String> getCourses () {
        CourseDataStore courseDataStore = new CourseDataStore();
        courseDataStore.open();
        ArrayList<Course> returnedCourses = courseDataStore.getCourses();
        returnedCourses.forEach((course) -> {
            courses.add(course.getTitle());
        });
        return courses;
    }

    private void setListViewCourses () {
        courseList.setItems(getCourses());
    }

    @FXML
    public void handleOpenCourse () {
        try {
            String courseTitle = courseList.getSelectionModel().getSelectedItem().toString();
            CourseDataStore courseDataStore = new CourseDataStore();
            courseDataStore.open();
            Course course = courseDataStore.findCourse(courseTitle);
            if (course != null) {
//                File courseFile = new File(course.getCourseUrl());
//                HostServices hostServices = getHostServices();
//                hostServices.showDocument(course.getCourseUrl());

            }
        } catch (NullPointerException ex) {
            CustomAlert.showAlert(Alert.AlertType.ERROR, "INVALID COURSE", "Please Select a Course to open", "");
        }
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
