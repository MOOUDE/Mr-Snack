package com.example.mrsnack.mrsnack.Modules;

import android.os.Parcel;
import android.os.Parcelable;

public class Product implements Parcelable {

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


    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Product createFromParcel(Parcel in) {
            return new Product(in);
        }

        public Product[] newArray(int size) {
            return new Product[size];
        }
    };

    public Product(Parcel in){
        this.price = in.readDouble();
        this.name = in.readString();
        this.image =  in.readString();
        this.wight =  in.readInt();
        this.type =  in.readString();
    }
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(this.price);
        dest.writeString(this.name);
        dest.writeString(this.image);
        dest.writeInt(this.wight);
        dest.writeString(this.type);
    }
}
