package com.example.planetze;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
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
import java.util.List;

public class AddHabitActivity extends AppCompatActivity {

    private RecyclerView preexistingHabitsRecyclerView;
    private HabitAdapter preexistingHabitAdapter;
    private List<Habit> preexistingHabitList;

    private DatabaseReference preexistingHabitsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_habit);

        // Initialize Firebase reference
        preexistingHabitsRef = FirebaseDatabase.getInstance().getReference("preexistingHabits");

        // Initialize RecyclerView
        preexistingHabitsRecyclerView = findViewById(R.id.preexistingHabitsRecyclerView);
        preexistingHabitsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        preexistingHabitList = new ArrayList<>();
        preexistingHabitAdapter = new HabitAdapter(preexistingHabitList,
                this::addHabitToUser, // Increment functionality (for adding habits)
                habit -> {}, false);
        preexistingHabitsRecyclerView.setAdapter(preexistingHabitAdapter);

        preexistingHabitsRecyclerView.setAdapter(preexistingHabitAdapter);

        // Fetch preexisting habits
        fetchPreexistingHabits();

        findViewById(R.id.backButton).setOnClickListener(view -> finish());

    }

private void fetchPreexistingHabits() {
    preexistingHabitsRef.addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            preexistingHabitList.clear(); // Clear the list before adding updated data

            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                Habit habit = snapshot.getValue(Habit.class);

                if (habit != null && "available".equals(habit.getStatus())) {
                    habit.setId(snapshot.getKey()); // Set the Firebase key as the habit ID
                    preexistingHabitList.add(habit);
                }
            }

            preexistingHabitAdapter.notifyDataSetChanged(); // Refresh the RecyclerView
            toggleEmptyState(); // Show/hide the "All habits added" message
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            Log.e("AddHabitActivity", "Failed to fetch habits", databaseError.toException());
        }
    });
}



    private void toggleEmptyState() {
        TextView emptyStateText = findViewById(R.id.emptyStateText);
        RecyclerView recyclerView = findViewById(R.id.preexistingHabitsRecyclerView);

        if (preexistingHabitList.isEmpty()) {
            emptyStateText.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            emptyStateText.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }


    private void addHabitToUser(Habit habit) {
        if (habit.getId() == null) {
            Toast.makeText(this, "Unable to add habit. Missing ID.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Reference to the user's habits
        DatabaseReference userHabitsRef = FirebaseDatabase.getInstance().getReference("habits");

        // Update user's habits
        userHabitsRef.child(habit.getId()).setValue(habit)
                .addOnSuccessListener(aVoid -> {
                    // Update status in preexistingHabits
                    preexistingHabitsRef.child(habit.getId()).child("status").setValue("added")
                            .addOnSuccessListener(unused -> {
                                preexistingHabitList.remove(habit); // Remove from local list
                                preexistingHabitAdapter.notifyDataSetChanged(); // Refresh RecyclerView
                                Toast.makeText(this, "Habit added successfully!", Toast.LENGTH_SHORT).show();
                            })
                            .addOnFailureListener(e -> {
                                Log.e("AddHabitActivity", "Failed to update habit status", e);
                                Toast.makeText(this, "Failed to update habit status", Toast.LENGTH_SHORT).show();
                            });
                })
                .addOnFailureListener(e -> {
                    Log.e("AddHabitActivity", "Failed to add habit to user", e);
                    Toast.makeText(this, "Failed to add habit", Toast.LENGTH_SHORT).show();
                });
    }



}
