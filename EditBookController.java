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

public class EditBookController {
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
    private Button editBookBTTN;
    @FXML
    private Button cancelEditBookBTTN;
    
    private String oldBookId;
    private Book book;
    void setBook(Book book) {
    	this.book = book;
    	loadBookData();
    }
    
    private void loadBookData() {
        if (book != null) {
        	BookIDTF.setText(book.getBookID() + "");
        	AuthIDTF.setText(book.getAuthor().getAuthID() + "");
        	BookTitleTF.setText(book.getBookName());
        	CountIDTF.setText(book.getBookCount() + "");
        	PubIDTF.setText(book.getPublisher().getPubID() + "");
        	
        	if(book.getBookCount() > 0) {
            	BookAvTF.setText("true");
        	} else {
            	BookAvTF.setText("false");
        	}
        	
        	oldBookId = BookIDTF.getText();
        }
    }
    
    @FXML
    void editBook(ActionEvent event) throws IOException {
        int bookId = 0;
        int authId = 0;
        int pubId = 0;
        int count = 0;
        boolean available = false;
        try{
        	bookId = Integer.parseInt(BookIDTF.getText());
        	authId = Integer.parseInt(AuthIDTF.getText());
        	pubId = Integer.parseInt(PubIDTF.getText());
        	count = Integer.parseInt(CountIDTF.getText());
        	available = Boolean.parseBoolean(BookAvTF.getText());
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Invalid input format. Please enter a valid values.");
        }
        String bookTitle = BookTitleTF.getText();
        
        String url = "jdbc:postgresql://localhost:5432/postgres";
        String user = "postgres";
        String password = "postgres@Muath29SQL";
        
        String sql = "UPDATE book SET bookid = ?, authid = ?, pubid = ?, bookcount = ?, bookname = ?, bookavailable = ? WHERE bookid = ?";
        try (Connection conn = DriverManager.getConnection(url, user, password); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, bookId);
            pstmt.setInt(2, authId);
            pstmt.setInt(3, pubId);
            pstmt.setInt(4, count);
            pstmt.setString(5, bookTitle);
            pstmt.setBoolean(6, available);
            pstmt.setInt(7, Integer.parseInt(oldBookId));
            
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
            	showAlert(Alert.AlertType.INFORMATION, "Successfully", "Book updated successfully!");
            	
                Stage stage = (Stage) editBookBTTN.getScene().getWindow();
                stage.close();
                
                App.setRoot("Book");
            } else {
            	showAlert(Alert.AlertType.ERROR, "Error", "Failed to update Book!");
                System.out.println("Failed to update Book!");
            }
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Error updating Book: " + e.getMessage());
        }
    }
    
    @FXML
    void cancelEditBook(ActionEvent event) {
        Stage stage = (Stage) cancelEditBookBTTN.getScene().getWindow();
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