package com.mycompany.GLibraryProject;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class EditAuthorController {
    @FXML
    private TextField AuthIDTF;
    @FXML
    private TextField AuthFnameTF;
    @FXML
    private TextField AuthLnameTF;
    @FXML
    private TextField AuthCountryTF;
    
    @FXML
    private Button editAuthorBTTN;
    @FXML
    private Button cancelEditAuthorBTTN;
    
    private Author auth;
    private String oldAuthId;
    void setAuth(Author auth) {
    	this.auth = auth;
    	loadAuthorData();
    }
    
    private void loadAuthorData() {
        if (auth != null) {
            AuthIDTF.setText(auth.getAuthID() + "");
            AuthFnameTF.setText(auth.getAuthFname());
            AuthLnameTF.setText(auth.getAuthLname());
            AuthCountryTF.setText(auth.getAuthCountry());
            
            oldAuthId = AuthIDTF.getText();
        }
    }
    
    @FXML
    void editAuthor(ActionEvent event) throws IOException {
        int authID;
        try {
            authID = Integer.parseInt(AuthIDTF.getText());
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "ERROR", "Invalid input format. Please enter a valid integer for AuthID.");
            return;
        }
        
        String authFName = AuthFnameTF.getText();
        String authLName = AuthLnameTF.getText();
        String authCountry = AuthCountryTF.getText();
        String url = "jdbc:postgresql://localhost:5432/postgres";
        String user = "postgres";
        String password = "postgres@Muath29SQL";

        String sql = "UPDATE author SET authid = ?, authfname = ?, authlname = ?, authcountry = ? WHERE authid = ?";

        try (Connection conn = DriverManager.getConnection(url, user, password); 
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
        	
            pstmt.setInt(1, authID);
            pstmt.setString(2, authFName);
            pstmt.setString(3, authLName);
            pstmt.setString(4, authCountry);
            pstmt.setInt(5, Integer.parseInt(oldAuthId));
            
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                showAlert(Alert.AlertType.INFORMATION, "Successfully", "Author edited successfully!");
                Stage stage = (Stage) editAuthorBTTN.getScene().getWindow();
                stage.close();
                App.setRoot("Author");
            } else {
                showAlert(Alert.AlertType.ERROR, "ERROR", "Failed to edit author!");
            }

        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "ERROR", "Error editing author: " + e.getMessage());
        }
    }
    
    @FXML
    void cancelEditAuthor(ActionEvent event) {
        Stage stage = (Stage) cancelEditAuthorBTTN.getScene().getWindow();
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