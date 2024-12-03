package com.example.planetze;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class EcoHubActivity extends AccountActivity {

    // Views
    private Toolbar appbar;
    private RecyclerView recyclerView;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eco_hub);

        // Upper Toolbar
        appbar = findViewById(R.id.appbar);
        setSupportActionBar(appbar);

        // Bottom Navigation
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.tracker);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.tracker) {
                startActivity(new Intent(getApplicationContext(), EcoTrackerActivity.class));
                return true;
            } else if (item.getItemId() == R.id.habit) {
                startActivity(new Intent(getApplicationContext(), HabitActivity.class));
                return true;
            } else if (item.getItemId() == R.id.add) {
                startActivity(new Intent(getApplicationContext(), RecordActivity.class));
                return true;
            } else if (item.getItemId() == R.id.gauge) {
                startActivity(new Intent(getApplicationContext(), EcoGaugeActivity.class));
                return true;
            }
            return false;
        });

        recyclerView = findViewById(R.id.resourceRecyclerView);
        ResourceRepository resourceRepository = new ResourceRepository(this);
        ResourceAdapter adapter = new ResourceAdapter(this, resourceRepository.getResources());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }
}