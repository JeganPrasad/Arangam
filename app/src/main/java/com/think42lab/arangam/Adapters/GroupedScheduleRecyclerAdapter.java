package com.think42lab.arangam.Adapters;

import android.content.Intent;
import android.graphics.Color;
import android.os.Parcelable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.think42lab.arangam.GroupActivity;
import com.think42lab.arangam.NotificationListener;
import com.think42lab.arangam.R;
import com.think42lab.arangam.TicketBookingActivity;
import com.think42lab.arangam.model.DateItem;
import com.think42lab.arangam.model.ListItem;
import com.think42lab.arangam.model.Segment;
import com.think42lab.arangam.util.DateUtil;
import com.think42lab.arangam.util.PreferenceUtil;
import com.think42lab.arangam.viewholder.DateViewHolder;
import com.think42lab.arangam.viewholder.SegmentGeneralViewHolder;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nishanth on 12/18/2016.
 */
public class GroupedScheduleRecyclerAdapter
        extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public interface ActionListener {
        public void onFavorite(Segment segment);

        public void onUnFavorite(Segment segment);

        public void onUber(Segment segment);

        public void onBook();
    }


    public List<ListItem> consolidatedList = new ArrayList<>();

    private ActionListener listener;
    int color = Color.parseColor("#F5F5F5");
    int color1 = Color.parseColor("#CD333B");
    //private Activity activity;

    public GroupedScheduleRecyclerAdapter(List<ListItem> consolidatedList, ActionListener listener) {
        this.consolidatedList = consolidatedList;
        this.listener = listener;
    }

    public void setFilter(List<ListItem> filteredModelList) {
        consolidatedList = new ArrayList<>();
        consolidatedList.addAll(filteredModelList);
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return consolidatedList.get(position).getType();
    }

    @Override
    public int getItemCount() {
        return consolidatedList != null ? consolidatedList.size() : 0;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                      int viewType) {

        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {

            case ListItem.TYPE_GENERAL:
                View v1 = inflater.inflate(R.layout.row_layout_schedule, parent,
                        false);
                viewHolder = new SegmentGeneralViewHolder(v1);
                break;

            case ListItem.TYPE_DATE:
                View v2 = inflater.inflate(R.layout.section_header, parent, false);
                viewHolder = new DateViewHolder(v2);
                break;
        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder viewHolder,
                                 final int position) {

        switch (viewHolder.getItemViewType()) {

            case ListItem.TYPE_GENERAL:
                final Segment generalItem
                        = (Segment) consolidatedList.get(position);
                final SegmentGeneralViewHolder holder
                        = (SegmentGeneralViewHolder) viewHolder;

                holder.view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent in = new Intent(v.getContext(), GroupActivity.class);

                        in.putExtra("venueId", generalItem.getVenues().getId());
                        in.putExtra("venueName", generalItem.getVenues().getName());

                        v.getContext().startActivity(in);

                    }
                });

                // Populate general item data here
                holder.iconLetter.setText(getVenueName(generalItem.getVenues().getName()));

                holder.iconLetter.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent in = new Intent(v.getContext(), GroupActivity.class);

//                        in.putParcelableArrayListExtra("segments", (ArrayList<? extends Parcelable>) consolidatedList);
                        in.putExtra("venueId", generalItem.getVenues().getId());
                        in.putExtra("venueName", generalItem.getVenues().getName());

                        v.getContext().startActivity(in);
                    }
                });

                holder.venue.setText((generalItem.getArtists()!=null)? generalItem.getArtists().getName() : "");
                holder.accompanists.setText(generalItem.getAccompanists());

                if (TextUtils.isEmpty(generalItem.getAccompanists())) {
                    holder.accompanists.setVisibility(View.GONE);
                } else {
                    holder.accompanists.setVisibility(View.VISIBLE);
                }


                String name = DateUtil.convertDateObjectToString(
                        DateUtil.convertStringToCalendar(generalItem.getSegmentDate(), "dd.MM.yy").getTime(),
                        "d");

                String dateth = DateUtil.convertDateObjectToString(
                        DateUtil.convertStringToCalendar(generalItem.getSegmentDate(), "dd.MM.yy").getTime(),
                        "d") + DateUtil.getDayOfMonthSuffix(Integer.parseInt(name));

                holder.segmentDateTime.setText(dateth + " " + DateUtil.convertDateObjectToString(
                        DateUtil.convertStringToCalendar(generalItem.getSegmentDate(), "dd.MM.yy").getTime(),
                        "MMMM yyyy") + ", " + generalItem.getSegmentTime());

                Log.e("Boolean status", "onBindViewHolder: " + generalItem.isFavorite());
                /*boolean a = generalItem.isFavorite();
                if (!a) {
                    holder.favourite.setImageResource(R.mipmap.star);
                } else {
                    holder.favourite.setImageResource(R.mipmap.star_selected);
                }*/

                boolean favoriteStatus = PreferenceUtil.getFavoriteStatus(holder.favourite.getContext(), generalItem);
                if (favoriteStatus) {
                    holder.favourite.setImageResource(R.mipmap.star_selected);
                } else {
                    holder.favourite.setImageResource(R.mipmap.star);
                }


                holder.favouriteLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //boolean a = generalItem.isFavorite();

//                        if (a) {
//                            generalItem.setIsFavorite(!a);
//                            listener.onUnFavorite(generalItem);
//                            notifyItemChanged(position);
//
//                        } else {
//                            generalItem.setIsFavorite(!a);
//                            listener.onFavorite(generalItem);
//                            notifyItemChanged(position);
//                        }
                        PreferenceUtil.updateFavoriteStatus(holder.favouriteLayout.getContext(), generalItem, listener);
                        notifyItemChanged(position);



//                        listener.onFavorite(generalItem);

                    }
                });

                holder.ticketLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent ticketBookingIntent = new Intent(holder.ticketLayout.getContext(), TicketBookingActivity.class);
                        ticketBookingIntent.putExtra("segment", generalItem);
                        holder.ticketLayout.getContext().startActivity(ticketBookingIntent);

                    }
                });

                holder.uber.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listener.onUber(generalItem);
                    }
                });


                break;

            case ListItem.TYPE_DATE:
                DateItem dateItem
                        = (DateItem) consolidatedList.get(position);
                DateViewHolder dateViewHolder
                        = (DateViewHolder) viewHolder;

                // Populate date item data here

                dateViewHolder.txvDate.setText(DateUtil.convertDateObjectToString(
                        DateUtil.convertStringToCalendar(dateItem.getDate(), "dd.MM.yy").getTime(),
                        "dd MMMM yyyy"));
                break;
        }
    }

    private String getVenueName(String venuName) {
        StringBuilder stringBuilder = new StringBuilder();
        if (!TextUtils.isEmpty(venuName)) {
            String[] nameArray = venuName.split(" ");
            for (String name : nameArray) {
                stringBuilder.append(name.substring(0, 1).toUpperCase());
            }
        }
        return stringBuilder.toString();
    }

}
