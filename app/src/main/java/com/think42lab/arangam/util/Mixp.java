package com.think42lab.arangam.util;

import android.app.Activity;
import android.util.Log;

import com.mixpanel.android.mpmetrics.MixpanelAPI;
import com.think42lab.arangam.MainActivity;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by yasar on 20/12/16.
 */
public class Mixp {

    public static void track(Activity activity, String value) {
        try {
            JSONObject props = new JSONObject();
            props.put(value, value);
//            props.put("Logged in", false);
            MixpanelAPI.getInstance(activity, MainActivity.projectToken).track(value, props);
        } catch (JSONException e) {
            Log.e("MYAPP", "Unable to add properties to JSONObject", e);
        }
    }
}
