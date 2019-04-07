package com.example.capstone.DB;

public class Product {

    private String id;
    private String name;
    private String cat_id;
    private String qty;
    private String price;
    private String desc;
    private String vendor_id;

    public Product(){

    }

    public Product(String name, String cat_id, String qty, String price, String desc, String vendor_id) {
        this.name = name;
        this.cat_id = cat_id;
        this.qty = qty;
        this.price = price;
        this.desc = desc;
        this.vendor_id = vendor_id;
    }

    public Product(String id, String name, String cat_id, String qty, String price, String desc, String vendor_id) {
        this.id = id;
        this.name = name;
        this.cat_id = cat_id;
        this.qty = qty;
        this.price = price;
        this.desc = desc;
        this.vendor_id = vendor_id;
    }

    public String getId() {
        return id;
    }

    public String getPrice() {
        return price;
    }

    public String getCat_id() {
        return cat_id;
    }

    public String getDesc() {
        return desc;
    }

    public String getName() {
        return name;
    }

    public String getQty() {
        return qty;
    }

    public String getVendor_id() {
        return vendor_id;
    }
}
