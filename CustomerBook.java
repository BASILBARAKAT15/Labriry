package com.mycompany.GLibraryProject;

public class CustomerBook {

    private final String name;
    private final String author;
    private final String endDate;

    public CustomerBook(String name, String author, String endDate) {
        this.name = name;
        this.author = author;
        this.endDate = endDate;
    }

    public String getName() {
        return name;
    }

    public String getAuthor() {
        return author;
    }

    public String getEndDate() {
        return endDate;
    }
}
