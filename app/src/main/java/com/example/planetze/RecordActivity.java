package com.example.planetze;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import cjh.WaveProgressBarlibrary.WaveProgressBar;

public class RecordActivity extends AppCompatActivity {

    private String category, activity;
    private TextView tvCategorySpinner, tvActivitySpinner;
    private Spinner categorySpinner, activitySpinner;
    private ArrayAdapter<CharSequence> categoryAdapter, activityAdapter;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        //Views Initialization
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
//                startActivity(new Intent(getApplicationContext(), EcoGaugeActivity.class));
//                return true;
            } else if (item.getItemId() == R.id.hub) {
//                startActivity(new Intent(getApplicationContext(), EcoHubActivity.class));
//                return true;
            }
            return false;
        });

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

//    FirebaseUser uid = FirebaseAuth.getInstance().getCurrentUser();
//    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
//    reference.child(uid.getUid()).child("survey").setValue("true")

}