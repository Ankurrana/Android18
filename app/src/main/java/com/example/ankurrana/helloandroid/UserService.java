package com.example.ankurrana.helloandroid;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by Ankur.Rana on 11/18/2016.
 */

public class UserService {
    private Context AppContext;
    private RequestQueue queue;
    private final String BaseURL = Globals.getInstance().getInstance().getAPIServer()+ "/users";
    private String token;
    UserService(Context context){
        AppContext = context;
        queue = MySingleton.getInstance(AppContext).getRequestQueue();
    }

    UserService(Context context,String token){
        AppContext = context;
        this.token = token;
        queue = MySingleton.getInstance(AppContext).getRequestQueue();
    }

    public void save(String name, String username, String email, String password, Response.Listener<JSONObject> Handler, Response.ErrorListener errorHandler){
        String URL =BaseURL;
        HashMap<String,String> params = new HashMap<String,String>  ();
        params.put("name",name);
        params.put("username",username);
        params.put("email",email);
        params.put("password",password);

        JSONObject pars = new JSONObject(params);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URL,pars ,Handler,errorHandler);
        queue.add(jsonObjectRequest);
    }
}
