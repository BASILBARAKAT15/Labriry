package com.mycompany.GLibraryProject;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
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
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

public class CustomerDashboardController {
    @FXML
    private TextField searchnameTF;
    
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

    @FXML
    private Label customercontact;

    @FXML
    private Label firstandlastname;

    @FXML
    private Label firstname;

    @FXML
    private Label id;

    @FXML
    private TextField searchTextField;

    @FXML
    private TableColumn<Feedback, String> feedbackdescriptioncolumn;
    
    @FXML
    private TableColumn<Feedback, Integer> feedbackratingcolumn;
    
    @FXML
    private TableColumn<Feedback, String> feedbackusernamecolumn;
    
    @FXML
    private TableView<Feedback> feedbacktable;

    private ObservableList<Feedback> feedbackData = FXCollections.observableArrayList();

    //i propably could have just made a new constructor instead of creating the CustomerBook.java now that i think about it
    private ObservableList<CustomerBook> purchasedBooksData = FXCollections.observableArrayList();
    private ObservableList<CustomerBook> borrowedBooksData = FXCollections.observableArrayList();

    @FXML
    private void LogOutAction(ActionEvent event) throws IOException {
        App.setRoot("LogIn");
    }
    
    @FXML
    private void GoToEditableDash(ActionEvent event) throws IOException {
        App.setRoot("CustomerDashboardEditable");
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
                String queryCustomer = "SELECT custfname, custlname, custcontact FROM customer WHERE custid = " + LogInController.loggedInCustID;
                ResultSet rsCustomer = stmt.executeQuery(queryCustomer);

                if (rsCustomer.next()) {
                    String firstName = rsCustomer.getString("custfname");
                    String lastName = rsCustomer.getString("custlname");
                    String fullName = firstName + " " + lastName;
                    String contact = rsCustomer.getString("custcontact");

                    firstname.setText(firstName);
                    firstandlastname.setText(fullName);
                    customercontact.setText(contact);
                    id.setText(String.valueOf(LogInController.loggedInCustID));
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
                borrowedtable.setItems(borrowedBooksData);
                borrowedtable.setStyle("-fx-font-family: serif; -fx-font-size: 13px;");
                borrowedtable.setFixedCellSize(20);
                
                // Load feedback
                feedbackData.clear();
                String queryFeedback = "SELECT f.description, f.rating_10, c.custfname, c.custlname "
                        + "FROM feedback f "
                        + "JOIN customer c ON f.custid = c.custid";
                ResultSet rsFeedback = stmt.executeQuery(queryFeedback);

                while (rsFeedback.next()) {
                    String description = rsFeedback.getString("description");
                    int rating = rsFeedback.getInt("rating_10");
                    String userName = rsFeedback.getString("custfname") + " " + rsFeedback.getString("custlname");
                    feedbackData.add(new Feedback(description, rating, userName)); // Using the new constructor
                }
                
                feedbackdescriptioncolumn.setCellValueFactory(new PropertyValueFactory<>("Description"));
                feedbackratingcolumn.setCellValueFactory(new PropertyValueFactory<>("rating_10"));
                feedbackusernamecolumn.setCellValueFactory(new PropertyValueFactory<>("userName"));  
                
                setCenterAlignment();
                feedbacktable.setItems(feedbackData);
                feedbacktable.setStyle("-fx-font-family: serif; -fx-font-size: 13px;");
                feedbacktable.setFixedCellSize(20);
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
    
    private void setCenterAlignment() {
    	feedbackratingcolumn.setCellFactory(column -> new TableCell<Feedback, Integer>() {
            @Override
            protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item + "");
                    setStyle("-fx-alignment: CENTER;");
                }
            }
        });
        
    	feedbackdescriptioncolumn.setCellFactory(column -> new TableCell<Feedback, String>() {
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
    	feedbackusernamecolumn.setCellFactory(feedbackdescriptioncolumn.getCellFactory());
    	
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