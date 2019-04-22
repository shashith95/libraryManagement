package main;

import java.sql.*;
import java.util.ArrayList;

public class DatabaseConnection {
    private static final String driverClassName = "com.mysql.jdbc.Driver";
    public static final String ISSUED = "ISSUED";
    public static final String RENEWED = "RENEWED";
    public static final String SUBMITTED = "SUBMITTED";
    private static final String dbURL = "jdbc:mysql://localhost:3306/library";
    private static Statement statement = null;
    private static DatabaseConnection databaseConnection;

    private DatabaseConnection() {
        createConnection();
        createBookTable();
        createMemberTable();
        createIssueBookTable();
        createIssueBookHistoryTable();
    }

    public static DatabaseConnection getInstance() {
        if (databaseConnection == null) {
            databaseConnection = new DatabaseConnection();
        }
        return databaseConnection;
    }

    private void createConnection() {
        try {
            Class.forName(driverClassName);
            Connection connection = DriverManager.getConnection(dbURL, "user", "password");
            statement = connection.createStatement();
            System.out.println("Connection Successful");
        } catch (Exception e) {
            System.out.println("Error in Connection" + e);
        }
    }

    private void createBookTable() {
        try {
            String sql = "CREATE TABLE IF NOT EXISTS book (\n" +
                    "    id INT AUTO_INCREMENT,\n" +
                    "    title VARCHAR(255) NOT NULL,\n" +
                    "    code VARCHAR(255) NOT NULL,\n" +
                    "    author VARCHAR(255),\n" +
                    "    publisher VARCHAR(255),\n" +
                    "    status TINYINT,\n" +
                    "    description TEXT,\n" +
                    "    PRIMARY KEY (id)\n" +
                    ")";
            statement.execute(sql);
        } catch (Exception e) {
            System.out.println("Error in create book table" + e);
        }
    }

    private void createMemberTable() {
        try {
            String sql = "CREATE TABLE IF NOT EXISTS member (\n" +
                    "    id INT AUTO_INCREMENT,\n" +
                    "    name VARCHAR(255) NOT NULL,\n" +
                    "    code VARCHAR(255) NOT NULL,\n" +
                    "    mobile VARCHAR(255),\n" +
                    "    email VARCHAR(255),\n" +
                    "    status TINYINT,\n" +
                    "    PRIMARY KEY (id)\n" +
                    ")";
            statement.execute(sql);
        } catch (Exception e) {
            System.out.println("Error in create member table" + e);
        }
    }

    private void createIssueBookTable() {
        try {
            String sql = "CREATE TABLE IF NOT EXISTS book_issue (\n" +
                    "    id INT AUTO_INCREMENT,\n" +
                    "    book_id INTEGER ,\n" +
                    "    member_id INTEGER,\n" +
                    "    issued_date DATETIME,\n" +
                    "    renew_count INTEGER ,\n" +
                    "    PRIMARY KEY (id)\n" +
                    ") ";
            statement.execute(sql);
        } catch (Exception e) {
            System.out.println("Error in create book_issue table" + e);
        }
    }

    private void createIssueBookHistoryTable() {
        try {
            String sql = "CREATE TABLE IF NOT EXISTS book_issue_history (\n" +
                    "    id INT AUTO_INCREMENT,\n" +
                    "    book_id INTEGER ,\n" +
                    "    member_id INTEGER,\n" +
                    "    issued_date DATETIME,\n" +
                    "    status VARCHAR(255) ,\n" +
                    "    PRIMARY KEY (id)\n" +
                    ") ";
            statement.execute(sql);
        } catch (Exception e) {
            System.out.println("Error in create book_issue_table table" + e);
        }
    }

    public boolean checkDuplicates(String fieldName, String tableName, String enteredValue) throws SQLException {
        boolean result = false;
        String sql = "SELECT " + fieldName + " FROM " + tableName + " ";
        ResultSet resultSet = retrieveData(sql);
        while (resultSet.next()) {
            if (resultSet.getString(1).equals(enteredValue)) {
                result = true;
            }
        }
        return result;
    }

    public Long getBookIdbyBookCode(String bookCode) throws SQLException {
        Long bookId = null;
        String findBookId = "SELECT id FROM book WHERE code = '" + bookCode + "' ";
        ResultSet resultSetBook = retrieveData(findBookId);
        while (resultSetBook.next()) {
            bookId = resultSetBook.getLong("id");
        }
        return bookId;
    }

    public Long getMemberIdbyMemberCode(String memberCode) throws SQLException {
        Long memberId = null;
        String findMemberId = "SELECT id FROM member WHERE code = '" + memberCode + "' ";
        ResultSet resultSetMember = databaseConnection.retrieveData(findMemberId);
        while (resultSetMember.next()) {
            memberId = resultSetMember.getLong("id");
        }
        return memberId;
    }

    public Long getMemberIdbyBookId(Long bookId) throws SQLException {
        Long memberId = null;
        String findMemberId = "SELECT m.id FROM member m INNER JOIN book_issue bi ON bi.member_id = m.id WHERE bi.book_id = " + bookId + "";
        ResultSet resultSetMember = databaseConnection.retrieveData(findMemberId);
        while (resultSetMember.next()) {
            memberId = resultSetMember.getLong("id");
        }
        return memberId;
    }

    public ArrayList<String> getMemberCodes() throws SQLException {
        ArrayList<String> codes = new ArrayList<>();
        String memberCodes = "SELECT code FROM member";
        ResultSet resultSetMember = databaseConnection.retrieveData(memberCodes);
        while (resultSetMember.next()) {
            memberCodes = resultSetMember.getString("code");
            codes.add(memberCodes);
        }
        return codes;
    }

    public ArrayList<String> getBookCodes() throws SQLException {
        ArrayList<String> codes = new ArrayList<>();
        String memberCodes = "SELECT code FROM book";
        ResultSet resultSetMember = databaseConnection.retrieveData(memberCodes);
        while (resultSetMember.next()) {
            memberCodes = resultSetMember.getString("code");
            codes.add(memberCodes);
        }
        return codes;
    }

    public boolean saveOrUpdate(String query) throws SQLException {
        statement.execute(query);
        return true;
    }

    public ResultSet retrieveData(String query) throws SQLException {
        return statement.executeQuery(query);
    }

}
