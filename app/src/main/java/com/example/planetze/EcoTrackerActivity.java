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

    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    int progress = 0;
    boolean started = true;

    private String date;
    private int dailyEmission;
    private ArrayList<Breakdown> activityList;
    private BreakdownAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eco_tracker);

        // Selecting Date
        EditText calendarManagement = findViewById(R.id.calendarManagement);
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
                        date = Integer.toString(year * 10000 + (month + 1) * 100 + dayOfMonth);
                        calendarManagement.setText(year + "/" + (month + 1) + "/" + dayOfMonth);

                        // TODO: consider cases where data for the selected date doesn't exist.
                        WaveProgressBar waveProgressBar = findViewById(R.id.waveProgressBar);
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

                        // TODO: setText not working properly
                        fetchDailyEmission();
                        TextView textView = findViewById(R.id.dailyEmission);
                        textView.setText(Integer.toString(dailyEmission));

                        // Display Breakdown of Activities
                        RecyclerView recyclerView = findViewById(R.id.ecoRecyclerView);
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
        // ref.child(user.getUid()).child(date).child("emission");
        DatabaseReference ref = reference.child("X8cKomTutObPCZf5aXyhM2xwnUJ2").child(date).child("dailyEmission");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dailyEmission = snapshot.getValue(Integer.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Firebase", "Failed to fetch preexisting habits", error.toException());
            }
        });
    }
    private void fetchBreakdown() {
        // ref.child(user.getUid()).child(date).child("emission");
        DatabaseReference ref = reference.child("X8cKomTutObPCZf5aXyhM2xwnUJ2").child(date).child("activities");
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