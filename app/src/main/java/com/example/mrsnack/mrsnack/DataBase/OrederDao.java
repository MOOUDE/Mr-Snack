package com.example.mrsnack.mrsnack.DataBase;


import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;

import com.example.mrsnack.mrsnack.Modules.Product;

@Dao
public interface ProductDao  {


    @Insert
    public void addProduct(Product product);



}
