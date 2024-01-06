package com.example.store.Admin;

import org.bson.Document;

public class Admins {
    private String id;
    private String username;
    private String Password;


    // Constructors, getters, setters, etc.


    public Admins(String username, String password) {
        this.username = username;
        Password = password;
    }

    public Admins(String id, String username, String password) {
        this.id = id;
        this.username = username;
        this.Password = password;
    }

    public Document toDocument() {
        return new Document()
                .append("username", username)
                .append("password", Password);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

}
