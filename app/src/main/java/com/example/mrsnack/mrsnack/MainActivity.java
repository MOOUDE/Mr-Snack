package com.example.mrsnack.mrsnack;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;


import com.example.mrsnack.mrsnack.Adapters.MainRecyclerViewAdapter;
import com.example.mrsnack.mrsnack.Adapters.SlideAdapter;
import com.example.mrsnack.mrsnack.Modules.Product;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<ArrayList<Product>> {

    private ViewPager viewPager;
    private SlideAdapter mSlideAdapter;
    LinearLayout sliderDots;
    private int dotsCount;
    private ImageView[] dots;
    RecyclerView recyclerView;
    FirebaseDatabase mFirebaseDatabase;
    DatabaseReference mRef;
    View view;
    boolean dotSets = false;

    ArrayList<Product> products = new ArrayList<Product>();
    private final int LOADER = 20;
    private final String TABLE_NAME = "snacks";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sliderDots = (LinearLayout) findViewById(R.id.sliderDots);
        recyclerView = findViewById(R.id.main_recyclerview);
        viewPager = findViewById(R.id.viewPager);

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mRef = mFirebaseDatabase.getReference(TABLE_NAME);

        getSupportLoaderManager().initLoader(LOADER, null, this);
        startLodader();

        //set up image slider
    }
    public void startLodader() {


        Bundle queryBundel = new Bundle();

        Log.d("Loading", "start Loading");
        LoaderManager loaderManager = getSupportLoaderManager();

        Loader<ArrayList<Product>> loader = loaderManager.getLoader(LOADER);

        if (loader == null) {
            loaderManager.initLoader(LOADER, queryBundel, this);
        } else {
            loaderManager.restartLoader(LOADER, queryBundel, this);
        }
    }




    @Override
    protected void onStart() {
        super.onStart();
        startLodader();
    }

    public void startView(ArrayList<Product> productsArray) {

        mSlideAdapter = new SlideAdapter(this, productsArray);

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
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

            // ProductsSamples samples = new ProductsSamples();

            recyclerView.stopNestedScroll();

            recyclerView.scrollToPosition(0);
            //creating adapter
            MainRecyclerViewAdapter adapter
                    = new MainRecyclerViewAdapter(getBaseContext(), products);
            //attaching adapter to the listview
            recyclerView.setAdapter(adapter);
            dotSets = true;
        }



    public void catigoryes(View v){

        Intent intent = new Intent(this ,CatActivity.class);

        startActivity(intent);
    }


    @NonNull
    @Override
    public Loader<ArrayList<Product>> onCreateLoader(int i, @Nullable Bundle bundle) {
        return new AsyncTaskLoader<ArrayList<Product>>(this) {

            @Override
            protected void onStartLoading() {
                products.clear();

                forceLoad();
            }

            @Nullable
            @Override
            public ArrayList<Product> loadInBackground() {
                Log.d("Loading", "Load in Back");
                products.clear();
                mRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        //clearing the previous artist list
                        products.clear();

                        //iterating through all the nodes
                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                            //getting artist
                            Product artist = postSnapshot.getValue(Product.class);
                            //adding artist to the list

                            products.add(new Product(artist.getPrice(), artist.getName()
                                    , artist.getImage(), artist.getWight(), artist.getType()));

                        }
                    }


                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }



                });
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return products;
            }

        };
    }

    @Override
    public void onLoadFinished(@NonNull Loader<ArrayList<Product>> loader, ArrayList<Product> products) {
        if (!products.isEmpty()) {
           Log.d("Loading" , "after load");
              if(!dotSets){
                  startView(products);
                  dotSets = true;
              }
            }

    }

    @Override
    public void onLoaderReset(@NonNull Loader<ArrayList<Product>> loader) {
    }
}


