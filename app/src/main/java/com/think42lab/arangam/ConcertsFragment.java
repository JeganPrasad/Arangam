package com.think42lab.arangam;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.think42lab.arangam.Adapters.ScheduleAdapter;
import com.think42lab.arangam.model.Artists;
import com.think42lab.arangam.model.MyLocation;
import com.think42lab.arangam.model.Segment;
import com.think42lab.arangam.model.SegmentType;
import com.think42lab.arangam.model.Venues;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lenovo on 12/3/2016.
 */
public class ConcertsFragment extends Fragment {

    private RecyclerView recyclerView;
    Context context;
    private List<Artists> data;
    private ScheduleAdapter customAdapter;
    // private Activity active;
    public static final String PREFS_NAME = "AOP_PREFS";

    private ArrayList<Segment> ConcertList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        View v = inflater.inflate(R.layout.october, container, false);
        ///  this.active=aa;
        String str = loadData("segment_json");
//        customAdapter = new ScheduleAdapter(ConcertList,getContext());

        if (str.equals("def")) {

            new RestClient(new RequestCallBack() {
                @Override
                public void success(String response) {
                    saveData(response);
                    parseJson(response);
                }

                @Override
                public void fail() {

                }
            }).execute("http://173.255.238.139:3030/api/1.0/segments/composite?page=1");

        } else {
            parseJson(str);
        }
        recyclerView = (RecyclerView) v.findViewById(R.id.recycle);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        recyclerView.setAdapter(customAdapter);
        recyclerView.setBottom(customAdapter.getItemCount());

        return v;
    }

    public void parseJson(String response) {
        try {

            JSONObject jsonObj = new JSONObject(response);

            // Getting JSON Array node
            JSONArray data = jsonObj.getJSONArray("data");

            for (int i = 0; i < data.length(); i++) {
                JSONObject obj = data.getJSONObject(i);
                Segment s = new Segment();

                s.setSegmentDate(obj.getString("segment_date"));
                s.setSegmentTime(obj.getString("segment_time"));
                s.setTicketsAvailable(obj.getString("ticketsAvailable"));
                s.setTicketsCost(obj.getString("ticketCost"));
//                String vid = (obj.getString("VenueId"));
                s.setVenueID(obj.getString("VenueId"));
                s.setArtistID(obj.getString("ArtistId"));
                s.setId(obj.getString("id"));
                s.setAccompanists(obj.getString("accompanists"));
                s.setSegId(obj.getString("SegmentTypeId"));

                JSONObject segmentTypeObj = obj.getJSONObject("SegmentType");
                s.setSegmentType(new SegmentType(segmentTypeObj.getString("id"), segmentTypeObj.getString("name")));

                JSONObject venueseObj = obj.getJSONObject("Venue");
                Venues venues = new Venues();
                venues.setId(venueseObj.getString("id"));
                venues.setName(venueseObj.getString("name"));
                JSONObject venueseLocationObj = venueseObj.getJSONObject("location");
                venues.setMyLocation(new MyLocation(venueseLocationObj.getString("X"), venueseLocationObj.getString("Y")));
                s.setVenues(venues);

                JSONObject artistsObj = obj.getJSONObject("Artist");
                s.setArtists(new Artists(artistsObj.getString("id"), artistsObj.getString("name")));
                if (s.getSegId().equals("1") || s.getSegId().equals("5") || s.getSegId().equals("6")) {
                    ConcertList.add(s);
                }
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
        customAdapter.notifyDataSetChanged();


    }

    private String getVenueName(String vid) {
        String venueName = "";
        try {
            String venueJson = loadData("venue_json");
            JSONObject j = new JSONObject(venueJson);
            JSONArray data = j.getJSONArray("data");
            for (int i = 0; i < data.length(); i++) {
                JSONObject obj = data.getJSONObject(i);
                Venues v = new Venues();
                if (obj.getString("id").equals(vid)) {
                    venueName = obj.getString("name");
                    //Toast.makeText(getActivity(),venueName,Toast.LENGTH_LONG).show();
                    break;
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return venueName;
    }


    private String loadData(String filename) {
        context = getActivity();
        SharedPreferences sp = context.getSharedPreferences(filename, Context.MODE_PRIVATE);
        String response = sp.getString("response", "def");
        return response;
    }

    private void saveData(String response) {
        context = getActivity();
        SharedPreferences sharedPreferences = context.getSharedPreferences("segment_json", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("response", response);
        editor.commit();
    }

   /* public void upDateRecyclerView(ArrayList<Segment> data) {
//        if (ConcertList != null) {
//            this.ConcertList = ConcertList;
//            customAdapter.notifyDataSetChanged();
//            Log.e(TAG, "upDateRecyclerView: " + ConcertList.toString());
//        }

        customAdapter.setListUpdate(data);

        Toast.makeText(getContext(), "Se :" + ConcertList.toString(), Toast.LENGTH_SHORT).show();
    }*/


}




