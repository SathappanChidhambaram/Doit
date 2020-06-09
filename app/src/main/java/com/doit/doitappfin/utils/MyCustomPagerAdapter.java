package com.doit.doitappfin.utils;

import  android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;


import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.doit.doitappfin.R;

import java.util.ArrayList;

public class MyCustomPagerAdapter extends PagerAdapter {
    Context context;
    ArrayList<adboxmodel> model;
    LayoutInflater layoutInflater;


    public MyCustomPagerAdapter(Context context,  ArrayList<adboxmodel> model) {
        this.context = context;
        this.model = model;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return model.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View itemView = layoutInflater.inflate(R.layout.item, container, false);

        ImageView imageView = itemView.findViewById(R.id.imageView);
       //
        //imageView.setImageResource();
        Glide.with(context).load(model.get(position).getImage()).into(imageView);

        container.addView(itemView);

        //listening to image click
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }
}