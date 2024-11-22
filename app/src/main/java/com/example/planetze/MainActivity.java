package com.example.planetze;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.planetze.databinding.ActivityMainBinding;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    // RadioGroups for Transportation
    private RadioGroup question1Group, question2Group, question3Group, question4Group, question5Group, question6Group, question7Group;
    // RadioGroups for Transportation
    private RadioGroup  question8Group, question10Group;
    private LinearLayout question2Layout, question9Layout;
    private RadioGroup question9BeefGroup, question9PorkGroup, question9ChickenGroup, question9FishGroup;

    // RadioGroups for Housing
    private RadioGroup question11Group, question12Group, question13Group, question14Group, question15Group, question16Group, question17Group;

    // RadioGroups for Consumption
    private RadioGroup question18Group, question19Group, question20Group, question21Group;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
/*
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);
    */
        setContentView(R.layout.activity_main);

        question1Group = findViewById(R.id.question1Group);
        question2Group = findViewById(R.id.question2Group);
        question3Group = findViewById(R.id.question3Group);
        question4Group = findViewById(R.id.question4Group);
        question5Group = findViewById(R.id.question5Group);
        question6Group = findViewById(R.id.question6Group);
        question7Group = findViewById(R.id.question7Group);
        question8Group = findViewById(R.id.question8Group);
        question10Group = findViewById(R.id.question10Group);
        question11Group = findViewById(R.id.question11Group);
        question12Group = findViewById(R.id.question12Group);
        question13Group = findViewById(R.id.question13Group);
        question14Group = findViewById(R.id.question14Group);
        question15Group = findViewById(R.id.question15Group);
        question16Group = findViewById(R.id.question16Group);
        question17Group = findViewById(R.id.question17Group);
        question18Group = findViewById(R.id.question18Group);
        question19Group = findViewById(R.id.question19Group);
        question20Group = findViewById(R.id.question20Group);
        question21Group = findViewById(R.id.question21Group);
        // Question 9 layout and checkboxes

        question2Layout = findViewById(R.id.question2Layout);
        question9Layout = findViewById(R.id.question9Layout);
        question9BeefGroup = findViewById(R.id.question9BeefGroup);
        question9PorkGroup = findViewById(R.id.question9PorkGroup);
        question9ChickenGroup = findViewById(R.id.question9ChickenGroup);
        question9FishGroup = findViewById(R.id.question9FishGroup);

        // Monitor changes to Question 1
        question1Group.setOnCheckedChangeListener((group, checkedId) -> {
            RadioButton selectedButton = findViewById(checkedId);
            if (selectedButton != null && selectedButton.getText().toString().equals("Yes")) {
                question2Layout.setVisibility(View.VISIBLE); // Show Q2 & Q3
            } else {
                question2Layout.setVisibility(View.GONE); // Hide Q2 & Q3
            }
        });
        // Monitor changes to Question 8
        question8Group.setOnCheckedChangeListener((group, checkedId) -> {
            RadioButton selectedButton = findViewById(checkedId);
            if (selectedButton != null && selectedButton.getText().toString().equals("Meat-based")) {
                question9Layout.setVisibility(View.VISIBLE); // Show Q9
            } else {
                question9Layout.setVisibility(View.GONE); // Hide Q9
            }
        });


        // Submit Button
        Button submitButton = findViewById(R.id.submitButton);
        submitButton.setOnClickListener(view -> {
            double transportationCO2 = calculateTransportationCO2();
            double foodCO2 = calculateFoodCO2();
            double housingCO2 = calculateHousingCO2();
            double consumptionCO2 = calculateConsumptionCO2();
            double totalCO2 = transportationCO2 + foodCO2 + housingCO2 + consumptionCO2;

            Intent intent = new Intent(MainActivity.this, ResultActivity.class);
            intent.putExtra("totalCO2", totalCO2);
            intent.putExtra("transportationCO2", transportationCO2);
            intent.putExtra("foodCO2", foodCO2);
            intent.putExtra("housingCO2", housingCO2);
            intent.putExtra("consumptionCO2", consumptionCO2);
            startActivity(intent);
        });

        Spinner countrySpinner = findViewById(R.id.country_spinner);

        // Load country data from the CSV file
        ArrayList<String> countriesList = loadCountriesFromCSV();

        // Set up an ArrayAdapter for the Spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, countriesList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        countrySpinner.setAdapter(adapter);

        // Set up a listener for the Spinner
        countrySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View view, int position, long id) {
                String selectedCountry = parentView.getItemAtPosition(position).toString();
                Toast.makeText(MainActivity.this, "Selected Country: " + selectedCountry, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Do nothing
            }
        });
    }
    private double calculateTransportationCO2() {
        return 0;
    }

    private double calculateFoodCO2() {
        return 0;
    }

    private double calculateHousingCO2() {
        return 0;
    }

    private double calculateConsumptionCO2() {
        double consumptionCO2 = 0;

        int q18Id = question18Group.getCheckedRadioButtonId();
        int q19Id = question19Group.getCheckedRadioButtonId();
        int q20Id = question20Group.getCheckedRadioButtonId();
        int q21Id = question21Group.getCheckedRadioButtonId();
        RadioButton q18Button = findViewById(q18Id);
        RadioButton q19Button = findViewById(q19Id);
        RadioButton q20Button = findViewById(q20Id);
        RadioButton q21Button = findViewById(q21Id);

        if (q18Button != null) {
            String frequency = q18Button.getText().toString();

            if (frequency.equals("Monthly")) {
                consumptionCO2 = 360;
            } else if (frequency.equals("Quarterly")) {
                consumptionCO2 = 120;
            } else if (frequency.equals("Annually")) {
                consumptionCO2 = 100;
            } else {
                consumptionCO2 = 5;
            }
        }

        if (q19Button != null) {
            String frequency = q19Button.getText().toString();

            if (frequency.equals("Yes, regularly")) {
                consumptionCO2 *= 0.5;
            } else if (frequency.equals("Yes, occasionally")) {
                consumptionCO2 *= 0.7;
            }
        }
        if (q20Button != null) {
            String frequency = q20Button.getText().toString();

            if (frequency.equals("1")) {
                consumptionCO2 += 300;
            } else if (frequency.equals("2")) {
                consumptionCO2 += 600;
            } else if (frequency.equals("3")) {
                consumptionCO2 += 900;
            } else if(frequency.equals("4 or more")){
                consumptionCO2 += 1200;
            }
        }

        if (q21Button != null) {
            String frequency = q21Button.getText().toString();

            String q18 = q18Button.getText().toString();
            String q20 = q20Button.getText().toString();
            if (frequency.equals("Occasionally")) {
                if (q18.equals("Monthly")) {
                    consumptionCO2 -= 54;
                } else if (q18.equals("Quarterly")) {
                    consumptionCO2 -= 18;
                } else if (q18.equals("Annually")) {
                    consumptionCO2 -= 15;
                } else {
                    consumptionCO2 -= 0.75;
                }

                if (q20.equals("1")) {
                    consumptionCO2 -= 45;
                } else if (q20.equals("2")) {
                    consumptionCO2 -= 60;
                } else if (q20.equals("3")) {
                    consumptionCO2 -= 90;
                } else if(q20.equals("4 or more")){
                    consumptionCO2 -= 120;
                }
            } else if (frequency.equals("Frequently")) {
                if (q18.equals("Monthly")) {
                    consumptionCO2 -= 108;
                } else if (q18.equals("Quarterly")) {
                    consumptionCO2 -= 36;
                } else if (q18.equals("Annually")) {
                    consumptionCO2 -= 30;
                } else {
                    consumptionCO2 -= 1.5;
                }

                if (q20.equals("1")) {
                    consumptionCO2 -= 60;
                } else if (q20.equals("2")) {
                    consumptionCO2 -= 120;
                } else if (q20.equals("3")) {
                    consumptionCO2 -= 180;
                } else if(q20.equals("4 or more")){
                    consumptionCO2 -= 240;
                };

            } else if (frequency.equals("Always")) {
                if (q18.equals("Monthly")) {
                    consumptionCO2 -= 180;
                } else if (q18.equals("Quarterly")) {
                    consumptionCO2 -= 60;
                } else if (q18.equals("Annually")) {
                    consumptionCO2 -= 50;
                } else {
                    consumptionCO2 -= 2.5;
                }

                if (q20.equals("1")) {
                    consumptionCO2 -= 90;
                } else if (q20.equals("2")) {
                    consumptionCO2 -= 180;
                } else if (q20.equals("3")) {
                    consumptionCO2 -= 270;
                } else if(q20.equals("4 or more")){
                    consumptionCO2 -= 360;
                };
            }
        }
        return consumptionCO2/1000;
    }
    private ArrayList<String> loadCountriesFromCSV() {
        ArrayList<String> countries = new ArrayList<>();
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(getAssets().open("Global_Averages.csv")));
            String line;
            reader.readLine();
            while ((line = reader.readLine()) != null) {
                String[] columns = line.split(",");
                countries.add(columns[0]);
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return countries;
    }

}