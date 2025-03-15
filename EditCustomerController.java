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

public class EditCustomerController {
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
    private Button editCustomerBTTN;
    @FXML
    private Button cancelEditCustomerBTTN;
    
    private String oldCustId;
    private Customer cust;
    void setCustomer(Customer cust) {
    	this.cust = cust;
    	loadCustomerData();
    }
    
    private void loadCustomerData() {
        if (cust != null) {
        	CustIDTF.setText(cust.getCustID() + "");
        	CustFnameTF.setText(cust.getCustFname());
        	CustLnameTF.setText(cust.getCustLname());
        	CustContactTF.setText(cust.getCustContact());
        	
        	oldCustId = CustIDTF.getText();
        }
    }
    
    @FXML
    public void initialize() {
        LibraryCardCC.getItems().addAll(true, false);        
        LibraryCardCC.setValue(false);
    }
    
    @FXML
    void editCustomer(ActionEvent event) throws IOException {
        int custId = 0;
        try{
        	custId = Integer.parseInt(CustIDTF.getText());
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Invalid input format. Please enter a valid values.");
        }
        String custFName = CustFnameTF.getText();
        String custLName = CustLnameTF.getText();
        String custContact = CustContactTF.getText();
        String libarayCard = LibraryCardCC.getSelectionModel().getSelectedItem().toString();
        
        String url = "jdbc:postgresql://localhost:5432/postgres";
        String user = "postgres";
        String password = "postgres@Muath29SQL";
        
        String sql = "UPDATE customer SET custid = ?, custfname = ?, custlname = ?, custcontact = ?, librarycard = ? WHERE custid = " + oldCustId;
        try (Connection conn = DriverManager.getConnection(url, user, password); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, custId);
            pstmt.setString(2, custFName);
            pstmt.setString(3, custLName);
            pstmt.setString(4, custContact);
            pstmt.setBoolean(5, Boolean.parseBoolean(libarayCard));
            
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
            	showAlert(Alert.AlertType.INFORMATION, "Successfully", "Customer updated successfully!");
            	
            	Stage stage = (Stage) editCustomerBTTN.getScene().getWindow();
                stage.close();
                
                App.setRoot("Customer");
            } else {
            	showAlert(Alert.AlertType.ERROR, "Error", "Failed to update Customer!");
                System.out.println("Failed to update Customer!");
            }
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Error updating Customer: " + e.getMessage());
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Invalid input format. Please enter a valid values.");
        }
    }
    
    @FXML
    void cancelEditCustomer(ActionEvent event) {
        Stage stage = (Stage) cancelEditCustomerBTTN.getScene().getWindow();
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