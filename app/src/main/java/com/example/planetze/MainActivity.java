package com.example.planetze;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    // Firestore instance
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();

        // Write test data to Firestore
        writeTestData();
    }

    private void writeTestData() {
        // Create a map to hold sample data
        Map<String, Object> testData = new HashMap<>();
        testData.put("name", "Planetze");
        testData.put("type", "Habit Tracking");
        testData.put("created_at", System.currentTimeMillis());

        // Write to Firestore
        db.collection("testCollection")
                .add(testData)
                .addOnSuccessListener(documentReference -> {
                    Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                })
                .addOnFailureListener(e -> {
                    Log.w(TAG, "Error adding document", e);
                });
    }
}
