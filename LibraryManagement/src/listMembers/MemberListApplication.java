package listMembers;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MemberListApplication extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("listMembers.fxml"));
        Scene scene = new Scene(root);
        primaryStage.setTitle("Member List");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
