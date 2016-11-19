package com.example.ankurrana.helloandroid;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONObject;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText name,username,email,password;
    private Button register;
    private UserService userService;
    private Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        setTitle("Register");

        /* Initializing Controls */

        name = (EditText) findViewById(R.id.name);
        username = (EditText) findViewById(R.id.username);
        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        register = (Button) findViewById(R.id.submit);
        context = this;

        register.setOnClickListener(this);
        userService = new UserService(this);

    }




    private boolean validate(EditText a,String validationString){
        /*
        * Validations String is a | separated list of validations
        * */
        String[] validations = validationString.split("\\|");

        for(String validation : validations){
            if(validation.equalsIgnoreCase("required")){
                if(a.getText().toString().isEmpty() || a.getText() == null){
                    a.setError("Required Field");
                    return false;
                }
            }
        }

        return true;
    }


    @Override
    public void onClick(View view) {

        Boolean isInputValid;

        isInputValid = validate(name,"required|none");
        isInputValid &= validate(username,"required|none");
        isInputValid &= validate(email,"required|none");
        isInputValid &= validate(password,"required|none");


        if(isInputValid){
            String _name = name.getText().toString();
            String _username = username.getText().toString();
            String _email = email.getText().toString();
            String _password = password.getText().toString();

            Log.i("password",_password);
            userService.save(_name, _username, _email, _password, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.i("Success", "User saved!");

                            Intent i = new Intent(context ,MainActivity.class);
                            i.putExtra("message","Welcome " + name.getText().toString() +". Login to continue" );
                            startActivity(i);
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.i("Failed","Failed to Store User!");
                        }
                    });

        }else{
            Log.i("Invalid Input","Invalid Input");
        }
    }
}
