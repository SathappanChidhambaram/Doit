package com.doit.doitappfin.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.doit.doitappfin.R;

import java.util.ArrayList;

public class RadioListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;

    private ArrayList<String> Address;
    private ArrayList<Float> Distance;

    private OnItemClickListener mItemClickListener;


    private int lastCheckedPosition = -1;


    public RadioListAdapter(Context context, ArrayList<String> data, ArrayList<Float> data1) {
        this.mContext = context;
        this.Address = data;
        this.Distance=data1;
    }

    public void updateList(ArrayList<String> addr,ArrayList<Float> dist) {
        this.Address = addr;
        this.Distance=dist;

        notifyDataSetChanged();

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_radio_list, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        //Here you can fill your row view
        if (holder instanceof ViewHolder) {
            ViewHolder genericViewHolder = (ViewHolder) holder;

           // genericViewHolder.itemTxtTitle.setText(model);
         //   genericViewHolder.itemTxtMessage.setText(model);

            if(Distance.size()>0) {
                if (Address.get(position) != null) {
                    String addr = Address.get(position);
                    Float dist = Distance.get(position);
                    genericViewHolder.itemTxtTitle.setText("Doit - " + addr);
                    String c = dist + "";
                    c = c.substring(0, 4);
                    genericViewHolder.itemTxtMessage.setText((c + " km").trim());

                    // System.out.println(Address);
                }
            }

            genericViewHolder.radioList.setChecked(position == lastCheckedPosition);

            genericViewHolder.radioList.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    lastCheckedPosition = position;
                    notifyItemRangeChanged(0, Address.size());

                    mItemClickListener.onItemClick(view, position, Address.get(position));

                }
            });


        }
    }


    @Override
    public int getItemCount() {

        return Address.size();
    }

    public void SetOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    private String getItem(int position) {
        return Address.get(position);
    }


    public interface OnItemClickListener {
        void onItemClick(View view, int position, String model);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView itemTxtTitle;
        private TextView itemTxtMessage;
        private RadioButton radioList;


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

            this.itemTxtTitle = itemView.findViewById(R.id.item_txt_title);
            this.itemTxtMessage = itemView.findViewById(R.id.item_txt_message);
            this.radioList = itemView.findViewById(R.id.radio_list);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    lastCheckedPosition = getAdapterPosition();
                    notifyItemRangeChanged(0, Address.size());
                    mItemClickListener.onItemClick(itemView, getAdapterPosition(), Address.get(getAdapterPosition()));


                }
            });

        }
    }

}