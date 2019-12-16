package sample;

import models.CourseDataStore;
import javafx.application.Application;
import javafx.application.HostServices;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    HostServices hostServices;
    public Main () {
        this.hostServices = getHostServices();
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("home.fxml"));
        Parent root = loader.load();

        primaryStage.setTitle("E-Learning");
        primaryStage.setScene(new Scene(root, 430, 490));
        primaryStage.setResizable(false);
        primaryStage.show();

        // Creating the course table when the app first runs
        CourseDataStore courseDataStore = new CourseDataStore();
        courseDataStore.open(); // This will open and create the course table and close the connection
    }

    public HostServices returnHostServices() {
        return this.hostServices;
    }

    public static void main(String[] args) {
        launch(args);
    }
}