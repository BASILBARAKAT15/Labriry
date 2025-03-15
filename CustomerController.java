package com.mycompany.GLibraryProject;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class CustomerController {
    @FXML
    private TableColumn<Customer, Integer> id;

    @FXML
    private TableColumn<Customer, String> firstname;

    @FXML
    private TableColumn<Customer, String> lastname;

    @FXML
    private TableColumn<Customer, Boolean> librarycard;

    @FXML
    private TableColumn<Customer, String> contact;

    @FXML
    private TableView<Customer> table;

    @FXML
    private ComboBox<String> customerCombo;

    @FXML
    private TextField searchAttribute;

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
    void addCustomerWindowCall(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("AddCustomer.fxml"));
        Parent addCustRoot = loader.load();
        
        Stage addCustomerStage = new Stage();
        addCustomerStage.setScene(new Scene(addCustRoot));
        addCustomerStage.setResizable(false);
        addCustomerStage.show();
    }
    
    @FXML
    void removeCustomer(ActionEvent event) {
        try {
            Customer selectedCustomer = table.getSelectionModel().getSelectedItem();
            
            if (selectedCustomer != null) {
                int custid = selectedCustomer.getCustID();
                
                Class.forName("org.postgresql.Driver");
                String url = "jdbc:postgresql://localhost:5432/postgres";
                String user = "postgres";
                String password = "postgres@Muath29SQL";
                Connection conn = DriverManager.getConnection(url, user, password);
                
                Statement stmt = conn.createStatement();
                stmt.executeUpdate("DELETE FROM feedback WHERE custid = " + custid);
                int rowsAffected = stmt.executeUpdate("DELETE FROM customer WHERE custid = " + custid);
                
                if (rowsAffected > 0) {
                	showAlert(Alert.AlertType.INFORMATION, "Successful", "Row with customer id " + custid + " deleted successfully.");
                    initialize();
                } else {
                	showAlert(Alert.AlertType.ERROR, "Input Error", "No rows found with customer id " + custid + ".");
                }

                stmt.close();
                conn.close();
            } else {
            	showAlert(Alert.AlertType.ERROR, "Error", "Please select a customer from the table.");
            }
        } catch (NumberFormatException e) {
        	showAlert(Alert.AlertType.ERROR, "Input Error", "Invalid customer id format. Please enter a valid integer.");
        } catch (Exception e) {
            e.printStackTrace();
        	showAlert(Alert.AlertType.ERROR, "Database Error", "An error occurred while deleting the row. Please try again later.");
        }
    }

    @FXML
    void searchAction(ActionEvent event) {
        try {
            customerCombo.setItems(FXCollections.observableArrayList("None", "CustID", "CustFname", "CustLname", "LibraryCard", "CustContact"));

            Class.forName("org.postgresql.Driver");
            String url = "jdbc:postgresql://localhost:5432/postgres";
            String user = "postgres";
            String password = "postgres@Muath29SQL";
            Connection conn = DriverManager.getConnection(url, user, password);

            String selectedColumn = customerCombo.getValue();

            String query;
            if ("None".equals(selectedColumn)) {
                query = "SELECT * FROM customer";
            } else if ("LibraryCard".equals(selectedColumn)) {
                query = "SELECT * FROM customer WHERE librarycard = ?";
            } else if ("CustID".equals(selectedColumn)) {
                query = "SELECT * FROM customer WHERE custid = ?";
            } else {
                query = "SELECT * FROM customer WHERE " + selectedColumn + " ILIKE ?";
            }

            java.sql.PreparedStatement stmt = conn.prepareStatement(query);

            if (!"None".equals(selectedColumn)) {
                try {
                    if ("CustID".equals(selectedColumn)) {
                        stmt.setInt(1, Integer.parseInt(searchAttribute.getText()));
                    } else if ("LibraryCard".equals(selectedColumn)) {
                        boolean value = Boolean.parseBoolean(searchAttribute.getText());
                        stmt.setBoolean(1, value);
                    } else {
                        stmt.setString(1, "%" + searchAttribute.getText() + "%"); // Use "%" for pattern matching
                    }
                } catch (NumberFormatException e) {
                	showAlert(Alert.AlertType.ERROR, "Input Error", "Invalid input for " + selectedColumn + ". Please enter a valid value.");
                    return;
                }
            }

            ResultSet rs = stmt.executeQuery();

            ObservableList<Customer> customerList = FXCollections.observableArrayList();

            while (rs.next()) {
                int custid = rs.getInt("custid");
                String firstname = rs.getString("custfname");
                String lastname = rs.getString("custlname");
                boolean librarycard = rs.getBoolean("librarycard");
                String contact = rs.getString("custcontact");

                Customer customer = new Customer(custid, firstname, lastname, librarycard, contact);
                customerList.add(customer);
            }

            rs.close();
            stmt.close();
            conn.close();

            table.setItems(customerList);

        } catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    public void initialize() {
        try {
            searchAttribute.setOnKeyPressed(event -> {
                if (event.getCode() == KeyCode.ENTER) {
                    searchAction(new ActionEvent()); // Call the searchAction method when Enter key is pressed
                }
            });
            customerCombo.setItems(FXCollections.observableArrayList("None", "CustID", "CustFname", "CustLname", "LibraryCard", "CustContact"));

            Class.forName("org.postgresql.Driver");
            String url = "jdbc:postgresql://localhost:5432/postgres";
            String user = "postgres";
            String password = "postgres@Muath29SQL";
            Connection conn = DriverManager.getConnection(url, user, password);

            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM Customer");

            ObservableList<Customer> customerList = FXCollections.observableArrayList();

            while (rs.next()) {
                int custid = rs.getInt("custid");
                String firstname = rs.getString("custfname");
                String lastname = rs.getString("custlname");
                boolean librarycard = rs.getBoolean("librarycard");
                String contact = rs.getString("custcontact");

                Customer customer = new Customer(custid, firstname, lastname, librarycard, contact);
                customerList.add(customer);
            }
            
            rs.close();
            stmt.close();
            conn.close();

            table.setItems(customerList);
            table.setStyle("-fx-font-family: serif; -fx-font-size: 13px;");
            table.setFixedCellSize(20);
            
            id.setCellValueFactory(new PropertyValueFactory<>("custID"));
            firstname.setCellValueFactory(new PropertyValueFactory<>("custFname"));
            lastname.setCellValueFactory(new PropertyValueFactory<>("custLname"));
            librarycard.setCellValueFactory(new PropertyValueFactory<>("libraryCard"));
            contact.setCellValueFactory(new PropertyValueFactory<>("custContact"));
            setCenterAlignment();
            
            ContextMenu contextMenu = new ContextMenu();
            MenuItem updateItem = new MenuItem("Update Row");
            MenuItem printItem = new MenuItem("Print Row");
            
            updateItem.setOnAction(event -> {
            	Customer selectedRow = table.getSelectionModel().getSelectedItem();
                if (selectedRow != null) {
                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("EditCustomer.fxml"));
                        Parent editAuthorRoot = loader.load();
                        
                        EditCustomerController editController = loader.getController();
                        editController.setCustomer(selectedRow);
                        
                        Stage stage = new Stage();
                        stage.setScene(new Scene(editAuthorRoot));
                        stage.show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            printItem.setOnAction(event -> {
            	Customer selectedAuthor = table.getSelectionModel().getSelectedItem();
                if (selectedAuthor != null) {
                    printRow(selectedAuthor);
                }
            });
            contextMenu.getItems().addAll(updateItem, printItem);
            
            table.setRowFactory(tv -> {
                TableRow<Customer> row = new TableRow<>();
                row.setOnMouseClicked(event -> {
                    if (event.getButton() == MouseButton.SECONDARY && !row.isEmpty()) {
                        contextMenu.show(row, event.getScreenX(), event.getScreenY());
                    }
                });
                return row;
            });
        } catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void setCenterAlignment() {
        // Center align each column's text
    	id.setCellFactory(column -> new TableCell<Customer, Integer>() {
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
    	
    	firstname.setCellFactory(column -> new TableCell<Customer, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.toString());
                    setStyle("-fx-alignment: CENTER;");
                }
            }
        });
    	lastname.setCellFactory(firstname.getCellFactory());
    	contact.setCellFactory(firstname.getCellFactory());
    	
    	librarycard.setCellFactory(column -> new TableCell<Customer, Boolean>() {
            @Override
            protected void updateItem(Boolean item, boolean empty) {
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
    
    private void printRow(Customer item) {
        showAlert(Alert.AlertType.INFORMATION, "Print Customer",
                  "Customer Details:\n\nCustomer ID: " + item.getCustID() +
                  "\nFirst Name: " + item.getCustFname() +
                  "\nLast Name: " + item.getCustLname() +
                  "\nContact: " + item.getCustContact());
    }
    
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}