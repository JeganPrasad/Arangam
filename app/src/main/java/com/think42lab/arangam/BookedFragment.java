package com.think42lab.arangam;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.think42lab.arangam.Adapters.TicketsAdapter;

import java.util.ArrayList;

public class BookedFragment extends Fragment {

    private RecyclerView recyclerView;

    private TicketsAdapter customAdapter;

    private ArrayList<com.think42lab.arangam.DataModel> list;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.booked, container, false);
        recyclerView = (RecyclerView) v.findViewById(R.id.recycle);

        list = new ArrayList<>();

        list.add(new com.think42lab.arangam.DataModel("Krishna Gana Sabha", "र 500", "8 Oct,2016"));
        list.add(new com.think42lab.arangam.DataModel("Thyaga Brahma Gana Sabha", "र 500", "8 Oct,2016"));
        list.add(new com.think42lab.arangam.DataModel("Narada Gana Sabha", "र 500", "8 Oct,2016"));
        list.add(new com.think42lab.arangam.DataModel("Parthasarathy Swami Sabha", "र500", "8 Oct,2016"));
        list.add(new DataModel("Sat Sangam Sabha", "र 500", "8 Oct,2016"));
        list.add(new DataModel("Kalarasana", "र 500", "8 Oct,2016"));
        //list.add(new DataModel("","र500","8 Oct,2016"));

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());


        customAdapter = new TicketsAdapter(list);


        recyclerView.setAdapter(customAdapter);

        return v;
    }
}