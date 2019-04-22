package sample.addBook;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import main.DatabaseConnection;


import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class BookController implements Initializable {

    @FXML
    private TextField title;
    @FXML
    private TextField code;
    @FXML
    private TextField author;
    @FXML
    private TextField publisher;
    @FXML
    private TextArea description;
    @FXML
    private Button saveButton;
    @FXML
    private Button closeButton;
    @FXML
    private VBox vBox;

    private DatabaseConnection databaseConnection;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        databaseConnection = DatabaseConnection.getInstance();
    }

    @FXML
    private void addBook(ActionEvent actionEvent) throws SQLException {
        if (title.getText().isEmpty() || code.getText().isEmpty() || author.getText().isEmpty() || publisher.getText().isEmpty() || description.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Please Enter All Fields");
            alert.showAndWait();
            return;
        }

        if (databaseConnection.checkDuplicates("code","book", code.getText())) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Book Code Already Exists");
            alert.showAndWait();
            return;
        }

        String query = "INSERT INTO book(title, code, author, publisher, status, description) " +
                "VALUE ('" + title.getText() + "', '" + code.getText() + "', '" + author.getText() + "', '" + publisher.getText() + "', true, '" + description.getText() + "') ";
        System.out.print(query);

        if (databaseConnection.saveOrUpdate(query)) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText(null);
            alert.setContentText("Successfully Added Book");
            alert.showAndWait();
            clearData();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Error in Adding Book");
            alert.showAndWait();
        }
    }

    private void clearData() {
        title.setText("");
        code.setText("");
        author.setText("");
        publisher.setText("");
        description.setText("");
    }

    @FXML
    private void closeBook(ActionEvent actionEvent) {
        Stage stage = (Stage) vBox.getScene().getWindow();
        stage.close();
    }
}
