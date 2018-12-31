package com.example.mrsnack.mrsnack.Tabs;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mrsnack.mrsnack.Adapters.TabRecyclerAdapter;
import com.example.mrsnack.mrsnack.Modules.Product;
import com.example.mrsnack.mrsnack.R;

import java.util.ArrayList;

public class Salty extends Fragment {

    RecyclerView recyclerView;
    ArrayList<Product> saltsArray;

    private static final String SALT_KEY ="SALT";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.salty_tab , container ,false);


        recyclerView = (RecyclerView) view.findViewById(R.id.saltRecyclerView);

        Bundle bundle = this.getArguments();

        if (bundle != null) {
            saltsArray = bundle.getParcelableArrayList(SALT_KEY);
        }


        return view;

    }


    public static Salty getInstance(ArrayList<Product> products){
        Salty frag = new Salty();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(SALT_KEY,products);
        frag.setArguments(bundle);
        return frag;
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
        TabRecyclerAdapter tabRecyclerAdapter = new TabRecyclerAdapter( getContext() ,saltsArray );

        //attaching adapter to the listview
        recyclerView.setAdapter(tabRecyclerAdapter);


    }


}
