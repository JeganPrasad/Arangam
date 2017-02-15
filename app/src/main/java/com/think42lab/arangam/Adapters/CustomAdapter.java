package com.think42lab.arangam.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.think42lab.arangam.DataModel;
import com.think42lab.arangam.R;

import java.util.ArrayList;

/**
 * Created by Lenovo on 12/2/2016.
 */

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {

    private ArrayList<DataModel> dataSet;

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView textViewName;
        TextView textViewdate;
        TextView imageViewprice;
        Button book;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.textViewName = (TextView) itemView.findViewById(R.id.textView4);
            this.textViewdate = (TextView) itemView.findViewById(R.id.textView2);
            this.imageViewprice = (TextView) itemView.findViewById(R.id.test);
        }
    }

    public CustomAdapter(ArrayList<DataModel> data) {
        this.dataSet = data;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent,
                                           int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_layout, parent, false);


//        view.setOnClickListener(MainActivity.myOnClickListener);

        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {


        DataModel dataModel = dataSet.get(listPosition);
        holder.textViewName.setText(dataModel.getName());
        //holder.textViewName.setTextColor(Integer.parseInt("#808080"));
        holder.imageViewprice.setText(dataModel.getPrice());
        // holder.imageViewprice.setTextColor(Integer.parseInt("#ADADAD"));
        holder.textViewdate.setText(dataModel.getDate());
        // holder.textViewdate.setTextColor(Integer.parseInt("#CECECE"));
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }
}