package com.think42lab.arangam;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.think42lab.arangam.Adapters.ScheduleAdapter;
import com.think42lab.arangam.Adapters.ScheduleAdapterGroupActivity;
import com.think42lab.arangam.model.Artists;
import com.think42lab.arangam.model.ListItem;
import com.think42lab.arangam.model.MyLocation;
import com.think42lab.arangam.model.Segment;
import com.think42lab.arangam.model.SegmentType;
import com.think42lab.arangam.model.Venues;
import com.think42lab.arangam.util.DateSort;
import com.think42lab.arangam.util.DateUtil;
import com.think42lab.arangam.util.PreferenceUtil;
import com.think42lab.arangam.util.SegmentTypes;
import com.think42lab.arangam.util.SortByDate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import static com.facebook.internal.FacebookDialogFragment.TAG;


public class GroupActivity extends AppCompatActivity implements ScheduleAdapter.ActionListener {
    private RecyclerView recyclerView;
    Context context;
    private ScheduleAdapterGroupActivity customAdapter;
    private List<Segment> segmentList;
    private List<ListItem> consolidatedList = new ArrayList<>();
    SimpleDateFormat parser = new SimpleDateFormat("dd.MM.yyyy");
    String venueId, venueName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.october);


        recyclerView = (RecyclerView) findViewById(R.id.recycle);

        if(getIntent()!=null){
            venueId = getIntent().getStringExtra("venueId");
            venueName = getIntent().getStringExtra("venueName");
        }


        if(TextUtils.isEmpty(venueId) || TextUtils.isEmpty(venueName)){
            finish();
        }

        if (getSupportActionBar() != null) {
//            getSupportActionBar().setTitle(values);
            getSupportActionBar().setTitle((Html.fromHtml("<font color=\"#ffffff\">" + venueName + "</font>")));
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        categorizeSegmentDataByVenueId();

        Collections.sort(segmentList, new DateSort());


        customAdapter = new ScheduleAdapterGroupActivity(segmentList, this);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(customAdapter);
        recyclerView.setBottom(customAdapter.getItemCount());


        //parseJsonNew(response, values);


    }

    private void categorizeSegmentDataByVenueId(){
        segmentList = new ArrayList<>();
        List<Segment> segmentLocalList = PreferenceUtil.getSegmentList(this);
        if(segmentLocalList !=null ){
            for(Segment segment : segmentLocalList){
                if(segment.getVenueID().equalsIgnoreCase(venueId)){
                    Calendar segmentDate = DateUtil.convertStringToCalendar(segment.getSegmentDate(), "dd.MM.yy");
                    segmentDate.set(Calendar.HOUR_OF_DAY, 0);
                    segmentDate.set(Calendar.MINUTE, 0);
                    segmentDate.set(Calendar.SECOND, 0);
                    segmentDate.set(Calendar.MILLISECOND, 0);


                    Calendar todayDate = Calendar.getInstance();
                    todayDate.set(Calendar.HOUR_OF_DAY, 0);
                    todayDate.set(Calendar.MINUTE, 0);
                    todayDate.set(Calendar.SECOND, 0);
                    todayDate.set(Calendar.MILLISECOND, 0);

                    if (segmentDate.compareTo(todayDate) >= 0) {
                        segmentList.add(segment);
                    }


                }
            }
        }
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;

        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onFavorite(Segment segment) {
        try {
            /*SharedPreferences preferences = getSharedPreferences("favorite_segments", Context.MODE_PRIVATE);
            String jsonResult = preferences.getString("response", " ");
            Log.e(TAG, "onFavorite: " + jsonResult);
            List<Segment> previousList = toList(jsonResult);
            segment.setIsFavorite(true);
            int prviousListSize = previousList.size();
            previousList.add(segment);
            preferences.edit()
                    .putString("response", new JSONArray(previousList.toString()).toString()).commit();
//            NotificationListener listener = (NotificationListener) getParentActivityIntent();
//            listener.notifyFavorites(1);

            */

            sendMessage("favorite");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUnFavorite(Segment segment) {
        try {
            /*SharedPreferences preferences = getSharedPreferences("favorite_segments", Context.MODE_PRIVATE);
            String jsonResult = preferences.getString("response", " ");
            Log.e(TAG, "onUnFavorite: " + jsonResult);
            List<Segment> previousList = toList(jsonResult);
            int i = 0;
            for (i = 0; i < previousList.size(); i++) {
                if (segment.getId().equals(previousList.get(i).getId())) {
                    previousList.remove(i);
                    break;
                }

            }
            preferences.edit()
                    .putString("response", new JSONArray(previousList.toString()).toString()).commit();*/
//
        } catch (Exception e) {
            e.printStackTrace();
            //sendMessage("unfavorite");
//            NotificationListener listener = (NotificationListener) getActivity();
//            listener.notifyFavorites(-1);
        }

        sendMessage("unfavorite");

    }

    private void sendMessage(String values) {
       Log.d("sender", "Broadcasting message");
        Intent intent = new Intent("customfavoriteunfavorite");
        // You can also include some extra data.
        intent.putExtra("message", values);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    @Override
    public void onUber(Segment segment) {

        try {
            PackageManager pm = getPackageManager();
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
            Toast.makeText(getApplicationContext(), "No drop location", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onBook() {

    }

    /*private List<Segment> toList(String response) {
        List<Segment> segmentList = new ArrayList<>();
        Log.e(TAG, "parseJson: " + response);
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

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return segmentList;
    }

    private List<ListItem> getConsolidatedList(List<Segment> list) {

        HashMap<String, List<Segment>> groupedHashMap = new HashMap<>(0);
        String groupKey = "";
        for (Segment item : list) {

            if (groupedHashMap.containsKey(item.getSegmentDate())) {
                groupedHashMap.get(item.getSegmentDate()).add(item);
            } else {
                List<Segment> generalItems = new ArrayList<>();
                generalItems.add(item);
                groupedHashMap.put(item.getSegmentDate(), generalItems);
            }
        }
        List<ListItem> consolidatedList = new ArrayList<>();

        for (String key : groupedHashMap.keySet()) {
//            consolidatedList.add(new DateItem(key));
            for (Segment segment : groupedHashMap.get(key)) {
                consolidatedList.add(segment);
            }
        }
        return consolidatedList;
    }*/
}
