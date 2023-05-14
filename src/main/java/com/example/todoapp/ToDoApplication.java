package com.example.todoapp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class ToDoApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(ToDoApplication.class.getResource("list-view.fxml"));

        Scene scene = new Scene(fxmlLoader.load(), 850, 500);
        stage.getIcons().add(new Image(new File("images/tasks-app.png").toURI().toString()));

        stage.setMinWidth(730);
        stage.setMinHeight(160);
        stage.setTitle("ToDoApp");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}