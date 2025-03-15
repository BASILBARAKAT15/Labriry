package com.mycompany.GLibraryProject;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javafx.scene.control.Alert;
import javafx.scene.control.Button;

public class EditEmployeeController {
    @FXML
    private TextField EmployeeFNTF;
    @FXML
    private TextField EmployeeIDTF;
    @FXML
    private TextField EmployeeLNTF;
    @FXML
    private TextField EmployeeSalaryTF;
    @FXML
    private TextField EmployeeContactTF;
    
    @FXML
    private Button editEmployeeBTTN;
    @FXML
    private Button cancelEditEmployeeBTTN;
    
    private String oldEmpId;
    private Employee emp;
    void setEmployee(Employee emp) {
    	this.emp = emp;
    	loadEmployeeData();
    }
    
    private void loadEmployeeData() {
        if (emp != null) {
        	EmployeeIDTF.setText(emp.getEmpID() + "");
        	EmployeeFNTF.setText(emp.getEmpFname());
        	EmployeeLNTF.setText(emp.getEmpLname());
        	EmployeeSalaryTF.setText(emp.getEmpSalary() + "");
        	EmployeeContactTF.setText(emp.getEmpContact());
        	
        	oldEmpId = EmployeeIDTF.getText();
        }
    }
    
    @FXML
    void editEmployee(ActionEvent event) throws IOException {
        int empId = 0;
        float empSalarty = 0;
        try{
        	empId = Integer.parseInt(EmployeeIDTF.getText());
        	empSalarty = Float.parseFloat(EmployeeSalaryTF.getText());
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Invalid input format. Please enter a valid values.");
        }
        String empFName = EmployeeFNTF.getText();
        String empLName = EmployeeLNTF.getText();
        String empContact = EmployeeContactTF.getText();
        
        String url = "jdbc:postgresql://localhost:5432/postgres";
        String user = "postgres";
        String password = "postgres@Muath29SQL";
        
        String sql = "UPDATE employee SET empid = ?, empfname = ?, emplname = ?, empsalary = ?, empcontact = ? WHERE empid = " + oldEmpId;
        try (Connection conn = DriverManager.getConnection(url, user, password); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, empId);
            pstmt.setString(2, empFName);
            pstmt.setString(3, empLName);
            pstmt.setFloat(4, empSalarty);
            pstmt.setString(5, empContact);
            
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
            	showAlert(Alert.AlertType.INFORMATION, "Successfully", "Employee updated successfully!");
            	
            	Stage stage = (Stage) editEmployeeBTTN.getScene().getWindow();
                stage.close();
                
                App.setRoot("Employee");
            } else {
            	showAlert(Alert.AlertType.ERROR, "Error", "Failed to update Employee!");
                System.out.println("Failed to update Employee!");
            }
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Error updating Employee: " + e.getMessage());
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Invalid input format. Please enter a valid values.");
        }        
    }
    
    @FXML
    void cancelEditEmployee(ActionEvent event) {
        Stage stage = (Stage) cancelEditEmployeeBTTN.getScene().getWindow();
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
