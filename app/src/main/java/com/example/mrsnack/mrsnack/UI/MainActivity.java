package com.example.mrsnack.mrsnack;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;


import com.example.mrsnack.mrsnack.Adapters.MainRecyclerViewAdapter;
import com.example.mrsnack.mrsnack.Adapters.SlideAdapter;
import com.example.mrsnack.mrsnack.Modules.Product;
import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;


public class MainActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<ArrayList<Product>> {


    public static final int RC_SIGN_IN = 1;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListner;
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
    LinearLayout centerLinear;

    ArrayList<Product> products = new ArrayList<Product>();
    private final int LOADER = 20;
    private final String TABLE_NAME = "snacks";
    ProgressBar progressBar;
    CardView drink,sweet,salty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sliderDots = (LinearLayout) findViewById(R.id.sliderDots);
        recyclerView = findViewById(R.id.main_recyclerview);
        viewPager = findViewById(R.id.viewPager);
        centerLinear = (LinearLayout) findViewById(R.id.centerLinear);

        drink = (CardView) findViewById(R.id.drinksChoice);
        sweet = (CardView) findViewById(R.id.sweetChoice);
        salty = (CardView) findViewById(R.id.saltyChoice);
        // ****************** //setting click direction
        drink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startCat(0);
            }
        });

        sweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startCat(1);
            }
        });

        salty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startCat(2);
            }
        });






        // ***************** //



        mAuth = FirebaseAuth.getInstance();

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mRef = mFirebaseDatabase.getReference(TABLE_NAME);

        progressBar = (ProgressBar) findViewById(R.id.progress);

        progressBar.setVisibility(View.VISIBLE);


        getSupportLoaderManager().initLoader(LOADER, null, this);
        startLodader();

        mAuthListner = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = mAuth.getCurrentUser();
                if(user !=null){
                    //user Logeed in
                    Toast.makeText(getApplicationContext() , "USER in" , Toast.LENGTH_SHORT);
                }else{
                    //user Not Logen in

                    startActivityForResult(
                            AuthUI.getInstance()
                                    .createSignInIntentBuilder()
                                    .setIsSmartLockEnabled(true)
                                    .setAvailableProviders(Arrays.asList(
                                            new AuthUI.IdpConfig.GoogleBuilder().build(),
                                            new AuthUI.IdpConfig.EmailBuilder().build()

                                          ))
                                    .build(),
                            RC_SIGN_IN);
                }

            }
        };
        //set up image slider
    }

    @Override
    protected void onResume() {
        super.onResume();
        mAuth.addAuthStateListener(mAuthListner);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mAuth.removeAuthStateListener(mAuthListner);
    }

    public void startCat(int i){

        Intent intent = new Intent( getApplicationContext(), CatActivity.class);
        intent.putExtra("choice",i);
        startActivity(intent);
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
                        setRecycler(products);

                    }


                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }



                });

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
                  progressBar.setVisibility(View.GONE);
                  centerLinear.setVisibility(View.VISIBLE);
                  recyclerView.setVisibility(View.VISIBLE);

              }
            }

    }
    public void setRecycler(ArrayList<Product> productsArray){
        if (!products.isEmpty()) {
            Log.d("Loading" , "after load");
            if(!dotSets){
                startView(productsArray);
                dotSets = true;
                progressBar.setVisibility(View.GONE);
                centerLinear.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<ArrayList<Product>> loader) {
    }
}


