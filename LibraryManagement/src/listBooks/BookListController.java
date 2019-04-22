package listBooks;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import main.DatabaseConnection;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class BookListController implements Initializable {
    public AnchorPane rootPane;
    public TableColumn<BookDto, String> codeCol;
    public TableColumn<BookDto, String> authorCol;
    public TableColumn<BookDto, String> publisherCol;
    public TableColumn<BookDto, String> descCol;
    public TableColumn<BookDto, Boolean> availCol;
    public TableColumn<BookDto, String> titleCol;
    public TableView<BookDto> tableView;
    private ObservableList<BookDto> books = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initColumn();
        try {
            loadData();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void initColumn() {
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        codeCol.setCellValueFactory(new PropertyValueFactory<>("code"));
        authorCol.setCellValueFactory(new PropertyValueFactory<>("author"));
        descCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        publisherCol.setCellValueFactory(new PropertyValueFactory<>("publisher"));
        availCol.setCellValueFactory(new PropertyValueFactory<>("status"));
    }

    private void loadData() throws SQLException {
        DatabaseConnection databaseConnection = DatabaseConnection.getInstance();
        String sql = "SELECT * FROM book ";
        ResultSet resultSet = databaseConnection.retrieveData(sql);
        while (resultSet.next()) {
            String title = resultSet.getString("title");
            String code = resultSet.getString("code");
            String author = resultSet.getString("author");
            String publisher = resultSet.getString("publisher");
            String description = resultSet.getString("description");
            Boolean status = resultSet.getBoolean("status");

            books.add(new BookDto(title, code, author, publisher, description, status));
        }
        tableView.getItems().setAll(books);
    }
}
