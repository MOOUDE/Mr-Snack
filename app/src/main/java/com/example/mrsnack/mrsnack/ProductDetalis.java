package com.example.mrsnack.mrsnack;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.mrsnack.mrsnack.Adapters.MainRecyclerViewAdapter;

public class ProductDetalis extends AppCompatActivity {

    Toolbar toolbar;
    ImageView image;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detalis);
        toolbar = (Toolbar) findViewById(R.id.anim_toolbar);
        image = (ImageView) findViewById(R.id.header);

        String name = getIntent().getStringExtra(MainRecyclerViewAdapter.NAME);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbar.setTitle(name);
        }


        Glide.with(getApplicationContext()).load(getIntent().getStringExtra(MainRecyclerViewAdapter.IMAGE)).into(image);






    }
}
