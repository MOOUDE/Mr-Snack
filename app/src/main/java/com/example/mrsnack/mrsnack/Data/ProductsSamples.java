package com.example.mrsnack.mrsnack.Data;

import android.support.annotation.NonNull;
import android.util.Log;

import com.example.mrsnack.mrsnack.Modules.Product;
import com.example.mrsnack.mrsnack.Modules.ProductCollector;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ProductsSamples {
    ArrayList<Product> products = new ArrayList<Product>();
    FirebaseDatabase mFirebaseDatabase;
    DatabaseReference mRef;
    public ProductsSamples() {

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mRef = mFirebaseDatabase.getReference("snacks");



    }
    public ArrayList<Product> getProduct(){

        Log.d("firebase size : " , ""+ products.size());

        return products;
    }

}
