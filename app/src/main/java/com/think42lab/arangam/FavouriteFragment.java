package com.think42lab.arangam;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.think42lab.arangam.Adapters.ScheduleAdapter;
import com.think42lab.arangam.model.Artists;
import com.think42lab.arangam.model.MyLocation;
import com.think42lab.arangam.model.Segment;
import com.think42lab.arangam.model.SegmentType;
import com.think42lab.arangam.model.Venues;
import com.think42lab.arangam.util.PreferenceUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class FavouriteFragment extends Fragment implements ScheduleAdapter.ActionListener {
    private RecyclerView recyclerView;
    Context context;
    private ScheduleAdapter customAdapter;
    private List<Segment> favouriteList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.october, container, false);
        setHasOptionsMenu(true);
        favouriteList = new ArrayList<>();
        recyclerView = (RecyclerView) v.findViewById(R.id.recycle);
        //String str = getFavoutiteSP("favorite_segments");
        //favouriteList = toList(str);

        favouriteList = PreferenceUtil.getFavoriteSegmentList(getActivity());

        if(favouriteList == null) {
            favouriteList = new ArrayList<>();
        }

        customAdapter = new ScheduleAdapter(favouriteList, this);
        customAdapter.setFromFavoriteFragment(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(customAdapter);
        recyclerView.setBottom(customAdapter.getItemCount());
        return v;

    }

    private String getFavoutiteSP(String value) {
        context = getActivity();
        SharedPreferences sp = context.getSharedPreferences(value, Context.MODE_PRIVATE);
        String response = sp.getString("response", "");
        //Toast.makeText(getActivity(),response,Toast.LENGTH_LONG).show();
        return response;
    }

    private static final String TAG = "FavouriteFragment";

    /*private List<Segment> toList(String response) {
        List<Segment> segmentList = new ArrayList<>();
        Log.e(TAG, "parseJson: " + response);
//        if (response != null || !response.equalsIgnoreCase(" ") || !response.equalsIgnoreCase("")) {
        try {
            JSONArray data = new JSONArray(response);
            for (int i = 0; i < data.length(); i++) {
                JSONObject obj = data.getJSONObject(i);
                Segment s = new Segment();

                s.setSegmentDate(obj.getString("segmentDate"));
                s.setSegmentTime(obj.getString("segmentTime"));
                s.setTicketsAvailable(obj.getString("ticketsAvailable"));
                s.setTicketsCost(obj.getString("ticketsCost"));
//                String vid = (obj.getString("VenueId"));
                s.setVenueID(obj.getString("venueID"));
                s.setArtistID(obj.getString("artistID"));
                s.setId(obj.getString("id"));
                s.setAccompanists(obj.getString("Accompanists"));
                s.setSegId(obj.getString("segId"));

                JSONObject segmentTypeObj = obj.getJSONObject("segmentType");
                s.setSegmentType(new SegmentType(segmentTypeObj.getString("id"), segmentTypeObj.getString("name")));

                JSONObject venueseObj = obj.getJSONObject("venues");
                Venues venues = new Venues();
                venues.setId(venueseObj.getString("id"));
                venues.setName(venueseObj.getString("name"));

                try {
                    JSONObject venueseLocationObj = venueseObj.getJSONObject("myLocation");
                    venues.setMyLocation(new MyLocation(venueseLocationObj.getString("X"), venueseLocationObj.getString("Y")));
                } catch (JSONException e) {
                    venues.setLocation(venueseObj.getString("location"));

                }

                s.setIsFavorite(true);


                s.setVenues(venues);


                JSONObject artistsObj = obj.getJSONObject("artists");
                s.setArtists(new Artists(artistsObj.getString("id"), artistsObj.getString("name")));
                segmentList.add(s);

                favouriteList.add(s);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
//        }
        return segmentList;
    }*/

    @Override
    public void onFavorite(Segment segment) {
        /*favouriteList.clear();
        try {
            SharedPreferences preferences = getContext().getSharedPreferences("favorite_segments", Context.MODE_PRIVATE);
            String jsonResult = preferences.getString("response", " ");
            Log.e(TAG, "onFavorite: " + jsonResult);
            List<Segment> previousList = toList(jsonResult);
            segment.setIsFavorite(true);
            int prviousListSize = previousList.size();
            previousList.add(segment);
            preferences.edit()
                    .putString("response", new JSONArray(previousList.toString()).toString()).commit();
            NotificationListener listener = (NotificationListener) getActivity();
            listener.notifyFavorites(1);


        } catch (JSONException e) {
            e.printStackTrace();
        }
        customAdapter.notifyDataSetChanged();*/
    }

    @Override
    public void onUnFavorite(Segment segment) {
/*        favouriteList.clear();
        try {
            SharedPreferences preferences = getContext().getSharedPreferences("favorite_segments", Context.MODE_PRIVATE);
            String jsonResult = preferences.getString("response", " ");
            Log.e(TAG, "onUnFavorite: " + jsonResult);
            List<Segment> previousList = toList(jsonResult);
//            Toast.makeText(getActivity(), "" + previousList.size(), Toast.LENGTH_SHORT).show();
//            if (previousList.size() == favouriteList.size()) {
//                favouriteList.clear();
//            }
            int i = 0;
            for (i = 0; i < previousList.size(); i++) {
                if (segment.getId().equals(previousList.get(i).getId())) {
//                    previousList.get(i).setFavorite(false);
                    previousList.remove(i);
                    break;
                }

            }
            preferences.edit()
                    .putString("response", new JSONArray(previousList.toString()).toString()).commit();
            NotificationListener listener = (NotificationListener) getActivity();
            listener.notifyFavorites(-1);
            customAdapter.UpdateList(previousList);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        customAdapter.notifyDataSetChanged();
//        Toast.makeText(getContext(), "" + favouriteList.size(), Toast.LENGTH_SHORT).show();*/
    }

    @Override
    public void onUber(Segment segment) {


        try {
            PackageManager pm = getContext().getPackageManager();
            pm.getPackageInfo("com.ubercab", PackageManager.GET_ACTIVITIES);
//            String uri = "uber://?client_id=SXQhacvgT_5SpouyV_iPYQynPfJ7pk4q&action=setPickup&pickup[latitude]=13.0311140&pickup[longitude]=80.2579190&pickup[nickname]=UberHQ&dropoff[latitude]=13.0387062&dropoff[longitude]=80.2567343&dropoff[nickname]=Gana"; //"uber://?action=setPickup&pickup=my_location&client_id=<CLIENT_ID>";
            String uri = "uber://?client_id=SXQhacvgT_5SpouyV_iPYQynPfJ7pk4q&action=setPickup&dropoff[latitude]=" + segment.getVenues().getMyLocation().getX() + "&dropoff[longitude]=" + segment.getVenues().getMyLocation().getY() + "&dropoff[nickname]=" + segment.getVenues().getName(); //"uber://?action=setPickup&pickup=my_location&client_id=<CLIENT_ID>";
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(uri));
            startActivity(intent);
        } catch (PackageManager.NameNotFoundException e) {
            // No Uber app! Open mobile website.
//            String url = "https://m.uber.com/ul?client_id=SXQhacvgT_5SpouyV_iPYQynPfJ7pk4q&action=setPickup&pickup[latitude]=13.0311140&pickup[longitude]=80.2579190&pickup[nickname]=UberHQ&dropoff[latitude]=13.0387062&dropoff[longitude]=80.2567343&dropoff[nickname]=Gana";//"https://m.uber.com/sign-up?client_id=<CLIENT_ID>";
            String url = "https://m.uber.com/ul?client_id=SXQhacvgT_5SpouyV_iPYQynPfJ7pk4q&action=setPickup&dropoff[latitude]=" + segment.getVenues().getMyLocation().getX() + "&dropoff[longitude]=" + segment.getVenues().getMyLocation().getY() + "&dropoff[nickname]=" + segment.getVenues().getName();
            ;//"https://m.uber.com/sign-up?client_id=<CLIENT_ID>";
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);
        } catch (NullPointerException e) {
            Toast.makeText(getActivity(), "No drop location", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onBook() {

    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.getItem(0).setVisible(false);
        super.onCreateOptionsMenu(menu, inflater);
    }
}
