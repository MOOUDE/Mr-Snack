package com.example.mrsnack.mrsnack;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.mrsnack.mrsnack.Adapters.ItemsAdapter;

import com.example.mrsnack.mrsnack.Adapters.SlideAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class MainActivity extends AppCompatActivity {


    private ViewPager viewPager;
    private SlideAdapter mSlideAdapter;
    LinearLayout sliderDots;
    private int dotsCount;
    private ImageView[] dots;
    RecyclerView recyclerView;
    FirebaseDatabase mFirebaseDatabase;
    DatabaseReference mRef;
    View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sliderDots = (LinearLayout) findViewById(R.id.sliderDots);
        recyclerView = findViewById(R.id.main_recyclerview);
        viewPager = findViewById(R.id.viewPager);

        ItemsAdapter itemsAdapter = new ItemsAdapter(this ,recyclerView);



        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //set up image slider
        mSlideAdapter = new SlideAdapter(this , itemsAdapter.getProducts());

        viewPager.setAdapter(mSlideAdapter);
        view = getCurrentFocus();


/*************************** setting slider *******************************/
        dotsCount = mSlideAdapter.getCount();
        dots = new ImageView[dotsCount];

        for (int i = 0; i < dotsCount; i++) {
            dots[i] = new ImageView(this);

            dots[i].setImageDrawable(
                    ContextCompat.getDrawable(
                            getApplicationContext(), R.drawable.nonactive_dot));
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);

            params.setMargins(8, 0, 8, 0);
            sliderDots.addView(dots[i], params);


        }
        dots[0].setImageDrawable(ContextCompat.getDrawable(
                getApplicationContext(), R.drawable.active_dot));
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                for (int ii = 0; ii < dotsCount; ii++) {
                    dots[ii].setImageDrawable(ContextCompat.getDrawable(
                            getApplicationContext(), R.drawable.nonactive_dot));

                }
                dots[i].setImageDrawable(ContextCompat.getDrawable(
                        getApplicationContext(), R.drawable.active_dot));

            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
/*********************** End of setting slider ********************************/




    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    public void catigoryes(View v){

        Intent intent = new Intent(this ,CatActivity.class);

        startActivity(intent);
    }


}


