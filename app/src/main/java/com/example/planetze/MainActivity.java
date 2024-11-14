package com.example.planetze;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.planetze.ui.dashboard.HabitAdapter;
import com.example.planetze.repository.HabitRepository;
import com.example.planetze.ui.dashboard.Habit;
import java.util.ArrayList;
import java.util.List;



public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private HabitAdapter habitAdapter;
    private HabitRepository habitRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize HabitAdapter with an empty list initially
        habitAdapter = new HabitAdapter(new ArrayList<>());
        recyclerView.setAdapter(habitAdapter);

        // Initialize HabitRepository and fetch data
        habitRepository = new HabitRepository();
        habitRepository.fetchHabits(new HabitRepository.HabitFetchCallback() {
            @Override
            public void onSuccess(List<Habit> habits) {
                habitAdapter = new HabitAdapter(habits);
                recyclerView.setAdapter(habitAdapter);
                habitAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Exception e) {
                // Handle the error, e.g., show a toast or log the error
            }
        });
    }
}
