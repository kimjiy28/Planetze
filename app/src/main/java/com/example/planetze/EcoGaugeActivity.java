package com.example.planetze;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.github.anastr.speedviewlib.SpeedView;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class EcoGaugeActivity extends AppCompatActivity {

    private SpeedView ecoGauge;
    private RecyclerView emissionBreakdownRecyclerView;
    private EmissionBreakdownAdapter adapter;
    private BottomNavigationView bottomNavigationView;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eco_gauge);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        // Bottom Navigation
        bottomNavigationView.setSelectedItemId(R.id.gauge);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.tracker) {
                startActivity(new Intent(getApplicationContext(), EcoTrackerActivity.class));
                return true;
            } else if (item.getItemId() == R.id.add) {
                startActivity(new Intent(getApplicationContext(), RecordActivity.class));
                return true;
            } else if (item.getItemId() == R.id.gauge) {
                startActivity(new Intent(getApplicationContext(), EcoGaugeActivity.class));
                return true;
            } else if (item.getItemId() == R.id.hub) {
//                startActivity(new Intent(getApplicationContext(), EcoHubActivity.class));
//                return true;
            }
            return false;
        });

        // Initialize Eco Gauge
        ecoGauge = findViewById(R.id.ecoGauge);

        // Set initial CO2e value
        ecoGauge.speedTo(72); // Replace 72

        // Initialize RecyclerView
        emissionBreakdownRecyclerView = findViewById(R.id.emissionBreakdownRecyclerView);
        emissionBreakdownRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Dummy data for emissions breakdown
        List<EmissionCategory> emissionData = new ArrayList<>();
        emissionData.add(new EmissionCategory("Transportation", 30));
        emissionData.add(new EmissionCategory("Energy", 20));
        emissionData.add(new EmissionCategory("Food", 15));
        emissionData.add(new EmissionCategory("Shopping", 7));

        // Set up RecyclerView adapter
        adapter = new EmissionBreakdownAdapter(emissionData);
        emissionBreakdownRecyclerView.setAdapter(adapter);
    }
}
