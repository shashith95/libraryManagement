package addMember;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import main.DatabaseConnection;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class MemberAddController implements Initializable {
    public AnchorPane rootPane;
    public TextField memName;
    public TextField memCode;
    public TextField memMobile;
    public TextField memEmail;
    public Button btnSave;
    public Button btnClose;
    private DatabaseConnection databaseConnection = DatabaseConnection.getInstance();

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void saveMember(ActionEvent actionEvent) throws SQLException {
        if (memCode.getText().isEmpty() || memName.getText().isEmpty() || memMobile.getText().isEmpty() || memEmail.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Please Fill ALL the Fields");
            alert.showAndWait();
            return;
        }

        if (databaseConnection.checkDuplicates("code","member", memCode.getText())) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Member Code Already Exists");
            alert.showAndWait();
            return;
        }

        String query = "INSERT INTO member(name, code, mobile, email, status) " +
                "VALUE ('" + memName.getText() + "', '" + memCode.getText() + "', '" + memMobile.getText() + "', '" + memEmail.getText() + "', true) ";
        System.out.print(query);
        if (databaseConnection.saveOrUpdate(query)) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText(null);
            alert.setContentText("Successfully Added Member");
            alert.showAndWait();
            clearData();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Error in Adding Member");
            alert.showAndWait();
        }
    }

    private void clearData() {
        memName.setText("");
        memCode.setText("");
        memMobile.setText("");
        memEmail.setText("");
    }

    public void closeMember(ActionEvent actionEvent) {
        Stage stage = (Stage) rootPane.getScene().getWindow();
        stage.close();
    }
}
