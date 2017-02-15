package com.think42lab.arangam;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
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

import com.mixpanel.android.mpmetrics.MixpanelAPI;
import com.think42lab.arangam.Adapters.GroupedScheduleRecyclerAdapter;
import com.think42lab.arangam.Adapters.ScheduleAdapter;
import com.think42lab.arangam.model.APIResponse;
import com.think42lab.arangam.model.Artists;
import com.think42lab.arangam.model.DateItem;
import com.think42lab.arangam.model.ListItem;
import com.think42lab.arangam.model.MyLocation;
import com.think42lab.arangam.model.Segment;
import com.think42lab.arangam.model.SegmentType;
import com.think42lab.arangam.model.Venues;
import com.think42lab.arangam.util.DateSort;
import com.think42lab.arangam.util.DateUtil;
import com.think42lab.arangam.util.Mixp;
import com.think42lab.arangam.util.PreferenceUtil;
import com.think42lab.arangam.util.SegmentTypes;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.facebook.internal.FacebookDialogFragment.TAG;


public class SegmentsFragment extends Fragment implements SearchView.OnQueryTextListener, GroupedScheduleRecyclerAdapter.ActionListener, SwipeRefreshLayout.OnRefreshListener {


    int totalPages = -1;
    int currentPage = 1;
    int countPerPage = 100;
    List<Segment> refreshSegmentList = new ArrayList<Segment>();

    Map<String, Segment> refreshSegmentMap = new HashMap<String, Segment>();

    Map<String, Artists> refreshArtistsMap = new HashMap<String, Artists>();
    List<Artists> refreshArtistsList = new ArrayList<Artists>();


    private RecyclerView recyclerView;
    Context context;
    private List<Artists> data;
    private ScheduleAdapter customAdapter;
    private List<ListItem> consolidatedList;
    private GroupedScheduleRecyclerAdapter groupedScheduleRecyclerAdapter;
    // private Activity active;
    public static final String PREFS_NAME = "AOP_PREFS";

    private List<Segment> segmentList;

    private SwipeRefreshLayout swipeRefreshLayout;

    private boolean isLoading;

    private int currentFragment;


    public SegmentsFragment() {
        // Required empty public constructor
    }

    public static SegmentsFragment newInstance(List<Segment> segments, int currentFragment) {

        SegmentsFragment fragment = new SegmentsFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("segments", (Serializable) segments);
        bundle.putInt("currentFragment",currentFragment);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

    }

    public List<Segment> getsegmentList() {
        if (segmentList == null) {
            segmentList = new ArrayList<>();
        }
        return segmentList;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.october, container, false);


        Mixp.track(getActivity(), "Schedule_Segment_Control_Changed");

        setHasOptionsMenu(true);
        Bundle bundle = getArguments();
        if (bundle != null) {
            segmentList = (List<Segment>) bundle.getSerializable("segments");
            currentFragment = bundle.getInt("currentFragment");
//            toList(bundle.getString("segment_list"));
        } else {
            segmentList = new ArrayList<>();
        }
//        customAdapter = new ScheduleAdapter(segmentList, this);
        //consolidatedList = getConsolidatedList(segmentList);
        consolidatedList = generateGroupedSegmentList(segmentList);



        groupedScheduleRecyclerAdapter = new GroupedScheduleRecyclerAdapter(consolidatedList, this);
        recyclerView = (RecyclerView) v.findViewById(R.id.recycle);
        swipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setOnRefreshListener(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
//        recyclerView.setAdapter(customAdapter);
//        recyclerView.setBottom(customAdapter.getItemCount());
        recyclerView.setAdapter(groupedScheduleRecyclerAdapter);
        recyclerView.setBottom(groupedScheduleRecyclerAdapter.getItemCount());

        return v;
    }

    private static final String TAG = "SegmentsFragment";

