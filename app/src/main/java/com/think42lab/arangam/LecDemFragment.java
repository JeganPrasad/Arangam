package com.think42lab.arangam;

import android.app.Activity;
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
import com.think42lab.arangam.model.Segment;
import com.think42lab.arangam.model.Venues;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class LecDemFragment extends Fragment implements RequestCallBack {

    private RecyclerView recyclerView;
    private List<DataModelSchedule> data;
    private ScheduleAdapter customAdapter;
    private View v;
    Context context;
    private Activity a;
    public ArrayList<Segment> LecDemList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.november, container, false);
        //this.a=aa;
        // new RestClient(this).execute("http://173.255.238.139:3030/api/1.0/segments");
        recyclerView = (RecyclerView) v.findViewById(R.id.recycle);
        String str = loadData("segment_json");
//        customAdapter = new ScheduleAdapter(LecDemList,getContext());
        if (str.equals("def")) {
            new RestClient(this).execute("http://173.255.238.139:3030/api/1.0/segments");
        } else {
            parseJson(str);
        }
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(customAdapter);
        return v;
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
                String vid = (obj.getString("VenueId"));
                s.setVenueID(vid);
                String vname = getVenueName(vid);
                s.setTicketsAvailable(obj.getString("ticketsAvailable"));
                s.setTicketsCost(obj.getString("ticketCost"));
                s.setVenueName(vname);
                s.setArtistID(obj.getString("ArtistId"));
                s.setId(obj.getString("id"));
                s.setAccompanists(obj.getString("accompanists"));
                s.setSegId(obj.getString("SegmentTypeId"));
                if (s.getSegId().equals("2")) {
                    LecDemList.add(s);

                }

            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
        customAdapter.notifyDataSetChanged();


    }

    private String loadData(String filename) {
        context = getActivity();
        SharedPreferences sp = context.getSharedPreferences(filename, Context.MODE_PRIVATE);
        String response = sp.getString("response", "def");
        return response;
    }
/*
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_schedule,menu);

        final MenuItem item=menu.findItem(R.id.action_search);
        final SearchView searchView=(SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(this);

        MenuItemCompat.setOnActionExpandListener(item,
                new MenuItemCompat.OnActionExpandListener() {
                    @Override
                    public boolean onMenuItemActionCollapse(MenuItem item) {
// Do something when collapsed
                        customAdapter.setFilter(list);
                        return true; // Return true to collapse action view
                    }

                    @Override
                    public boolean onMenuItemActionExpand(MenuItem item) {
// Do something when expanded

                        return true; // Return true to expand action view
                    }
                });

    }



    @Override
    public boolean onQueryTextChange(String newText) {
        final List<DataModelSchedule> filteredModelList = filter(list, newText);


        customAdapter.setFilter(filteredModelList);

        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    private List<DataModelSchedule> filter(List<DataModelSchedule> models, String query) {
        query = query.toLowerCase();final List<DataModelSchedule> filteredModelList = new ArrayList<>();
        for (DataModelSchedule model : models) {
            final String text = model.getName().toLowerCase();
            if (text.contains(query)) {
                filteredModelList.add(model);
            }
        }
        return filteredModelList;
    }*/

    @Override
    public void success(String response) {
        saveData(response);
        parseJson(response);
    }

    @Override
    public void fail() {

    }

    private void saveData(String response) {
        context = getActivity();
        SharedPreferences sharedPreferences = context.getSharedPreferences("segment_json", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("response", response);
        editor.commit();
    }
}

