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
import com.google.common.net.InternetDomainName;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
    private DatabaseReference userHabitsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_habit);

        String userId = "c04ro6FxLuaJ6TegP0wPmT8dzvp2";

        // Initialize Firebase Realtime Database references
        userHabitsRef = FirebaseDatabase.getInstance()
                .getReference("users")
                .child(userId)
                .child("habits");
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

        // Fetch preexisting habits
        fetchPreexistingHabits();

        findViewById(R.id.backButton).setOnClickListener(view -> finish());

    }
    private void fetchPreexistingHabits() {
        preexistingHabitsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot preexistingSnapshot) {
                List<Habit> availableHabits = new ArrayList<>();

                // Fetch the current user's habits
                userHabitsRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot userSnapshot) {
                        // Collect the IDs of the user's added habits
                        List<String> userAddedHabitIds = new ArrayList<>();
                        for (DataSnapshot habitSnapshot : userSnapshot.getChildren()) {
                            userAddedHabitIds.add(habitSnapshot.getKey());
                        }

                        // Filter preexisting habits
                        for (DataSnapshot preexistingHabitSnapshot : preexistingSnapshot.getChildren()) {
                            String habitId = preexistingHabitSnapshot.getKey();
                            if (!userAddedHabitIds.contains(habitId)) {
                                Habit habit = preexistingHabitSnapshot.getValue(Habit.class);
                                if (habit != null) {
                                    habit.setId(habitId);
                                    availableHabits.add(habit);
                                }
                            }
                        }

                        // Update the adapter with filtered habits
                        preexistingHabitAdapter.updateHabitList(availableHabits);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.e("AddHabitActivity", "Failed to fetch user habits", error.toException());
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("AddHabitActivity", "Failed to fetch preexisting habits", error.toException());
            }
        });
    }

private void addHabitToUser(Habit habit) {
    if (habit == null || habit.getId() == null) {
        Log.e("AddHabitActivity", "Cannot add a habit with a null ID");
        return;
    }

    userHabitsRef.child(habit.getId()).setValue(habit)
            .addOnSuccessListener(unused -> {
                Log.d("AddHabitActivity", "Habit added successfully");
                fetchPreexistingHabits(); // Refresh the adapter to remove the added habit
            })
            .addOnFailureListener(e -> Log.e("AddHabitActivity", "Failed to add habit to user", e));
}

}
