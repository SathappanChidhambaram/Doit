package com.doit.doitappfin.utils;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.doit.doitappfin.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.ViewHolder>  {

   // private ArrayList<Float> Distance;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private ArrayList examheading,clientheading;
    Integer lastSelectedItemPos;
    ArrayList<trainLocationObj> locobjHashmap;
    int selectedPosition=-1,first=1;
    // data is passed into the constructor
    public MyRecyclerViewAdapter(Context context, ArrayList<trainLocationObj> locobjHashmap) {
        this.mInflater = LayoutInflater.from(context);
        this.locobjHashmap=locobjHashmap;
//        this.Address = data;
  //      this.Distance = data1;




    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.recyclerview_row, parent, false);





        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        if(selectedPosition==position || first==1){
            holder.myTextViewaddr.setTextColor(Color.parseColor("#000000"));
            holder.myTextViewdist.setTextColor(Color.parseColor("#000000"));

        }
        else{
            holder.myTextViewaddr.setTextColor(Color.parseColor("#AAAAAA"));
            holder.myTextViewdist.setTextColor(Color.parseColor("#AAAAAA"));

        }

        if(locobjHashmap.size()>0) {
            if (locobjHashmap.get(position) != null )   {
                String addr = locobjHashmap.get(position).getLocation()+" DOIT-"+locobjHashmap.get(position).getLocid();
               // Float dist = Distance.get(position);
                holder.myTextViewaddr.setText("" + addr+"");
               String c = locobjHashmap.get(position).getY() + "0000000";
                c = c.substring(0, 4);
                holder.myTextViewdist.setText((c+" km").trim());
               // System.out.println(Address);


            }
        }








    }

    // total number of rows
    @Override
    public int getItemCount() {
        return locobjHashmap.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView myTextViewaddr,myTextViewdist;

        ViewHolder(View itemView) {
            super(itemView);
            myTextViewaddr = itemView.findViewById(R.id.addr);
            myTextViewdist = itemView.findViewById(R.id.dist);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
            selectedPosition=getAdapterPosition();
            first=0;
            notifyDataSetChanged();
        }



    }



    // convenience method for getting data at click position
    trainLocationObj getItem(int id) {
        return locobjHashmap.get(id);
    }

    // allows clicks events to be caught
    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

    public void UpdateItemsList(ArrayList<trainLocationObj> locobjHashmap)
    {
        this.locobjHashmap=locobjHashmap;
        notifyDataSetChanged();

    }

    class SubcategoryGetSet{
        //your other objects, getters and setters
        boolean selected;
        public boolean isSelected() { return selected; }
        public void setSelected(boolean selected) { this.selected = selected; }
    }

}