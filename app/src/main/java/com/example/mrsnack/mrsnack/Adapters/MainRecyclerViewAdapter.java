package com.example.mrsnack.mrsnack.Adapters;


import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.mrsnack.mrsnack.Modules.Product;
import com.example.mrsnack.mrsnack.ProductDetalis;
import com.example.mrsnack.mrsnack.R;

import java.util.ArrayList;

public class MainRecyclerViewAdapter extends RecyclerView.Adapter<MainRecyclerViewAdapter.ProductViewHolder>{

    private Context context;
    private ArrayList<Product> product;
    public static final String NAME = "name";
    public static final String IMAGE = "image";

    public MainRecyclerViewAdapter(Context context, ArrayList<Product> product) {
        this.context = context;
        this.product = product;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.main_recyclerview_layout , viewGroup,false);
        ProductViewHolder holder = new ProductViewHolder(view);



        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder productViewHolder, int i) {

        Product productRecycler = product.get(i);
        productViewHolder.name.setText(productRecycler.getName());
        productViewHolder.price.setText(Double.valueOf(productRecycler.getPrice()).toString());


        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.img_1)
                .error(R.drawable.img_3);


        Glide.with(context).load(product.get(i).getImage()).apply(options).into(productViewHolder.image);
        Log.d("image", product.get(i).getImage());

        productViewHolder.setItemOnClickListner(new RecyclerListner() {
            @Override
            public void onClick(View v, int position) {
                Intent intent = new Intent(context , ProductDetalis.class);

                intent.putExtra( NAME,product.get(position).getName());
                intent.putExtra( IMAGE,product.get(position).getImage());

                context.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return product.size();
    }

    class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        ImageView image;
        TextView name,price;
        RecyclerListner listner;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.recyclerImage);
            name = itemView.findViewById(R.id.productName);
            price = itemView.findViewById(R.id.productPrice);




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
