package com.mycompany.GLibraryProject;

public class Borrow {

    private int custID;
    private int bookID;
    private String startDate;
    private String endDate;

    public Borrow(int custID, int bookID, String startDate, String endDate) {
        this.custID = custID;
        this.bookID = bookID;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    // Getters and Setters
    public int getCustID() {
        return custID;
    }

    public void setCustID(int custID) {
        this.custID = custID;
    }

    public int getBookID() {
        return bookID;
    }

    public void setBookID(int bookID) {
        this.bookID = bookID;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
}
