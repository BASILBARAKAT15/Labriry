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

public class PublisherController {
    @FXML
    private ComboBox<String> publisherCombo;
    
    @FXML
    private TableColumn<Publisher, Integer> id;

    @FXML
    private TableColumn<Publisher, String> name;

    @FXML
    private TableColumn<Publisher, String> Country;

    @FXML
    private TextField searchAttribute;
    
    @FXML
    private TableView<Publisher> table;
    
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
    
    //jasper report call
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
            input = new FileInputStream(new File("PublishersReport.jrxml"));
            jd = JRXmlLoader.load(input);
            jr = JasperCompileManager.compileReport(jd);
            jp = JasperFillManager.fillReport(jr, null, con);
            JFrame frame = new JFrame("PublishersReport");
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
    void addPublisherWindowCall(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("AddPublisher.fxml"));
        Parent addPublisherRoot = loader.load();
        Stage addPublisherStage = new Stage();
        addPublisherStage.setScene(new Scene(addPublisherRoot));
        addPublisherStage.show();
    }

    @FXML
    void removePublisher(ActionEvent event) {
        try {
            Publisher selectedPublisher = table.getSelectionModel().getSelectedItem();

            if (selectedPublisher != null) {
                int pubid = selectedPublisher.getPubID();

                Class.forName("org.postgresql.Driver");
                String url = "jdbc:postgresql://localhost:5432/postgres";
                String user = "postgres";
                String password = "postgres@Muath29SQL";
                Connection conn = DriverManager.getConnection(url, user, password);

                Statement stmt = conn.createStatement();
                int rowsAffected = stmt.executeUpdate("DELETE FROM publisher WHERE pubid = " + pubid);

                if (rowsAffected > 0) {
                    showAlert(Alert.AlertType.INFORMATION, "Successful", "Row with publisher id " + pubid + " deleted successfully.");
                    initialize();
                } else {
                    showAlert(Alert.AlertType.ERROR, "Error", "No rows found with publisher id " + pubid + ".");
                }

                stmt.close();
                conn.close();
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Please select a publisher from the table.");
            }
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Input Error", "Invalid publisher id format. Please enter a valid integer.");
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", "An error occurred while deleting the row. Please try again later.");
        }
    }

    @FXML
    void searchAction(ActionEvent event) {
        try {
            publisherCombo.setItems(FXCollections.observableArrayList("None", "PubID", "PubName", "PubCountry"));

            Class.forName("org.postgresql.Driver");
            String url = "jdbc:postgresql://localhost:5432/postgres";
            String user = "postgres";
            String password = "postgres@Muath29SQL";
            Connection conn = DriverManager.getConnection(url, user, password);

            String selectedColumn = publisherCombo.getValue();
            
            String query;
            if ("None".equals(selectedColumn)) {
                query = "SELECT * FROM publisher";
            } else if ("PubID".equals(selectedColumn)) {
                query = "SELECT * FROM publisher WHERE pubid = ?";
            } else {
                query = "SELECT * FROM publisher WHERE " + selectedColumn + " ILIKE ?";
            }

            java.sql.PreparedStatement stmt = conn.prepareStatement(query);

            if (!"None".equals(selectedColumn)) {
                try {
                    if ("PubID".equals(selectedColumn)) {
                        stmt.setInt(1, Integer.parseInt(searchAttribute.getText()));
                    } else {
                        stmt.setString(1, "%" + searchAttribute.getText() + "%"); // Use "%" for pattern matching
                    }
                } catch (NumberFormatException e) {
                    showAlert(Alert.AlertType.ERROR, "Input Error", "Invalid input for " + selectedColumn + ". Please enter a valid input.");
                    return;
                }
            }

            ResultSet rs = stmt.executeQuery();

            ObservableList<Publisher> publisherList = FXCollections.observableArrayList();

            while (rs.next()) {
                int pubid = rs.getInt("pubid");
                String pubname = rs.getString("pubname");
                String pubcountry = rs.getString("pubcountry");

                Publisher publisher = new Publisher(pubid, pubname, pubcountry);
                publisherList.add(publisher);
            }

            rs.close();
            stmt.close();
            conn.close();

            table.setItems(publisherList);

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
            publisherCombo.setItems(FXCollections.observableArrayList("None", "PubID", "PubName", "PubCountry"));

            Class.forName("org.postgresql.Driver");
            String url = "jdbc:postgresql://localhost:5432/postgres";
            String user = "postgres";
            String password = "postgres@Muath29SQL";
            Connection conn = DriverManager.getConnection(url, user, password);

            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM publisher");

            ObservableList<Publisher> publisherList = FXCollections.observableArrayList();

            while (rs.next()) {
                int pubid = rs.getInt("pubid");
                String pubname = rs.getString("pubname");
                String pubcountry = rs.getString("pubcountry");

                Publisher publisher = new Publisher(pubid, pubname, pubcountry);
                publisherList.add(publisher);
            }
            
            rs.close();
            stmt.close();
            conn.close();
            
            table.setItems(publisherList);
            table.setStyle("-fx-font-family: serif; -fx-font-size: 13px;");
            table.setFixedCellSize(20);
            
            id.setCellValueFactory(new PropertyValueFactory<>("pubID"));
            name.setCellValueFactory(new PropertyValueFactory<>("pubName"));
            Country.setCellValueFactory(new PropertyValueFactory<>("pubCountry"));
            setCenterAlignment();
            
            ContextMenu contextMenu = new ContextMenu();
            MenuItem updateItem = new MenuItem("Update Row");
            MenuItem printItem = new MenuItem("Print Row");
            
            updateItem.setOnAction(event -> {
            	Publisher selectedRow = table.getSelectionModel().getSelectedItem();
                if (selectedRow != null) {
                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("EditPublisher.fxml"));
                        Parent editAuthorRoot = loader.load();
                        
                        EditPublisherController editController = loader.getController();
                        editController.setPublisher(selectedRow);
                        
                        Stage stage = new Stage();
                        stage.setScene(new Scene(editAuthorRoot));
                        stage.show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            printItem.setOnAction(event -> {
            	Publisher selectedAuthor = table.getSelectionModel().getSelectedItem();
                if (selectedAuthor != null) {
                    printRow(selectedAuthor);
                }
            });
            contextMenu.getItems().addAll(updateItem, printItem);
            
            table.setRowFactory(tv -> {
                TableRow<Publisher> row = new TableRow<>();
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
    	id.setCellFactory(column -> new TableCell<Publisher, Integer>() {
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
        
    	name.setCellFactory(column -> new TableCell<Publisher, String>() {
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
    	
    	Country.setCellFactory(column -> new TableCell<Publisher, String>() {
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
    }
    
    private void printRow(Publisher item) {
        showAlert(Alert.AlertType.INFORMATION, "Print Publisher",
                  "Publisher Details:\n\nPublisher ID: " + item.getPubID() +
                  "\nPublisher Name: " + item.getPubName() +
                  "\nPublisher Country: " + item.getPubCountry());
    }
    
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}