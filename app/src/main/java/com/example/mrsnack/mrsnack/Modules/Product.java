package com.example.mrsnack.mrsnack.Modules;

public class Product {

    private double price;
    private String name;
    private String image;
    private int wight;
    private String type;


    public Product(){}
    public Product(double price, String name, String image, int wight, String type) {
        this.price = price;
        this.name = name;
        this.image = image;
        this.wight = wight;
        this.type = type;
    }

    public void setWight(int wight) {
        this.wight = wight;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getWight() {
        return wight;
    }

    public String getType() {
        return type;
    }

    public double getPrice() {
        return price;
    }

    public String getName() {
        return name;
    }

    public String getImage() {
        return image;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
