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

public class AddAuthorController {

    @FXML
    private TextField AuthIDTF;

    @FXML
    private TextField AuthFnameTF;

    @FXML
    private TextField AuthLnameTF;

    @FXML
    private TextField AuthCountryTF;

    @FXML
    private Button addAuthorBTTN;

    @FXML
    private Button cancelAddAuthorBTTN;

    @FXML
    void addAuthor(ActionEvent event) throws IOException {
        int authID = Integer.parseInt(AuthIDTF.getText());
        String authFname = AuthFnameTF.getText();
        String authLname = AuthLnameTF.getText();
        String authCountry = AuthCountryTF.getText();

        // Database connection details
        String url = "jdbc:postgresql://localhost:5432/postgres";
        String user = "postgres";
        String password = "postgres@Muath29SQL";

        // SQL statement to insert a new author
        String sql = "INSERT INTO author (authID, authfname, authlname, authcountry) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = DriverManager.getConnection(url, user, password); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, authID);
            pstmt.setString(2, authFname);
            pstmt.setString(3, authLname);
            pstmt.setString(4, authCountry);

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
            	showAlert(Alert.AlertType.INFORMATION, "Successfully", "Author added successfully!");
            	
                // Close the window after adding the author
                Stage stage = (Stage) addAuthorBTTN.getScene().getWindow();
                stage.close();

                // Refresh the TableView in the AuthorController
                App.setRoot("Author");
            } else {
            	showAlert(Alert.AlertType.ERROR, "ERROR", "Failed to add author!");
            }

        } catch (SQLException e) {
        	showAlert(Alert.AlertType.ERROR, "ERROR", "Error adding author: " + e.getMessage());
        } catch (NumberFormatException e) {
        	showAlert(Alert.AlertType.ERROR, "ERROR", "Invalid input format. Please enter valid integers for AuthID.");
        }
    }

    @FXML
    void cancelAddAuthor(ActionEvent event) {
        Stage stage = (Stage) cancelAddAuthorBTTN.getScene().getWindow();
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