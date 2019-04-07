package com.example.capstone.DB;

public class ProductImages {
    private String id;
    private String imgPath;

    public ProductImages(){}

    public ProductImages(String id, String imgPath) {
        this.id = id;
        this.imgPath = imgPath;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String getImgPath() {
        return imgPath;
    }
}
