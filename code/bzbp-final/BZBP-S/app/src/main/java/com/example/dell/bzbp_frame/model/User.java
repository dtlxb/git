package com.example.dell.bzbp_frame.model;


import java.io.Serializable;
import java.sql.Date;

public class User implements Serializable {

    private int id;
    private String username;
    private String password;
    private String role;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User(int id, String username, String password, String role){
        this.id = id;this.username = username;this.password = password;this.role = role;
    }

    public User(){

    }

    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
