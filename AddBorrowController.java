package com.mycompany.GLibraryProject;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Date;

public class AddBorrowController {
    @FXML
    private TextField BookIDTF;
    @FXML
    private TextField CustIDTF;
    
    @FXML
    private DatePicker StartDateTF;
    
    @FXML
    private Button addBorrowBTTN;
    @FXML
    private Button cancelAddBorrowBTTN;
    
    @FXML
    void addBorrow(ActionEvent event) throws IOException {
        int bookID = Integer.parseInt(BookIDTF.getText());
        int custID = Integer.parseInt(CustIDTF.getText());
        Date startDate = Date.valueOf(StartDateTF.getValue());
        
        String url = "jdbc:postgresql://localhost:5432/postgres";
        String user = "postgres";
        String password = "postgres@Muath29SQL";
        
        String sql = "INSERT INTO borrows (bookid, custid, startdate) VALUES (?, ?, ?)";        
        try (Connection conn = DriverManager.getConnection(url, user, password); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, bookID);
            pstmt.setInt(2, custID);
            pstmt.setDate(3, startDate);
            
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                showAlert(Alert.AlertType.INFORMATION, "Successfully", "Borrow added successfully!");
                
                // Close the window after adding the borrow
                Stage stage = (Stage) addBorrowBTTN.getScene().getWindow();
                stage.close();

                // Refresh the TableView in the BorrowController
                App.setRoot("Borrows");

            } else {
            	showAlert(Alert.AlertType.ERROR, "ERROR", "Failed to add borrow!");
            }

        } catch (SQLException e) {
        	showAlert(Alert.AlertType.ERROR, "ERROR", "Error adding borrow: " + e.getMessage());
        } catch (NumberFormatException e) {
        	showAlert(Alert.AlertType.ERROR, "ERROR", "Invalid input format. Please enter valid integers for BookID and CustID.");
        }
    }

    @FXML
    void cancelAddBorrow(ActionEvent event) {
        Stage stage = (Stage) cancelAddBorrowBTTN.getScene().getWindow();
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