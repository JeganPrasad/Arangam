package com.think42lab.arangam.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.think42lab.arangam.R;

/**
 * Created by Nishanth on 12/18/2016.
 */
public class SegmentGeneralViewHolder extends RecyclerView.ViewHolder {

    public TextView iconLetter;
    public TextView venue;
    public TextView accompanists;
    public TextView segmentDateTime;
    public ImageView favourite;
    public ImageView uber;
    public ImageView ticket;
    public View favouriteLayout, uberLayout, ticketLayout, view;


    public SegmentGeneralViewHolder(View itemView) {
        super(itemView);
        view = itemView;
        iconLetter = (TextView) itemView.findViewById(R.id.iconLetter);
        venue = (TextView) itemView.findViewById(R.id.venue);
        accompanists = (TextView) itemView.findViewById(R.id.accompanists);
        segmentDateTime = (TextView) itemView.findViewById(R.id.dateTime);
        //this.imageView=(ImageView)itemView.findViewById(R.id.icon);
        favourite = (ImageView) itemView.findViewById(R.id.favourite);
        //this.isPressed=(Boolean)itemView.findViewById(R.id.imageView3);
        uber = (ImageView) itemView.findViewById(R.id.uber);
        ticket = (ImageView) itemView.findViewById(R.id.ticket);

        favouriteLayout = itemView.findViewById(R.id.favouriteLayout);
        uberLayout = itemView.findViewById(R.id.uberLayout);
        ticketLayout = itemView.findViewById(R.id.ticketLayout);
    }
}
