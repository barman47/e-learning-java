package sample;

import classes.CourseDataStore;
import controllers.StudentDashboard;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("home.fxml"));
        primaryStage.setTitle("E-Learning");
        primaryStage.setScene(new Scene(root, 430, 490));
        primaryStage.setResizable(false);
        primaryStage.show();

        // Creating the course table when the app first runs
        CourseDataStore courseDataStore = new CourseDataStore();
        courseDataStore.open(); // This will open and create the course table and close the connection
    }


    public static void main(String[] args) {
        launch(args);
    }
}