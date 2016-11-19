package com.example.ankurrana.helloandroid;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Ankur.Rana on 11/19/2016.
 */
public class Globals {
    private static Globals ourInstance = new Globals();

    public static Globals getInstance() {
        return ourInstance;
    }
    private static JSONObject globalValues;
    private Globals() {
        globalValues = new JSONObject();
        try {
            globalValues.put("server","http://taskkeeper.awesomeankur.com");
            globalValues.put("apiAddress","http://taskkeeper.awesomeankur.com/api");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static String getString(String name){
        /*
        *   String name is the key name
        *
        * **/
        try {
            return globalValues.getString(name);
        } catch (JSONException e) {
            Log.i("Error Message","property " + name + " not found in globla variables");
            e.printStackTrace();
        }
        return "Unavailable";
    }

    public String getServer(){
        try {
            return globalValues.getString("server");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "Unavilable";
    }
    public String getAPIServer() {
        try {
            return globalValues.getString("apiAddress");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "Unavilable";
    }

}
