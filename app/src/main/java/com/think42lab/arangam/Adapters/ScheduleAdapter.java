package com.think42lab.arangam.Adapters;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

//import com.amulyakhare.textdrawable.TextDrawable;
//import com.amulyakhare.textdrawable.util.ColorGenerator;

import com.think42lab.arangam.R;
import com.think42lab.arangam.TicketBookingActivity;
import com.think42lab.arangam.model.Segment;
import com.think42lab.arangam.model.Venues;
import com.think42lab.arangam.util.DateUtil;
import com.think42lab.arangam.util.Mixp;
import com.think42lab.arangam.util.PreferenceUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lenovo on 12/2/2016.
 */

public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.MyViewHolder> {

    private boolean isFromFavoriteFragment;

    public boolean isFromFavoriteFragment() {
        return isFromFavoriteFragment;
    }

    public void setFromFavoriteFragment(boolean fromFavoriteFragment) {
        isFromFavoriteFragment = fromFavoriteFragment;
    }

    public void filter(ArrayList<Segment> filterlist) {
        dataSet = filterlist;
        notifyDataSetChanged();
    }

    public void setFilter(List<Segment> filteredModelList) {
        dataSet = new ArrayList<>();
        dataSet.addAll(filteredModelList);
        notifyDataSetChanged();
    }

    public interface ActionListener {
        public void onFavorite(Segment segment);

        public void onUnFavorite(Segment segment);

        public void onUber(Segment segment);

        public void onBook();
    }

    private ActionListener listener;
    private List<Segment> dataSet = new ArrayList<>();
    int color = Color.parseColor("#F5F5F5");
    int color1 = Color.parseColor("#CD333B");
    //private Activity activity;

    public void UpdateList(List<Segment> dataSet) {
        this.dataSet = dataSet;
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        final TextView iconLetter;
        final TextView venue;
        final TextView accompanists;
        final TextView segmentDateTime;
        final ImageView favourite;
        final ImageView uber;
        final ImageView ticket;
        final View favouriteLayout, uberLayout, ticketLayout;

        // public View testview;

        public MyViewHolder(View itemView) {
            super(itemView);
            //    testview=itemView;
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


    public ScheduleAdapter(List<Segment> data, ActionListener listener) {
        this.dataSet = data;
        this.listener = listener;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent,
                                           int viewType) {
        View view;

        view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_layout_schedule, parent, false);

//        view.setOnClickListener(MainActivity.myOnClickListener);

        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {

        final Segment segment = dataSet.get(listPosition);

        // String firstletter = Character.toString(dataModel.getName().charAt(0));
        //String color = "#CC955F";
        //TextDrawable drawable = TextDrawable.builder().buildRound(firstletter,Integer.parseInt(color));
        //ColorGenerator gen = ColorGenerator.MATERIAL;
        //int color = gen.

//        Venues venues=segment.getVenues().getName();

        holder.iconLetter.setText(getVenueName(dataSet.get(listPosition).getVenues().getName()));

        holder.venue.setText(dataSet.get(listPosition).getArtists().getName());
        holder.accompanists.setText(segment.getAccompanists());

        if (TextUtils.isEmpty(segment.getAccompanists())) {
            holder.accompanists.setVisibility(View.GONE);
        } else {
            holder.accompanists.setVisibility(View.VISIBLE);
        }

        String name = DateUtil.convertDateObjectToString(
                DateUtil.convertStringToCalendar(segment.getSegmentDate(), "dd.MM.yy").getTime(),
                "d");

        String dateth = DateUtil.convertDateObjectToString(
                DateUtil.convertStringToCalendar(segment.getSegmentDate(), "dd.MM.yy").getTime(),
                "d") + DateUtil.getDayOfMonthSuffix(Integer.parseInt(name));

        holder.segmentDateTime.setText(dateth + " " + DateUtil.convertDateObjectToString(
                DateUtil.convertStringToCalendar(segment.getSegmentDate(), "dd.MM.yy").getTime(),
                "MMMM yyyy") + ", " + segment.getSegmentTime());


        // holder.textViewName.setTextColor(Integer.parseInt("#808080"));
        //      holder.imageViewprice.setText(dataModel.getPrice());
        // holder.imageViewprice.setTextColor(Integer.parseInt("#ADADAD"));
        //    holder.textViewdate.setText(dataModel.getDate());
        // holder.textViewdate.setTextColor(Integer.parseInt("#CECECE"));
        //holder.imageView.setImageDrawable(drawable);
        //this.imageView1=(ImageView)itemView.findViewById(R.id.imageView3);
        Log.e("Boolean status", "onBindViewHolder: " + segment.isFavorite());

        /*boolean a = segment.isFavorite();
        if (!a) {
            holder.favourite.setImageResource(R.mipmap.star);
        } else {
            holder.favourite.setImageResource(R.mipmap.star_selected);
        }*/

        if(isFromFavoriteFragment){
            holder.favourite.setImageResource(R.mipmap.star_selected);
        }else {
            boolean favoriteStatus = PreferenceUtil.getFavoriteStatus(holder.favourite.getContext(), segment);
            if (favoriteStatus) {
                holder.favourite.setImageResource(R.mipmap.star_selected);
            } else {
                holder.favourite.setImageResource(R.mipmap.star);
            }

        }

        holder.favouriteLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PreferenceUtil.updateFavoriteStatus(holder.favouriteLayout.getContext(), segment);

                if(isFromFavoriteFragment){
                    dataSet.remove(listPosition);
                    notifyDataSetChanged();
                }else {
                    notifyItemChanged(listPosition);
                }
            }
        });


        holder.ticketLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ticketBookingIntent = new Intent(holder.ticketLayout.getContext(), TicketBookingActivity.class);
                ticketBookingIntent.putExtra("segment", segment);
                holder.ticketLayout.getContext().startActivity(ticketBookingIntent);

            }
        });

        holder.uber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onUber(segment);
            }
        });

      /*  if(b)
        {
            holder.imageView1.setColorFilter(color);
        }
        else {
            holder.imageView1.setColorFilter(color1);
        }*/
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
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

