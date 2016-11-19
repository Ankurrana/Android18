package com.example.ankurrana.helloandroid;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class TaskDetailsActivity extends AppCompatActivity {
    private String key;
    private TextView description,status,owner,startDate,completedAt;
    private String token;
    private Button deleteButton;
    private TaskAPIService taskAPIService;
    private ListView checkpoints;
    private final Context context = this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_details);
        setTitle("Task Details");
        description = (TextView) findViewById(R.id.description);
        status = (TextView) findViewById(R.id.status);
        owner = (TextView) findViewById(R.id.owner);
        completedAt = (TextView) findViewById(R.id.completedAt);
        startDate = (TextView) findViewById(R.id.startDate);
        deleteButton = (Button) findViewById(R.id.deleteButtonId);
        checkpoints = (ListView) findViewById(R.id.checkpoints);


        description.setTextColor(Color.BLACK);

        Intent intent = getIntent();
        Bundle b = intent.getExtras();
        key = b.getString("key");
        token = b.getString("token");

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteTask(key);
            }
        });
        initiate();
    }


    @Override
    protected void onResume() {
        super.onResume();
        Log.i("Restarting ACtivity","Restarting ");
        initiate();
    }

    public void initiate(){
        taskAPIService = new TaskAPIService(this,token);
        taskAPIService.getOne(key, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                JSONObject jo = response;

                String statusString = null;
                String keyString = null;
                String descString = null;
                String author = null;
                String StartDate = null;
                String CompletedAt = null;
                JSONArray checkpointsArrayObject = new JSONArray();
                try {
                    descString = jo.getString("description");
                    keyString = jo.getString("key");
                    statusString = jo.getString("status");
                    author = jo.getString("author");
                    StartDate = jo.getString("startDate");
                    checkpointsArrayObject = jo.getJSONArray("checkpoints");
                    Log.i("CHECKPOINTS",checkpointsArrayObject.toString());
                    if(jo.has("completedAt")){
                        CompletedAt = jo.getString("completedAt");
                    }else{
                        CompletedAt = "Not Completed At";
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                List<String> checkpointStrings = new ArrayList<String>();
                for(int i=0;i<checkpointsArrayObject.length();i++) {
                    try {
                        JSONObject checkpointJSONObject = checkpointsArrayObject.getJSONObject(i);
                        checkpointStrings.add( checkpointJSONObject.getString("description"));
                        Log.i("Descrition",checkpointJSONObject.getString("description"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

                ArrayAdapter<String> aa = new ArrayAdapter<String>(context,android.R.layout.simple_list_item_1,android.R.id.text1,checkpointStrings);
                checkpoints.setAdapter(aa);

                status.setText(statusString);
                description.setText(descString);
                owner.setText(author);
                startDate.setText(StartDate);
                completedAt.setText(CompletedAt);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("Error", "Task was not read!");
            }
        });
    }

    private void deleteTask(String key) {
        taskAPIService.delete(key, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.i("Delete","Task Deleted Successfully");
                finishActivity(0);
                finish();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("Delete","Task deletion failed!");
            }
        });
    }
}
