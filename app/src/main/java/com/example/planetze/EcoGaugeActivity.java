package com.example.planetze;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.anastr.speedviewlib.SpeedView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EcoGaugeActivity extends AccountActivity {

    private static final String TAG = "EcoGaugeActivity";

    private SpeedView ecoGauge;
    private RecyclerView emissionBreakdownRecyclerView;
    private EmissionBreakdownAdapter adapter;
    private BottomNavigationView bottomNavigationView;
    private Toolbar toolbar;

    private DatabaseReference userEmissionsRef;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eco_gauge);

        // Firebase user validation
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        String userId = currentUser.getUid();

        // Firebase reference for user emissions
        userEmissionsRef = FirebaseDatabase.getInstance()
                .getReference("users")
                .child(userId);

        // Views initialization
        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        // Upper Toolbar
        toolbar = findViewById(R.id.appbar);
        setSupportActionBar(toolbar);

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

        // Initialize RecyclerView
        emissionBreakdownRecyclerView = findViewById(R.id.emissionBreakdownRecyclerView);
        emissionBreakdownRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Set up RecyclerView adapter
        List<EmissionCategory> emissionData = new ArrayList<>();
        adapter = new EmissionBreakdownAdapter(emissionData);
        emissionBreakdownRecyclerView.setAdapter(adapter);

        // Fetch monthly emissions and category breakdown
        fetchMonthlyEmissionsAndBreakdown();
    }

    private void fetchMonthlyEmissionsAndBreakdown() {
        userEmissionsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    Log.d(TAG, "No emissions data found for user.");
                    ecoGauge.speedTo(0); // Set gauge to 0 if no data
                    return;
                }

                // Get the current month and year
                Calendar calendar = Calendar.getInstance();
                int currentMonth = calendar.get(Calendar.MONTH);
                int currentYear = calendar.get(Calendar.YEAR);

                long totalMonthlyEmissions = 0;
                Map<String, Long> categoryEmissions = new HashMap<>();

                for (DataSnapshot dateSnapshot : dataSnapshot.getChildren()) {
                    String dateKey = dateSnapshot.getKey();
                    Log.d(TAG, "Processing date key: " + dateKey);

                    // Skip non-date keys
                    if (dateKey == null || !dateKey.matches("\\d{4}-\\d{2}-\\d{2}")) {
                        Log.d(TAG, "Skipping non-date key: " + dateKey);
                        continue;
                    }

                    String[] dateParts = dateKey.split("-");
                    if (dateParts.length != 3) continue;

                    try {
                        int year = Integer.parseInt(dateParts[0]);
                        int month = Integer.parseInt(dateParts[1]) - 1;

                        if (year == currentYear && month == currentMonth) {
                            DataSnapshot activitiesSnapshot = dateSnapshot.child("activities");
                            for (DataSnapshot activitySnapshot : activitiesSnapshot.getChildren()) {
                                String category = activitySnapshot.child("category").getValue(String.class);
                                Long emission = activitySnapshot.child("emission").getValue(Long.class);

                                if (category != null && emission != null) {
                                    totalMonthlyEmissions += emission;

                                    categoryEmissions.put(category,
                                            categoryEmissions.getOrDefault(category, 0L) + emission);
                                }
                            }
                        }
                    } catch (NumberFormatException e) {
                        Log.e(TAG, "Error parsing date: " + dateKey, e);
                    }
                }

                Log.d(TAG, "Total Monthly Emissions: " + totalMonthlyEmissions);
                ecoGauge.speedTo((int) totalMonthlyEmissions);

                List<EmissionCategory> emissionData = new ArrayList<>();
                for (Map.Entry<String, Long> entry : categoryEmissions.entrySet()) {
                    String category = entry.getKey();
                    long emission = entry.getValue();
                    int percentage = totalMonthlyEmissions == 0 ? 0 :
                            (int) ((emission * 100.0) / totalMonthlyEmissions);
                    emissionData.add(new EmissionCategory(category, (double) percentage));
                }

                Log.d(TAG, "Category Breakdown: " + emissionData.toString());
                adapter.updateEmissionList(emissionData);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "Failed to fetch emissions data", databaseError.toException());
                Toast.makeText(EcoGaugeActivity.this, "Failed to fetch emissions data", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
