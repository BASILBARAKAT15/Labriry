package com.mycompany.GLibraryProject;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class LogInController {
    @FXML
    private Button loginB;
    
    @FXML
    private TextField passTF;
    @FXML
    private TextField userTF;
    
    public static int loggedInEmpID;
    public static int loggedInCustID;
    
    @FXML
    void LogInButtonAction(ActionEvent event) throws IOException {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "ERROR", "Database driver not found.");
            return;
        }
        
        String url = "jdbc:postgresql://localhost:5432/postgres";
        String user = "postgres";
        String password = "postgres@Muath29SQL";
        
        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            // Create a statement
            try (Statement stmt = conn.createStatement()) {
                // Query the Employee table to check if the entered credentials are valid
                String empQuery = "SELECT COUNT(*) FROM employee WHERE empid = " + userTF.getText() + " AND password = '" + passTF.getText() + "'";
                try (ResultSet empRs = stmt.executeQuery(empQuery)) {
                    if (empRs.next() && empRs.getInt(1) > 0) {
                        loggedInEmpID = Integer.parseInt(userTF.getText());
                        
                        double sceneWidth = 1290;
                        double sceneHeight = 700;
                        
                        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
                        double screenWidth = screenBounds.getWidth();
                        double screenHeight = screenBounds.getHeight();
                        
                        double x = (screenWidth - sceneWidth) / 2;
                        double y = (screenHeight - sceneHeight) / 2;
                        
                        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                        stage.close();
                        stage.setResizable(false);
                        stage.setWidth(sceneWidth);
                        stage.setHeight(sceneHeight);
                        stage.setX(x);
                        stage.setY(y);
                        stage.show();
                        
                        App.setRoot("Dashboard");
                        return;
                    }
                }
                
                String custQuery = "SELECT COUNT(*) FROM customer WHERE custid = " + userTF.getText() + " AND password = '" + passTF.getText() + "'";
                try (ResultSet custRs = stmt.executeQuery(custQuery)) {
                    if (custRs.next() && custRs.getInt(1) > 0) {
                        loggedInCustID = Integer.parseInt(userTF.getText());
                        
                        double sceneWidth = 1300;
                        double sceneHeight = 700;
                        
                        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
                        double screenWidth = screenBounds.getWidth();
                        double screenHeight = screenBounds.getHeight();
                        
                        double x = (screenWidth - sceneWidth) / 2;
                        double y = (screenHeight - sceneHeight) / 2;
                        
                        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                        stage.close();
                        stage.setResizable(false);
                        stage.setWidth(sceneWidth);
                        stage.setHeight(sceneHeight);
                        stage.setX(x);
                        stage.setY(y);
                        stage.show();
                        
                        App.setRoot("CustomerDashboard");
                        return;
                    }
                }
                
                showAlert(Alert.AlertType.ERROR, "ERROR", "invalid credentials. Please try again.");
            }
        } catch (NumberFormatException e) {
        	showAlert(Alert.AlertType.ERROR, "ERROR", "Invalid user ID format. Please enter a valid integer.");
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "ERROR", "An error occurred while connecting to the database. Please try again later.");
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "ERROR", "An unexpected error occurred. Please try again later.");
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