package com.think42lab.arangam;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.think42lab.arangam.Adapters.CustomAdapter;

import java.util.ArrayList;

public class FavouritesFragment extends Fragment {

    private RecyclerView recyclerView;

    private CustomAdapter customAdapter;

    private ArrayList<DataModel> list;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.available, container, false);

        recyclerView = (RecyclerView) v.findViewById(R.id.recycle);

        list = new ArrayList<>();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        customAdapter = new CustomAdapter(list);
        recyclerView.setAdapter(customAdapter);

        return v;
    }
}