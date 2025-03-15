package com.mycompany.GLibraryProject;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

public class FeedbackController {
    @FXML
    private TableColumn<Feedback, String> Description;
    
    @FXML
    private ComboBox<String> FeedbackCombo;

    @FXML
    private TableColumn<Feedback, Integer> customerid;

    @FXML
    private TableColumn<Feedback, Integer> ratingoutof10;

    @FXML
    private TextField searchAttribute;

    @FXML
    private TableView<Feedback> table;

    //--------------------METHODS-----------------------------
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
    void searchAction(ActionEvent event) {
        try {
            FeedbackCombo.setItems(FXCollections.observableArrayList("None", "Description", "rating_10", "CustID"));

            Class.forName("org.postgresql.Driver");
            String url = "jdbc:postgresql://localhost:5432/postgres";
            String user = "postgres";
            String password = "postgres@Muath29SQL";
            Connection conn = DriverManager.getConnection(url, user, password);

            String selectedColumn = FeedbackCombo.getValue();

            String query;
            if ("None".equals(selectedColumn)) {
                query = "SELECT * FROM feedback";
            } else if ("Description".equals(selectedColumn)) {
                query = "SELECT * FROM feedback WHERE description ILIKE ?";
            } else {
                query = "SELECT * FROM feedback WHERE " + selectedColumn + " = ?";
            }
            
            java.sql.PreparedStatement stmt = conn.prepareStatement(query);

            if ("Description".equals(selectedColumn)) {
                String searchPattern = "%" + searchAttribute.getText().trim().replaceAll("\\s+", "%") + "%";
                stmt.setString(1, searchPattern);
            } else if (!"None".equals(selectedColumn)) {
                try {
                    stmt.setInt(1, Integer.parseInt(searchAttribute.getText()));
                } catch (NumberFormatException e) {
                	showAlert(Alert.AlertType.ERROR, "Input Error", "Invalid input for " + selectedColumn + ". Please enter a valid value.");
                    return;
                }
            }

            ResultSet rs = stmt.executeQuery();

            ObservableList<Feedback> feedbackList = FXCollections.observableArrayList();

            while (rs.next()) {
                String description = rs.getString("description");
                int rating = rs.getInt("rating_10");
                int custID = rs.getInt("custid");

                Feedback feedback = new Feedback(description, rating, custID);
                feedbackList.add(feedback);
            }

            rs.close();
            stmt.close();
            conn.close();

            this.Description.setCellValueFactory(new PropertyValueFactory<>("Description"));
            this.ratingoutof10.setCellValueFactory(new PropertyValueFactory<>("rating_10"));
            this.customerid.setCellValueFactory(new PropertyValueFactory<>("custID"));
            setCenterAlignment();
            
            this.table.setItems(feedbackList);
            this.table.setStyle("-fx-font-family: serif; -fx-font-size: 13px;");
            this.table.setFixedCellSize(20);
        } catch (Exception e) {
            e.printStackTrace();
        	showAlert(Alert.AlertType.ERROR, "Database Error", "An error occurred while searching. Please try again later.");
        }
    }

    @FXML
    public void initialize() {
        try {
            FeedbackCombo.setItems(FXCollections.observableArrayList("None", "Description", "rating_10", "CustID"));
            
            Class.forName("org.postgresql.Driver");
            String url = "jdbc:postgresql://localhost:5432/postgres";
            String user = "postgres";
            String password = "postgres@Muath29SQL";
            Connection conn = DriverManager.getConnection(url, user, password);
            
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Feedback");
            ResultSet rs = stmt.executeQuery();
            
            ObservableList<Feedback> feedbackList = FXCollections.observableArrayList();
            while (rs.next()) {
                String description = rs.getString("description");
                int rating = rs.getInt("rating_10");
                int custID = rs.getInt("custid");

                Feedback feedback = new Feedback(description, rating, custID);
                feedbackList.add(feedback);
            }

            rs.close();
            stmt.close();
            conn.close();

            Description.setCellValueFactory(new PropertyValueFactory<>("Description"));
            ratingoutof10.setCellValueFactory(new PropertyValueFactory<>("rating_10"));
            customerid.setCellValueFactory(new PropertyValueFactory<>("custID"));
            setCenterAlignment();
            
            table.setItems(feedbackList);
            table.setStyle("-fx-font-family: serif; -fx-font-size: 13px;");
            table.setFixedCellSize(20);
        } catch (Exception e) {
            e.printStackTrace();
        	showAlert(Alert.AlertType.ERROR, "Database Error", "An error occurred while initializing. Please try again later.");
        }
    }
    
    private void setCenterAlignment() {
        // Center align each column's text
    	Description.setCellFactory(column -> new TableCell<Feedback, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item);
                    setStyle("-fx-alignment: CENTER;");
                }
            }
        });
        
    	ratingoutof10.setCellFactory(column -> new TableCell<Feedback, Integer>() {
            @Override
            protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.toString());
                    setStyle("-fx-alignment: CENTER;");
                }
            }
        });
    	
    	customerid.setCellFactory(column -> new TableCell<Feedback, Integer>() {
            @Override
            protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.toString());
                    setStyle("-fx-alignment: CENTER;");
                }
            }
        });
    }
    
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}