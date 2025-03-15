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

public class EditPublisherController {
    @FXML
    private TextField PubIDTF;
    @FXML
    private TextField PubNameTF;
    @FXML
    private TextField PubCountryTF;
    
    @FXML
    private Button editPublisherBTTN;
    @FXML
    private Button cancelEditPublisherBTTN;
    
    private String oldPubId;
    private Publisher pub;
    void setPublisher(Publisher pub) {
    	this.pub = pub;
    	loadPublisherData();
    }
    
    private void loadPublisherData() {
        if (pub != null) {
        	PubIDTF.setText(pub.getPubID() + "");
        	PubNameTF.setText(pub.getPubName());
        	PubCountryTF.setText(pub.getPubName());
        	
        	oldPubId = PubIDTF.getText();
        }
    }
    
    @FXML
    void editPublisher(ActionEvent event) throws IOException {
        int pubID = 0;
        try{
        	pubID = Integer.parseInt(PubIDTF.getText());
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Invalid input format. Please enter a valid values.");
        }
        String pubName = PubNameTF.getText();
        String pubCountry = PubCountryTF.getText();
        
        String url = "jdbc:postgresql://localhost:5432/postgres";
        String user = "postgres";
        String password = "postgres@Muath29SQL";
        
        String sql = "UPDATE publisher SET pubid = ?, pubname = ?, pubcountry = ? WHERE pubid = " + oldPubId;
        try (Connection conn = DriverManager.getConnection(url, user, password); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, pubID);
            pstmt.setString(2, pubName);
            pstmt.setString(3, pubCountry);
            
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
            	showAlert(Alert.AlertType.INFORMATION, "Successfully", "Publisher updated successfully!");
            	
            	Stage stage = (Stage) editPublisherBTTN.getScene().getWindow();
                stage.close();
                
                App.setRoot("Publisher");
            } else {
            	showAlert(Alert.AlertType.ERROR, "Error", "Failed to update Publisher!");
                System.out.println("Failed to update Publisher!");
            }
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Error updating Publisher: " + e.getMessage());
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Invalid input format. Please enter a valid values.");
        }
    }
    
    @FXML
    void cancelEditPublisher(ActionEvent event) {
        Stage stage = (Stage) cancelEditPublisherBTTN.getScene().getWindow();
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