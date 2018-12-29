package com.example.mrsnack.mrsnack.Tabs;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.mrsnack.mrsnack.Adapters.TabRecyclerAdapter;
import com.example.mrsnack.mrsnack.Modules.Product;
import com.example.mrsnack.mrsnack.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;

public class Drinks extends Fragment implements LoaderManager.LoaderCallbacks<ArrayList<Product>> {


    ProgressBar progressBar;
    RecyclerView recyclerView;
    LoaderManager loaderManager;

    FirebaseDatabase mFirebaseDatabase;
    DatabaseReference mRef;
    ArrayList<Product> productsArray = new ArrayList<Product>();
    private static final String DRINK_KEY ="drink";


    public static final int OPERATION_LOADER = 22;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.dinks_tab, container, false);

        loaderManager = getActivity().getSupportLoaderManager();
        recyclerView = (RecyclerView) view.findViewById(R.id.drinksRecyclerView);


        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mRef = mFirebaseDatabase.getReference("snacks");

        startLodader();
        return view;

    }


    public void startLodader() {

        Bundle queryBundel = new Bundle();

        Log.d("Loading", "start Loading");

        LoaderManager loaderManager = getLoaderManager();
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
        Log.d("Loading", "" + "create loader");

        return new AsyncTaskLoader<ArrayList<Product>>(getActivity().getApplicationContext()) {

            @Override
            protected void onStartLoading() {

                if (mRef == null) {
                    Log.d("Loading", "null mRef");

                    mFirebaseDatabase = FirebaseDatabase.getInstance();
                    mRef = mFirebaseDatabase.getReference("snacks");
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
                        productsArray.clear();
                        //iterating through all the nodes
                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                            //getting artist
                            Product artist = postSnapshot.getValue(Product.class);
                            //adding artist to the list
                            if(artist.getType().equals(DRINK_KEY)){
                            productsArray.add(new Product(artist.getPrice(), artist.getName()
                                    , artist.getImage(), artist.getWight(), artist.getType()));

                            Log.d("Loading", "Load in Back" + artist.getImage());

                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }

                });

                return productsArray;

            }

        };

    }



    @Override
    public void onLoadFinished(@NonNull Loader<ArrayList<Product>> loader, ArrayList<Product> products) {


        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext() , 2));

        // ProductsSamples samples = new ProductsSamples();

        recyclerView.stopNestedScroll();

        recyclerView.scrollToPosition(0);
        //creating adapter
        TabRecyclerAdapter tabRecyclerAdapter = new TabRecyclerAdapter(getContext(), productsArray);

        //attaching adapter to the listview
        recyclerView.setAdapter(tabRecyclerAdapter);


            }



    @Override
    public void onLoaderReset(@NonNull Loader<ArrayList<Product>> loader) {

    }
}
