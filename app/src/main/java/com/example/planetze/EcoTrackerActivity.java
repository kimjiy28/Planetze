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
import androidx.appcompat.widget.Toolbar;
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

public class EcoTrackerActivity extends AccountActivity implements BreakdownViewInterface {

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    // DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users").child(user.getUid());
    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users").
            child("4U6brQiObtULMg94EPA2zN6nH2h1");
    int progress = 0;
    boolean started = true;
    private static String date;
    private double dailyEmission;
    private ArrayList<Breakdown> activityList;
    private BreakdownAdapter adapter;

    // Views
    private Toolbar appbar;
    EditText calendarManagement;
    TextView textView;
    WaveProgressBar waveProgressBar;
    RecyclerView recyclerView;
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eco_tracker);
        BreakdownViewInterface breakdownViewInterface = this;

        // Upper Toolbar
        appbar = findViewById(R.id.appbar);
        setSupportActionBar(appbar);

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
        if (RecordActivity.getDate() != null) {
            date = RecordActivity.getDate();
            calendarManagement.setText(date);
        }
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
                        date = year + "-" + (month + 1) + "-" + dayOfMonth;
                        calendarManagement.setText(date);

                        // Display Carbon Emission Footprint
                        fetchDailyEmission();

                        // Display Breakdown of Activities
                        recyclerView.setLayoutManager(new LinearLayoutManager(EcoTrackerActivity.this));
                        activityList = new ArrayList<Breakdown>();
                        adapter = new BreakdownAdapter(EcoTrackerActivity.this, activityList,
                                breakdownViewInterface);
                        recyclerView.setAdapter(adapter);
                        fetchBreakdown();
                    }
                }, year, month, day);
                picker.show();
            }
        });

        if (date != null) {
            calendarManagement.setText(date);

            // Display Carbon Emission Footprint
            fetchDailyEmission();

            // Display Breakdown of Activities
            recyclerView.setLayoutManager(new LinearLayoutManager(EcoTrackerActivity.this));
            activityList = new ArrayList<Breakdown>();
            adapter = new BreakdownAdapter(EcoTrackerActivity.this, activityList,
                    breakdownViewInterface);
            recyclerView.setAdapter(adapter);
            fetchBreakdown();
        }

    }

    private void fetchDailyEmission() {
        DatabaseReference ref = reference.child(date).child("dailyEmission");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue() != null) {
                    dailyEmission = snapshot.getValue(Double.class);
                    textView.setText(Double.toString(dailyEmission));
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Firebase", "Failed to fetch Daily Emission", error.toException());
            }
        });
    }

    private void fetchBreakdown() {
        DatabaseReference ref = reference.child(date).child("activities");
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
                Log.e("Firebase", "Failed to fetch Activity List", error.toException());
            }
        });
    }

    public static String getDate() {
        return date;
    }

    @Override
    public void onItemClicked(int position, ArrayList<Breakdown> activities) {
        Intent update = new Intent(EcoTrackerActivity.this, RecordActivity.class);
        update.putExtra("id", activities.get(position).getId());
        startActivity(update);
    }
}