package com.example.mrsnack.mrsnack.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.transition.Slide;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.mrsnack.mrsnack.Modules.Product;
import com.example.mrsnack.mrsnack.R;

import java.util.ArrayList;

public class SlideAdapter extends PagerAdapter {
    Context context;
    LayoutInflater inflater;
    ArrayList<Product> products;


    public int[] imgs_lst = {
            R.drawable.img_1,
            R.drawable.img_2,
            R.drawable.img_3,
            R.drawable.img_4

    };
    public String[] titles = {
            "Pop cake",
            "Choclate",
            "Backing",
            "Drink"

    };

    public SlideAdapter(Context context , ArrayList<Product> products){
        this.context = context;
        this.products = products;

    }

    @Override
    public int getCount() {

        return titles.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {


        return (view ==(LinearLayout)o);
    }

    RequestOptions options = new RequestOptions()
            .centerCrop()
            .placeholder(R.drawable.img_1)
            .error(R.drawable.img_2);




    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.slide , container , false);
        LinearLayout layout = (LinearLayout) view.findViewById(R.id.sliderLayout);
        ImageView slideImage = (ImageView) view.findViewById(R.id.slideImage);
        TextView title = (TextView) view.findViewById(R.id.slideTitle);



        Glide.with(context).load(products.get(position).getImage()).apply(options).into(slideImage);

        slideImage.setImageResource(imgs_lst[position]);
        Log.d("position","the posis"+imgs_lst[position]);
        title.setText(titles[position]);
        container.addView(view);
        return view;

    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
    container.removeView((LinearLayout)object);

    }
}
