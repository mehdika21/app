package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.TextView;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        SharedPreferences preferences =PreferenceManager.getDefaultSharedPreferences(this);
        String firstName = preferences.getString("first_name", "");
        String familyName = preferences.getString("family_name", "");
        TextView welcomeTextView = findViewById(R.id.welcome_textview);
        welcomeTextView.setText("Hello " + firstName + " " + familyName);
    }
}