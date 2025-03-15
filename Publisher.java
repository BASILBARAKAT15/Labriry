/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.GLibraryProject;
// Publisher.java

public class Publisher {

    private int pubID;
    private String pubName;
    private String pubCountry;

    // Constructors
    public Publisher() {
    }

    public Publisher(int pubID, String pubName, String pubCountry) {
        this.pubID = pubID;
        this.pubName = pubName;
        this.pubCountry = pubCountry;
    }

    public Publisher(int pubID, String pubName) {
        this.pubID = pubID;
        this.pubName = pubName;
    }

    // Getters and setters
    public int getPubID() {
        return pubID;
    }

    public void setPubID(int pubID) {
        this.pubID = pubID;
    }

    public String getPubName() {
        return pubName;
    }

    public void setPubName(String pubName) {
        this.pubName = pubName;
    }

    public String getPubCountry() {
        return pubCountry;
    }

    public void setPubCountry(String pubCountry) {
        this.pubCountry = pubCountry;
    }

    public String toString() {
        return "" + pubID;
    }
}
