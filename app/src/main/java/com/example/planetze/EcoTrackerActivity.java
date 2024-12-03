package com.example.planetze;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

import cjh.WaveProgressBarlibrary.WaveProgressBar;

public class EcoTrackerActivity extends AppCompatActivity {

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    // DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users").child(user.getUid());
    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users").child("c04ro6FxLuaJ6TegP0wPmT8dzvp2");
    int progress = 0;
    boolean started = true;
    private int dailyEmission;
    private ArrayList<Breakdown> activityList;
    private BreakdownAdapter adapter;

    // Views
    EditText calendarManagement;
    TextView textView;
    WaveProgressBar waveProgressBar;
    RecyclerView recyclerView;
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eco_tracker);

        // Views Initialization
        calendarManagement = findViewById(R.id.calendarManagement);
        textView = findViewById(R.id.dailyEmission);
        waveProgressBar = findViewById(R.id.waveProgressBar);
        recyclerView = findViewById(R.id.ecoRecyclerView);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        // Bottom Navigation
        bottomNavigationView.setSelectedItemId(R.id.tracker);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.habit) {
                startActivity(new Intent(getApplicationContext(), HabitActivity.class));
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


        // Progress Animation
        Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                if (started) {
                    progress++;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            waveProgressBar.setProgress(progress);
                        }
                    });
                    if (progress == 45) {
                        started = !started;
                    }
                }
            }
        };

        timer.schedule(timerTask, 0, 80);
        waveProgressBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progress = 0;
                started = !started;
            }
        });

        // Calendar Management
        calendarManagement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();
                int day = calendar.get(calendar.DAY_OF_MONTH);
                int month = calendar.get(calendar.MONTH);
                int year = calendar.get(calendar.YEAR);

                DatePickerDialog picker = new DatePickerDialog(EcoTrackerActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String date = Integer.toString(year * 10000 + (month + 1) * 100 + dayOfMonth);
                        calendarManagement.setText(year + "/" + (month + 1) + "/" + dayOfMonth);
                        reference = reference.child(date);

                        // Display Carbon Emission Footprint
                        fetchDailyEmission();

                        // Display Breakdown of Activities
                        recyclerView.setLayoutManager(new LinearLayoutManager(EcoTrackerActivity.this));
                        activityList = new ArrayList<Breakdown>();
                        adapter = new BreakdownAdapter(EcoTrackerActivity.this, activityList);
                        recyclerView.setAdapter(adapter);
                        fetchBreakdown();
                    }
                }, year, month, day);
                picker.show();
            }
        });
    }

    private void fetchDailyEmission() {
        DatabaseReference ref = reference.child("dailyEmission");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue() != null) {
                    dailyEmission = snapshot.getValue(Integer.class);
                    textView.setText(Integer.toString(dailyEmission));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Firebase", "Failed to fetch preexisting habits", error.toException());
            }
        });
    }
    private void fetchBreakdown() {
        DatabaseReference ref = reference.child("activities");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                activityList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Breakdown activity = snapshot.getValue(Breakdown.class);
                    if (activity != null) {
                        activity.setId(snapshot.getKey());
                        activityList.add(activity);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Firebase", "Failed to fetch preexisting habits", error.toException());
            }
        });
    }
}