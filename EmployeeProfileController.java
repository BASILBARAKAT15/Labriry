package com.mycompany.GLibraryProject;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

public class EmployeeProfileController {
	@FXML
    private TextField idInfoField;
	@FXML
    private TextField passInfoField;
	@FXML
    private TextField fNameField;
	@FXML
    private TextField lNameField;
	@FXML
    private TextField ContactField;
	@FXML
    private TextField salaryEmpField;
	
	@FXML
    private Label idEmpCard;
	@FXML
    private Label fullEmpNameCard;
	@FXML
    private Label contactEmpCard;
	
    @FXML
    void LogOutAction(ActionEvent event) throws IOException {
        App.setRoot("LogIn");
    }
    
    @FXML
    void EmployeeTable(ActionEvent event) throws IOException {
        App.setRoot("Employee");
    }
    
    @FXML
    void CustomerTable(ActionEvent event) throws IOException {
        App.setRoot("Customer");
    }
    
    @FXML
    void BookTable(ActionEvent event) throws IOException {
        App.setRoot("Book");
    }
    
    @FXML
    void PublisherTable(ActionEvent event) throws IOException {
        App.setRoot("Publisher");
    }

    @FXML
    void AuthorTable(ActionEvent event) throws IOException {
        App.setRoot("Author");
    }
    
    @FXML
    void BorrowsTable(ActionEvent event) throws IOException {
        App.setRoot("Borrows");
    }
    
    @FXML
    void PurchasesTable(ActionEvent event) throws IOException {
        App.setRoot("Purchases");
    }
    
    @FXML
    void FeedbackTable(ActionEvent event) throws IOException {
        App.setRoot("Feedback");
    }
    
    @FXML
    void AdminProfile(MouseEvent event) throws IOException {
        App.setRoot("EmployeeProfile");
    }
    
    @FXML
    void dashboardAction(ActionEvent event) throws IOException {
        App.setRoot("Dashboard");
    }
    
    @FXML
    void saveEmpEditable(ActionEvent event) throws IOException {
        int empID = Integer.parseInt(idInfoField.getText());
        String empPass = passInfoField.getText();
        String empFname = fNameField.getText();
        String empLname = lNameField.getText();
        float empSalary = Float.parseFloat(salaryEmpField.getText());
        String empContact = ContactField.getText();
        
        // Database connection details
        String url = "jdbc:postgresql://localhost:5432/postgres";
        String user = "postgres";
        String password = "postgres@Muath29SQL";
        
        // SQL statement to update an existing customer
        String sql = "UPDATE employee SET empfname = ?, emplname = ?, empsalary = ?, password = ?, empcontact = ? WHERE empid = ?";
        try (Connection conn = DriverManager.getConnection(url, user, password); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, empFname);
            pstmt.setString(2, empLname);
            pstmt.setFloat(3, empSalary);
            pstmt.setString(4, empPass);
            pstmt.setString(5, empContact);
            pstmt.setInt(6, empID);
            
            idEmpCard.setText(empID + "");
            fullEmpNameCard.setText(empFname + " " + empLname);
            contactEmpCard.setText(empContact);
            
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
            	showAlert(Alert.AlertType.INFORMATION, "Successfully", "Employee updated successfully!");
            } else {
            	showAlert(Alert.AlertType.ERROR, "Error", "Failed to update employee!");
                System.out.println("Failed to update employee!");
            }
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Error updating employee: " + e.getMessage());
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Invalid input format. Please enter a valid integer for EmpID.");
        }
        
    	App.setRoot("Dashboard");
    }
    
    public void initialize() {
        try {
            Class.forName("org.postgresql.Driver");
            
            String url = "jdbc:postgresql://localhost:5432/postgres";
            String user = "postgres";
            String password = "postgres@Muath29SQL";
            Connection conn = DriverManager.getConnection(url, user, password);
            
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT password, empfname, emplname, empsalary, empcontact FROM employee WHERE empid = " + LogInController.loggedInEmpID);
            
            while (rs.next()) {
                int empID = LogInController.loggedInEmpID;
                String empPass = rs.getString("password");
                String empFname = rs.getString("empfname");
                String empLname = rs.getString("emplname");
                float empSalary = rs.getFloat("empsalary");
                String empContact = rs.getString("empcontact");
                
                idInfoField.setText(empID + "");
                passInfoField.setText(empPass);
                fNameField.setText(empFname);
            	lNameField.setText(empLname);
            	ContactField.setText(empContact);
            	salaryEmpField.setText(empSalary + "");
            	
            	idEmpCard.setText(empID + "");
            	fullEmpNameCard.setText(empFname + " " + empLname);
            	contactEmpCard.setText(empContact);
            }
            
            rs.close();
            stmt.close();
            conn.close();
        } catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
