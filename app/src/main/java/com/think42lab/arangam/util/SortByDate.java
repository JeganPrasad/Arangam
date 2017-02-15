package com.think42lab.arangam.util;

import com.think42lab.arangam.model.Segment;

import java.util.Comparator;

/**
 * Created by yasar on 20/12/16.
 */
public class SortByDate implements Comparator<Segment> {
    @Override
    public int compare(Segment lhs, Segment rhs) {
        return lhs.getSegmentDate().compareTo(rhs.getSegmentDate());
    }
}
