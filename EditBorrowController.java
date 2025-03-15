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

public class EditBorrowController {
    @FXML
    private TextField BookIDTF;
    @FXML
    private TextField CustIDTF;
    @FXML
    private DatePicker StartDateTF;
    
    @FXML
    private Button editBorrowBTTN;
    @FXML
    private Button cancelEditBorrowBTTN;
    
    private String oldCustId;
    private String oldBookId;
    private Borrow borrow;
    void setBorrow(Borrow borrow) {
    	this.borrow = borrow;
    	loadBorrowData();
    }
    
    private void loadBorrowData() {
        if (borrow != null) {
        	CustIDTF.setText(borrow.getCustID() + "");
        	BookIDTF.setText(borrow.getBookID() + "");
        	
        	oldCustId = CustIDTF.getText();
        	oldBookId = BookIDTF.getText();
        }
    }
    
    @FXML
    void editBorrow(ActionEvent event) throws IOException {
        int custId = 0;
        int bookId = 0;
        try{
        	custId = Integer.parseInt(CustIDTF.getText());
        	bookId = Integer.parseInt(BookIDTF.getText());
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Invalid input format. Please enter a valid values.");
        }
        Date startDate = Date.valueOf(StartDateTF.getValue());
        
        String url = "jdbc:postgresql://localhost:5432/postgres";
        String user = "postgres";
        String password = "postgres@Muath29SQL";
        
        String sql = "UPDATE borrows SET custid = ?, bookid = ?, startdate = ? WHERE custid = " + oldCustId + " and bookid = " + oldBookId;
        try (Connection conn = DriverManager.getConnection(url, user, password); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, custId);
            pstmt.setInt(2, bookId);
            pstmt.setDate(3, startDate);
            
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
            	showAlert(Alert.AlertType.INFORMATION, "Successfully", "Borrow updated successfully!");
            	
            	Stage stage = (Stage) editBorrowBTTN.getScene().getWindow();
                stage.close();
                
                App.setRoot("Borrows");
            } else {
            	showAlert(Alert.AlertType.ERROR, "Error", "Failed to update Borrow!");
                System.out.println("Failed to update Borrow!");
            }
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Error updating Borrow: " + e.getMessage());
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Invalid input format. Please enter a valid values.");
        }
    }
    
    @FXML
    void cancelEditBorrow(ActionEvent event) {
        Stage stage = (Stage) cancelEditBorrowBTTN.getScene().getWindow();
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