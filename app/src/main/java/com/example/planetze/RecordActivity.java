package com.example.planetze;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RecordActivity extends AppCompatActivity {

    String date;
    FirebaseDatabase db;
    DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        db = FirebaseDatabase.getInstance();
        /*
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user.getUid();
        ref = db.getReference("users").child(uid);
         */
        ref = db.getReference("users").child("X8cKomTutObPCZf5aXyhM2xwnUJ2");
    }

    public void getDate(View v) {

    }


    // Intents
    public void launchTransportation(View v) {
        Intent transportation = new Intent(this, TransportationRecordActivity.class);
        transportation.putExtra("date", date);
        startActivity(transportation);
    }

    public void launchFood(View v) {
        Intent food = new Intent(this, FoodRecordActivity.class);
        food.putExtra("date", date);
        startActivity(food);
    }

    public void launchConsumption(View v) {
        Intent consumption = new Intent(this, ConsumptionRecordActivity.class);
        consumption.putExtra("date", date);
        startActivity(consumption);
    }
}