package com.mycompany.GLibraryProject;
import java.awt.Dimension;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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
import javax.swing.JFrame;

import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.swing.JRViewer;

public class BorrowController {
    @FXML
    private ComboBox<String> borrowsCombo;
    
    @FXML
    private TableColumn<Borrow, Integer> bookid;
    
    @FXML
    private TableColumn<Borrow, Integer> customerid;
    
    @FXML
    private TableColumn<Borrow, String> enddate;
    
    @FXML
    private TableColumn<Borrow, String> startdate;
    
    @FXML
    private TableView<Borrow> table;
    
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
    
    @SuppressWarnings("unused")
	@FXML
    void report(ActionEvent event) {
        Connection con;
        InputStream input;
        JasperDesign jd;
        JasperReport jr;
        JasperPrint jp;
        OutputStream output;
        try {
            String url = "jdbc:postgresql://localhost:5432/postgres";
            String user = "postgres";
            String password = "postgres@Muath29SQL";

            con = DriverManager.getConnection(url, user, password);
            input = new FileInputStream(new File("BorrowReport.jrxml"));
            jd = JRXmlLoader.load(input);
            jr = JasperCompileManager.compileReport(jd);
            jp = JasperFillManager.fillReport(jr, null, con);
            JFrame frame = new JFrame("BorrowsReport");
            frame.setSize(new Dimension(1000, 750));
            frame.getContentPane().add(new JRViewer(jp));
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
            input.close();
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void addBorrowWindowCall(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("AddBorrow.fxml"));
        Parent addPublisherRoot = loader.load();
        Stage addPublisherStage = new Stage();
        addPublisherStage.setScene(new Scene(addPublisherRoot));
        addPublisherStage.show();
    }

    @FXML
    void removeBorrow(ActionEvent event) {
        try {
            Borrow selectedBorrow = table.getSelectionModel().getSelectedItem();
            
            if (selectedBorrow != null) {
                int custid = selectedBorrow.getCustID();
                int bookid = selectedBorrow.getBookID();
                
                Class.forName("org.postgresql.Driver");
                String url = "jdbc:postgresql://localhost:5432/postgres";
                String user = "postgres";
                String password = "postgres@Muath29SQL";
                Connection conn = DriverManager.getConnection(url, user, password);
                
                Statement stmt = conn.createStatement();
                int rowsAffected = stmt.executeUpdate("DELETE FROM borrows WHERE custid = " + custid + " AND bookid = " + bookid);

                if (rowsAffected > 0) {
                	showAlert(Alert.AlertType.INFORMATION, "Successful", "Row with customer id " + custid + " and book id " + bookid + " deleted successfully.");
                    initialize();
                } else {
                	showAlert(Alert.AlertType.ERROR, "Input Error", "No rows found with customer id " + custid + " and book id " + bookid + ".");
                }

                stmt.close();
                conn.close();
            } else {
            	showAlert(Alert.AlertType.ERROR, "Error", "Please select a borrow from the table.");
            }
        } catch (NumberFormatException e) {
        	showAlert(Alert.AlertType.ERROR, "Input Error", "Invalid input format. Please enter valid integers.");
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", "An error occurred while deleting the row. Please try again later.");
        }
    }

    @FXML
    void searchAction(ActionEvent event) {
        try {
            borrowsCombo.setItems(FXCollections.observableArrayList("None", "CustID", "BookID", "StartDate", "EndDate"));

            Class.forName("org.postgresql.Driver");
            String url = "jdbc:postgresql://localhost:5432/postgres";
            String user = "postgres";
            String password = "postgres@Muath29SQL";
            Connection conn = DriverManager.getConnection(url, user, password);

            String selectedColumn = borrowsCombo.getValue();

            String query;
            if ("None".equals(selectedColumn)) {
                query = "SELECT * FROM borrows";
            } else if ("CustID".equals(selectedColumn)) {
                query = "SELECT * FROM borrows WHERE custid = ?";
            } else if ("BookID".equals(selectedColumn)) {
                query = "SELECT * FROM borrows WHERE bookid = ?";
            } else {
                query = "SELECT * FROM borrows WHERE " + selectedColumn + " = ?";
            }

            java.sql.PreparedStatement stmt = conn.prepareStatement(query);

            if (!"None".equals(selectedColumn)) {
                try {
                    switch (selectedColumn) {
                        case "CustID":
                        case "BookID":
                            stmt.setInt(1, Integer.parseInt(searchAttribute.getText()));
                            break;
                        case "StartDate":
                        case "EndDate":
                            stmt.setDate(1, java.sql.Date.valueOf(searchAttribute.getText()));
                            break;
                        default:
                            stmt.setString(1, searchAttribute.getText());
                            break;
                    }
                } catch (NumberFormatException e) {
                	showAlert(Alert.AlertType.ERROR, "Input Error", "Invalid input for " + selectedColumn + ". Please enter a valid value.");
                    return;
                }
            }

            ResultSet rs = stmt.executeQuery();

            ObservableList<Borrow> borrowList = FXCollections.observableArrayList();

            while (rs.next()) {
                int custID = rs.getInt("custid");
                int bookID = rs.getInt("bookid");
                String startDate = rs.getString("startdate");
                String endDate = rs.getString("enddate");

                Borrow borrow = new Borrow(custID, bookID, startDate, endDate);
                borrowList.add(borrow);
            }

            rs.close();
            stmt.close();
            conn.close();

            table.setItems(borrowList);

        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", "An error occurred while searching. Please try again later.");
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
            borrowsCombo.setItems(FXCollections.observableArrayList("None", "CustID", "BookID", "StartDate", "EndDate"));

            Class.forName("org.postgresql.Driver");
            String url = "jdbc:postgresql://localhost:5432/postgres";
            String user = "postgres";
            String password = "postgres@Muath29SQL";
            Connection conn = DriverManager.getConnection(url, user, password);

            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM Borrows");

            ObservableList<Borrow> borrowList = FXCollections.observableArrayList();

            while (rs.next()) {
                int custID = rs.getInt("custid");
                int bookID = rs.getInt("bookid");
                String startDate = rs.getString("startdate");
                String endDate = rs.getString("enddate");

                Borrow borrow = new Borrow(custID, bookID, startDate, endDate);
                borrowList.add(borrow);
            }

            rs.close();
            stmt.close();
            conn.close();

            bookid.setCellValueFactory(new PropertyValueFactory<>("bookID"));
            customerid.setCellValueFactory(new PropertyValueFactory<>("custID"));
            startdate.setCellValueFactory(new PropertyValueFactory<>("startDate"));
            enddate.setCellValueFactory(new PropertyValueFactory<>("endDate"));
            setCenterAlignment();
            
            table.setItems(borrowList);
            table.setStyle("-fx-font-family: serif; -fx-font-size: 13px;");
            table.setFixedCellSize(20);
            
            ContextMenu contextMenu = new ContextMenu();
            MenuItem updateItem = new MenuItem("Update Row");
            MenuItem printItem = new MenuItem("Print Row");
            
            updateItem.setOnAction(event -> {
            	Borrow selectedRow = table.getSelectionModel().getSelectedItem();
                if (selectedRow != null) {
                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("EditBorrow.fxml"));
                        Parent editAuthorRoot = loader.load();
                        
                        EditBorrowController editController = loader.getController();
                        editController.setBorrow(selectedRow);
                        
                        Stage stage = new Stage();
                        stage.setScene(new Scene(editAuthorRoot));
                        stage.show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            printItem.setOnAction(event -> {
            	Borrow selectedAuthor = table.getSelectionModel().getSelectedItem();
                if (selectedAuthor != null) {
                    printRow(selectedAuthor);
                }
            });
            contextMenu.getItems().addAll(updateItem, printItem);
            
            table.setRowFactory(tv -> {
                TableRow<Borrow> row = new TableRow<>();
                row.setOnMouseClicked(event -> {
                    if (event.getButton() == MouseButton.SECONDARY && !row.isEmpty()) {
                        contextMenu.show(row, event.getScreenX(), event.getScreenY());
                    }
                });
                return row;
            });
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", "An error occurred while initializing. Please try again later.");
        }
    }
    
    private void setCenterAlignment() {
        // Center align each column's text
    	bookid.setCellFactory(column -> new TableCell<Borrow, Integer>() {
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
        
    	startdate.setCellFactory(column -> new TableCell<Borrow, String>() {
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
    	customerid.setCellFactory(bookid.getCellFactory());
    	enddate.setCellFactory(startdate.getCellFactory());
    }
    
    private void printRow(Borrow item) {
        showAlert(Alert.AlertType.INFORMATION, "Print Borrow",
                  "Borrow Details:\n\nCustomer ID: " + item.getCustID() +
                  "\nBook ID: " + item.getBookID() +
                  "\nStart Date: " + item.getStartDate() +
                  "\nEnd Date: " + item.getEndDate());
    }
    
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}