package com.mycompany.GLibraryProject;
import javafx.scene.control.cell.PropertyValueFactory;
import java.io.IOException;
import java.sql.ResultSet;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableCell;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class BookController {
    @FXML
    private TableColumn<Book, Integer> id;
    @FXML
    private TableColumn<Book, String> title;
    @FXML
    private TableColumn<Book, Boolean> available;
    @FXML
    private TableColumn<Book, Author> author;
    @FXML
    private TableColumn<Book, Publisher> publisher;
    @FXML
    private TableColumn<Book, Integer> count;
    @FXML
    private TableColumn<Book, Employee> employee;
    
    @FXML
    private TableView<Book> table;
    
    @FXML
    private ComboBox<String> bookCombo;
    
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
    void searchAction(ActionEvent event) {
        try {
            bookCombo.setItems(FXCollections.observableArrayList("None", "bookid", "bookname", "bookavailable", "empid",
                    "pubid", "authid", "bookcount"));

            // Load the PostgreSQL JDBC driver
            Class.forName("org.postgresql.Driver");

            // Establish the connection
            String url = "jdbc:postgresql://localhost:5432/postgres";
            String user = "postgres";
            String password = "postgres@Muath29SQL";
            Connection conn = DriverManager.getConnection(url, user, password);
            String query;
            
            if ("None".equals(bookCombo.getValue())) {
                query = "SELECT b.*, e.*, p.*, a.* FROM book b "
                        + "LEFT JOIN employee e ON b.empid = e.empid "
                        + "LEFT JOIN publisher p ON b.pubid = p.pubid "
                        + "LEFT JOIN author a ON b.authid = a.authid ";
            } else if ("bookname".equals(bookCombo.getValue())) {
            	query = "SELECT b.*, e.*, p.*, a.* FROM book b "
                        + "LEFT JOIN employee e ON b.empid = e.empid "
                        + "LEFT JOIN publisher p ON b.pubid = p.pubid "
                        + "LEFT JOIN author a ON b.authid = a.authid "
                        + "WHERE b.bookname ILIKE ?";
            } else if ("bookavailable".equals(bookCombo.getValue())) {
            	query = "SELECT b.*, e.*, p.*, a.* FROM book b "
                        + "LEFT JOIN employee e ON b.empid = e.empid "
                        + "LEFT JOIN publisher p ON b.pubid = p.pubid "
                        + "LEFT JOIN author a ON b.authid = a.authid "
                        + "WHERE b.bookavailable = ?";
            } else {
            	query = "SELECT b.*, e.*, p.*, a.* FROM book b "
                        + "LEFT JOIN employee e ON b.empid = e.empid "
                        + "LEFT JOIN publisher p ON b.pubid = p.pubid "
                        + "LEFT JOIN author a ON b.authid = a.authid "
                        + "WHERE b." + bookCombo.getValue() + " = ?";
            }

            // Create a PreparedStatement
            PreparedStatement stmt = conn.prepareStatement(query);

            if (!"None".equals(bookCombo.getValue())) {
                try {
                    switch (bookCombo.getValue()) {
                        case "bookname":
                            stmt.setString(1, "%" + searchAttribute.getText() + "%"); // Use "%" for pattern matching
                            break;
                        case "bookavailable":
                            stmt.setBoolean(1, Boolean.parseBoolean(searchAttribute.getText()));
                            break;
                        default:
                            stmt.setInt(1, Integer.parseInt(searchAttribute.getText()));
                            break;
                    }
                } catch (NumberFormatException e) {
                	showAlert(Alert.AlertType.ERROR, "Input Error", "Invalid input for " + bookCombo.getValue() + ". Please enter a valid value.");
                    return;
                }
            }

            ResultSet rs = stmt.executeQuery();

            // Create an ObservableList to store Book objects
            ObservableList<Book> bookList = FXCollections.observableArrayList();

            // Iterate through the ResultSet and populate the ObservableList
            while (rs.next()) {
                // Retrieve book data
                int bookID = rs.getInt("bookid");
                String bookName = rs.getString("bookname");
                boolean bookAvailable = rs.getBoolean("bookavailable");
                int bookCount = rs.getInt("bookcount");

                // Retrieve employee data
                Employee employee = new Employee();
                employee.setEmpID(rs.getInt("empid"));
                employee.setEmpFname(rs.getString("empfname"));
                employee.setEmpLname(rs.getString("emplname"));

                // Retrieve publisher data
                Publisher publisher = new Publisher();
                publisher.setPubID(rs.getInt("pubid"));
                publisher.setPubName(rs.getString("pubname"));
                publisher.setPubCountry(rs.getString("pubcountry"));

                // Retrieve author data
                Author author = new Author();
                author.setAuthID(rs.getInt("authid"));
                author.setAuthFname(rs.getString("authfname"));
                author.setAuthLname(rs.getString("authlname"));

                // Create a new Book object with associated entities and add it to the list
                Book book = new Book(bookID, bookName, bookAvailable, bookCount, employee, publisher, author);
                bookList.add(book);
            }

            // Close the ResultSet, statement, and connection
            rs.close();
            stmt.close();
            conn.close();

            // Set the items to the table
            table.setItems(bookList);

            // Bind columns to model properties
            id.setCellValueFactory(new PropertyValueFactory<>("bookID"));
            title.setCellValueFactory(new PropertyValueFactory<>("bookName"));
            available.setCellValueFactory(new PropertyValueFactory<>("bookAvailable"));
            count.setCellValueFactory(new PropertyValueFactory<>("bookCount"));
            employee.setCellValueFactory(new PropertyValueFactory<>("Employee"));
            publisher.setCellValueFactory(new PropertyValueFactory<>("Publisher"));
            author.setCellValueFactory(new PropertyValueFactory<>("Author"));
            setCenterAlignment();
        } catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
            e.printStackTrace(); // Print stack trace for debugging
        }
    }
    
    @FXML
    void addBookWindowCall(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("AddBook.fxml"));
        Parent addBookRoot = loader.load();
        
        Stage addBookStage = new Stage();
        addBookStage.setScene(new Scene(addBookRoot));
        addBookStage.setResizable(false);
        addBookStage.show();
    }
    
    @FXML
    void removeBook(ActionEvent event) {
        Book selectedBook = table.getSelectionModel().getSelectedItem();

        if (selectedBook != null) {
            int bookid = selectedBook.getBookID(); // Retrieve bookID from selected book

            try {
                // Load the PostgreSQL JDBC driver
                Class.forName("org.postgresql.Driver");

                // Establish the connection
                String url = "jdbc:postgresql://localhost:5432/postgres";
                String user = "postgres";
                String password = "postgres@Muath29SQL";
                Connection conn = DriverManager.getConnection(url, user, password);

                // Create a statement
                Statement stmt = conn.createStatement();

                // Execute the delete query
                int rowsAffected = stmt.executeUpdate("DELETE FROM book WHERE bookid = " + bookid);

                // Check if any rows were deleted
                if (rowsAffected > 0) {
                	showAlert(Alert.AlertType.INFORMATION, "Successful", "Row with book id " + bookid + " deleted successfully.");
                    initialize();
                } else {
                	showAlert(Alert.AlertType.ERROR, "Error", "No rows found with book id " + bookid + ".");
                }

                // Close statement and connection
                stmt.close();
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            	showAlert(Alert.AlertType.ERROR, "Database Error", "An error occurred while deleting the row. Please try again later.");
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            	showAlert(Alert.AlertType.ERROR, "Error", "Database driver not found.");
            }
        } else {
        	showAlert(Alert.AlertType.ERROR, "Error", "Please select a book from the table.");
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
            //set teh combo list items
            bookCombo.setItems(FXCollections.observableArrayList("None", "bookid", "bookname", "bookavailable", "empid",
                    "pubid", "authid", "bookcount"));

            // Load the PostgreSQL JDBC driver
            Class.forName("org.postgresql.Driver");

            // Establish the connection
            String url = "jdbc:postgresql://localhost:5432/postgres";
            String user = "postgres";
            String password = "postgres@Muath29SQL";
            Connection conn = DriverManager.getConnection(url, user, password);

            // Create a statement
            Statement stmt = conn.createStatement();

            // Execute the query to retrieve book data with associated entities
            // Execute the query to retrieve book data with associated entities
            ResultSet rs = stmt.executeQuery("SELECT b.*, e.*, p.*, a.* FROM book b "
                    + "LEFT JOIN employee e ON b.empid = e.empid "
                    + "LEFT JOIN publisher p ON b.pubid = p.pubid "
                    + "LEFT JOIN author a ON b.authid = a.authid "
            );
            
            // Create an ObservableList to store Book objects
            ObservableList<Book> bookList = FXCollections.observableArrayList();

            // Iterate through the ResultSet and populate the ObservableList
            while (rs.next()) {
                // Retrieve book data
                int bookID = rs.getInt("bookid");
                String bookName = rs.getString("bookname");
                boolean bookAvailable = rs.getBoolean("bookavailable");
                int bookCount = rs.getInt("bookcount");

                // Retrieve employee data
                Employee employee = new Employee();
                employee.setEmpID(rs.getInt("empid")); // Use the correct column name
                employee.setEmpFname(rs.getString("empfname"));
                employee.setEmpLname(rs.getString("emplname"));
                // Similarly, retrieve and set other employee fields

                // Retrieve publisher data
                Publisher publisher = new Publisher();
                publisher.setPubID(rs.getInt("pubid")); // Use the correct column name
                publisher.setPubName(rs.getString("pubname"));
                publisher.setPubCountry(rs.getString("pubcountry"));
                // Similarly, retrieve and set other publisher fields

                // Retrieve author data
                Author author = new Author();
                author.setAuthID(rs.getInt("authid")); // Use the correct column name
                author.setAuthFname(rs.getString("authfname"));
                author.setAuthLname(rs.getString("authlname"));
                // Similarly, retrieve and set other author fields

                // Create a new Book object with associated entities and add it to the list
                Book book = new Book(bookID, bookName, bookAvailable, bookCount, employee, publisher, author);
                bookList.add(book);
            }

            // Close the ResultSet, statement, and connection
            rs.close();
            stmt.close();
            conn.close();

            // Set the items to the table
            table.setItems(bookList);
            table.setStyle("-fx-font-family: serif; -fx-font-size: 13px;");
            table.setFixedCellSize(20);
            
            // Bind columns to model properties
            id.setCellValueFactory(new PropertyValueFactory<>("bookID"));
            title.setCellValueFactory(new PropertyValueFactory<>("bookName"));
            available.setCellValueFactory(new PropertyValueFactory<>("bookAvailable"));
            count.setCellValueFactory(new PropertyValueFactory<>("bookCount"));
            employee.setCellValueFactory(new PropertyValueFactory<>("Employee"));
            publisher.setCellValueFactory(new PropertyValueFactory<>("Publisher"));
            author.setCellValueFactory(new PropertyValueFactory<>("Author"));
            setCenterAlignment();
            
            ContextMenu contextMenu = new ContextMenu();
            MenuItem updateItem = new MenuItem("Update Row");
            MenuItem printItem = new MenuItem("Print Row");
            
            updateItem.setOnAction(event -> {
                Book selectedRow = table.getSelectionModel().getSelectedItem();
                if (selectedRow != null) {
                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("EditBook.fxml"));
                        Parent editAuthorRoot = loader.load();
                        
                        EditBookController editController = loader.getController();
                        editController.setBook(selectedRow);
                        
                        Stage stage = new Stage();
                        stage.setScene(new Scene(editAuthorRoot));
                        stage.show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            printItem.setOnAction(event -> {
            	Book selectedAuthor = table.getSelectionModel().getSelectedItem();
                if (selectedAuthor != null) {
                    printRow(selectedAuthor);
                }
            });
            contextMenu.getItems().addAll(updateItem, printItem);
            
            table.setRowFactory(tv -> {
                TableRow<Book> row = new TableRow<>();
                row.setOnMouseClicked(event -> {
                    if (event.getButton() == MouseButton.SECONDARY && !row.isEmpty()) {
                        contextMenu.show(row, event.getScreenX(), event.getScreenY());
                    }
                });
                return row;
            });
        } catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
            e.printStackTrace(); // Print stack trace for debugging
        }
    }
    
    private void setCenterAlignment() {
        // Center align each column's text
        id.setCellFactory(column -> new TableCell<Book, Integer>() {
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
        count.setCellFactory(id.getCellFactory()); // Reuse the same cell factory for count (Integer)
        
        title.setCellFactory(column -> new TableCell<Book, String>() {
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
        
        available.setCellFactory(column -> new TableCell<Book, Boolean>() {
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
        
        // For custom objects like Author, Publisher, and Employee, use their `toString` or a specific display property
        author.setCellFactory(column -> new TableCell<Book, Author>() {
            @Override
            protected void updateItem(Author item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.toString()); // Assuming Author class has a meaningful toString method
                    setStyle("-fx-alignment: CENTER;");
                }
            }
        });
        
        publisher.setCellFactory(column -> new TableCell<Book, Publisher>() {
            @Override
            protected void updateItem(Publisher item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.toString()); // Assuming Publisher class has a meaningful toString method
                    setStyle("-fx-alignment: CENTER;");
                }
            }
        });
        
        employee.setCellFactory(column -> new TableCell<Book, Employee>() {
            @Override
            protected void updateItem(Employee item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.toString()); // Assuming Employee class has a meaningful toString method
                    setStyle("-fx-alignment: CENTER;");
                }
            }
        });
    }
    
    private void printRow(Book item) {
        showAlert(Alert.AlertType.INFORMATION, "Print Book",
                  "Book Details:\n\nBook ID: " + item.getBookID() +
                  "\nBook Name: " + item.getBookName() +
                  "\nAuthor Name: " + item.getAuthor().getAuthFname() + " " + item.getAuthor().getAuthLname() +
                  "\nPublisher Name: " + item.getPublisher().getPubName() +
                  "\nPublisher Country: " + item.getPublisher().getPubCountry());
    }
    
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}