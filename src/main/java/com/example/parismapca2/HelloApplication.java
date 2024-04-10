package com.example.parismapca2;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class HelloApplication extends Application {



    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));

        // Create a scene using the FXML layout and set its dimensions
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);

        // Set the title of the stage
        stage.setTitle("Paris Route Finder!!!!!");

        // Set the scene for the primary stage and make it visible
        stage.setScene(scene);
        stage.show();
    }
    }
