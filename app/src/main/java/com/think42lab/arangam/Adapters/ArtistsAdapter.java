package com.think42lab.arangam.Adapters;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.think42lab.arangam.ArtistsGroupActivity;
import com.think42lab.arangam.R;
import com.think42lab.arangam.fastscroll.FastScrollRecyclerViewInterface;
import com.think42lab.arangam.model.Artists;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by flaviusmester on 23/02/15.
 */
public class ArtistsAdapter extends RecyclerView.Adapter<ArtistsAdapter.ViewHolder> implements FastScrollRecyclerViewInterface {
    private ArrayList<Artists> mDataset;
    private HashMap<String, Integer> mMapIndex;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView mTextView;

        public ViewHolder(TextView v) {
            super(v);
            mTextView = v;
        }
    }

    public void filter(ArrayList<Artists> filterlist) {
        mDataset = filterlist;
        notifyDataSetChanged();
    }

    public void setFilter(List<Artists> filteredModelList) {
        mDataset = new ArrayList<>();
        mDataset.addAll(filteredModelList);
        notifyDataSetChanged();
    }


    // Provide a suitable constructor (depends on the kind of dataset)
    public ArtistsAdapter(ArrayList<Artists> myDataset, HashMap<String, Integer> mapIndex) {
        mDataset = myDataset;
        mMapIndex = mapIndex;
    }

    public void upDateList(ArrayList<Artists> myDataset, HashMap<String, Integer> mapIndex) {
        mDataset = myDataset;
        mMapIndex = mapIndex;
        notifyDataSetChanged();
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.my_text_view, parent, false);
        // set the view's size, margins, paddings and layout parameters
//        ...
        ViewHolder vh = new ViewHolder((TextView) v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.mTextView.setText(mDataset.get(position).getName());

        holder.mTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(v.getContext(), ArtistsGroupActivity.class);
                in.putExtra("artist", mDataset.get(position));
                v.getContext().startActivity(in);
            }
        });

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    @Override
    public HashMap<String, Integer> getMapIndex() {
        return this.mMapIndex;
    }
}