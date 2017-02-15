package com.think42lab.arangam;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.think42lab.arangam.Adapters.TicketsPagerAdapter;
import com.think42lab.arangam.model.Artists;
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

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.facebook.internal.FacebookDialogFragment.TAG;


public class ScheduleFragment extends Fragment {

    private ViewPager mViewPager;
    private Context context;
    private TicketsPagerAdapter adapter;
    private List<Venues> mVenuesList;


    private List<Segment> mConcertList = new ArrayList<Segment>();
    private List<Segment> mLecDemList  = new ArrayList<Segment>();
    private List<Segment> mDanceDramaList  = new ArrayList<Segment>();
    private List<Segment> mFavoriteSegmentList  = new ArrayList<Segment>();
    public static List<String> mFavoriteIdList = new ArrayList<String>();
    private Map<String, Venues> mVenuesIdMap = new HashMap<String, Venues>() ;


    private ProgressBar progressBar;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.schedule_layout, container, false);
        // new RestClient(this).execute("http://173.255.238.139:3030/api/1.0/segments");
        progressBar = (ProgressBar) v.findViewById(R.id.progressBar);
        context = getActivity();
//        loadSegmentListFromLocal();

        //showProgress();

        /*new RestClient(new RequestCallBack() {
            @Override
            public void success(String response) {
                saveSegmentData(response);
                hideProgress();
                adapter.notifyDataSetChanged();
                mViewPager.setAdapter(adapter);

            }

            @Override
            public void fail() {
                hideProgress();

            }
        }).execute("http://nb-139-162-26-19.singapore.nodebalancer.linode.com/api/1.0/segments/composite?page=1");*/

        categorizeSegments();

        mViewPager = (ViewPager) v.findViewById(R.id.viewpager);
        TabLayout tabLayout = (TabLayout) v.findViewById(R.id.tabs);
        tabLayout.addTab(tabLayout.newTab().setText("Concerts"));
        tabLayout.addTab(tabLayout.newTab().setText("Lec-Dem"));
        tabLayout.addTab(tabLayout.newTab().setText("Others"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        // new RestClient(getActivity()).execute("http://173.255.238.139:3030/api/1.0/segments");

        adapter = new TicketsPagerAdapter(getFragmentManager(), tabLayout.getTabCount());
        adapter.setConcertList(mConcertList);
        adapter.setLecDemList(mLecDemList);
        adapter.setDanceDrama(mDanceDramaList);
        adapter.setContext(getContext());
        mViewPager.setAdapter(adapter);
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


        return v;
    }


    private void categorizeSegments(){
        List<Segment> localSegmentList = PreferenceUtil.getSegmentList(getActivity());

        if(localSegmentList!=null){
            for(Segment segment : localSegmentList){
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
                    if(Arrays.asList(SegmentTypes.TYPE_CONCERTS).contains(segment.getSegId())){
                        mConcertList.add(segment);
                    }if(Arrays.asList(SegmentTypes.TYPE_LEGDEM).contains(segment.getSegId())){
                        mLecDemList.add(segment);
                    }else{
                        mDanceDramaList.add(segment);
                    }
                }

            }
            Collections.sort(mConcertList, new DateSort());
            Collections.sort(mLecDemList, new DateSort());
            Collections.sort(mDanceDramaList, new DateSort());
        }

    }


    /*private void showProgress() {
        progressBar.setVisibility(View.VISIBLE);

    }

    private void hideProgress() {
        progressBar.setVisibility(View.GONE);
    }


    public void loadSegmentListFromLocal() {

        mVenuesList = new ArrayList<>();
        mConcertList = new ArrayList<>();
        mLecDemList = new ArrayList<>();
        mDanceDramaList = new ArrayList<>();
        mVenuesIdMap = new HashMap<>();
        mFavoriteSegmentList = new ArrayList<>();
        mFavoriteIdList = new ArrayList<String>();

//        SharedPreferences venuePreference = context.getSharedPreferences("venue_json", Context.MODE_PRIVATE);
//        String venueResponse = venuePreference.getString("response", "def");
//        parseVenues(venueResponse);
        SharedPreferences favoriteSegments = context.getSharedPreferences("favorite_segments", Context.MODE_PRIVATE);
        toList(favoriteSegments.getString("response", "def"), mFavoriteSegmentList);
        mFavoriteSegmentList.clear();
        for (Segment item :
                mFavoriteSegmentList) {
            mFavoriteIdList.add(item.getId());
        }

        SharedPreferences segmentPreference = context.getSharedPreferences("segment_json", Context.MODE_PRIVATE);
        parseJsonNew(segmentPreference.getString("response", "def"));
//        toList(segmentPreference.getString("concerts", " "), mConcertList);
//        toList(segmentPreference.getString("concerts", " "), mLecDemList);
//        toList(segmentPreference.getString("concerts", " "), mDanceDramaList);

        Log.e(TAG, "loadSegmentListFromLocal: " + segmentPreference.getString("response", "def"));


        Toast.makeText(getActivity(), "Called", Toast.LENGTH_SHORT).show();


    }


    private void toSegmentList(String jsonString) {

    }

    private void parseVenues(String response) {
        try {
            String venueJson = loadData("venue_json");
            JSONObject j = new JSONObject(venueJson);
            JSONArray data = j.getJSONArray("data");
            for (int i = 0; i < data.length(); i++) {
                JSONObject obj = data.getJSONObject(i);
                Venues v = new Venues();
                v.setId(obj.getString("id"));
                v.setName(obj.getString("name"));
                mVenuesList.add(v);
                mVenuesIdMap.put(v.getId(), v);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private String loadData(String value) {
        SharedPreferences sp = context.getSharedPreferences(value, Context.MODE_PRIVATE);
        String response = sp.getString("response", "def");
        return response;
    }

    private void saveData(String response) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("venue_json", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("response", response);
        editor.commit();
    }

    private void saveSegmentData(String response) {

        parseJsonNew(response);

        SharedPreferences sharedPreferences = context.getSharedPreferences("segment_json", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("response", response);

        Log.e(TAG, "saveSegmentData: " + response);

        try {
            editor.putString("concerts", new JSONArray(mConcertList.toString()).toString());
            editor.putString("lec_dems", new JSONArray(mLecDemList.toString()).toString());
            editor.putString("dance_dramas", new JSONArray(mDanceDramaList.toString()).toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        editor.commit();
    }


    public void parseJsonNew(String response) {
        Log.e(TAG, "parseJson: " + response);
        try {
            JSONObject jsonObj = new JSONObject(response);
            JSONObject data1 = jsonObj.getJSONObject("data");
            JSONArray data = data1.getJSONArray("rows");
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

                try {
                    JSONObject venueseLocationObj = venueseObj.getJSONObject("location");
                    venues.setMyLocation(new MyLocation(venueseLocationObj.getString("X"), venueseLocationObj.getString("Y")));
                } catch (JSONException e) {
                    venues.setLocation(venueseObj.getString("location"));

                }

//                Object o = venueseObj.getString("location");

//                if (o instanceof JSONObject) {
//
//                    JSONObject venueseLocationObj = venueseObj.getJSONObject("location");
//                    venues.setMyLocation(new MyLocation(venueseLocationObj.getString("X"), venueseLocationObj.getString("Y")));
//                } else {
//                    venues.setLocation(venueseObj.getString("location"));
//                }

                s.setVenues(venues);


                JSONObject artistsObj = obj.getJSONObject("Artist");
                s.setArtists(new Artists(artistsObj.getString("id"), artistsObj.getString("name")));


                if (mFavoriteIdList.contains(s.getId())) {
                    s.setIsFavorite(true);
                }
                if (s.getSegId().equals("1") || s.getSegId().equals("5") || s.getSegId().equals("6")) {
                    mConcertList.add(s);
                } else if (s.getSegId().equals("2")) {
                    mLecDemList.add(s);
                } else if (s.getSegId().equals("3") || s.getSegId().equals("4")) {
                    mDanceDramaList.add(s);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Collections.sort(mConcertList, new DateSort());
        Collections.sort(mLecDemList, new DateSort());
        Collections.sort(mDanceDramaList, new DateSort());

    }


    private void toList(String jsonString, List<Segment> list) {


        try {
            JSONArray data = new JSONArray(jsonString);
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

                if (mFavoriteIdList.contains(s.getId())) {
                    s.setIsFavorite(true);
                }


                s.setVenues(venues);


                JSONObject artistsObj = obj.getJSONObject("artists");
                s.setArtists(new Artists(artistsObj.getString("id"), artistsObj.getString("name")));
                list.add(s);
                Collections.sort(list, new DateSort());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }*/

}

