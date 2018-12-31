package com.example.mrsnack.mrsnack;

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
import com.example.mrsnack.mrsnack.Adapters.MainRecyclerViewAdapter;
import com.example.mrsnack.mrsnack.Modules.Product;

public class ProductDetalis extends AppCompatActivity {

    Toolbar toolbar;
    ImageView image;
    TextView amount;
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
        Product product = getIntent().getParcelableExtra(PRODUCTS);
        amount.setText("0");

        String name = product.getName();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbar.setTitle(name);
        }

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.product_activity_Layout);

        Glide.with(getApplicationContext()).load(product.getImage()).into(image);


    }
    public void increaseAmount(View v){
        int amountVal = Integer.valueOf(amount.getText().toString());
        if( amountVal >= 0 ){
            amountVal++;
            amount.setText(String.valueOf(amountVal));
        }
    }

    public void decreaseAmount(View v){
        int amountVal= Integer.valueOf(amount.getText().toString());
        if(amountVal > 0 ){
            amountVal--;
            amount.setText(String.valueOf(amountVal));
        }




    }
    public void onOrder(View v){
                Snackbar snackbar = Snackbar
                        .make(coordinatorLayout , "Added to Cart !", Snackbar.LENGTH_LONG)
                        .setAction("Check out", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {


                                Intent intent = new Intent(getApplicationContext() , MainActivity.class);

                                startActivity(intent);
                            }
                        });
                snackbar.show();
            }

    }

