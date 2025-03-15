package com.mycompany.GLibraryProject;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AddBookController {

    @FXML
    private TextField AuthIDTF;

    @FXML
    private TextField BookAvTF;

    @FXML
    private TextField BookIDTF;

    @FXML
    private TextField BookTitleTF;

    @FXML
    private TextField CountIDTF;

    @FXML
    private TextField PubIDTF;

    @FXML
    private Button addBookBTTN;

    @FXML
    private Button cancelAddBookBTTN;

    @FXML
    void addBook(ActionEvent event) throws IOException {
        int bookID = Integer.parseInt(BookIDTF.getText());
        String bookTitle = BookTitleTF.getText();
        boolean availability = Boolean.parseBoolean(BookAvTF.getText());
        int publisherID = Integer.parseInt(PubIDTF.getText());
        int authorID = Integer.parseInt(AuthIDTF.getText());
        int count = Integer.parseInt(CountIDTF.getText());

        // database connection
        String url = "jdbc:postgresql://localhost:5432/postgres";
        String user = "postgres";
        String password = "postgres@Muath29SQL";

        // quer to insert a new book
        String sql = "INSERT INTO book (bookid, bookname, bookavailable, bookcount, pubid, authid, empid) VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DriverManager.getConnection(url, user, password); PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, bookID);
            pstmt.setString(2, bookTitle);
            pstmt.setBoolean(3, availability);
            pstmt.setInt(4, count);
            pstmt.setInt(5, publisherID);
            pstmt.setInt(6, authorID);
            pstmt.setInt(7, LogInController.loggedInEmpID);

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                showAlert(Alert.AlertType.INFORMATION, "Successfully", "Book added successfully!");
                
                // Close the window after adding the book
                Stage stage = (Stage) addBookBTTN.getScene().getWindow();
                stage.close();
                
                // Refresh the TableView in the BookController
                App.setRoot("Book");

            } else {
            	showAlert(Alert.AlertType.ERROR, "ERROR", "Failed to add book!");
            }

        } catch (SQLException e) {
        	showAlert(Alert.AlertType.ERROR, "ERROR", "Error adding book: " + e.getMessage());
        } catch (NumberFormatException e) {
        	showAlert(Alert.AlertType.ERROR, "ERROR", "Invalid input format. Please enter valid integers for BookID and Count.");
        }

    }

    @FXML
    void cancelAddBook(ActionEvent event) {
        Stage stage = (Stage) addBookBTTN.getScene().getWindow();
        stage.close();
    }
    
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}