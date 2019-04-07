package com.example.capstone.DB;

public class Category {

    private String id;
    private String cat_name;

    public Category(String id, String name) {
        this.id = id;
        this.cat_name = name;
    }

    public String getCat_name() {
        return cat_name;
    }

    public String getId() {
        return id;
    }
}
