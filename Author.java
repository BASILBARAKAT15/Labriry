/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.GLibraryProject;
// Author.java

public class Author {

    private int authID;
    private String authFname;
    private String authLname;
    private String authCountry;

    // Constructors
    public Author() {
    }

    public Author(int authID, String authFname, String authLname, String authCountry) {
        this.authID = authID;
        this.authFname = authFname;
        this.authLname = authLname;
        this.authCountry = authCountry;
    }

    public Author(int authID, String authFname, String authLname) {
        this.authID = authID;
        this.authFname = authFname;
        this.authLname = authLname;
    }

    // Getters and setters
    public int getAuthID() {
        return authID;
    }

    public void setAuthID(int authID) {
        this.authID = authID;
    }

    public String getAuthFname() {
        return authFname;
    }

    public void setAuthFname(String authFname) {
        this.authFname = authFname;
    }

    public String getAuthLname() {
        return authLname;
    }

    public void setAuthLname(String authLname) {
        this.authLname = authLname;
    }

    public String getAuthCountry() {
        return authCountry;
    }

    public void setAuthCountry(String authCountry) {
        this.authCountry = authCountry;
    }

    public String toString() {
        return "" + authID;
    }
}
