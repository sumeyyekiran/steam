package com.example.steam.Models;

public class User {


    private String Uuid;
    private String username;
    private String fullname;
    private String email;
    private String ImageLink;

    public User(String uuid, String username, String fullname, String email, String imageLink) {
        Uuid = uuid;
        this.username = username;
        this.fullname = fullname;
        this.email = email;
        this.ImageLink=imageLink;

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

    public String getImageLink() {
        return ImageLink;
    }

    public void setImageLink(String imageLink) {
        ImageLink = imageLink;
    }
}



