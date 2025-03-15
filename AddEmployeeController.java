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

public class AddEmployeeController {
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
    private Button addEmployeeBTTN;
    @FXML
    private Button cancelAddEmployeeBTTN;
    
    @FXML
    void addEmployee(ActionEvent event) throws IOException {
        int empID = Integer.parseInt(EmployeeIDTF.getText());
        String empFname = EmployeeFNTF.getText();
        String empLname = EmployeeLNTF.getText();
        float empSalary = Float.parseFloat(EmployeeSalaryTF.getText());
        String empContact = EmployeeContactTF.getText();

        // Database connection details
        String url = "jdbc:postgresql://localhost:5432/postgres";
        String user = "postgres";
        String password = "postgres@Muath29SQL";

        // SQL statement to insert a new employee
        String sql = "INSERT INTO employee (empid, empfname, emplname, empsalary, password, empcontact) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(url, user, password); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, empID);
            pstmt.setString(2, empFname);
            pstmt.setString(3, empLname);
            pstmt.setFloat(4, empSalary);
            pstmt.setString(5, empID + "");
            pstmt.setString(6, empContact);

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Employee added successfully!");
                showAlert(Alert.AlertType.INFORMATION, "Successfully", "Employee added successfully!");
                
                // Close the window after adding the employee
                Stage stage = (Stage) addEmployeeBTTN.getScene().getWindow();
                stage.close();

                // Refresh the TableView in the EmployeeController
                App.setRoot("Employee");
            } else {
            	showAlert(Alert.AlertType.ERROR, "Error", "Failed to add employee!");
            }
            
        } catch (SQLException e) {
        	showAlert(Alert.AlertType.ERROR, "Error", "Error adding employee: " + e.getMessage());
        } catch (NumberFormatException e) {
        	showAlert(Alert.AlertType.ERROR, "Error", "Invalid input format. Please enter a valid values. " + e.getMessage());
        }

    }

    @FXML
    void cancelAddEmployee(ActionEvent event) {
        Stage stage = (Stage) cancelAddEmployeeBTTN.getScene().getWindow();
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
