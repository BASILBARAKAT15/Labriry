package com.mycompany.GLibraryProject;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

public class CustomerDashboardEditableController {
    @FXML
    private TextField searchnameTF;
    
    @FXML
    private TextField searchTextField;
    
    @FXML
    private TextField idInfoField;
    
    @FXML
    private TextField passInfoField;
    
    @FXML
    private TextField fNameField;
    
    @FXML
    private TextField lNameField;
    
    @FXML
    private TextField ContactField;
    
    @FXML
    private Label fullNameCard;
    
    @FXML
    private Label contactCard;
    
    @FXML 
    private Label idCard;
    
    @FXML
    private TableColumn<CustomerBook, String> purchasedbookname;
    
    @FXML
    private TableColumn<CustomerBook, String> purchasedbookauthor;
    
    @FXML
    private TableView<CustomerBook> purchasedtable;
    
    @FXML
    private TableColumn<CustomerBook, String> borrowedbookname;

    @FXML
    private TableColumn<CustomerBook, String> borrowedbookenddate;

    @FXML
    private TableView<CustomerBook> borrowedtable;
    
    private ObservableList<CustomerBook> purchasedBooksData = FXCollections.observableArrayList();
    private ObservableList<CustomerBook> borrowedBooksData = FXCollections.observableArrayList();
    
    @FXML
    private void LogOutAction(ActionEvent event) throws IOException {
        App.setRoot("LogIn");
    }
    
    @FXML
    void backToProfile(ActionEvent event) throws IOException {
        App.setRoot("CustomerDashboard");
    }
    
