/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.GLibraryProject;
// Customer.java

public class Customer {

    private int custID;
    private String custFname;
    private String custLname;
    private boolean libraryCard;
    private String custContact;

    // Constructors
    public Customer() {
    }

    public Customer(int custID, String custFname, String custLname, boolean libraryCard, String custContact) {
        this.custID = custID;
        this.custFname = custFname;
        this.custLname = custLname;
        this.libraryCard = libraryCard;
        this.custContact = custContact;
    }

    // Getters and setters
    public int getCustID() {
        return custID;
    }

    public void setCustID(int custID) {
        this.custID = custID;
    }

    public String getCustFname() {
        return custFname;
    }

    public void setCustFname(String custFname) {
        this.custFname = custFname;
    }

    public String getCustLname() {
        return custLname;
    }

    public void setCustLname(String custLname) {
        this.custLname = custLname;
    }

    public boolean isLibraryCard() {
        return libraryCard;
    }

    public void setLibraryCard(boolean libraryCard) {
        this.libraryCard = libraryCard;
    }

    public String getCustContact() {
        return custContact;
    }

    public void setCustContact(String custContact) {
        this.custContact = custContact;
    }

    public String toString() {
        return "" + custID;
    }
}
