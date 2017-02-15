package com.think42lab.arangam.util;

import com.google.gson.Gson;

/**
 * Created by yasar on 19/12/16.
 */
public class JSONUtils {
    private static final Gson gson = new Gson();

    private JSONUtils() {
    }

    public static boolean isJSONValid(String jsonInString) {
        try {
            gson.fromJson(jsonInString, Object.class);
            return true;
        } catch (com.google.gson.JsonSyntaxException ex) {
            return false;
        }
    }
}
