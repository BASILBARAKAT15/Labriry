package com.mycompany.GLibraryProject;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

public class CustomerFeedBackController {

    @FXML
    private Button addFeedbackBTTN;

    @FXML
    private Button cancelFeedbackBTTN;

    @FXML
    private TextArea descriptionTextArea;

    @FXML
    private Slider ratingslider;

    @FXML
    void addFeedback(ActionEvent event) throws IOException {
        int rating_10 = (int) ratingslider.getValue();
        int custID = LogInController.loggedInCustID;
        String description = descriptionTextArea.getText();

        String url = "jdbc:postgresql://localhost:5432/postgres";
        String user = "postgres";
        String password = "postgres@Muath29SQL";

        String sql = "INSERT INTO feedback (description, rating_10, custid) VALUES (?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(url, user, password); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, description);
            pstmt.setInt(2, rating_10);
            pstmt.setInt(3, custID);

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                // Close the window after adding the review
                Stage stage = (Stage) addFeedbackBTTN.getScene().getWindow();
                stage.close();

                // Refresh the feedback table
                App.setRoot("CustomerDashboard");
            }
            
        } catch (SQLException e) {
            System.out.println("Error adding feedback: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("Invalid input format. Please enter valid integers for rating.");
        }
    }

    @FXML
    void cancelFeedback(ActionEvent event) {
        Stage stage = (Stage) cancelFeedbackBTTN.getScene().getWindow();
        stage.close();
    }
}