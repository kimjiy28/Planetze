package com.example.planetze;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class EcoTrackerBreakdownActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eco_tracker_breakdown);
    }

    public void launchRecord(View v) {
        Intent record = new Intent(this, RecordActivity.class);
        startActivity(record);
    }
}