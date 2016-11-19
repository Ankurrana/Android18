package com.example.ankurrana.helloandroid;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class newTaskActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText description;
    private Spinner scheduleSpinner;
    private Button submit;
    private TaskAPIService taskAPIService;
    LinearLayout complexOptions;
    EditText dayOfWeek,dayOfMonth,weekOfMonth,weekOfYear,month,year;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_task);
        setTitle("Create a new Task");
        Intent intent = getIntent();
        Bundle b = intent.getExtras();
        String token = b.getString("token");


        scheduleSpinner = (Spinner) findViewById(R.id.spinner);
        description = (EditText) findViewById(R.id.descriptionView);
        submit = (Button) findViewById(R.id.submit);
        submit.setOnClickListener(this);
        taskAPIService = new TaskAPIService(this,token);
        complexOptions = (LinearLayout) findViewById(R.id.complexOptions);
        final List<String> scheduleStrings = new ArrayList<String>();
        scheduleStrings.add("today");
        scheduleStrings.add("tomorrow");
        scheduleStrings.add("week");
        scheduleStrings.add("month");
        scheduleStrings.add("complex");

        dayOfWeek = (EditText) findViewById(R.id.dayOfWeek);
        dayOfMonth = (EditText) findViewById(R.id.dayOfMonth);
        weekOfMonth = (EditText) findViewById(R.id.weekOfMonth);
        weekOfYear = (EditText) findViewById(R.id.weekOfYear);
        month = (EditText) findViewById(R.id.month);
        year = (EditText) findViewById(R.id.year);




        ArrayAdapter<String> ScheduleAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,scheduleStrings);
        scheduleSpinner.setAdapter(ScheduleAdapter);

//        scheduleSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                if( scheduleSpinner.getSelectedItem().toString().equals("complex")){
//                    complexOptions.setVisibility(View.VISIBLE);
//               }
//            }
//        });


        scheduleSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if( scheduleSpinner.getSelectedItem().toString().equals("complex")){
                    complexOptions.setVisibility(View.VISIBLE);
               }else{
                    complexOptions.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }


    @Override
    public void onClick(View view) {
        String desc = description.getText().toString();
        String schedule = scheduleSpinner.getSelectedItem().toString();

        JSONObject complexScheduleParams = new JSONObject();
        if(schedule.equals("complex")){
            try {
                complexScheduleParams.put("dayOfWeek", dayOfWeek.getText().toString());
                complexScheduleParams.put("dayOfMonth", dayOfMonth.getText().toString());
                complexScheduleParams.put("weekOfMonth", weekOfMonth.getText().toString());
                complexScheduleParams.put("weekOfYear", weekOfYear.getText().toString());
                complexScheduleParams.put("month", month.getText().toString());
                complexScheduleParams.put("year", year.getText().toString());
                schedule = complexScheduleParams.toString();
            }
            catch(JSONException e)
            {

            }
        }
        Log.i("Schdeule",schedule);
        Task t = new Task(desc,schedule,1);
        taskAPIService.save(t, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.i("Success", "A new Task Saved!");
                finish();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("Failed","Failed");
            }
        });
    }



}
