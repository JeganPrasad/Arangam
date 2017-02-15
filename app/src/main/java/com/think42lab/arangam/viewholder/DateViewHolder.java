package com.think42lab.arangam.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.think42lab.arangam.R;

/**
 * Created by Nishanth on 12/18/2016.
 */
public class DateViewHolder extends RecyclerView.ViewHolder {

    public TextView txvDate;
    public DateViewHolder(View itemView) {
        super(itemView);
        txvDate = (TextView) itemView.findViewById(R.id.txvDate);
    }
}
