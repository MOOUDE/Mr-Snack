package com.example.mrsnack.mrsnack.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import com.example.mrsnack.mrsnack.Modules.Product;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ItemsAdapter {
    final ArrayList<Product> products = new ArrayList<Product>();
    private RecyclerView recyclerView;

    FirebaseDatabase mFirebaseDatabase;
    DatabaseReference mRef;

    public ItemsAdapter(){}
    public ItemsAdapter(final Context context, final RecyclerView recyclerView) {

        this.recyclerView = recyclerView;


        if (FirebaseDatabase.getInstance() != null) {
            FirebaseDatabase.getInstance().goOffline();
        }

        FirebaseDatabase.getInstance().goOnline();

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mRef = mFirebaseDatabase.getReference("snacks");

        Log.d("Key is : ", mRef.getKey());


        /***************/
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
                    products.add(artist);
                }

                Log.d("size","prod size : "+products.size());

                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new LinearLayoutManager(context));

                // ProductsSamples samples = new ProductsSamples();

                recyclerView.stopNestedScroll();

                recyclerView.scrollToPosition(0);
                //creating adapter
                MainRecyclerViewAdapter adapter = new MainRecyclerViewAdapter(context, products);
                //attaching adapter to the listview
                recyclerView.setAdapter(adapter);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }


        });

        /*************/

    }
    public ArrayList<Product> getProducts(){
        return products;
    }
}



