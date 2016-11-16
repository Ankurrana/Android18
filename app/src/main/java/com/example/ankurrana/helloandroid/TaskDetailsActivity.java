package com.example.ankurrana.helloandroid;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

public class TaskDetailsActivity extends AppCompatActivity {
    private String key;
    private TextView description,status;
    private String token;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_details);
        description = (TextView) findViewById(R.id.description);
        status = (TextView) findViewById(R.id.status);

        Intent intent = getIntent();
        Bundle b = intent.getExtras();
        key = b.getString("key");
        token = b.getString("token");

        status.setText(key);
        description.setText(key);

        TaskAPIService taskAPIService = new TaskAPIService(this,token);
        taskAPIService.getOne(key, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                JSONObject jo = response;

                String statusString = null;
                String keyString = null;
                String descString = null;
                String author = null;
                String StartDate = null;
                String completedAt = null;
                try {
                    descString = jo.getString("description");
                    keyString = jo.getString("key");
                    statusString = jo.getString("status");
                    author = jo.getString("author");
                    StartDate = jo.getString("startDate");
                    completedAt = jo.getString("completedAt");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                status.setText(statusString);

                description.setText(descString);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("Error", "Task was not read!");
            }
        });

    }
}
