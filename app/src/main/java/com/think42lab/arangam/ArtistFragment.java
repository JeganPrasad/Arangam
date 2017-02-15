package com.think42lab.arangam;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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

import com.think42lab.arangam.Adapters.ArtistsAdapter;
import com.think42lab.arangam.Adapters.GroupedScheduleRecyclerAdapter;
import com.think42lab.arangam.fastscroll.FastScrollRecyclerViewItemDecoration;
import com.think42lab.arangam.model.APIResponse;
import com.think42lab.arangam.model.Artists;
import com.think42lab.arangam.model.Segment;
import com.think42lab.arangam.util.DateSort;
import com.think42lab.arangam.util.DateUtil;
import com.think42lab.arangam.util.PreferenceUtil;
import com.think42lab.arangam.util.SegmentTypes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by krishna on 12/10/16.
 */

public class ArtistFragment extends Fragment implements SearchView.OnQueryTextListener, SwipeRefreshLayout.OnRefreshListener {

    private RecyclerView mRecyclerView;
    private ArtistsAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private Context context;

    private SwipeRefreshLayout swipeRefreshLayout;

    private boolean isLoading;


    ArrayList<Artists> mArtistList;

    int totalPages = -1;
    int currentPage = 1;
    int countPerPage = 100;
    List<Segment> refreshSegmentList = new ArrayList<Segment>();

    Map<String, Segment> refreshSegmentMap = new HashMap<String, Segment>();

    Map<String, Artists> refreshArtistsMap = new HashMap<String, Artists>();
    List<Artists> refreshArtistsList = new ArrayList<Artists>();
    FastScrollRecyclerViewItemDecoration decoration;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.artist_list_layout, container, false);
        setRetainInstance(true);

        mArtistList = PreferenceUtil.getArtistList(getActivity());

        if(mArtistList == null ){
            mArtistList = new ArrayList<>();
        }

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycle);

        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setOnRefreshListener(this);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)

//        for (int i = 0; i < 26; i++) {
//            myDataset.add(Character.toString((char) (65 + i)) + " Row item");
//        }

        Collections.sort(mArtistList, new Comparator<Artists>() {
            public int compare(Artists o1, Artists o2) {
                return (o1.getName().compareToIgnoreCase(o2.getName()));
            }
        });

        HashMap<String, Integer> mapIndex = calculateIndexesForName(mArtistList);

        mAdapter = new ArtistsAdapter(mArtistList, mapIndex);
//        myDataset.clear();
        mRecyclerView.setAdapter(mAdapter);
        decoration = new FastScrollRecyclerViewItemDecoration(getActivity());
        mRecyclerView.addItemDecoration(decoration);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());


        return rootView;

    }

    private HashMap<String, Integer> calculateIndexesForName(ArrayList<Artists> items) {
        HashMap<String, Integer> mapIndex = new LinkedHashMap<String, Integer>();
        for (int i = 0; i < items.size(); i++) {
            String name = items.get(i).getName();
            String index = name.substring(0, 1);
            index = index.toUpperCase();

            if (!mapIndex.containsKey(index)) {
                mapIndex.put(index, i);
            }
        }
        return mapIndex;
    }


    private List<Artists> filter(List<Artists> models, String query) {
        query = query.toLowerCase();
        final List<Artists> filteredModelList = new ArrayList<>();
        for (Artists model : models) {

            final String text = model.getName().toLowerCase();
            if (text.contains(query)) {
                filteredModelList.add(model);
            }

        }
        return filteredModelList;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

//        inflater.inflate(R.menu.menu_schedule,menu);

        final MenuItem item = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(this);

        MenuItemCompat.setOnActionExpandListener(item,
                new MenuItemCompat.OnActionExpandListener() {
                    @Override
                    public boolean onMenuItemActionCollapse(MenuItem item) {
                        mAdapter.setFilter(mArtistList);
                        return true; // Return true to collapse action view
                    }

                    @Override
                    public boolean onMenuItemActionExpand(MenuItem item) {

                        return true; // Return true to expand action view
                    }
                });
        //  menu.clear();
        super.onCreateOptionsMenu(menu, inflater);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        final List<Artists> filteredModelList = filter(mArtistList, newText);

        mAdapter.setFilter(filteredModelList);

        return true;
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
                            updateAdapter();
                            swipeRefreshLayout.setRefreshing(false);
                            isLoading = false;
                            Toast.makeText(getActivity(),"Refreshed successfully", Toast.LENGTH_LONG).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        refreshSegmentList.clear();
                        totalPages = -1;
                        currentPage = 1;
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


    public void updateAdapter(){
        mArtistList = PreferenceUtil.getArtistList(getActivity());

        if(mArtistList == null ){
            mArtistList = new ArrayList<>();
        }
        Collections.sort(mArtistList, new Comparator<Artists>() {
            public int compare(Artists o1, Artists o2) {
                return (o1.getName().compareToIgnoreCase(o2.getName()));
            }
        });

        HashMap<String, Integer> mapIndex = calculateIndexesForName(mArtistList);

        if(decoration!=null){
            mRecyclerView.removeItemDecoration(decoration);
        }

        mAdapter = new ArtistsAdapter(mArtistList, mapIndex);
//        myDataset.clear();
        mRecyclerView.setAdapter(mAdapter);
        FastScrollRecyclerViewItemDecoration decoration = new FastScrollRecyclerViewItemDecoration(getActivity());
        mRecyclerView.addItemDecoration(decoration);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
    }
}