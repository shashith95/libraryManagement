package sample.addBook;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class AddBookApplication extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("addBook.fxml"));
        primaryStage.setTitle("Register Book");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }
}

