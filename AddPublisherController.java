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

public class AddPublisherController {

    @FXML
    private TextField PubIDTF;

    @FXML
    private TextField PubNameTF;

    @FXML
    private TextField PubCountryTF;

    @FXML
    private Button addPublisherBTTN;

    @FXML
    private Button cancelAddPublisherBTTN;

    @FXML
    void addPublisher(ActionEvent event) throws IOException {
        int pubID = Integer.parseInt(PubIDTF.getText());
        String pubName = PubNameTF.getText();
        String pubCountry = PubCountryTF.getText();

        // Database connection details
        String url = "jdbc:postgresql://localhost:5432/postgres";
        String user = "postgres";
        String password = "postgres@Muath29SQL";
        
        // SQL statement to insert a new publisher
        String sql = "INSERT INTO publisher (pubid, pubname, pubcountry) VALUES (?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(url, user, password); PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, pubID);
            pstmt.setString(2, pubName);
            pstmt.setString(3, pubCountry);

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                showAlert(Alert.AlertType.INFORMATION, "Successfully", "Publisher added successfully!");
                
                // Close the window after adding the publisher
                Stage stage = (Stage) addPublisherBTTN.getScene().getWindow();
                stage.close();

                // Refresh the TableView in the PublisherController
                App.setRoot("Publisher");

            } else {
            	showAlert(Alert.AlertType.ERROR, "ERROR", "Failed to add publisher!");
            }

        } catch (SQLException e) {
        	showAlert(Alert.AlertType.ERROR, "ERROR", "Error adding publisher: " + e.getMessage());
        } catch (NumberFormatException e) {
        	showAlert(Alert.AlertType.ERROR, "ERROR", "Invalid input format. Please enter a valid integer for PubID.");
        }

    }

    @FXML
    void cancelAddPublisher(ActionEvent event) {
        Stage stage = (Stage) cancelAddPublisherBTTN.getScene().getWindow();
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