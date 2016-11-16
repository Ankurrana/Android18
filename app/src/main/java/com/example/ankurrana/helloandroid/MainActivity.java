package com.example.ankurrana.helloandroid;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import com.android.volley.*;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.loopj.android.http.*;
import cz.msebera.android.httpclient.*;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.Console;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private EditText email, password;
    private Button loginButton;
    private TextView info;
    private RequestQueue rq;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        info = (TextView) findViewById(R.id.info);
//        info.setText("Find Info here!");
        loginButton = (Button)findViewById(R.id.login);

        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login(email.getText().toString(),password.getText().toString());
            }
        });

        // Comment these once the testing is complete
        email.setText("ankurrana");
        password.setText("ankurrana");
        loginButton.performClick();
    }

    private void openTasksListActivity(String token){
        Intent intent= new Intent(MainActivity.this,TaskList.class);
        Bundle b = new Bundle();
        b.putString("token",token);
        intent.putExtra("token",token);
        intent.putExtras(b);
        startActivity(intent);
    }


    private void login(String username, String password){
        String url ="http://taskkeeper.awesomeankur.com/api/token";
        Map<String, String> params = new HashMap<String, String>();
        params.put("username",username);
        params.put("password",password);


        JSONObject parameters = new JSONObject(params);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url,
                parameters,new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String token =  response.getString("token");
                    openTasksListActivity(token);
                    info.setText(token);
                } catch (JSONException e) {
                    info.setText("Incorrect Credentials Provided!");
                }
                Log.i("Success","Login Successfull");
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("Failed","Login Failed");
                    }
                });
        RequestQueue queue = MySingleton.getInstance(this.getApplicationContext()).getRequestQueue();
        queue.add(jsonObjectRequest);
    }

}
