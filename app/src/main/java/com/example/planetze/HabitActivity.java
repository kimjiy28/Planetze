package com.example.planetze;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

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

import java.util.ArrayList;
import java.util.List;

public class HabitActivity extends AccountActivity {

    private static final String TAG = "HabitActivity";
    private RecyclerView habitRecyclerView;
    private HabitAdapter habitAdapter;
    private DatabaseReference habitsRef;
    private DatabaseReference userHabitsRef;
    private List<Habit> habitList;
    private String userId;
    private BottomNavigationView bottomNavigationView;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habit);

        //  Views Initialization
        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        // Upper Toolbar
        toolbar = findViewById(R.id.appbar);
        setSupportActionBar(toolbar);

        // Bottom Navigation
        bottomNavigationView.setSelectedItemId(R.id.habit);
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

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            Toast.makeText(this, "No user is logged in. Redirecting to login.", Toast.LENGTH_SHORT).show();
            // Redirect to login activity
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }
        userId = currentUser.getUid();

        userHabitsRef = FirebaseDatabase.getInstance()
                .getReference("users")
                .child(userId)
                .child("habits");


        // Initialize Firebase Realtime Database
        Log.d("Firebase", "Initializing Firebase");
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://planetze-4ccca-default-rtdb.firebaseio.com/");
        DatabaseReference habitsRef = database.getReference("users").child("c04ro6FxLuaJ6TegP0wPmT8dzvp2").child("habits");


        userHabitsRef = FirebaseDatabase.getInstance().getReference("users").child(userId).child("habits");
        //habitsRef = database.getReference("habits");

        // Initialize RecyclerView
        habitRecyclerView = findViewById(R.id.habitRecyclerView);
        habitRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize Habit List and Adapter
        habitList = new ArrayList<>();
        habitAdapter = new HabitAdapter(habitList,
                this::incrementHabitDays, // Handle increment logic
                this::deleteHabit,true);
        habitRecyclerView.setAdapter(habitAdapter);

        fetchUserHabits();

        // Add New Habit Button
        findViewById(R.id.addHabitButton).setOnClickListener(view -> {

            //  implementation for navigating to Add Habit screen
            Intent intent = new Intent(HabitActivity.this, AddHabitActivity.class);
            startActivity(intent);
        });
    }
    private void fetchUserHabits() {
        userHabitsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                habitList.clear(); // Clear the local list before adding updated data

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Habit habit = snapshot.getValue(Habit.class);
                    if (habit != null) {
                        habit.setId(snapshot.getKey()); // Assign the unique Firebase key as habit ID
                        habitList.add(habit);
                    }
                }
                habitAdapter.notifyDataSetChanged(); // Refresh RecyclerView
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "Failed to fetch habits", databaseError.toException());
            }
        });
    }
    private void incrementHabitDays(Habit habit) {
        if (habit.getId() == null) {
            Log.e(TAG, "Cannot increment days for a habit without ID");
            return;
        }

        int newDays = habit.getDaysCompleted() + 1;

        userHabitsRef.child(habit.getId()).child("daysCompleted").setValue(newDays)
                .addOnSuccessListener(aVoid -> {
                    habit.setDaysCompleted(newDays);
                    habitAdapter.notifyDataSetChanged();
                    Toast.makeText(this, "Habit updated successfully!", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> Log.e(TAG, "Failed to update habit", e));
    }
    private void deleteHabit(Habit habit) {
        if (habit.getId() == null) {
            Toast.makeText(this, "Unable to delete habit. Missing ID.", Toast.LENGTH_SHORT).show();
            return;
        }

        userHabitsRef.child(habit.getId()).removeValue()
                .addOnSuccessListener(aVoid -> {
                    habitList.remove(habit);
                    habitAdapter.notifyDataSetChanged();
                    Toast.makeText(this, "Habit deleted successfully!", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Failed to delete habit", e);
                    Toast.makeText(this, "Failed to delete habit", Toast.LENGTH_SHORT).show();
                });
    }
}
