package com.example.capstone.DB;

public class Customer {

    private String id;
    private String email;
    private String pass;
    private String fname;
    private String lname;
    private String address;
    private String city;
    private String phone;
    private String image;

    public Customer() {

    }

    public Customer(String id, String email, String fname, String lname, String address, String city, String phone, String image) {
        this.id = id;
        this.email = email;
        this.fname = fname;
        this.lname = lname;
        this.address = address;
        this.city = city;
        this.phone = phone;
        this.image = image;
    }

    public Customer(String email, String fname, String lname, String address, String city, String phone) {
        this.email = email;
        this.fname = fname;
        this.lname = lname;
        this.address = address;
        this.city = city;
        this.phone = phone;
    }

    //Setter methods

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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

    public String getFname() {
        return fname;
    }

    public String getLname() {
        return lname;
    }

    public String getAddress() {
        return address;
    }

    public String getCity() {
        return city;
    }

    public String getPhone() {
        return phone;
    }

    public String getImage() {
        return image;
    }
}