    @FXML
    void saveEditables(ActionEvent event) throws IOException {
        int custID = Integer.parseInt(idInfoField.getText());
        String custPass = passInfoField.getText();
        String custFname = fNameField.getText();
        String custLname = lNameField.getText();
        String custContact = ContactField.getText();
        
        // Database connection details
        String url = "jdbc:postgresql://localhost:5432/postgres";
        String user = "postgres";
        String password = "postgres@Muath29SQL";
        
        // SQL statement to update an existing customer
        String sql = "UPDATE customer SET custfname = ?, custlname = ?, custcontact = ?, password = ? WHERE custid = ?";
        try (Connection conn = DriverManager.getConnection(url, user, password); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, custFname);
            pstmt.setString(2, custLname);
            pstmt.setString(3, custContact);
            pstmt.setString(4, custPass);
            pstmt.setInt(5, custID);
            
            idCard.setText(custID + "");
            fullNameCard.setText(custFname + " " + custLname);
            contactCard.setText(custContact);
            
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
            	showAlert(Alert.AlertType.INFORMATION, "Successfully", "Customer updated successfully!");
            } else {
            	showAlert(Alert.AlertType.ERROR, "Error", "Failed to update customer!");
                System.out.println("Failed to update customer!");
            }
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Error updating customer: " + e.getMessage());
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Invalid input format. Please enter a valid integer for CustID.");
        }
        
        App.setRoot("CustomerDashboard");
    }
    
    @FXML
    private void callWindowFeedback(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("CustomerFeedback.fxml"));
        Parent addFeedbackRoot = loader.load();
        Stage addFeedbackStage = new Stage();
        addFeedbackStage.setScene(new Scene(addFeedbackRoot));
        addFeedbackStage.show();
    }

    @FXML
    private void initialize() {
        // Add an event handler to the TextField
        searchTextField.setOnMouseClicked(event -> {
            try {
                App.setRoot("CustomerBookSearch");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        
        //same for the search borrowed and purchased
        searchnameTF.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                String searchText = searchnameTF.getText();

                searchPurchasedBooks(searchText);
                searchBorrowedBooks(searchText);
            }
        });
        
        // Set customer details
        try {
            // Load the PostgreSQL JDBC driver
            Class.forName("org.postgresql.Driver");
            
            // Establish the connection
            String url = "jdbc:postgresql://localhost:5432/postgres";
            String user = "postgres";
            String password = "postgres@Muath29SQL";
            
            try (Connection conn = DriverManager.getConnection(url, user, password); Statement stmt = conn.createStatement()) {

                // Fetch customer details
                String queryCustomer = "SELECT custfname, custlname, custcontact, password FROM customer WHERE custid = " + LogInController.loggedInCustID;
                ResultSet rsCustomer = stmt.executeQuery(queryCustomer);

                if (rsCustomer.next()) {
                    String firstName = rsCustomer.getString("custfname");
                    String lastName = rsCustomer.getString("custlname");
                    String contact = rsCustomer.getString("custcontact");
                    String pass = rsCustomer.getString("password");
                    
                    idInfoField.setText(String.valueOf(LogInController.loggedInCustID));
                    passInfoField.setText(pass);
                    fNameField.setText(firstName);
                    lNameField.setText(lastName);
                    ContactField.setText(contact);
                    
                    idCard.setText(idInfoField.getText());
                    fullNameCard.setText(firstName + " " + lastName);
                    contactCard.setText(contact);
                }
                
                // Load purchased books
                purchasedBooksData.clear();
                String queryPurchasedBooks = "SELECT b.bookname, a.authfname, a.authlname FROM book b "
                        + "JOIN author a ON b.authid = a.authid "
                        + "JOIN purchases p ON b.bookid = p.bookid "
                        + "WHERE p.custid = " + LogInController.loggedInCustID;
                ResultSet rsPurchased = stmt.executeQuery(queryPurchasedBooks);

                while (rsPurchased.next()) {
                    String bookName = rsPurchased.getString("bookname");
                    String authorFirstName = rsPurchased.getString("authfname");
                    String authorLastName = rsPurchased.getString("authlname");
                    purchasedBooksData.add(new CustomerBook(bookName, authorFirstName + " " + authorLastName, ""));
                }

                purchasedbookname.setCellValueFactory(new PropertyValueFactory<>("Name"));
                purchasedbookauthor.setCellValueFactory(new PropertyValueFactory<>("Author"));
                
                purchasedtable.setItems(purchasedBooksData);
                purchasedtable.setStyle("-fx-font-family: serif; -fx-font-size: 13px;");
                purchasedtable.setFixedCellSize(20);
                
                // Load borrowed books
                borrowedBooksData.clear();
                String queryBorrowedBooks = "SELECT b.bookname, bb.enddate FROM book b "
                        + "JOIN borrows bb ON b.bookid = bb.bookid "
                        + "WHERE bb.custid = " + LogInController.loggedInCustID;
                ResultSet rsBorrowed = stmt.executeQuery(queryBorrowedBooks);

                while (rsBorrowed.next()) {
                    String bookName = rsBorrowed.getString("bookname");
                    String endDate = rsBorrowed.getString("enddate");
                    borrowedBooksData.add(new CustomerBook(bookName, "", endDate));
                }

                borrowedbookname.setCellValueFactory(new PropertyValueFactory<>("Name"));
                borrowedbookenddate.setCellValueFactory(new PropertyValueFactory<>("endDate"));
                setCenterAlignment();
                
                borrowedtable.setItems(borrowedBooksData);
                borrowedtable.setStyle("-fx-font-family: serif; -fx-font-size: 13px;");
                borrowedtable.setFixedCellSize(20);
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    //----------search methods----------
    private void searchPurchasedBooks(String searchText) {
        purchasedBooksData.clear();

        try {
            // Load the PostgreSQL JDBC driver
            Class.forName("org.postgresql.Driver");

            // Establish the connection
            String url = "jdbc:postgresql://localhost:5432/postgres";
            String user = "postgres";
            String password = "postgres@Muath29SQL";

            try (Connection conn = DriverManager.getConnection(url, user, password); Statement stmt = conn.createStatement()) {

                String query = "SELECT b.bookname, a.authfname, a.authlname FROM book b "
                        + "JOIN author a ON b.authid = a.authid "
                        + "JOIN purchases p ON b.bookid = p.bookid "
                        + "WHERE p.custid = " + LogInController.loggedInCustID + " "
                        + "AND b.bookname LIKE '%" + searchText + "%'";

                ResultSet rs = stmt.executeQuery(query);

                while (rs.next()) {
                    String bookName = rs.getString("bookname");
                    String authorFirstName = rs.getString("authfname");
                    String authorLastName = rs.getString("authlname");
                    purchasedBooksData.add(new CustomerBook(bookName, authorFirstName + " " + authorLastName, ""));
                }

                purchasedbookname.setCellValueFactory(new PropertyValueFactory<>("Name"));
                purchasedbookauthor.setCellValueFactory(new PropertyValueFactory<>("Author"));
                setCenterAlignment();
                
                purchasedtable.setItems(purchasedBooksData);
                purchasedtable.setStyle("-fx-font-family: serif; -fx-font-size: 13px;");
                purchasedtable.setFixedCellSize(20);
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }
    
    private void searchBorrowedBooks(String searchText) {
        borrowedBooksData.clear();

        try {
            // Load the PostgreSQL JDBC driver
            Class.forName("org.postgresql.Driver");

            // Establish the connection
            String url = "jdbc:postgresql://localhost:5432/postgres";
            String user = "postgres";
            String password = "postgres@Muath29SQL";

            try (Connection conn = DriverManager.getConnection(url, user, password); Statement stmt = conn.createStatement()) {

                String query = "SELECT b.bookname, bb.enddate FROM book b "
                        + "JOIN borrows bb ON b.bookid = bb.bookid "
                        + "WHERE bb.custid = " + LogInController.loggedInCustID + " "
                        + "AND b.bookname LIKE '%" + searchText + "%'";

                ResultSet rs = stmt.executeQuery(query);
                
                while (rs.next()) {
                    String bookName = rs.getString("bookname");
                    String endDate = rs.getString("enddate");
                    borrowedBooksData.add(new CustomerBook(bookName, "", endDate));
                }

                borrowedbookname.setCellValueFactory(new PropertyValueFactory<>("Name"));
                borrowedbookenddate.setCellValueFactory(new PropertyValueFactory<>("endDate"));
                setCenterAlignment();
                
                borrowedtable.setItems(borrowedBooksData);
                borrowedtable.setStyle("-fx-font-family: serif; -fx-font-size: 13px;");
                borrowedtable.setFixedCellSize(20);
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }
    
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    private void setCenterAlignment() {
    	purchasedbookname.setCellFactory(column -> new TableCell<CustomerBook, String>() {
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
    	purchasedbookauthor.setCellFactory(purchasedbookname.getCellFactory());
    	borrowedbookname.setCellFactory(purchasedbookname.getCellFactory());
    	borrowedbookenddate.setCellFactory(purchasedbookname.getCellFactory());
    }
}