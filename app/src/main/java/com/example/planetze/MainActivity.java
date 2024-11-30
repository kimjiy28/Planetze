package com.example.planetze;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // After the survey
        Intent ecoTracker = new Intent(this, EcoTrackerActivity.class);
        startActivity(ecoTracker);
    }

}