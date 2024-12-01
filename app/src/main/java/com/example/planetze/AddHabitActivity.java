package com.example.planetze;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.SearchView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
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
    private List<Habit> filteredHabitList;
    private DatabaseReference preexistingHabitsRef;
    private DatabaseReference userHabitsRef;

    private TextView impactFilterTextView;
    private SeekBar impactFilterSeekBar;

    private int currentImpactLevel = 0;
    private String currentSearchQuery = "";
    private String currentCategory = "All";

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
        filteredHabitList = new ArrayList<>();
        preexistingHabitAdapter = new HabitAdapter(filteredHabitList,
                this::addHabitToUser,
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
                preexistingHabitList.clear();

                for (DataSnapshot snapshot : preexistingSnapshot.getChildren()) {
                    Habit habit = snapshot.getValue(Habit.class);
                    if (habit != null) {
                        habit.setId(snapshot.getKey());
                        preexistingHabitList.add(habit);
                    }
                }

                Log.d("fetchPreexistingHabits", "Preexisting habit list size: " + preexistingHabitList.size());

                setupSearchFunctionality();
                setupCategorySpinner();
                setupImpactFilter();
                applyFilters();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("fetchPreexistingHabits", "Failed to fetch preexisting habits", error.toException());
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

                    // Remove the added habit from the preexisting list
                    preexistingHabitList.remove(habit);

                    // Reapply filters to update the displayed list
                    applyFilters();

                    Toast.makeText(this, "Habit added successfully!", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> Log.e("AddHabitActivity", "Failed to add habit to user", e));
    }
    private void setupSearchFunctionality() {
        androidx.appcompat.widget.SearchView searchView = findViewById(R.id.habitSearchView);
        searchView.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                currentSearchQuery = query; // Update the search query
                applyFilters(); // Call filter method
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                currentSearchQuery = newText; // Update the search query
                applyFilters(); // Call filter method
                return true;
            }
        });
    }
    private void setupCategorySpinner() {
        Spinner categorySpinner = findViewById(R.id.categorySpinner);
        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                currentCategory = parent.getItemAtPosition(position).toString();
                applyFilters();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });
    }
    private void setupImpactFilter() {
        impactFilterTextView = findViewById(R.id.impactFilterTextView);
        impactFilterSeekBar = findViewById(R.id.impactFilterSeekBar);

        impactFilterSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                currentImpactLevel = progress;
                impactFilterTextView.setText("Impact Level: " + progress);
                applyFilters();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
    }
    private void applyFilters() {
        Log.d("applyFilters", "Applying filters: Query='" + currentSearchQuery + "', Category='" + currentCategory + "', Impact=" + currentImpactLevel);
        Log.d("applyFilters", "Preexisting list size before filtering: " + preexistingHabitList.size());

        filteredHabitList.clear(); // Clear the filtered list

        for (Habit habit : preexistingHabitList) {
            boolean matchesQuery = currentSearchQuery.isEmpty() ||
                    habit.getName().toLowerCase().contains(currentSearchQuery.toLowerCase());
            boolean matchesCategory = currentCategory.equals("All") ||
                    habit.getCategory().equalsIgnoreCase(currentCategory);
            boolean matchesImpact = habit.getImpactLevel() >= currentImpactLevel;

            if (matchesQuery && matchesCategory && matchesImpact) {
                filteredHabitList.add(habit);
            }
        }

        Log.d("applyFilters", "Filtered list size after filtering: " + filteredHabitList.size());

        preexistingHabitAdapter.updateHabitList(filteredHabitList); // Update the adapter with filtered habits
    }
}
