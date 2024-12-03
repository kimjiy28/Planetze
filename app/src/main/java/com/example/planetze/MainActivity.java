package com.example.planetze;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.planetze.databinding.ActivityMainBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    // Firebase
    private FirebaseAuth auth;
    private FirebaseUser user;

    // Views
    private BottomNavigationView bottomNavigationView;
    private Button logout, survey;
    private TextView tvID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Users
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        // Views
        logout = findViewById(R.id.logout);
        survey = findViewById(R.id.survey);
        tvID = findViewById(R.id.email);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        // Bottom Navigation
        bottomNavigationView.setSelectedItemId(R.id.tracker);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.habit) {
                startActivity(new Intent(getApplicationContext(), HabitActivity.class));
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

        if (user == null){
            Intent login = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(login);
            finish();
        } else{
            tvID.setText(user.getEmail());
        }

        survey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent survey = new Intent(getApplicationContext(), SurveyActivity.class);
                startActivity(survey);
                finish();
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent logout = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(logout);
                finish();
            }
        });

    }
}
