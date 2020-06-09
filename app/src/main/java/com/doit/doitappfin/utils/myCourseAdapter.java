package com.doit.doitappfin.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.doit.doitappfin.R;

import java.util.ArrayList;
public class myCourseAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    ArrayList<myCourseModel> model;
    ArrayList<String> date,course;
    private OnItemClickListener mItemClickListener;


    public myCourseAdapter(Context mContext, ArrayList<myCourseModel> model, ArrayList<String> date, ArrayList<String> course) {
        this.mContext = mContext;
        this.model = model;
        this.date = date;
        this.course = course;
        System.out.println(model.size());
        System.out.println(model);
    }

    public myCourseAdapter() {
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_recycler_mycourse, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ViewHolder) {
            final myCourseModel model1 = getItem(position);


            // System.out.println(list);
            System.out.println(model.get(position));
            System.out.println(date);
            ViewHolder genericViewHolder = (ViewHolder) holder;
            genericViewHolder.tit.setText(model1.getTitle());
            genericViewHolder.tim.setText(model1.getTime());
            genericViewHolder.amt.setText(model1.getAmmount());
            genericViewHolder.loc.setText(model1.getLocation());
            genericViewHolder.other.setText(model1.getOther());

            genericViewHolder.payid.setText(model1.getPaymentid());
            Glide.with(mContext)
                    .load(model1.getImage()).fitCenter().override(1000,1000)
                    .into(genericViewHolder.imageView);


            //    System.out.println(model.getTitle()+" "+model.getPic()+" "+model.getPrice());

        }

    }

    @Override
    public int getItemCount() {

        return date.size();
    }

    public void SetOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    private myCourseModel getItem(int position) {
        return model.get(position);
    }


    public interface OnItemClickListener {
        void onItemClick(View view, int position, myCourseModel model);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView amt,loc,other,payid,tit,tim;
        ImageView imageView;
        public ViewHolder(final View itemView) {
            super(itemView);
            amt=(TextView) itemView.findViewById(R.id.amount);
            tim=(TextView) itemView.findViewById(R.id.time);
            tit=(TextView) itemView.findViewById(R.id.mtitle);
            loc=(TextView) itemView.findViewById(R.id.location);
            other=(TextView) itemView.findViewById(R.id.other);
            payid=(TextView) itemView.findViewById(R.id.paymentId);
            imageView=(ImageView) itemView.findViewById(R.id.pic);





        }



    }


}