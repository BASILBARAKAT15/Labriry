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

public class AddPurchaseController {

    @FXML
    private TextField BookIDTF;

    @FXML
    private TextField CustIDTF;

    @FXML
    private Button addPurchaseBTTN;

    @FXML
    private Button cancelAddPurchaseBTTN;

    @FXML
    void addPurchase(ActionEvent event) throws IOException {
        int bookID = Integer.parseInt(BookIDTF.getText());
        int custID = Integer.parseInt(CustIDTF.getText());

        // Database connection details
        String url = "jdbc:postgresql://localhost:5432/postgres";
        String user = "postgres";
        String password = "postgres@Muath29SQL";

        // SQL statement to insert a new purchase
        String sql = "INSERT INTO purchases (bookid, custid) VALUES (?, ?)";

        try (Connection conn = DriverManager.getConnection(url, user, password); PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, bookID);
            pstmt.setInt(2, custID);

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                showAlert(Alert.AlertType.INFORMATION, "Successfully", "Purchase added successfully!");
                
                // Close the window after adding the purchase
                Stage stage = (Stage) addPurchaseBTTN.getScene().getWindow();
                stage.close();

                // Refresh the TableView in the PurchasesController
                App.setRoot("Purchases");

            } else {
            	showAlert(Alert.AlertType.ERROR, "ERROR", "Failed to add purchase!");
            }
            
        } catch (SQLException e) {
        	showAlert(Alert.AlertType.ERROR, "ERROR", "Error adding purchase: " + e.getMessage());
        } catch (NumberFormatException e) {
        	showAlert(Alert.AlertType.ERROR, "ERROR", "Invalid input format. Please enter valid integers for BookID and CustID.");
        }
    }

    @FXML
    void cancelAddPurchase(ActionEvent event) {
        Stage stage = (Stage) cancelAddPurchaseBTTN.getScene().getWindow();
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