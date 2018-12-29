package com.example.mrsnack.mrsnack.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.mrsnack.mrsnack.Modules.Product;
import com.example.mrsnack.mrsnack.ProductDetalis;
import com.example.mrsnack.mrsnack.R;

import java.util.ArrayList;

import static com.example.mrsnack.mrsnack.Adapters.MainRecyclerViewAdapter.IMAGE;
import static com.example.mrsnack.mrsnack.Adapters.MainRecyclerViewAdapter.NAME;

public class TabRecyclerAdapter extends RecyclerView.Adapter<TabRecyclerAdapter.TabsRecyclerViewHolder>{

    private Context context;
    private ArrayList<Product> products;

    public TabRecyclerAdapter(Context context , ArrayList<Product> products){
        this.context = context;
        this.products = products;
    }



    @NonNull
    @Override
    public TabsRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.tab_recycler_element , viewGroup,false);
        TabsRecyclerViewHolder holder = new TabsRecyclerViewHolder(view);



        return holder;

    }

    @Override
    public void onBindViewHolder(@NonNull TabsRecyclerViewHolder tabsRecyclerViewHolder, int i) {

        tabsRecyclerViewHolder.name.setText(products.get(i).getName());
        tabsRecyclerViewHolder.price.setText(Double.valueOf(products.get(i).getPrice()).toString());

        Glide.with(context).load(products.get(i).getImage()).into(tabsRecyclerViewHolder.image);

        tabsRecyclerViewHolder.setItemOnClickListner(new RecyclerListner() {
            @Override
            public void onClick(View v, int position) {
                Intent intent = new Intent(context , ProductDetalis.class);

                intent.putExtra( NAME,products.get(position).getName());
                intent.putExtra( IMAGE,products.get(position).getImage());

                context.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    class TabsRecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        ImageView image;
        TextView name,price;
        RecyclerListner listner;

        public TabsRecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.tabImage);
            name = itemView.findViewById(R.id.snackName);
            price = itemView.findViewById(R.id.snackPrice);




            itemView.setOnClickListener(this);

        }

        public void setItemOnClickListner(RecyclerListner listner){
            this.listner = listner;

        }

        @Override
        public void onClick(View v) {
            listner.onClick(v, getAdapterPosition());

        }
    }
    }

