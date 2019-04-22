package listMembers;

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

public class MemberListController implements Initializable {
    public AnchorPane rootPane;
    public TableView<MemberDto> tableView;
    public TableColumn<MemberDto, String> codeCol;
    public TableColumn<MemberDto, String> nameCol;
    public TableColumn<MemberDto, String> mobileCol;
    public TableColumn<MemberDto, String> emailCol;
    public TableColumn<MemberDto, String> availCol;
    private ObservableList<MemberDto> members = FXCollections.observableArrayList();

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
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        codeCol.setCellValueFactory(new PropertyValueFactory<>("code"));
        mobileCol.setCellValueFactory(new PropertyValueFactory<>("mobile"));
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        availCol.setCellValueFactory(new PropertyValueFactory<>("status"));
    }

    private void loadData() throws SQLException {
        DatabaseConnection databaseConnection = DatabaseConnection.getInstance();
        String sql = "SELECT * FROM member ";
        ResultSet resultSet = databaseConnection.retrieveData(sql);
        while (resultSet.next()) {
            String name = resultSet.getString("name");
            String code = resultSet.getString("code");
            String mobile = resultSet.getString("mobile");
            String email = resultSet.getString("email");
            Boolean status = resultSet.getBoolean("status");

            members.add(new MemberDto(name, code, mobile, email, status));
        }
        tableView.getItems().setAll(members);
    }
}
