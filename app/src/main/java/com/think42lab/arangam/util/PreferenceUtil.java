package com.think42lab.arangam.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.think42lab.arangam.Adapters.GroupedScheduleRecyclerAdapter;
import com.think42lab.arangam.Adapters.ScheduleAdapter;
import com.think42lab.arangam.ArtistsGroupActivity;
import com.think42lab.arangam.model.Artists;
import com.think42lab.arangam.model.Segment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by think42lab on 20/12/16.
 */

public class PreferenceUtil {

    public static final Gson GSON = new Gson();

    public static final String PREF_FILE_NAME = "PreferenceUtil";

    public static final String SEGMENT_PREFERENCES = "SegmentPreferences";

    public static final String ARTIST_PREFERENCES = "ArtistPreferences";

    public static final String FAVORITE_PREFERENCES = "FavoritePreferences";

    public synchronized static SharedPreferences getSharedPreference(final Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_FILE_NAME, context.MODE_PRIVATE);
        return prefs;
    }

    public synchronized static SharedPreferences.Editor getSharedPreferenceEditor(final Context context) {
        return getSharedPreference(context).edit();
    }


    public synchronized static void saveSegments(final Context context, List<Segment> segmentList) {
        SharedPreferences.Editor editor = getSharedPreferenceEditor(context);
        Gson gson = new Gson();
        String jsonSegmentList = gson.toJson(segmentList);
        editor.putString(SEGMENT_PREFERENCES, jsonSegmentList);
        editor.apply();         //editor.commit();;
    }

    public synchronized static void clearSegments(final Context context) {
        Gson gson = new Gson();
        String jsonSegmentList = gson.toJson(new ArrayList<Segment>());
        SharedPreferences.Editor editor = getSharedPreferenceEditor(context);
        editor.putString(SEGMENT_PREFERENCES, jsonSegmentList);
        editor.apply();         //editor.commit();;
    }

    public synchronized static void addSegment(final Context context, Segment segment) {
        List<Segment> segmentList = getSegmentList(context);
        if (segmentList == null)
            segmentList = new ArrayList<Segment>();
        segmentList.add(segment);
        saveSegments(context, segmentList);
    }

    public synchronized static void removeSegment(final Context context, Segment segment) {
        ArrayList<Segment> segmentArrayList = getSegmentList(context);
        if (segmentArrayList != null) {
            segmentArrayList.remove(segment);
            saveSegments(context, segmentArrayList);
        }
    }

    public synchronized static ArrayList<Segment> getSegmentList(final Context context) {
        SharedPreferences sharedPreferences = getSharedPreference(context);
        List<Segment> segmentList;
        if (sharedPreferences.contains(SEGMENT_PREFERENCES)) {
            String jsonSegment = sharedPreferences.getString(SEGMENT_PREFERENCES, null);
            Gson gson = new Gson();
            Segment[] segments = gson.fromJson(jsonSegment,
                    Segment[].class);

            segmentList = Arrays.asList(segments);
            segmentList = new ArrayList<Segment>(segmentList);
        } else
            return null;
        return (ArrayList<Segment>) segmentList;
    }

    public synchronized static ArrayList<Segment> getFavoriteSegmentList(final Context context) {
        SharedPreferences sharedPreferences = getSharedPreference(context);
        List<Segment> segmentList;
        if (sharedPreferences.contains(FAVORITE_PREFERENCES)) {
            String jsonSegment = sharedPreferences.getString(FAVORITE_PREFERENCES, null);
            Gson gson = new Gson();
            Segment[] segments = gson.fromJson(jsonSegment,
                    Segment[].class);

            segmentList = Arrays.asList(segments);
            segmentList = new ArrayList<Segment>(segmentList);
        } else
            return null;
        return (ArrayList<Segment>) segmentList;
    }

    public synchronized static void saveFavoriteSegments(final Context context, List<Segment> segmentList) {
        SharedPreferences.Editor editor = getSharedPreferenceEditor(context);
        Gson gson = new Gson();
        String jsonSegmentList = gson.toJson(segmentList);
        editor.putString(FAVORITE_PREFERENCES, jsonSegmentList);
        editor.apply();         //editor.commit();;
    }

    public synchronized static void clearFavoriteSegments(final Context context) {
        SharedPreferences.Editor editor = getSharedPreferenceEditor(context);
        Gson gson = new Gson();
        String jsonSegmentList = gson.toJson(new ArrayList<Segment>());
        editor.putString(FAVORITE_PREFERENCES, jsonSegmentList);
        editor.apply();         //editor.commit();;
    }

    public synchronized static void addFavoriteSegment(final Context context, Segment segment) {
        List<Segment> segmentList = getFavoriteSegmentList(context);
        if (segmentList == null)
            segmentList = new ArrayList<Segment>();
        segmentList.add(segment);
        saveFavoriteSegments(context, segmentList);
    }

    public synchronized static void removeFavoriteSegment(final Context context, Segment segment) {
        ArrayList<Segment> segmentArrayList = getFavoriteSegmentList((context));
        if (segmentArrayList != null) {
            segmentArrayList.remove(segment);
            saveFavoriteSegments(context, segmentArrayList);
        }
    }

    public synchronized static void updateFavoriteStatus(final Context context, Segment segment){
        List<Segment> segmentList = getFavoriteSegmentList((context));
        Segment presentSegment = null;
        if (segmentList != null) {
            for (Segment segmentNew : segmentList){
                if(segmentNew.getId().equalsIgnoreCase(segment.getId())){
                    presentSegment = segmentNew;
                    break;
                }
            }
        }else {
            segmentList = new ArrayList<Segment>();
        }

        if(presentSegment == null) {
            segmentList.add(segment);
        }else {
            segmentList.remove(presentSegment);
        }

        saveFavoriteSegments(context, segmentList);
    }


    public synchronized static<T> void updateFavoriteStatus(final Context context, Segment segment, T listener){
        List<Segment> segmentList = getFavoriteSegmentList((context));
        Segment presentSegment = null;
        if (segmentList != null) {
            for (Segment segmentNew : segmentList){
                if(segmentNew.getId().equalsIgnoreCase(segment.getId())){
                    presentSegment = segmentNew;
                    break;
                }
            }
        }else {
            segmentList = new ArrayList<Segment>();
        }

        if(presentSegment == null) {
            segmentList.add(segment);
            if(listener instanceof ScheduleAdapter.ActionListener){
                ((ScheduleAdapter.ActionListener)listener).onFavorite(segment);
            }else if(listener instanceof GroupedScheduleRecyclerAdapter.ActionListener){
                ((GroupedScheduleRecyclerAdapter.ActionListener)listener).onFavorite(segment);
            }

        }else {
            segmentList.remove(presentSegment);
            if(listener instanceof ScheduleAdapter.ActionListener){
                ((ScheduleAdapter.ActionListener)listener).onUnFavorite(segment);
            }else if(listener instanceof GroupedScheduleRecyclerAdapter.ActionListener){
                ((GroupedScheduleRecyclerAdapter.ActionListener)listener).onUnFavorite(segment);
            }
        }

        saveFavoriteSegments(context, segmentList);
    }


    public synchronized static boolean getFavoriteStatus(final Context context, Segment segment) {
        List<Segment> segmentList = getFavoriteSegmentList((context));
        if (segmentList != null) {
            for (Segment segmentNew : segmentList){
                if(segmentNew.getId().equalsIgnoreCase(segment.getId())){
                    return true;
                }
            }
        }
        return false;
    }

    public synchronized static int getFavoriteCount(final Context context){
        List<Segment> favoriteList = getFavoriteSegmentList(context);
        return (favoriteList != null ) ? favoriteList.size() : 0;
    }



    public synchronized static void saveArtists(final Context context, List<Artists> artistsList) {
        SharedPreferences.Editor editor = getSharedPreferenceEditor(context);
        Gson gson = new Gson();
        String jsonArtistsList = gson.toJson(artistsList);
        editor.putString(ARTIST_PREFERENCES, jsonArtistsList);
        editor.apply();         //editor.commit();;
    }

    public synchronized static void clearArtists(final Context context) {
        SharedPreferences.Editor editor = getSharedPreferenceEditor(context);
        Gson gson = new Gson();
        String jsonArtistsList = gson.toJson(new ArrayList<Artists>());
        editor.putString(ARTIST_PREFERENCES, jsonArtistsList);
        editor.apply();         //editor.commit();;
    }

    public synchronized static void addArtist(final Context context, Artists artists) {
        List<Artists> artistsList = getArtistList((context));
        if (artistsList == null)
            artistsList = new ArrayList<Artists>();
        artistsList.add(artists);
        saveArtists(context, artistsList);
    }

    public synchronized static void removeArtist(final Context context, Artists segment) {
        ArrayList<Artists> artistsList = getArtistList(context);
        if (artistsList != null) {
            artistsList.remove(segment);
            saveArtists(context, artistsList);
        }
    }

    public synchronized static ArrayList<Artists> getArtistList(final Context context) {
        SharedPreferences sharedPreferences = getSharedPreference(context);
        List<Artists> artistsList;
        if (sharedPreferences.contains(ARTIST_PREFERENCES)) {
            String jsonArtist = sharedPreferences.getString(ARTIST_PREFERENCES, null);
            Gson gson = new Gson();
            Artists[] artistses = gson.fromJson(jsonArtist,
                    Artists[].class);

            artistsList = Arrays.asList(artistses);
            artistsList = new ArrayList<Artists>(artistsList);
        } else
            return null;
        return (ArrayList<Artists>) artistsList;
    }

}
