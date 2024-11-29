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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
    private DatabaseReference userHabitsRef;
    private List<Habit> habitList;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habit);

        //HARD CODING A USER, DELETE LATER
        userId = "c04ro6FxLuaJ6TegP0wPmT8dzvp2"; // Replace this

        userHabitsRef = FirebaseDatabase.getInstance()
                .getReference("users")
                .child(userId)
                .child("habits");

        //UNCOMMENT THIS LATER
//        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
//        if (currentUser == null) {
//            Toast.makeText(this, "No user is logged in", Toast.LENGTH_SHORT).show();
//            finish(); // Close the activity if no user is logged in
//            return;
//        }
//        userId = currentUser.getUid();

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
        // Fetch Habits from Realtime Database
        //fetchHabits();

        // Add New Habit Button
        findViewById(R.id.addHabitButton).setOnClickListener(view -> {

            //  implementation for navigating to Add Habit screen
            Intent intent = new Intent(HabitActivity.this, AddHabitActivity.class);
            startActivity(intent);
        });
    }

//    private void deleteHabit(Habit habit) {
//        if (habit.getId() == null) {
//            Toast.makeText(this, "Unable to delete habit. Missing ID.", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        // Remove habit from Firebase
//        habitsRef.child(habit.getId()).removeValue()
//                .addOnSuccessListener(unused -> {
//                    // Remove habit from local list and update RecyclerView
//                    habitList.remove(habit);
//                    habitAdapter.notifyDataSetChanged();
//                    Toast.makeText(this, "Habit deleted successfully!", Toast.LENGTH_SHORT).show();
//                })
//                .addOnFailureListener(e -> {
//                    Log.e("HabitActivity", "Error deleting habit", e);
//                    Toast.makeText(this, "Failed to delete habit", Toast.LENGTH_SHORT).show();
//                });
//    }
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
    private void fetchHabits() {
        habitsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                habitList.clear(); // Clear the list before adding updated data

                // Loop through each child in the habits node
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

    private void addOrUpdateHabit(Habit habit) {
        if (habit.getId() == null) {
            userHabitsRef.push().setValue(habit)
                    .addOnSuccessListener(aVoid -> Toast.makeText(this, "Habit added successfully!", Toast.LENGTH_SHORT).show())
                    .addOnFailureListener(e -> Log.e(TAG, "Failed to add habit", e));
        } else {
            userHabitsRef.child(habit.getId()).setValue(habit)
                    .addOnSuccessListener(aVoid -> Toast.makeText(this, "Habit updated successfully!", Toast.LENGTH_SHORT).show())
                    .addOnFailureListener(e -> Log.e(TAG, "Failed to update habit", e));
        }
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
//    private void incrementHabitDays(Habit habit) {
//        if (habit == null || habit.getId() == null) {
//            Log.e(TAG, "Habit or Habit ID is null");
//            return;
//        }
//
//        int newDays = habit.getDaysCompleted() + 1;
//
//        // Update Realtime Database
//        Map<String, Object> updates = new HashMap<>();
//        updates.put("daysCompleted", newDays);
//
//        habitsRef.child(habit.getId())
//                .updateChildren(updates)
//                .addOnSuccessListener(aVoid -> {
//                    habit.setDaysCompleted(newDays); // Update local list
//                    habitAdapter.notifyDataSetChanged(); // Refresh RecyclerView
//                    Log.d(TAG, "Habit updated successfully");
//                })
//                .addOnFailureListener(e -> Log.e(TAG, "Error updating habit", e));
//    }
}
