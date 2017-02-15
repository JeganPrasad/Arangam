package com.think42lab.arangam.Adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.think42lab.arangam.SegmentsFragment;
import com.think42lab.arangam.model.Segment;
import com.think42lab.arangam.model.SegmentType;
import com.think42lab.arangam.util.SegmentTypes;

import java.util.ArrayList;
import java.util.List;

public class TicketsPagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    private List<Segment> mConcertList;


    private List<Segment> mLecDemList;
    private List<Segment> mDanceDrama;

    public void setContext(Context context) {
        this.context = context;
    }

    private Context context;


    public TicketsPagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }


    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                return SegmentsFragment.newInstance(mConcertList, 1);
            case 1:
                return SegmentsFragment.newInstance(mLecDemList, 2);
            case 2:
                return SegmentsFragment.newInstance(mDanceDrama, 3);
            default:
                return null;
        }

    }


    @Override
    public int getCount() {
        return mNumOfTabs;
    }

    public void setConcertList(List<Segment> mConcertList) {
        this.mConcertList = mConcertList;
        if (this.mConcertList == null) {
            mConcertList = new ArrayList<>();
        }
    }

    public void setLecDemList(List<Segment> mLecDemList) {
        this.mLecDemList = mLecDemList;
        if (this.mLecDemList == null) {
            this.mLecDemList = new ArrayList<>();
        }
    }

    public void setDanceDrama(List<Segment> mDanceDrama) {
        this.mDanceDrama = mDanceDrama;
        if (this.mDanceDrama == null) {
            this.mDanceDrama = new ArrayList<>();
        }
    }
}