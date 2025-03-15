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

public class AuthorController {
    @FXML
    private ComboBox<String> authorCombo;
    
    @FXML
    private TableColumn<Author, Integer> id;
    
    @FXML
    private TableColumn<Author, String> firstname;
    
    @FXML
    private TableColumn<Author, String> lastname;
    
    @FXML
    private TableColumn<Author, String> country;
    
    @FXML
    private TableView<Author> table;
    
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
            input = new FileInputStream(new File("AuthorsReport.jrxml"));
            jd = JRXmlLoader.load(input);
            jr = JasperCompileManager.compileReport(jd);
            jp = JasperFillManager.fillReport(jr, null, con);
            
            JFrame frame = new JFrame("Authors Report");
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
    void addAuthorWindowCall(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("AddAuthor.fxml"));
        Parent addAuthorRoot = loader.load();
        Stage addAuthorStage = new Stage();
        addAuthorStage.setScene(new Scene(addAuthorRoot));
        addAuthorStage.show();
    }
    
    @FXML
    void removeAuthor(ActionEvent event) {
        Author selectedAuthor = table.getSelectionModel().getSelectedItem();
        
        if (selectedAuthor != null) {
            int authid = selectedAuthor.getAuthID();
            try {
                Class.forName("org.postgresql.Driver");
                String url = "jdbc:postgresql://localhost:5432/postgres";
                String user = "postgres";
                String password = "postgres@Muath29SQL";
                Connection conn = DriverManager.getConnection(url, user, password);
                
                Statement stmt = conn.createStatement();
                int rowsAffected = stmt.executeUpdate("DELETE FROM author WHERE authid = " + authid);
                
                if (rowsAffected > 0) {
                	showAlert(Alert.AlertType.INFORMATION, "Successful", "Row with author id " + authid + " deleted successfully.");
                    initialize();
                } else {
                	showAlert(Alert.AlertType.ERROR, "Error", "No rows found with author id " + authid + ".");
                }
                
                stmt.close();
                conn.close();
            } catch (Exception e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Database Error", "An error occurred while deleting the row. Please try again later.");
            }
        } else {
        	showAlert(Alert.AlertType.ERROR, "Error", "Please select an author from the table.");
        }
    }
    
    @FXML
    void searchAction(ActionEvent event) {
        try {
            authorCombo.setItems(FXCollections.observableArrayList("None", "authid", "authfname", "authlname", "authcountry"));
            
            Class.forName("org.postgresql.Driver");
            String url = "jdbc:postgresql://localhost:5432/postgres";
            String user = "postgres";
            String password = "postgres@Muath29SQL";
            Connection conn = DriverManager.getConnection(url, user, password);
            
            String selectedColumn = authorCombo.getValue();
            String query;
            if ("None".equals(selectedColumn)) {
                query = "SELECT * FROM author order by authid";
            } else if ("authid".equals(selectedColumn)) {  // تأكد من مطابقة الحالة
                query = "SELECT * FROM author WHERE authid = ?";
            } else {
                query = "SELECT * FROM author WHERE " + selectedColumn + " ILIKE ?";
            }
            
            java.sql.PreparedStatement stmt = conn.prepareStatement(query);
            
            if (!"None".equals(selectedColumn)) {
                try {
                    if ("authid".equals(selectedColumn)) { // تأكد من مطابقة الحالة
                        stmt.setInt(1, Integer.parseInt(searchAttribute.getText()));
                    } else {
                        stmt.setString(1, "%" + searchAttribute.getText() + "%"); // استخدام "%" للمطابقة بالنمط
                    }
                } catch (NumberFormatException e) {
                	showAlert(Alert.AlertType.ERROR, "Input Error", "Invalid input for \" + selectedColumn + \". Please enter a valid input.");
                    return;
                }
            }
            
            ResultSet rs = stmt.executeQuery();
            
            ObservableList<Author> authorList = FXCollections.observableArrayList();
            while (rs.next()) {
                int authid = rs.getInt("authId");
                String firstname = rs.getString("authFname");
                String lastname = rs.getString("authLname");
                String country = rs.getString("authCountry");
                
                Author author = new Author(authid, firstname, lastname, country);
                authorList.add(author);
            }
            
            rs.close();
            stmt.close();
            conn.close();
            
            table.setItems(authorList);
            table.setStyle("-fx-font-family: serif; -fx-font-size: 13px;");
            table.setFixedCellSize(20);
            
            id.setCellValueFactory(new PropertyValueFactory<>("authID"));
            firstname.setCellValueFactory(new PropertyValueFactory<>("authFname"));
            lastname.setCellValueFactory(new PropertyValueFactory<>("authLname"));
            country.setCellValueFactory(new PropertyValueFactory<>("authCountry"));
            setCenterAlignment();
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
            authorCombo.setItems(FXCollections.observableArrayList("None", "authid", "authfname", "authlname", "authcountry"));
            
            Class.forName("org.postgresql.Driver");
            String url = "jdbc:postgresql://localhost:5432/postgres";
            String user = "postgres";
            String password = "postgres@Muath29SQL";
            Connection conn = DriverManager.getConnection(url, user, password);
            
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM author order by authid");
            
            ObservableList<Author> authorList = FXCollections.observableArrayList();
            while (rs.next()) {
                int authid = rs.getInt("AuthID");
                String firstname = rs.getString("authfname");
                String lastname = rs.getString("authlname");
                String country = rs.getString("authcountry");
                
                Author author = new Author(authid, firstname, lastname, country);
                authorList.add(author);
            }
            
            rs.close();
            stmt.close();
            conn.close();
            
            table.setItems(authorList);
            table.setStyle("-fx-font-family: serif; -fx-font-size: 13px;");
            table.setFixedCellSize(20);
            
            id.setCellValueFactory(new PropertyValueFactory<>("authID"));
            firstname.setCellValueFactory(new PropertyValueFactory<>("authFname"));
            lastname.setCellValueFactory(new PropertyValueFactory<>("authLname"));
            country.setCellValueFactory(new PropertyValueFactory<>("authCountry"));
            setCenterAlignment();
            
            ContextMenu contextMenu = new ContextMenu();
            MenuItem updateItem = new MenuItem("Update Row");
            MenuItem printItem = new MenuItem("Print Row");
            
            updateItem.setOnAction(event -> {
                Author selectedAuthor = table.getSelectionModel().getSelectedItem();
                if (selectedAuthor != null) {
                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("EditAuthor.fxml"));
                        Parent editAuthorRoot = loader.load();
                        
                        EditAuthorController editController = loader.getController();
                        editController.setAuth(selectedAuthor);
                        
                        Stage editAuthorStage = new Stage();
                        editAuthorStage.setScene(new Scene(editAuthorRoot));
                        editAuthorStage.show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            printItem.setOnAction(event -> {
                Author selectedAuthor = table.getSelectionModel().getSelectedItem();
                if (selectedAuthor != null) {
                    printRow(selectedAuthor);
                }
            });
            contextMenu.getItems().addAll(updateItem, printItem);
            
            table.setRowFactory(tv -> {
                TableRow<Author> row = new TableRow<>();
                row.setOnMouseClicked(event -> {
                    if (event.getButton() == MouseButton.SECONDARY && !row.isEmpty()) {
                        contextMenu.show(row, event.getScreenX(), event.getScreenY());
                    }
                });
                return row;
            });
        } catch (Exception e) {
        	showAlert(Alert.AlertType.ERROR, "Error", "Exception: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void setCenterAlignment() {
        id.setCellFactory(column -> new TableCell<Author, Integer>() {
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
        
        country.setCellFactory(column -> new TableCell<Author, String>() {
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
        firstname.setCellFactory(country.getCellFactory());
        lastname.setCellFactory(country.getCellFactory());
    }
    
    private void printRow(Author author) {
        showAlert(Alert.AlertType.INFORMATION, "Print Author",
                  "Author Details:\n\nAuthor ID: " + author.getAuthID() +
                  "\nFirst Name: " + author.getAuthFname() +
                  "\nLast Name: " + author.getAuthLname() +
                  "\nCountry: " + author.getAuthCountry());
    }
    
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}