package com.example.planetze;

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
        preexistingHabitsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                preexistingHabitList.clear();

                // Loop through preexisting habits
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Habit habit = snapshot.getValue(Habit.class);
                    if (habit != null) {
                        preexistingHabitList.add(habit);
                    }
                }

                // Notify adapter
                preexistingHabitAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("Firebase", "Failed to fetch preexisting habits", databaseError.toException());
            }
        });
    }

    private void addHabitToUser(Habit habit) {
        DatabaseReference userHabitsRef = FirebaseDatabase.getInstance().getReference("habits");

        userHabitsRef.push().setValue(habit)
                .addOnSuccessListener(aVoid -> {
                    preexistingHabitList.remove(habit);

                    // Notify the adapter that the data has changed
                    preexistingHabitAdapter.notifyDataSetChanged();
                    Toast.makeText(this, "Habit added successfully!", Toast.LENGTH_SHORT).show();
                    //finish(); // Close AddHabitActivity
                })
                .addOnFailureListener(e -> {
                    Log.e("Firebase", "Failed to add habit to user", e);
                    Toast.makeText(this, "Failed to add habit", Toast.LENGTH_SHORT).show();
                });
    }
}
