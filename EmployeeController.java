package com.mycompany.GLibraryProject;
import java.awt.Dimension;
import java.io.File;
import java.io.FileInputStream;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Optional;

import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

import javax.swing.JFrame;

import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.swing.JRViewer;

public class EmployeeController {

    @FXML
    private ComboBox<String> employeeCombo;

    @FXML
    private TableColumn<Employee, Integer> id;

    @FXML
    private TableColumn<Employee, String> firstname;

    @FXML
    private TableColumn<Employee, String> lastname;

    @FXML
    private TableColumn<Employee, Float> salary;
    
    @FXML
    private TableColumn<Employee, String> contact;

    @FXML
    private TextField searchAttribute;

    @FXML
    private TableView<Employee> table;

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
            input = new FileInputStream(new File("EmployeeReport.jrxml"));
            jd = JRXmlLoader.load(input);
            jr = JasperCompileManager.compileReport(jd);
            jp = JasperFillManager.fillReport(jr, null, con);
            
            JFrame frame = new JFrame("Employees Report");
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
    void searchAction(ActionEvent event) {
        try {
            employeeCombo.setItems(FXCollections.observableArrayList("None", "EmpID", "EmpFname", "EmpLname", "EmpSalary", "EmpContact"));
            
            Class.forName("org.postgresql.Driver");
            String url = "jdbc:postgresql://localhost:5432/postgres";
            String user = "postgres";
            String password = "postgres@Muath29SQL";
            Connection conn = DriverManager.getConnection(url, user, password);
            
            String selectedColumn = employeeCombo.getValue();
            String query;
            if ("None".equals(selectedColumn)) {
                query = "SELECT empid, empfname, emplname, empsalary, empcontact FROM employee";
            } else if ("EmpID".equals(selectedColumn)) {
                query = "SELECT * FROM employee WHERE empid = ?";
            } else if ("EmpSalary".equals(selectedColumn)) {
                query = "SELECT * FROM employee WHERE empsalary = ?";
            } else if ("EmpContact".equals(selectedColumn)) {
                query = "SELECT * FROM employee WHERE empcontact = ?";
            } else {
                query = "SELECT empid, empfname, emplname, empsalary, empcontact FROM employee WHERE " + selectedColumn + " ILIKE ?";
            }
            
            java.sql.PreparedStatement stmt = conn.prepareStatement(query);
            
            if (!"None".equals(selectedColumn)) {
                try {
                    if ("EmpID".equals(selectedColumn)) {
                        stmt.setInt(1, Integer.parseInt(searchAttribute.getText()));
                    } else if ("EmpSalary".equals(selectedColumn)) {
                        stmt.setFloat(1, Float.parseFloat(searchAttribute.getText()));
                    } else if ("EmpContact".equals(selectedColumn)) {
                        stmt.setString(1, searchAttribute.getText());
                    } else {
                        stmt.setString(1, "%" + searchAttribute.getText() + "%"); // Use "%" for pattern matching
                    }
                } catch (NumberFormatException e) {
                	showAlert(Alert.AlertType.ERROR, "Input Error", "Invalid input for " + selectedColumn + ". Please enter a valid input.");
                    return;
                }
            }
            
            ResultSet rs = stmt.executeQuery();
            ObservableList<Employee> employeeList = FXCollections.observableArrayList();
            
            while (rs.next()) {
                int empID = rs.getInt("empid");
                String empFname = rs.getString("empfname");
                String empLname = rs.getString("emplname");
                float empSalary = rs.getFloat("empsalary");
                String empContact = rs.getString("empcontact");
                
                Employee employee = new Employee(empID, empFname, empLname, empSalary, empContact);
                employeeList.add(employee);
            }

            rs.close();
            stmt.close();
            conn.close();

            table.setItems(employeeList);
            table.setStyle("-fx-font-family: serif; -fx-font-size: 13px;");
            table.setFixedCellSize(20);
            
            id.setCellValueFactory(new PropertyValueFactory<>("empID"));
            firstname.setCellValueFactory(new PropertyValueFactory<>("empFname"));
            lastname.setCellValueFactory(new PropertyValueFactory<>("empLname"));
            salary.setCellValueFactory(new PropertyValueFactory<>("empSalary"));
            contact.setCellValueFactory(new PropertyValueFactory<>("empContact"));
            setCenterAlignment();
        } catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    @FXML
    void removeEmployee(ActionEvent event) {
        try {
            Employee selectedEmployee = table.getSelectionModel().getSelectedItem();

            if (selectedEmployee != null) {
                int empid = selectedEmployee.getEmpID();

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
                int rowsAffected = stmt.executeUpdate("DELETE FROM employee WHERE empid = " + empid);
                
                // Check if any rows were deleted
                if (rowsAffected > 0) {
                	showAlert(Alert.AlertType.INFORMATION, "Successful", "Row with Employee id " + empid + " deleted successfully.");
                    searchAction(new ActionEvent());
                } else {
                	showAlert(Alert.AlertType.ERROR, "Input Error", "No rows found with Employee id " + empid + ".");
                }

                // Close statement and connection
                stmt.close();
                conn.close();
                
                initialize();
            } else {
            	showAlert(Alert.AlertType.ERROR, "Error", "Please select an employee from the table.");
            }
        } catch (NumberFormatException e) {
        	showAlert(Alert.AlertType.ERROR, "Input Error", "Invalid input. Please enter a valid Employee ID.");
        } catch (Exception e) {
            e.printStackTrace();
        	showAlert(Alert.AlertType.ERROR, "Database Error", "An error occurred while deleting the employee. Please try again later.");
        }
    }
    
    @FXML
    void addEmployeeWindowCall(ActionEvent event) {
        try {
            // Load AddEmployee.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("AddEmployee.fxml"));
            Parent addEmployeeRoot = loader.load();

            // Set a new stage
            Stage addEmployeeStage = new Stage();
            addEmployeeStage.setScene(new Scene(addEmployeeRoot));
            addEmployeeStage.show();
        } catch (IOException e) {
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
            employeeCombo.setItems(FXCollections.observableArrayList("None", "EmpID", "EmpFname", "EmpLname", "EmpSalary", "EmpContact"));
            
            // Load the PostgreSQL JDBC driver
            Class.forName("org.postgresql.Driver");

            // Establish the connection
            String url = "jdbc:postgresql://localhost:5432/postgres";
            String user = "postgres";
            String password = "postgres@Muath29SQL";
            Connection conn = DriverManager.getConnection(url, user, password);
            
            // Create a statement
            Statement stmt = conn.createStatement();

            // Execute the SQL query
            ResultSet rs = stmt.executeQuery("SELECT empid, empfname, emplname, empsalary, empcontact FROM employee");
            
            // Create an ObservableList to store Employee objects
            ObservableList<Employee> employeeList = FXCollections.observableArrayList();
            
            // Iterate through the ResultSet and populate the ObservableList
            while (rs.next()) {
                // Retrieve employee data
                int empID = rs.getInt("empid");
                String empFname = rs.getString("empfname");
                String empLname = rs.getString("emplname");
                float empSalary = rs.getFloat("empsalary");
                String empContact = rs.getString("empcontact");
                
                // Create a new Employee object and add it to the list
                Employee employee = new Employee(empID, empFname, empLname, empSalary, empContact);
                employeeList.add(employee);
            }

            // Close the ResultSet, statement, and connection
            rs.close();
            stmt.close();
            conn.close();
            
            // Set the items to the table
            table.setItems(employeeList);
            table.setStyle("-fx-font-family: serif; -fx-font-size: 13px;");
            table.setFixedCellSize(20);
            
            // Bind columns to model properties
            id.setCellValueFactory(new PropertyValueFactory<>("empID"));
            firstname.setCellValueFactory(new PropertyValueFactory<>("empFname"));
            lastname.setCellValueFactory(new PropertyValueFactory<>("empLname"));
            salary.setCellValueFactory(new PropertyValueFactory<>("empSalary"));
            contact.setCellValueFactory(new PropertyValueFactory<>("empContact"));
            setCenterAlignment();
            
            ContextMenu contextMenu = new ContextMenu();
            MenuItem updateItem = new MenuItem("Update Row");
            MenuItem newItem = new MenuItem("Add New");
            MenuItem printItem = new MenuItem("Print Row");
            
            updateItem.setOnAction(event -> {
            	Employee selectedRow = table.getSelectionModel().getSelectedItem();
                if (selectedRow != null) {
                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("EditEmployee.fxml"));
                        Parent editAuthorRoot = loader.load();
                        
                        EditEmployeeController editController = loader.getController();
                        editController.setEmployee(selectedRow);
                        
                        Stage stage = new Stage();
                        stage.setScene(new Scene(editAuthorRoot));
                        stage.show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            newItem.setOnAction(event -> {
            	addEmployee(event);
            });
            printItem.setOnAction(event -> {
            	Employee selectedAuthor = table.getSelectionModel().getSelectedItem();
                if (selectedAuthor != null) {
                    printRow(selectedAuthor);
                }
            });
            contextMenu.getItems().addAll(updateItem, newItem, printItem);
            
            table.setRowFactory(tv -> {
                TableRow<Employee> row = new TableRow<>();
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
    	id.setCellFactory(column -> new TableCell<Employee, Integer>() {
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
        
    	firstname.setCellFactory(column -> new TableCell<Employee, String>() {
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
    	lastname.setCellFactory(firstname.getCellFactory());
    	contact.setCellFactory(firstname.getCellFactory());
    	
    	salary.setCellFactory(column -> new TableCell<Employee, Float>() {
            @Override
            protected void updateItem(Float item, boolean empty) {
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
    
    private void printRow(Employee item) {
        showAlert(Alert.AlertType.INFORMATION, "Print Employee",
                  "Employee Details:\n\nBook ID: " + item.getEmpID() +
                  "\nFirst Name: " + item.getEmpFname() +
                  "\nLast Name: " + item.getEmpLname() +
                  "\nContact: " + item.getEmpContact() +
                  "\nSalary: " + item.getEmpSalary());
    }
    
    void addEmployee(ActionEvent event) {
        try {
            // Load the PostgreSQL JDBC driver
            Class.forName("org.postgresql.Driver");
            
            // Establish the connection
            String url = "jdbc:postgresql://localhost:5432/postgres";
            String user = "postgres";
            String password = "postgres@Muath29SQL";
            Connection conn = DriverManager.getConnection(url, user, password);
            
            // Prompt the user to enter employee details
            String empID =  showInputAlert(Alert.AlertType.INFORMATION, "Input", "Enter the ID of the employee: ");
            String empFname =  showInputAlert(Alert.AlertType.INFORMATION, "Input", "Enter the first name of the employee: ");
            String empLname = showInputAlert(Alert.AlertType.INFORMATION, "Input", "Enter the last name of the employee: ");
            String empSalaryString = showInputAlert(Alert.AlertType.INFORMATION, "Input", "Enter the salary of the employee: ");
            String empContact =  showInputAlert(Alert.AlertType.INFORMATION, "Input", "Enter the Contact of the employee: ");

            if (empFname != null && empLname != null && empSalaryString != null) {
                float empSalary = Float.parseFloat(empSalaryString);
                
                // Create a PreparedStatement
                PreparedStatement stmt = conn.prepareStatement("INSERT INTO employee(empid, empfname, emplname, empsalary, empcontact, password) VALUES (?, ?, ?, ?, ?, ?)");
                stmt.setInt(1, Integer.parseInt(empID));
                stmt.setString(2, empFname);
                stmt.setString(3, empLname);
                stmt.setFloat(4, empSalary);
                stmt.setString(5, empContact);
                stmt.setString(6, empID);
                
                // Execute the insert query
                int rowsAffected = stmt.executeUpdate();
                if (rowsAffected > 0) {
                	showAlert(Alert.AlertType.INFORMATION, "Successful", "Employee added successfully.");
                    searchAction(new ActionEvent());
                } else {
                	showAlert(Alert.AlertType.ERROR, "Error", "Failed to add employee.");
                }
                
                // Close statement and connection
                stmt.close();
                conn.close();
                
                App.setRoot("Employee");
            }
        } catch (NumberFormatException e) {
        	showAlert(Alert.AlertType.ERROR, "Input Error", "Invalid salary format. Please enter a valid number.");
        } catch (Exception e) {
            e.printStackTrace();
        	showAlert(Alert.AlertType.ERROR, "Database Error", "An error occurred while adding the employee. Please try again later.");
        }
    }
    
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
	private String showInputAlert(Alert.AlertType alertType, String title, String message) {
	    TextInputDialog inputDialog = new TextInputDialog();
	    inputDialog.setTitle(title);
	    inputDialog.setHeaderText(null);
	    inputDialog.setContentText(message);
	    
	    Optional<String> result = inputDialog.showAndWait();
	    return result.orElse(null);
	}
}