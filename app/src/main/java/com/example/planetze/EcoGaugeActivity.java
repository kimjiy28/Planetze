package com.example.planetze;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.github.anastr.speedviewlib.SpeedView;
import java.util.ArrayList;
import java.util.List;

public class EcoGaugeActivity extends AppCompatActivity {

    private SpeedView ecoGauge;
    private RecyclerView emissionBreakdownRecyclerView;
    private EmissionBreakdownAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eco_gauge);

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
