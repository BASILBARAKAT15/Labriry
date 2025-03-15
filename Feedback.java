package com.mycompany.GLibraryProject;

public class Feedback {

    private String description;
    private int rating_10;
    private int custID;
    private String userName; // extra field

    public Feedback(String description, int rating_10, int custID) {
        this.description = description;
        this.rating_10 = rating_10;
        this.custID = custID;
    }

    public Feedback(String description, int rating_10, String userName) { // extra constructor
        this.description = description;
        this.rating_10 = rating_10;
        this.userName = userName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getRating_10() {
        return rating_10;
    }

    public void setRating_10(int rating_10) {
        this.rating_10 = rating_10;
    }

    public int getCustID() {
        return custID;
    }

    public void setCustID(int custID) {
        this.custID = custID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
