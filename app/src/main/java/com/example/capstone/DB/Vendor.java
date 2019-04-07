package com.example.capstone.DB;

public class Vendor {

    private String id;
    private String email;
    private String pass;
    private String name;
    private String address;
    private String city;
    private String image;

    public Vendor(){

    }

    public Vendor(String id, String email, String name, String address, String city, String image){
        this.id = id;
        this.email = email;
        this.name = name;
        this.address = address;
        this.city = city;
        this.image = image;
    }

    //Setter methods

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setImage(String image) {
        this.image = image;
    }

    //getter methods


    public String getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPass() {
        return pass;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getCity() {
        return city;
    }

    public String getImage() {
        return image;
    }


}
