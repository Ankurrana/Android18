package com.example.ankurrana.helloandroid;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
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
import android.widget.Toast;

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

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private EditText email, password;
    private Button loginButton;
    private RequestQueue rq;
    private TextView message,registerLink;
    private Context context;
    private String loginUrl = Globals.getInstance().getInstance().getAPIServer()+ "/token";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;

        setTitle("TaskKeeper");
        message = (TextView) findViewById(R.id.message);
        loginButton = (Button)findViewById(R.id.login);
        registerLink = (TextView) findViewById(R.id.register);
        registerLink.setOnClickListener(this);
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

    @Override
    protected void onResume() {
        super.onResume();
        Intent i = getIntent();
        Bundle b = i.getExtras();
        if( b!=null && b.containsKey("message") ) {
            Context context = getApplicationContext();
            CharSequence text = b.getString("message");
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
            getIntent().getExtras().clear();
        }
    }

    private void login(String username, String password){
        String url = this.loginUrl;
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
                    //info.setText(token);
                } catch (JSONException e) {
                    message.setText("Incorrect Credentials");
                }
                Log.i("Success","Login Successfull");
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        message.setText("Unable to communicate server");
                        Log.i("Failed","Login Failed");
                    }
                });
        RequestQueue queue = MySingleton.getInstance(this.getApplicationContext()).getRequestQueue();
        queue.add(jsonObjectRequest);
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(context,RegisterActivity.class);
        startActivity(intent);
    }
}
