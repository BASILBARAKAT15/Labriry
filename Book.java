package com.mycompany.GLibraryProject;

/*we'll be using all of these entites classes to display the data in the
table view rows */
public class Book {

    private int bookID;
    private String bookName;
    private boolean bookAvailable;
    private int bookCount;
    private Employee employee;
    private Publisher publisher;
    private Author author;

    // Constructor
    public Book(int bookID, String bookName, boolean bookAvailable, int bookCount, Employee employee, Publisher publisher, Author author) {
        this.bookID = bookID;
        this.bookName = bookName;
        this.bookAvailable = bookAvailable;
        this.bookCount = bookCount;
        this.employee = employee;
        this.publisher = publisher;
        this.author = author;
    }

    public Book(int bookID, String bookName, int bookCount, Employee employee, Publisher publisher, Author author) {
        this.bookID = bookID;
        this.bookName = bookName;
        this.bookCount = bookCount;
        this.employee = employee;
        this.publisher = publisher;
        this.author = author;
    }

    // Getters and setters
    public int getBookID() {
        return bookID;
    }

    public void setBookID(int bookID) {
        this.bookID = bookID;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public boolean isBookAvailable() {
        return bookAvailable;
    }

    public void setBookAvailable(boolean bookAvailable) {
        this.bookAvailable = bookAvailable;
    }

    public int getBookCount() {
        return bookCount;
    }

    public void setBookCount(int bookCount) {
        this.bookCount = bookCount;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public Publisher getPublisher() {
        return publisher;
    }

    public void setPublisher(Publisher publisher) {
        this.publisher = publisher;
    }
    
    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }
}