package addMember;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MemberAddApplication extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("memberAdd.fxml"));
        Scene scene = new Scene(root);
        primaryStage.setTitle("Add Member");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
