package com.example.ankurrana.helloandroid;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TaskList extends AppCompatActivity implements DatePickerFragment.OnCompleteListener {
    private ListView tasksList;
    private TextView infoBox;
    private String token;
    private Context context;
    private Date date;
    private Button datePicketButton,nextDayButton,previousDayButton;
    private TextView dateTextView;
    private TaskAPIService taskAPIService;
    private final SimpleDateFormat dateFormatter = new SimpleDateFormat("EEE, MMM d, ''yy");
    private final SimpleDateFormat dateParser = new SimpleDateFormat("yyyy-MM-dd");

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tasks_list_layout);
        tasksList = (ListView) findViewById(R.id.taskList);
        infoBox = (TextView) findViewById(R.id.infoBox);
        context = this;
        Button newTaskButton = (Button) findViewById(R.id.newTaskButton);
        dateTextView = (TextView) findViewById(R.id.dateTextView);
        Intent intent = getIntent();

        previousDayButton = (Button) findViewById(R.id.previousDayButton);
        nextDayButton = (Button) findViewById(R.id.nextDayButton);

        Bundle b = intent.getExtras();
//        token = b.getString("token");
        token = intent.getStringExtra("token");
        setTitle("Welcome to TaskKeeper");

        newTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent newTaskActivityIntent = new Intent(context,newTaskActivity.class);
                newTaskActivityIntent.putExtra("token",token);
                startActivity(newTaskActivityIntent);
            }
        });
//        infoBox.setText(token);
        taskAPIService = new TaskAPIService(context, token);



        previousDayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                cal.setTime(date);
                cal.add(Calendar.DATE, -1);
                Date previousDate = cal.getTime();
                String mydate = dateParser.format(previousDate);

                onComplete(mydate);
            }
        });


        nextDayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                cal.setTime(date);
                cal.add(Calendar.DATE, 1);
                Date previousDate = cal.getTime();
                String mydate = dateParser.format(previousDate);

                onComplete(mydate);
            }
        });



        initInterface();
    }


    protected void onResume() {
        super.onResume();
        Log.i("Restarting Activity","Restarting ");
        initInterface();
    }


    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getFragmentManager(), "datePicker");
    }

    private void initInterface() {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH) + 1;
        int day = c.get(Calendar.DAY_OF_MONTH);
        Log.i("Date for which Data", year + "-" + month + "-" + day);

        onComplete(year + "-" + month + "-" + day);
    }

    @Override
    public void onComplete(String date)  {
//            Calendar d = (Calendar)date;
            Log.i("Date for which Data", date);

            this.date = setDateTextViewer(date);

            taskAPIService.get(date, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONArray tasksListJSONArray = new JSONArray();
                try {

                    tasksListJSONArray = response;
                    JSONObject jo;
                    final ArrayList<Task> tasksArrayList = new ArrayList<Task>();
                    for (int i = 0; i < tasksListJSONArray.length(); i++) {
                        jo = tasksListJSONArray.getJSONObject(i);
                        String desc = jo.getString("description");
                        String key = jo.getString("key");
                        Task a = new Task(key, desc);
                        tasksArrayList.add(a);
                    }
                    TaskListAdapter tla = new TaskListAdapter(context,android.R.layout.simple_list_item_1,tasksArrayList);
                    tasksList.setAdapter(tla);

                    tasksList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            Task task = (Task)adapterView.getItemAtPosition(i);
                            Log.i("Task","A Task is clicked!" + task.Description());
                            showTaskDetails(task.Key());
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("Failure", "Failure");
            }
        });
    }

    private Date setDateTextViewer(String date)  {
//        Date d = new Date(date);
        String dateArray[] = date.split("-");
        int year = Integer.parseInt(dateArray[0]);
        int month = Integer.parseInt(dateArray[1]);
        int day = Integer.parseInt(dateArray[2]);


        Date dateObject = new Date();
        try {
            dateObject = dateParser.parse(date);
            Log.i("date" ,dateObject.toString());

        } catch (ParseException e) {
            e.printStackTrace();
        }

        dateTextView.setText("You are viewing tasks for " + dateFormatter.format(dateObject));
//        dateTextView.setText(String.format("You are viewing tasks for " + df.parse(date).toString()));
        return dateObject;
    }
    public void showTaskDetails(String key){
        Intent intent = new Intent(context,TaskDetailsActivity.class);
        intent.putExtra("key",key);
        intent.putExtra("token",token);
        startActivity(intent);
    }

}
