package com.example.myapplication;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends httpActivity {
    String status="";

    private EditText emailEditText;
    private EditText addressEditText;
    private EditText passwordEditText;
    private EditText ageEditText;
    private EditText FirstNameEditText;
    private EditText LastNameEditText;

    @Override
    protected void onCreate( Bundle savedInstanceState){
        super.onCreate (savedInstanceState);
        setContentView(R.layout.activity_main);
        FirstNameEditText = findViewById(R.id.first_name);
        LastNameEditText = findViewById(R.id.family_name);
        emailEditText = findViewById(R.id.email);
        passwordEditText = findViewById(R.id.password);
        ageEditText = findViewById(R.id.age);
        addressEditText = findViewById(R.id.address);
        Button signUpButton = findViewById(R.id.signup_button);
        signUpButton.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {
                signup();
            }
        });
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        String email = preferences.getString("email", "");
        if (!email.equals("")) {
            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        }
    }
    private void signup(){
        String first_name = FirstNameEditText.getText().toString();
        String family_name = LastNameEditText.getText().toString();
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        String age = ageEditText.getText().toString();
        String address = addressEditText.getText().toString();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ipadress+"/signup.php", new Response.Listener<String>(){
            @Override
            protected void responseReceived(String response, Map<String, String> params) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    if (status.equals("success")) {
                        // Get user information
                        String sessionToken = jsonObject.getString("session_token");
                        String sessionId = jsonObject.getString("session_id");

                        // Save user information to shared preferences
                        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("family_name", params.get("family_name"));
                        editor.putString("first_name", params.get("first_name"));
                        editor.putString("email", params.get("email"));
                        editor.putInt("age", Integer.parseInt(params.get("age")));
                        editor.putString("address", params.get("address"));
                        editor.putString("session_token", sessionToken);
                        editor.putString("session_id", sessionId);
                        editor.apply();

                        // Start HomeActivity
                        Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                        MainActivity(intent);
                        finish();
                    } else {
                        String errorMessage = jsonObject.getString("message");
                        Toast.makeText(MainActivity.this, errorMessage, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, "Error: " + error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String,String> getParams() throws AuthFailureError{//code smell
                Map<String,String> params = new HashMap<>();
                params.put("first_name", first_name);
                params.put("family_name", family_name);
                params.put("email",email);
                params.put ("password",password);
                params.put("age",age);
                params.put("address",address);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue (this);
        requestQueue.add(stringRequest);
    }
    public String getLastStatus() {
        return this.status;
    }//code smell
}