    @Override
    public void onFavorite(Segment segment) {
        try {
            /*SharedPreferences preferences = getContext().getSharedPreferences("favorite_segments", Context.MODE_PRIVATE);
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
            Mixp.track(getActivity(), "Segment_Added_To_Favourites");*/



            NotificationListener listener = (NotificationListener) getActivity();
            listener.notifyFavorites(1);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUnFavorite(Segment segment) {
        try {
            /*SharedPreferences preferences = getContext().getSharedPreferences("favorite_segments", Context.MODE_PRIVATE);
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
                    .putString("response", new JSONArray(previousList.toString()).toString()).commit();
            NotificationListener listener = (NotificationListener) getActivity();
            listener.notifyFavorites(-1);
*/
            NotificationListener listener = (NotificationListener) getActivity();
            listener.notifyFavorites(-1);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void sendMessage(String values) {

        Intent intent = new Intent("favUpdate");
        // You can also include some extra data.
        intent.putExtra("message", values);
        LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intent);

        Toast.makeText(getActivity(), "YYYYY", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        menu.clear();
        super.onCreateOptionsMenu(menu, inflater);
//        inflater.inflate(R.menu.menu_schedule, menu);

        final MenuItem item = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(this);

        MenuItemCompat.setOnActionExpandListener(item,
                new MenuItemCompat.OnActionExpandListener() {
                    @Override
                    public boolean onMenuItemActionCollapse(MenuItem item) {
                        groupedScheduleRecyclerAdapter.setFilter(consolidatedList);
                        return true; // Return true to collapse action view
                    }

                    @Override
                    public boolean onMenuItemActionExpand(MenuItem item) {

                        return true; // Return true to expand action view
                    }
                });

    }


    @Override
    public boolean onQueryTextChange(String newText) {
        final List<ListItem> filteredModelList = filter(consolidatedList, newText);

        groupedScheduleRecyclerAdapter.setFilter(filteredModelList);

        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {

        Mixp.track(getActivity(), "Searh_Performer");
        Mixp.track(getActivity(), "Search_Flow_Started");
        return false;
    }

    private List<ListItem> filter(List<ListItem> models, String query) {
        query = query.toLowerCase();
        final List<ListItem> filteredModelList = new ArrayList<>();
        for (ListItem model : models) {
            if (model.getType() == ListItem.TYPE_GENERAL) {
                Segment item = (Segment) model;
                final String text = item.getVenues().getName().toLowerCase();
                final String artistName = (item.getArtists() !=null) ? item.getArtists().getName() :  null;
                if (text.contains(query) || (artistName!=null && artistName.toLowerCase().contains(query))) {
                    filteredModelList.add(item);
                }
            }
        }
        return filteredModelList;
    }

    @Override
    public void onUber(Segment segment) {

        Mixp.track(getActivity(), "Uber_Option_Selected");


        try {
            JSONObject props = new JSONObject();
            props.put("Artist_Selected", "Artist_Selected");
//            props.put("Logged in", false);
            MixpanelAPI.getInstance(getActivity(), MainActivity.projectToken).track("Artist_Selected", props);
        } catch (JSONException e) {
            Log.e("MYAPP", "Unable to add properties to JSONObject", e);
        }


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
    }*/

    private List<ListItem> generateGroupedSegmentList(List<Segment> list){

        List<ListItem> consolidatedList = new ArrayList<>();
        consolidatedList.addAll(list);
        return consolidatedList;
    }

    private List<ListItem> getConsolidatedList(List<Segment> list) {

        Collections.sort(list, new DateSort());
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
    }

    @Override
    public void onResume() {
        super.onResume();
        if(groupedScheduleRecyclerAdapter != null){
            groupedScheduleRecyclerAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onRefresh() {
        if(!isLoading){
            syncData();
        }
    }

    private void syncData(){
        isLoading = true;
        new RestClient(new RequestCallBack() {
            @Override
            public void success(String response) {
                if(isAdded()) {
                    Log.d("response", response);

                    try {
                        APIResponse apiResponse = PreferenceUtil.GSON.fromJson(response, APIResponse.class);

                        refreshSegmentList.addAll(apiResponse.getData().getSegmentList());

                        Log.d("response", "apiResponse" + apiResponse);

                        if (totalPages == -1) {
                            int totalServerCount = apiResponse.getData().getCount();
                            totalPages = (totalServerCount % countPerPage == 0) ?
                                    (totalServerCount / countPerPage) : (totalServerCount / countPerPage) + 1;
                        }


                        if (isNextPageRequired()) {
                            currentPage = currentPage + 1;
                            syncData();
                        } else {

                            Artists artists = null;
                            for (Segment segment : refreshSegmentList) {
                                refreshSegmentMap.put(segment.getId(), segment);
                                artists = segment.getArtists();
                                if (artists != null) {
                                    refreshArtistsMap.put(artists.getId(), artists);
                                }
                            }


                            refreshSegmentList.clear();
                            refreshSegmentList.addAll(refreshSegmentMap.values());

                            refreshArtistsList.clear();
                            refreshArtistsList.addAll(refreshArtistsMap.values());
                            PreferenceUtil.clearSegments(getActivity());
                            PreferenceUtil.clearArtists(getActivity());
                            PreferenceUtil.saveSegments(getActivity(), refreshSegmentList);
                            PreferenceUtil.saveArtists(getActivity(), refreshArtistsList);

                            List<Segment>  futureList= new ArrayList<Segment>();
                            for(Segment segment : refreshSegmentList){
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
                                    switch (currentFragment){
                                        case 1:
                                            if(Arrays.asList(SegmentTypes.TYPE_CONCERTS).contains(segment.getSegId())){
                                                futureList.add(segment);
                                            }
                                            break;
                                        case 2:
                                            if(Arrays.asList(SegmentTypes.TYPE_LEGDEM).contains(segment.getSegId())){
                                                futureList.add(segment);
                                            }
                                            break;
                                        case 3:
                                            futureList.add(segment);
                                            break;

                                    }
                                }
                            }

                            Collections.sort(futureList, new DateSort());

                            consolidatedList.clear();
                            consolidatedList.addAll(generateGroupedSegmentList(futureList));

                            groupedScheduleRecyclerAdapter = new GroupedScheduleRecyclerAdapter(consolidatedList, SegmentsFragment.this);
                            recyclerView.setAdapter(groupedScheduleRecyclerAdapter);
                            recyclerView.setBottom(groupedScheduleRecyclerAdapter.getItemCount());
                            groupedScheduleRecyclerAdapter.notifyDataSetChanged();


                            swipeRefreshLayout.setRefreshing(false);
                            isLoading = false;

                            Toast.makeText(getActivity(),"Refreshed successfully", Toast.LENGTH_LONG).show();
                        }
                    } catch (Exception e) {
                        refreshSegmentList.clear();
                        totalPages = -1;
                        currentPage = 1;
                        e.printStackTrace();
                        swipeRefreshLayout.setRefreshing(false);
                        isLoading = false;
                        Toast.makeText(getActivity(),"Please check your connection and try again", Toast.LENGTH_LONG).show();
                    }
                }

            }

            @Override
            public void fail() {
                refreshSegmentList.clear();
                totalPages = -1;
                currentPage = 1;
                ///Fail Logic
                Log.d("fail", "API Failed");
                swipeRefreshLayout.setRefreshing(false);
                isLoading = false;

                Toast.makeText(getActivity(),"Please check your connection and try again", Toast.LENGTH_LONG).show();
            }
        }).execute("http://nb-139-162-26-19.singapore.nodebalancer.linode.com/api/1.0/segments/composite?page="+currentPage);
    }

    private boolean isNextPageRequired() {
        return (currentPage < totalPages);
    }
}
