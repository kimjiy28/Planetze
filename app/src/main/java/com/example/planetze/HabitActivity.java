package com.example.planetze;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.planetze.ui.dashboard.Habit;
import com.example.planetze.ui.dashboard.HabitAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HabitActivity extends AppCompatActivity {

    private static final String TAG = "HabitActivity";

    private RecyclerView habitRecyclerView;
    private HabitAdapter habitAdapter;

    private DatabaseReference habitsRef;
    private List<Habit> habitList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habit);

        // Initialize Firebase Realtime Database
        Log.d("Firebase", "Initializing Firebase");
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://planetze-4ccca-default-rtdb.firebaseio.com/");


        if (database != null) {
            Log.d("Firebase", "Firebase initialized successfully");
        } else {
            Log.e("Firebase", "Failed to initialize Firebase");
        }

        habitsRef = database.getReference("habits");

        // Initialize RecyclerView
        habitRecyclerView = findViewById(R.id.habitRecyclerView);
        habitRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize Habit List and Adapter
        habitList = new ArrayList<>();
        habitAdapter = new HabitAdapter(habitList,
                this::incrementHabitDays, // Handle increment logic
                this::deleteHabit,true);
        habitRecyclerView.setAdapter(habitAdapter);

        // Fetch Habits from Realtime Database
        fetchHabits();

        // Add New Habit Button
        findViewById(R.id.addHabitButton).setOnClickListener(view -> {

            //  implementation for navigating to Add Habit screen
            Intent intent = new Intent(HabitActivity.this, AddHabitActivity.class);
            startActivity(intent);
        });
    }

    private void deleteHabit(Habit habit) {
        if (habit.getId() == null) {
            Toast.makeText(this, "Unable to delete habit. Missing ID.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Remove habit from Firebase
        habitsRef.child(habit.getId()).removeValue()
                .addOnSuccessListener(unused -> {
                    // Remove habit from local list and update RecyclerView
                    habitList.remove(habit);
                    habitAdapter.notifyDataSetChanged();
                    Toast.makeText(this, "Habit deleted successfully!", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Log.e("HabitActivity", "Error deleting habit", e);
                    Toast.makeText(this, "Failed to delete habit", Toast.LENGTH_SHORT).show();
                });
    }

    private void fetchHabits() {
        habitsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                habitList.clear(); // Clear the list before adding updated data

                // Loop through each child in the "habits" node
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Habit habit = snapshot.getValue(Habit.class);
                    if (habit != null) {
                        habit.setId(snapshot.getKey()); // Set the unique key as the habit ID
                        habitList.add(habit);
                    }
                }

                // Notify the adapter to refresh the RecyclerView
                habitAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "Error fetching habits", databaseError.toException());
            }
        });
    }

    private void incrementHabitDays(Habit habit) {
        if (habit == null || habit.getId() == null) {
            Log.e(TAG, "Habit or Habit ID is null");
            return;
        }

        int newDays = habit.getDaysCompleted() + 1;

        // Update Realtime Database
        Map<String, Object> updates = new HashMap<>();
        updates.put("daysCompleted", newDays);

        habitsRef.child(habit.getId())
                .updateChildren(updates)
                .addOnSuccessListener(aVoid -> {
                    habit.setDaysCompleted(newDays); // Update local list
                    habitAdapter.notifyDataSetChanged(); // Refresh RecyclerView
                    Log.d(TAG, "Habit updated successfully");
                })
                .addOnFailureListener(e -> Log.e(TAG, "Error updating habit", e));
    }
}
