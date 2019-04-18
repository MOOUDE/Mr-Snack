package com.example.mrsnack.mrsnack;

import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.mrsnack.mrsnack.DataBase.ProductAppDatabase;
import com.example.mrsnack.mrsnack.Modules.Order;
import com.example.mrsnack.mrsnack.Modules.Product;



public class ProductDetalis extends AppCompatActivity {

    public static ProductAppDatabase mDataBase;

    private Product mainProduct;
    Toolbar toolbar;
    ImageView image;
    TextView amount , priceText , wightText;

    CoordinatorLayout coordinatorLayout;
    public static final String PRODUCTS = "product";


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detalis);
        toolbar = (Toolbar) findViewById(R.id.anim_toolbar);
        image = (ImageView) findViewById(R.id.header);
        amount = (TextView) findViewById(R.id.amount);
        priceText = (TextView) findViewById(R.id.price);
        wightText = (TextView) findViewById(R.id.wight);



        mainProduct = getIntent().getParcelableExtra(PRODUCTS);
        amount.setText("1");

        String name = mainProduct.getName();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbar.setTitle(name);
        }

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.product_activity_Layout);


        RequestOptions options = new RequestOptions()
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.loading)
                .error(R.drawable.loading);


        Glide.with(getApplicationContext()).load(mainProduct.getImage())
                .apply(options).into(image);
        priceText.setText(String.valueOf(mainProduct.getPrice())+" NIS");
        wightText.setText(String.valueOf(mainProduct.getWight())+" gm");

        mDataBase = Room.databaseBuilder(this , ProductAppDatabase.class , "orders").build();

    }

    public void increaseAmount(View v){
        int amountVal = Integer.valueOf(amount.getText().toString());
        if( amountVal >= 1 ){
            amountVal++;
            amount.setText(String.valueOf(amountVal));
        }
    }

    public void decreaseAmount(View v){
        int amountVal= Integer.valueOf(amount.getText().toString());
        if(amountVal > 1 ){
            amountVal--;
            amount.setText(String.valueOf(amountVal));
        }

    }

    public void onOrder(View v){

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Order order = new Order();

                order.setName(mainProduct.getName());
                order.setPrice(mainProduct.getPrice());
                order.setAmount(Integer.parseInt(amount.getText().toString()));
                order.setImage(mainProduct.getImage());

                ProductDetalis.mDataBase.mDao().addOrder(order);

                showSnackBar();
            }
        });
                thread.start();



            }


     public void showSnackBar(){
         Snackbar snackbar = Snackbar

                 .make(coordinatorLayout , "Added to Cart !", Snackbar.LENGTH_LONG)
                 .setAction("Check out", new View.OnClickListener() {
                     @Override
                     public void onClick(View view) {



                         Intent intent = new Intent(getApplicationContext() , Cart.class);


                         startActivity(intent);
                     }
                 });
         snackbar.show();


     }

    }

