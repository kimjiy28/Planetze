/* package com.example.planetze.repository;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.List;
import com.example.planetze.ui.dashboard.Habit;

public class HabitRepository {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    public interface HabitFetchCallback {
        void onSuccess(List<Habit> habits);
        void onFailure(Exception e);
    }

    public void fetchHabits(HabitFetchCallback callback) {
        db.collection("habits")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<Habit> habitList = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Habit habit = document.toObject(Habit.class); // Convert to Habit object
                            habitList.add(habit);
                        }
                        callback.onSuccess(habitList);
                    } else {
                        callback.onFailure(task.getException());
                    }
                });
    }

} */


