package com.mycompany.GLibraryProject;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class DashboardController {
	@FXML
	private PieChart pieChart;
	@FXML
	private BarChart<String, Number> barChart;
	
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
    
	private Connection connectToDB() {
	    String url = "jdbc:postgresql://localhost:5432/postgres";
	    String user = "postgres";
	    String password = "postgres@Muath29SQL";
	    
	    try {
	        return DriverManager.getConnection(url, user, password);
	    } catch (Exception e) {
	        e.printStackTrace();
	        return null;
	    }
	}
	
	private void loadPieChartData() {
	    try (Connection conn = connectToDB()) {
	        // Query to get the total number of books
	        String bookCountQuery = "SELECT COUNT(*) AS count FROM book";
	        PreparedStatement bookStmt = conn.prepareStatement(bookCountQuery);
	        ResultSet bookRs = bookStmt.executeQuery();
	        if (bookRs.next()) {
	            int bookCount = bookRs.getInt("count");
	            pieChart.getData().add(new PieChart.Data("Total Books", bookCount));
	        }
	        
	        // Query to get the total number of authors
	        String authorCountQuery = "SELECT COUNT(*) AS count FROM author";
	        PreparedStatement authorStmt = conn.prepareStatement(authorCountQuery);
	        ResultSet authorRs = authorStmt.executeQuery();
	        if (authorRs.next()) {
	            int authorCount = authorRs.getInt("count");
	            pieChart.getData().add(new PieChart.Data("Total Authors", authorCount));
	        }
	        
	        // Query to get the total number of publishers
	        String publisherCountQuery = "SELECT COUNT(*) AS count FROM publisher";
	        PreparedStatement publisherStmt = conn.prepareStatement(publisherCountQuery);
	        ResultSet publisherRs = publisherStmt.executeQuery();
	        if (publisherRs.next()) {
	            int publisherCount = publisherRs.getInt("count");
	            pieChart.getData().add(new PieChart.Data("Total Publishers", publisherCount));
	        }
	        
	        // Query to get the total number of borrowed books
	        String borrowedBooksQuery = "SELECT COUNT(*) AS count FROM borrows";
	        PreparedStatement borrowedStmt = conn.prepareStatement(borrowedBooksQuery);
	        ResultSet borrowedRs = borrowedStmt.executeQuery();
	        if (borrowedRs.next()) {
	            int borrowedCount = borrowedRs.getInt("count");
	            pieChart.getData().add(new PieChart.Data("Borrowed Books", borrowedCount));
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
	
	private void loadBarChartData() {
	    try (Connection conn = connectToDB()) {
	        // Query to get the number of borrowed books per customer
	        String query = "SELECT c.custfname AS custfname, COUNT(b.bookid) AS borrow_count " +
	                       "FROM customer c " +
	                       "JOIN borrows b ON c.custid = b.custid " +
	                       "GROUP BY c.custfname ORDER BY borrow_count DESC";
	        PreparedStatement stmt = conn.prepareStatement(query);
	        ResultSet rs = stmt.executeQuery();
	        
	        // Creating the data series for the BarChart
	        XYChart.Series<String, Number> series = new XYChart.Series<>();
	        series.setName("Number of Borrowed Books per Customer");
	        
	        // Adding data to the series
	        while (rs.next()) {
	            String customerName = rs.getString("custfname");
	            int borrowCount = rs.getInt("borrow_count");
	            series.getData().add(new XYChart.Data<>(customerName, borrowCount));
	        }
	        
	        // Adding the series to the BarChart
	        barChart.getData().add(series);
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
	
	@FXML
	public void initialize() {
	    loadPieChartData();
	    loadBarChartData();
	}
}