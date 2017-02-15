package com.think42lab.arangam.util;

import com.think42lab.arangam.model.Segment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;

import static com.think42lab.arangam.util.DateUtil.getCalendarWithoutTime;

/**
 * Created by yasar on 20/12/16.
 */
public class DateSort implements Comparator<Segment> {

    @Override
    public int compare(Segment lhs, Segment rhs) {
        Calendar lhsSegmentDate = DateUtil.convertStringToCalendar(lhs.getPeriod(), "yyyy-MM-dd'T'HH:mm");
        Calendar rhsSegmentDate = DateUtil.convertStringToCalendar(rhs.getPeriod(), "yyyy-MM-dd'T'HH:mm");
        return lhsSegmentDate.compareTo(rhsSegmentDate);
    }
}
