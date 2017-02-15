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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.think42lab.arangam.ArtistsGroupActivity;
import com.think42lab.arangam.GroupActivity;
import com.think42lab.arangam.R;
import com.think42lab.arangam.TicketBookingActivity;
import com.think42lab.arangam.model.Segment;
import com.think42lab.arangam.util.DateUtil;
import com.think42lab.arangam.util.PreferenceUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

//import com.amulyakhare.textdrawable.TextDrawable;
//import com.amulyakhare.textdrawable.util.ColorGenerator;

/**
 * Created by Lenovo on 12/2/2016.
 */

public class ScheduleAdapterArtistsGroupActivity extends RecyclerView.Adapter<ScheduleAdapterArtistsGroupActivity.MyViewHolder> {


    public void filter(ArrayList<Segment> filterlist) {
        dataSet = filterlist;
        notifyDataSetChanged();
    }

    public void setFilter(List<Segment> filteredModelList) {
        dataSet = new ArrayList<>();
        dataSet.addAll(filteredModelList);
        notifyDataSetChanged();
    }

    public void Updatelist(List<Segment> favouriteList1) {
        this.dataSet = favouriteList1;
    }

    public interface ActionListener {
        public void onFavorite(Segment segment);

        public void onUnFavorite(Segment segment);

        public void onUber(Segment segment);

        public void onBook();
    }

    private ArtistsGroupActivity listener;
    private List<Segment> dataSet = new ArrayList<>();
    int color = Color.parseColor("#F5F5F5");
    int color1 = Color.parseColor("#CD333B");

    ArrayList date_array = new ArrayList();
    //private Activity activity;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView datename, datenumber;
        final TextView iconLetter;
        final TextView venue;
        final TextView accompanists;
        final TextView segmentDateTime;
        final ImageView favourite;
        final ImageView uber;
        final ImageView ticket;
        final View favouriteLayout, uberLayout, ticketLayout;


        public MyViewHolder(View itemView) {
            super(itemView);

            iconLetter = (TextView) itemView.findViewById(R.id.iconLetter);
            datename = (TextView) itemView.findViewById(R.id.datename);
            datenumber = (TextView) itemView.findViewById(R.id.datenumber);
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


    public ScheduleAdapterArtistsGroupActivity(List<Segment> data, ArtistsGroupActivity listener) {
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

        final Segment generalItem = dataSet.get(listPosition);


        // Populate general item data here
        holder.iconLetter.setText(getVenueName(generalItem.getVenues().getName()));


        //holder.venue.setText(generalItem.getArtists().getName());
        holder.venue.setVisibility(View.GONE);
        holder.accompanists.setText(generalItem.getAccompanists());

        holder.accompanists.setTextColor(holder.itemView.getContext().getResources().getColor(R.color.txtcolor));
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

        boolean favoriteStatus = PreferenceUtil.getFavoriteStatus(holder.favourite.getContext(), generalItem);
        if (favoriteStatus) {
            holder.favourite.setImageResource(R.mipmap.star_selected);
        } else {
            holder.favourite.setImageResource(R.mipmap.star);
        }

        holder.favouriteLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PreferenceUtil.updateFavoriteStatus(holder.favouriteLayout.getContext(), generalItem, listener);
                notifyItemChanged(listPosition);
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

    public static String converDate(String value) {
        SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd");
        Date datetxt = null;
        try {
            datetxt = parser.parse(value);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yy");
        final String formattedDatefrom = formatter.format(datetxt);
        return formattedDatefrom;
    }
}

