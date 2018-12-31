package com.example.mrsnack.mrsnack.Tabs;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.mrsnack.mrsnack.Adapters.TabRecyclerAdapter;
import com.example.mrsnack.mrsnack.Modules.Product;
import com.example.mrsnack.mrsnack.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import java.util.ArrayList;

public class Drinks extends Fragment{



    RecyclerView recyclerView;

    ArrayList<Product> drinksArray;
    private static final String BUNDEL_KEY ="Products";



    public static Drinks getInstance(ArrayList<Product> products){
        Drinks frag = new Drinks();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(BUNDEL_KEY,products);
        frag.setArguments(bundle);
        return frag;

    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.dinks_tab, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.drinksRecyclerView);

        Bundle bundle = this.getArguments();

        if (bundle != null) {
            drinksArray = bundle.getParcelableArrayList(BUNDEL_KEY);
        }


        return view;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext() , 2));

        // ProductsSamples samples = new ProductsSamples();

        recyclerView.stopNestedScroll();

        recyclerView.scrollToPosition(0);
        //creating adapter
        TabRecyclerAdapter tabRecyclerAdapter = new TabRecyclerAdapter( getContext() ,drinksArray );

        //attaching adapter to the listview
        recyclerView.setAdapter(tabRecyclerAdapter);


    }


}
