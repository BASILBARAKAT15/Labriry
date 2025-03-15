package com.mycompany.GLibraryProject;

public class Purchase {

    private int custID;
    private int bookID;

    public Purchase(int custID, int bookID) {
        this.custID = custID;
        this.bookID = bookID;
    }

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
}
