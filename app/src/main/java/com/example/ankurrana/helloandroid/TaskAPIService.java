package com.example.ankurrana.helloandroid;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Ankur.Rana on 11/15/2016.
 */

public class TaskAPIService {
    private Context AppContext;
    private RequestQueue queue;
    private String token;
    private final String BaseURL = "http://taskkeeper.awesomeankur.com/api/tasks";

    TaskAPIService(Context a,String token){
        AppContext = a;
        this.token = token;
        queue = MySingleton.getInstance(AppContext).getRequestQueue();
    }

    public void get(String date, Response.Listener<JSONArray> Handler, Response.ErrorListener errorHandler ){

//        Map<String,String> params = new HashMap<String, String>();
//        params.put("date",date);
        String URL = BaseURL + "?date=" + date;
//        paramJSONArray.put(params);
//        JSONArray paramJSONArray = new JSONArray();

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET,URL,null,Handler,errorHandler){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return setTokenInHeaders();
            }
        };
        queue.add(jsonArrayRequest);
    }

    public void  getOne(String key,Response.Listener<JSONObject> Handler, Response.ErrorListener errorHandler){
        String URL = BaseURL + "/" + key;
        JsonObjectRequest jor = new JsonObjectRequest(Request.Method.GET,URL,null,Handler,errorHandler){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return setTokenInHeaders();
            }
        };
        queue.add(jor);
    }

    private Map<String, String> setTokenInHeaders() throws AuthFailureError {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("jwt",token);
        return map;
    }
}
