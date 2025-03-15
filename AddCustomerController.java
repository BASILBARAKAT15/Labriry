package com.mycompany.GLibraryProject;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AddCustomerController {

    @FXML
    private TextField CustIDTF;

    @FXML
    private TextField CustFnameTF;

    @FXML
    private TextField CustLnameTF;

    @FXML
    private TextField CustContactTF;
    
    @FXML
    private ChoiceBox<Boolean> LibraryCardCC;

    @FXML
    private Button addCustomerBTTN;

    @FXML
    private Button cancelAddCustomerBTTN;
    
    @FXML
    public void initialize() {
        LibraryCardCC.getItems().addAll(true, false);        
        LibraryCardCC.setValue(false);
    }
    
    @FXML
    void addCustomer(ActionEvent event) throws IOException {
        int custID = Integer.parseInt(CustIDTF.getText());
        String custFname = CustFnameTF.getText();
        String custLname = CustLnameTF.getText();
        boolean libraryCard = Boolean.parseBoolean(LibraryCardCC.getSelectionModel().getSelectedItem().toString());
        String custContact = CustContactTF.getText();

        // Database connection details
        String url = "jdbc:postgresql://localhost:5432/postgres";
        String user = "postgres";
        String password = "postgres@Muath29SQL";
        
        // SQL statement to insert a new customer
        String sql = "INSERT INTO customer (custid, custfname, custlname, librarycard, custcontact, password) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(url, user, password); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, custID);
            pstmt.setString(2, custFname);
            pstmt.setString(3, custLname);
            pstmt.setBoolean(4, libraryCard);
            pstmt.setString(5, custContact);
            pstmt.setString(6, custID + "");
            
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
            	showAlert(Alert.AlertType.INFORMATION, "Successfully", "Customer added successfully!");
            	
                // Close the window after adding the customer
                Stage stage = (Stage) addCustomerBTTN.getScene().getWindow();
                stage.close();
                
                // Refresh the TableView in the CustomerController
                App.setRoot("Customer");

            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to add customer!");
            }

        } catch (SQLException e) {
        	showAlert(Alert.AlertType.ERROR, "Error", "Error adding customer: " + e.getMessage());
        } catch (NumberFormatException e) {
        	showAlert(Alert.AlertType.ERROR, "Error", "Invalid input format. Please enter a valid integer for CustID. " + e.getMessage());
        }

    }
    
    @FXML
    void cancelAddCustomer(ActionEvent event) {
        Stage stage = (Stage) cancelAddCustomerBTTN.getScene().getWindow();
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