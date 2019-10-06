package com.example.steam.Models;

public class User {


    private String Uuid;
    private String username;
    private String fullname;
    private String email;
    private int ImageLink;
    private String Role;

    public String getRole() {
        return Role;
    }

    public void setRole(String role) {
        Role = role;
    }

    public User(String uuid, String username, String fullname, String email, int imageLink, String role) {
        Uuid = uuid;
        this.username = username;
        this.fullname = fullname;
        this.email = email;
        this.ImageLink=imageLink;
        this.Role= role;

    }

    public User() {

    }

    public String getUuid() {
        return Uuid;
    }

    public void setUuid(String uuid) {
        Uuid = uuid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getImageLink() {
        return ImageLink;
    }

    public void setImageLink(int imageLink) {
        ImageLink = imageLink;
    }
}



