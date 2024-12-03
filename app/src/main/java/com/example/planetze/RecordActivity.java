package com.example.planetze;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Calendar;

import cjh.WaveProgressBarlibrary.WaveProgressBar;

public class RecordActivity extends AppCompatActivity {

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    // DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users").child(user.getUid());
    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users").
            child("4U6brQiObtULMg94EPA2zN6nH2h1");

    private String category, activity;
    private static String date;

    // Views
    private EditText calendarManagement;
    private TextView tvCategorySpinner, tvActivitySpinner;
    private Spinner categorySpinner, activitySpinner;
    private ArrayAdapter<CharSequence> categoryAdapter, activityAdapter;
    private BottomNavigationView bottomNavigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        //Views Initialization
        calendarManagement = findViewById(R.id.calendarManagement);
        categorySpinner = findViewById(R.id.spCategory);
        categoryAdapter = ArrayAdapter.createFromResource(this, R.array.category, R.layout.spinner);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(categoryAdapter);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        // Bottom Navigation
        bottomNavigationView.setSelectedItemId(R.id.add);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.tracker) {
                startActivity(new Intent(getApplicationContext(), EcoTrackerActivity.class));
                return true;
            } else if (item.getItemId() == R.id.habit) {
                startActivity(new Intent(getApplicationContext(), HabitActivity.class));
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

        // Calendar Management
        if (EcoTrackerActivity.getDate() != null) {
            date = EcoTrackerActivity.getDate();
            calendarManagement.setText(date);
        }
        // Edit Activity
        String id = getIntent().getStringExtra("id");
        if (id != null ) {
            reference.child(date).child("activities").child(id).removeValue();
            Toast.makeText(this, "Update Your Activity", Toast.LENGTH_SHORT).show();
        }
        calendarManagement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();
                int day = calendar.get(calendar.DAY_OF_MONTH);
                int month = calendar.get(calendar.MONTH);
                int year = calendar.get(calendar.YEAR);

                DatePickerDialog picker = new DatePickerDialog(RecordActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        date = year + "-" + (month + 1) + "-" + dayOfMonth;
                        calendarManagement.setText(date);
                    }
                }, year, month, day);
                picker.show();
            }
        });

        // Activity Selection
        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                activitySpinner = findViewById(R.id.spActivity);
                category = categorySpinner.getSelectedItem().toString();
                int parentId = parent.getId();
                if (parentId == R.id.spCategory) {
                    switch(category) {
                        case "Select Category": activityAdapter = ArrayAdapter.createFromResource(parent.getContext(),
                                R.array.defaultList, R.layout.spinner);
                            break;
                        case "Transportation": activityAdapter = ArrayAdapter.createFromResource(parent.getContext(),
                                R.array.transportationList, R.layout.spinner);
                            break;
                        case "Food": activityAdapter = ArrayAdapter.createFromResource(parent.getContext(),
                                R.array.foodList, R.layout.spinner);
                            break;
                        case "Consumption": activityAdapter = ArrayAdapter.createFromResource(parent.getContext(),
                                R.array.consumptionList, R.layout.spinner);
                            break;
                        default:
                            break;
                    }
                    activityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    activitySpinner.setAdapter(activityAdapter);
                    activitySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            activity = activitySpinner.getSelectedItem().toString();
                            switch (activity) {
                                case "Personal Vehicle":
                                    PersonalVehicleFragment personalVehicle = new PersonalVehicleFragment();
                                    addFragment(personalVehicle);
                                    break;
                                case "Public Transportation":
                                    PublicTransportationFragment publicTransportation = new PublicTransportationFragment();
                                    addFragment(publicTransportation);
                                    break;
                                case "Cycling or Walking":
                                    CyclingFragment cycling = new CyclingFragment();
                                    addFragment(cycling);
                                    break;
                                case "Flight":
                                    FlightFragment flight = new FlightFragment();
                                    addFragment(flight);
                                    break;
                                case "Meal":
                                    MealFragment meal = new MealFragment();
                                    addFragment(meal);
                                    break;
                                case "Clothes":
                                    ClothesFragment clothes = new ClothesFragment();
                                    addFragment(clothes);
                                    break;
                                case "Electronics":
                                    ElectronicsFragment electronics = new ElectronicsFragment();
                                    addFragment(electronics);
                                    break;
                                case "Other Purchases":
                                    OtherPurchasesFragment otherPurchases = new OtherPurchasesFragment();
                                    addFragment(otherPurchases);
                                    break;
                                case "Energy Bills":
                                    EnergyBillsFragment energyBills = new EnergyBillsFragment();
                                    addFragment(energyBills);
                                    break;
                                default:
                                    break;
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                        }
                    });
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

    }

    private void addFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentContainer, fragment);
        fragmentTransaction.commit();
    }

    public static String getDate() {
        return date;
    }

}