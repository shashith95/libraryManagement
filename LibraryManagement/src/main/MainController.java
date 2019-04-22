package main;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.controlsfx.control.textfield.TextFields;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MainController implements Initializable {
    public TextField bookCode;
    public Label bookName;
    public Label authorName;
    public Label availability;
    public TextField memberCode;
    public Label memberName;
    public Label memberContact;
    public Label memberActive;
    public TextField issuedBook;
    public ListView<String> issuedBookList;
    private DatabaseConnection databaseConnection;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        databaseConnection = DatabaseConnection.getInstance();
        try {
            TextFields.bindAutoCompletion(memberCode, databaseConnection.getMemberCodes());
            TextFields.bindAutoCompletion(bookCode, databaseConnection.getBookCodes());
            TextFields.bindAutoCompletion(issuedBook, databaseConnection.getBookCodes());
        } catch (Exception e) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    public void loadAddMember(ActionEvent actionEvent) {
        loadWindow("/addMember/memberAdd.fxml", "Add New Member");
    }

    public void loadViewMember(ActionEvent actionEvent) {
        loadWindow("/listMembers/listMembers.fxml", "View Members");
    }

    public void loadAddBook(ActionEvent actionEvent) {
        loadWindow("/sample/addBook/addBook.fxml", "Add New Book");
    }

    public void loadViewBook(ActionEvent actionEvent) {
        loadWindow("/listBooks/listBook.fxml", "View Books");
    }

    public void loadSettings(ActionEvent actionEvent) {
    }

    private void loadWindow(String location, String title) {
        try {
            Parent parent = FXMLLoader.load(getClass().getResource(location));
            Stage stage = new Stage(StageStyle.DECORATED);
            stage.setTitle(title);
            stage.setScene(new Scene(parent));
            stage.show();
        } catch (Exception e) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    public void issueBook(ActionEvent actionEvent) throws SQLException {
        loadBookInfo(actionEvent);
        loadMemberInfo(actionEvent);
        if (bookCode.getText().isEmpty() || memberCode.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Fields are Empty");
            alert.showAndWait();
            return;
        }

        if (bookName.getText().equals("Invalid Book!") && bookName.getText().equals("Invalid Member!")) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Invalid Inputs");
            alert.showAndWait();
            return;
        }

        Long bookId = null;
        Long memberId = null;

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Issue Operation");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure want to issue book : " + bookName.getText() + "\n to member : " + memberName.getText() + "");

        Optional<ButtonType> response = alert.showAndWait();

        if (response.isPresent() && response.get() == ButtonType.OK) {
            bookId = databaseConnection.getBookIdbyBookCode(bookCode.getText());
            memberId = databaseConnection.getMemberIdbyMemberCode(memberCode.getText());

            if (bookId != null && memberId != null) {
                String insertBookIssue = "INSERT INTO book_issue(book_id, member_id, issued_date) VALUE (" + bookId + ", " + memberId + ", NOW()) ";
                String insertBookIssueHistory = "INSERT INTO book_issue_history(book_id, member_id, issued_date, status) VALUE (" + bookId + ", " + memberId + ", NOW(), '" + DatabaseConnection.ISSUED + "') ";
                String updateBookStatus = "UPDATE book SET status = 0 WHERE id = " + bookId + " ";
                if (databaseConnection.saveOrUpdate(insertBookIssue) && databaseConnection.saveOrUpdate(updateBookStatus)) {
                    databaseConnection.saveOrUpdate(insertBookIssueHistory);
                    Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                    successAlert.setTitle("Success");
                    successAlert.setHeaderText(null);
                    successAlert.setContentText("Successfully Issued the Book");
                    successAlert.showAndWait();
                } else {
                    Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                    errorAlert.setTitle("Error");
                    errorAlert.setHeaderText(null);
                    errorAlert.setContentText("Error in Issuing Book");
                    errorAlert.showAndWait();
                    return;
                }
            } else {
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setTitle("Error");
                errorAlert.setHeaderText(null);
                errorAlert.setContentText("Invalid Inputs");
                errorAlert.showAndWait();
                return;
            }
        } else {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setTitle("Error");
            errorAlert.setHeaderText(null);
            errorAlert.setContentText("Cancelled the Operation");
            errorAlert.showAndWait();
            return;
        }
    }

    public void loadBookInfo(ActionEvent actionEvent) throws SQLException {
        if (bookCode.getText().isEmpty()) {
            System.out.println("Book code is empty");
            return;
        }
        boolean validBook = false;
        String sql = "SELECT * FROM book WHERE code = '" + bookCode.getText() + "' ";
        ResultSet resultSet = databaseConnection.retrieveData(sql);
        try {
            while (resultSet.next()) {
                String bName = resultSet.getString("title");
                String bAuthor = resultSet.getString("author");
                boolean isAvail = resultSet.getBoolean("status");
                validBook = true;
                bookName.setText(bName);
                authorName.setText(bAuthor);
                if (isAvail) {
                    availability.setText("Available");
                } else {
                    availability.setText("Not Available");
                }
            }
            if (!validBook) {
                bookName.setText("Invalid Member!");
                authorName.setText("-");
                availability.setText("-");
            }
        } catch (Exception e) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    public void loadMemberInfo(ActionEvent actionEvent) throws SQLException {
        if (memberCode.getText().isEmpty()) {
            System.out.println("Member code is empty");
            return;
        }
        boolean validMember = false;
        String sql = "SELECT * FROM member WHERE code = '" + memberCode.getText() + "' ";
        ResultSet resultSet = databaseConnection.retrieveData(sql);
        try {
            while (resultSet.next()) {
                String mName = resultSet.getString("name");
                String mContact = resultSet.getString("mobile");
                boolean isAvail = resultSet.getBoolean("status");
                validMember = true;
                memberName.setText(mName);
                memberContact.setText(mContact);
                if (isAvail) {
                    memberActive.setText("Active");
                } else {
                    memberActive.setText("Not Active");
                }
            }
            if (!validMember) {
                memberName.setText("Invalid Book!");
                memberContact.setText("-");
                memberActive.setText("-");
            }
        } catch (Exception e) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    public void viewIssuedDetails(ActionEvent actionEvent) {
        ObservableList<String> issueDetailsList = FXCollections.observableArrayList();
        if (issuedBook.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Fields are Empty");
            alert.showAndWait();
            return;
        }

        String sql = "SELECT b.title, b.author, b.publisher, m.name, m.code, bi.issued_date, ifnull(bi.renew_count, 0) renew_count from book_issue bi " +
                "inner join book b on bi.book_id = b.id inner join member m on bi.member_id = m.id where b.code = '" + issuedBook.getText() + "' ";
        try {
            ResultSet resultSet = databaseConnection.retrieveData(sql);
            while (resultSet.next()) {
                String bookTitle = resultSet.getString("title");
                String author = resultSet.getString("author");
                String memberName = resultSet.getString("name");
                String code = resultSet.getString("code");
                Timestamp issuedDate = resultSet.getTimestamp("issued_date");
                int renewCount = resultSet.getInt("renew_count");

                issueDetailsList.add("Book Title : " + bookTitle);
                issueDetailsList.add("Book Author : " + author);
                issueDetailsList.add("Renew Count : " + renewCount);
                issueDetailsList.add("Issued Date Time : " + issuedDate);
                issueDetailsList.add("Member Name : " + memberName);
                issueDetailsList.add("Member Code : " + code);
            }
            issuedBookList.getItems().setAll(issueDetailsList);
        } catch (Exception e) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    private void clearData() {
        if (issuedBook.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Fields are Empty");
            alert.showAndWait();
            return;
        }


    }

    public void bookSubmission(ActionEvent actionEvent) {
        Long bookId = null;
        if (issuedBook.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Fields are Empty");
            alert.showAndWait();
            return;
        }
        try {
            bookId = databaseConnection.getBookIdbyBookCode(issuedBook.getText());
            if (bookId != null) {
                String deleteBookIssue = "DELETE FROM book_issue WHERE book_id = " + bookId + "";
                String updateBookStatus = "UPDATE book SET status = 1 WHERE id = " + bookId + "";
                Long memberId = databaseConnection.getMemberIdbyBookId(bookId);
                String insertBookIssueHistory = "INSERT INTO book_issue_history(book_id, member_id, issued_date, status) VALUE (" + bookId + ", " + memberId + ", NOW(), '" + DatabaseConnection.SUBMITTED + "') ";
                databaseConnection.saveOrUpdate(insertBookIssueHistory);
                if (databaseConnection.saveOrUpdate(deleteBookIssue) && databaseConnection.saveOrUpdate(updateBookStatus)) {
                    Alert errorAlert = new Alert(Alert.AlertType.INFORMATION);
                    errorAlert.setTitle("Success");
                    errorAlert.setHeaderText(null);
                    errorAlert.setContentText("Successfully Submitted the Book");
                    errorAlert.showAndWait();
                } else {
                    Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                    errorAlert.setTitle("Error");
                    errorAlert.setHeaderText(null);
                    errorAlert.setContentText("Error in Submitting the book");
                    errorAlert.showAndWait();
                    return;
                }
            }
        } catch (Exception e) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    public void bookRenewal(ActionEvent actionEvent) {
        Long bookId = null;
        if (issuedBook.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Fields are Empty");
            alert.showAndWait();
            return;
        }
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Issue Operation");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure want to renew book : " + bookName.getText() + "");

        Optional<ButtonType> response = alert.showAndWait();
        try {
            if (response.isPresent() && response.get() == ButtonType.OK) {
                bookId = databaseConnection.getBookIdbyBookCode(issuedBook.getText());
                if (bookId != null) {
                    String updateBookIssueDateTime = "UPDATE book_issue SET issued_date = NOW() WHERE id = " + bookId + "";
                    Long memberId = databaseConnection.getMemberIdbyBookId(bookId);
                    String insertBookIssueHistory = "INSERT INTO book_issue_history(book_id, member_id, issued_date, status) VALUE (" + bookId + ", " + memberId + ", NOW(), '" + DatabaseConnection.RENEWED + "') ";
                    databaseConnection.saveOrUpdate(insertBookIssueHistory);
                    if (databaseConnection.saveOrUpdate(updateBookIssueDateTime)) {
                        Alert errorAlert = new Alert(Alert.AlertType.INFORMATION);
                        errorAlert.setTitle("Success");
                        errorAlert.setHeaderText(null);
                        errorAlert.setContentText("Successfully Renewed the Book");
                        errorAlert.showAndWait();
                    } else {
                        Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                        errorAlert.setTitle("Error");
                        errorAlert.setHeaderText(null);
                        errorAlert.setContentText("Error in Renewing the book");
                        errorAlert.showAndWait();
                        return;
                    }
                }
            } else {
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setTitle("Error");
                errorAlert.setHeaderText(null);
                errorAlert.setContentText("Cancelled the Operation");
                errorAlert.showAndWait();
                return;
            }
        } catch (Exception e) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, e);
        }
    }
}
