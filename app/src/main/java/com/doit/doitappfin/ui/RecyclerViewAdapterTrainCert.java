package com.doit.doitappfin.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;


import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.doit.doitappfin.R;
import com.doit.doitappfin.utils.certModel;


/**
 * A custom adapter to use with the RecyclerView widget.
 */



public class RecyclerViewAdapterTrainCert extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private ArrayList<certModel> modelList;

    private OnItemClickListener mItemClickListener;


    public RecyclerViewAdapterTrainCert(Context context, ArrayList<certModel> modelList) {
        this.mContext = context;
        this.modelList = modelList;
        System.out.println(modelList);
    }

    public void updateList(ArrayList<certModel> modelList) {
        this.modelList = modelList;
        notifyDataSetChanged();

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_recycler_list, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        //Here you can fill your row view
        if (holder instanceof ViewHolder) {
            final certModel model = getItem(position);
            ViewHolder genericViewHolder = (ViewHolder) holder;


        //    System.out.println(model.getTitle()+" "+model.getPic()+" "+model.getPrice());

            genericViewHolder.itemTxtTitle.setText(model.getTitle());
            genericViewHolder.itemTxtMessage.setText(model.getPrice());

           // Picasso.get().load(model.getImage()).networkPolicy(NetworkPolicy.OFFLINE).centerCrop().into( genericViewHolder.imgUser);



            RequestOptions myOptions = new RequestOptions()
                    .override(100, 100);

            if(!model.getImage().equals("data"))
            Glide.with(mContext).load(model.getImage()).fitCenter().override(1000,1000).into(genericViewHolder.imgUser);


        }


    }


    @Override
    public int getItemCount() {

        return  modelList.size();
    }

    public void SetOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    private certModel getItem(int position) {
        return modelList.get(position);
    }


    public interface OnItemClickListener {
        void onItemClick(View view, int position, certModel model);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView imgUser;
        private TextView itemTxtTitle;
        private TextView itemTxtMessage;


        // @BindView(R.id.img_user)
        // ImageView imgUser;
        // @BindView(R.id.item_txt_title)
        // TextView itemTxtTitle;
        // @BindView(R.id.item_txt_message)
        // TextView itemTxtMessage;
        // @BindView(R.id.radio_list)
        // RadioButton itemTxtMessage;
        // @BindView(R.id.check_list)
        // CheckBox itemCheckList;
        public ViewHolder(final View itemView) {
            super(itemView);

            // ButterKnife.bind(this, itemView);

            this.imgUser = itemView.findViewById(R.id.img_user);
            this.itemTxtTitle = itemView.findViewById(R.id.item_txt_title);
            this.itemTxtMessage = itemView.findViewById(R.id.item_txt_message);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mItemClickListener.onItemClick(itemView, getAdapterPosition(), modelList.get(getAdapterPosition()));


                }
            });

        }
    }

}

