package listBooks;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class BookListApplication extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("listBook.fxml"));
        Scene scene = new Scene(root);
        primaryStage.setTitle("Book List");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
