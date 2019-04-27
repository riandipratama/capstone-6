package com.example.capstone.DB;

public class Vendor {

    private String id;
    private String email;
    private String pass;
    private String name;
    private String address;
    private String city;
    private String phone;
    private String image;

    public Vendor(){

    }

    public Vendor(String id, String email, String name, String address, String city, String phone, String image){
        this.id = id;
        this.email = email;
        this.name = name;
        this.address = address;
        this.city = city;
        this.phone = phone;
        this.image = image;
    }

    public Vendor(String email, String name, String address, String city, String phone){
        this.email = email;
        this.name = name;
        this.address = address;
        this.city = city;
        this.phone = phone;
    }

    public Vendor(String email, String pass, String name, String address, String city, String phone, String image, String just_null){
        this.email = email;
        this.pass = pass;
        this.name = name;
        this.address = address;
        this.city = city;
        this.phone = phone;
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

    public String getPhone() {
        return phone;
    }

    public String getImage() {
        return image;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }
}
