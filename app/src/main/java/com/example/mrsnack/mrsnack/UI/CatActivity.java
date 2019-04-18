package com.example.mrsnack.mrsnack;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import com.example.mrsnack.mrsnack.Modules.Product;
import com.example.mrsnack.mrsnack.Tabs.Drinks;
import com.example.mrsnack.mrsnack.Tabs.Salty;
import com.example.mrsnack.mrsnack.Tabs.Sweets;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CatActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<ArrayList<Product>>{

    static ArrayList<Product> drinksArray =  new ArrayList<Product>();
    static ArrayList<Product> sweetsArray =  new ArrayList<Product>();
    static ArrayList<Product> saltsArray  =  new ArrayList<Product>();

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mRef;

    private static final String DRINK_KEY = "drink";
    private static final String SWEET_KEY = "sweet";
    private static final String SALT_KEY = "salt";
    private static final String TABLE_NAME = "snacks";
    public static final int OPERATION_LOADER = 22;
    private LoaderManager loaderManager;



    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cat);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getApplicationContext() , Cart.class);

                startActivity(intent);


            }
        });

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mRef = mFirebaseDatabase.getReference(TABLE_NAME);

        loaderManager = getSupportLoaderManager();

        startLodader();



    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_cat, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void startLodader() {

        Bundle queryBundel = new Bundle();

        Log.d("Loading", "start Loading");

        LoaderManager loaderManager = getSupportLoaderManager();
        Loader<ArrayList<Product>> loader = loaderManager.getLoader(OPERATION_LOADER);

        if (loader == null) {
            loaderManager.initLoader(OPERATION_LOADER, queryBundel, this);
        } else {
            loaderManager.restartLoader(OPERATION_LOADER, queryBundel, this);
        }


    }



    @NonNull
    @Override
    public Loader<ArrayList<Product>> onCreateLoader(int i, @Nullable Bundle bundle) {
        return new AsyncTaskLoader<ArrayList<Product>>(getApplicationContext()) {

            @Override
            protected void onStartLoading() {

                if (mRef == null) {
                    Log.d("Loading", "null mRef");

                    mFirebaseDatabase = FirebaseDatabase.getInstance();
                    mRef = mFirebaseDatabase.getReference(TABLE_NAME);
                }

                //setProgressBar to visible

                forceLoad();

            }

            @Nullable
            @Override
            public ArrayList<Product> loadInBackground() {
                final ArrayList<Product> productsList = new ArrayList<Product>();

                Log.d("Loading", "Load in Back");

                mRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        //clearing the previous artist list
                        drinksArray.clear();
                        sweetsArray.clear();
                        saltsArray.clear();



                        //iterating through all the nodes
                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                            //getting artist
                            Product artist = postSnapshot.getValue(Product.class);
                            //adding artist to the list
                            if(artist.getType().equals(DRINK_KEY)){
                                drinksArray.add(new Product(artist.getPrice(), artist.getName()
                                        , artist.getImage(), artist.getWight(), artist.getType()));

                            }else if(artist.getType().equals(SWEET_KEY)){
                                sweetsArray.add(new Product(artist.getPrice(), artist.getName()
                                        , artist.getImage(), artist.getWight(), artist.getType()));

                            }else if(artist.getType().equals(SALT_KEY)){
                                saltsArray.add(new Product(artist.getPrice(), artist.getName()
                                        , artist.getImage(), artist.getWight(), artist.getType()));

                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }

                });

                return drinksArray;

            }

        };
    }

    @Override
    public void onLoadFinished(@NonNull Loader<ArrayList<Product>> loader, ArrayList<Product> products) {


        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        mSectionsPagerAdapter.getItem(getIntent().getIntExtra("choice",0));

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));


        setDrinksArray(drinksArray);
        setSaltsArray(saltsArray);
        setSweetsArray(sweetsArray);


    }

    @Override
    public void onLoaderReset(@NonNull Loader<ArrayList<Product>> loader) {

    }

    public ArrayList<Product> getDrinksArray() {
        return drinksArray;
    }

    public void setDrinksArray(ArrayList<Product> drinksArray) {
        this.drinksArray = drinksArray;
    }

    public ArrayList<Product> getSweetsArray() {
        return sweetsArray;
    }

    public void setSweetsArray(ArrayList<Product> sweetsArray) {
        this.sweetsArray = sweetsArray;
    }

    public ArrayList<Product> getSaltsArray() {
        return saltsArray;
    }

    public void setSaltsArray(ArrayList<Product> saltsArray) {
        this.saltsArray = saltsArray;
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).


            switch (position){
                case 0:
                    Drinks drinks = Drinks.getInstance(getDrinksArray());
                    return drinks;
                case 1:
                    Salty salts = Salty.getInstance(getSaltsArray());
                    return salts;
                case 2:
                    Sweets sweets = Sweets.getInstance(getSweetsArray());
                    return sweets;
                default:
                    return null;
            }
       }



        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }
    }
}
