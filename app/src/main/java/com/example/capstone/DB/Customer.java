package com.example.capstone.DB;

public class Customer {

    private int id;
    private String email;
    private String pass;
    private String fname;
    private String lname;
    private String address;
    private String city;
    private String phone;

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

    //getter methods

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

}
