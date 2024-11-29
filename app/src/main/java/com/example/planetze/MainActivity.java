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
    // RadioGroups for Food
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
        double consumptionCO2 = 0;

        int q1Id = question1Group.getCheckedRadioButtonId();
        int q2Id = question2Group.getCheckedRadioButtonId();
        int q3Id = question3Group.getCheckedRadioButtonId();
        int q4Id = question4Group.getCheckedRadioButtonId();
        int q5Id = question5Group.getCheckedRadioButtonId();
        int q6Id = question6Group.getCheckedRadioButtonId();
        int q7Id = question7Group.getCheckedRadioButtonId();

        RadioButton q1Button = findViewById(q1Id);
        RadioButton q2Button = findViewById(q2Id);
        RadioButton q3Button = findViewById(q3Id);
        RadioButton q4Button = findViewById(q4Id);
        RadioButton q5Button = findViewById(q5Id);
        RadioButton q6Button = findViewById(q6Id);
        RadioButton q7Button = findViewById(q7Id);

        if (q1Button != null) {
            String ans1 =  q1Button.getText().toString();

            if (ans1.equals("Yes")) {
                double rate = 0;
                double distance = 0;

                String ans2 = q2Button.getText().toString();
                if (ans2.equals("Gasoline")) {
                    rate = 0.24;
                } else if (ans2.equals("Diesel")) {
                    rate = 0.27;
                } else if (ans2.equals("Hybrid")) {
                    rate = 0.16;
                } else if (ans2.equals("Electric")) {
                    rate = 0.05;
                }

                String ans3 = q3Button.getText().toString();
                if (ans3.equals("Up to 5,000km")) {
                    distance = 5000;
                } else if (ans3.equals("5,000-10,000km")) {
                    distance = 10000;
                } else if (ans3.equals("10,000-15,000km")) {
                    distance = 15000;
                } else if (ans3.equals("15,000-20,000km")) {
                    distance = 20000;
                } else if (ans3.equals("20,000-25,000km")) {
                    distance = 25000;
                } else if (ans3.equals("More than 25,000km")) {
                    distance = 35000;
                }

                consumptionCO2 += (rate * distance);
            }
        }

        if (q4Button != null) {
            String ans4 = q4Button.getText().toString();
            String ans5 = q5Button.getText().toString();

            if (ans4.equals("Never")) {
                consumptionCO2 += 0;
            }

            if (ans4.equals("Occasionally (1-2 times/week)")) {
                if (ans5.equals("Under 1 hour")) {
                    consumptionCO2 += 246;
                } else if (ans5.equals("1-3 hours")) {
                    consumptionCO2 += 819;
                } else if (ans5.equals("3-5 hours")) {
                    consumptionCO2 += 1638;
                } else if (ans5.equals("5-10 hours")) {
                    consumptionCO2 += 3071;
                } else if (ans5.equals("More than 10 hours")) {
                    consumptionCO2 += 4095;
                }
            }

            if (ans4.equals("Frequently (3-4 times/week)")) {
                if (ans5.equals("Under 1 hour")) {
                    consumptionCO2 += 573;
                } else if (ans5.equals("1-3 hours")) {
                    consumptionCO2 += 1911;
                } else if (ans5.equals("3-5 hours")) {
                    consumptionCO2 += 3822;
                } else if (ans5.equals("5-10 hours")) {
                    consumptionCO2 += 7166;
                } else if (ans5.equals("More than 10 hours")) {
                    consumptionCO2 += 9555;
                }
            }

            if (ans4.equals("Always (5+ times/week)")) {
                if (ans5.equals("Under 1 hour")) {
                    consumptionCO2 += 573;
                } else if (ans5.equals("1-3 hours")) {
                    consumptionCO2 += 1911;
                } else if (ans5.equals("3-5 hours")) {
                    consumptionCO2 += 3822;
                } else if (ans5.equals("5-10 hours")) {
                    consumptionCO2 += 7166;
                } else if (ans5.equals("More than 10 hours")) {
                    consumptionCO2 += 9555;
                }
            }

        }

        if (q6Button != null) {
            String ans6 = q6Button.getText().toString();

            if (ans6.equals("None")) {
                consumptionCO2 += 0;
            } else if (ans6.equals("1-2 flights")) {
                consumptionCO2 += 225;
            } else if (ans6.equals("3-5 flights")) {
                consumptionCO2 += 600;
            } else if (ans6.equals("6-10 flights")) {
                consumptionCO2 += 1200;
            } else if (ans6.equals("More than 10 flights")) {
                consumptionCO2 += 1800;
            }
        }

        if (q7Button != null) {
            String ans7 = q7Button.getText().toString();

            if (ans7.equals("None")) {
                consumptionCO2 += 0;
            } else if (ans7.equals("1-2 flights")) {
                consumptionCO2 += 825;
            } else if (ans7.equals("3-5 flights")) {
                consumptionCO2 += 2200;
            } else if (ans7.equals("6-10 flights")) {
                consumptionCO2 += 4400;
            } else if (ans7.equals("More than 10 flights")) {
                consumptionCO2 += 6600;
            }
        }

        return consumptionCO2;
    }

    private double calculateFoodCO2() {
        double consumptionCO2 = 0;

        int q8Id = question8Group.getCheckedRadioButtonId();
        int q9PId = question9PorkGroup.getCheckedRadioButtonId();
        int q9BId = question9BeefGroup.getCheckedRadioButtonId();
        int q9CId = question9ChickenGroup.getCheckedRadioButtonId();
        int q9FId = question9FishGroup.getCheckedRadioButtonId();
        int q10Id = question10Group.getCheckedRadioButtonId();

        RadioButton q8Button = findViewById(q8Id);
        RadioButton q9PButton = findViewById(q9PId);
        RadioButton q9BButton = findViewById(q9BId);
        RadioButton q9CButton = findViewById(q9CId);
        RadioButton q9FButton = findViewById(q9FId);
        RadioButton q10Button = findViewById(q10Id);

        if (q8Button != null) {
            String ans8 = q8Button.getText().toString();
            String ans9P = q9PButton.getText().toString();
            String ans9B = q9BButton.getText().toString();
            String ans9C = q9CButton.getText().toString();
            String ans9F = q9FButton.getText().toString();

            if (ans8.equals("Vegetarian")) {
                consumptionCO2 += 1000;
            } else if (ans8.equals("Vegan")) {
                consumptionCO2 += 500;
            } else if (ans8.equals("Pescatarian")) {
                consumptionCO2 += 1500;
            } else if (ans8.equals("Meat-based")) {
                if (ans9B.equals("Daily")) {
                    consumptionCO2 += 2500;
                } else if (ans9B.equals("Frequently (3-5 times/week)")) {
                    consumptionCO2 += 1900;
                } else if (ans9B.equals("Occasionally (1-2 times/week)")) {
                    consumptionCO2 += 1300;
                } else consumptionCO2 += 0;;

                if (ans9P.equals("Daily")) {
                    consumptionCO2 += 1450;
                } else if (ans9B.equals("Frequently (3-5 times/week)")) {
                    consumptionCO2 += 860;
                } else if (ans9B.equals("Occasionally (1-2 times/week)")) {
                    consumptionCO2 += 450;
                } else consumptionCO2 += 0;;

                if (ans9C.equals("Daily")) {
                    consumptionCO2 += 950;
                } else if (ans9B.equals("Frequently (3-5 times/week)")) {
                    consumptionCO2 += 600;
                } else if (ans9B.equals("Occasionally (1-2 times/week)")) {
                    consumptionCO2 += 200;
                } else consumptionCO2 += 0;;

                if (ans9F.equals("Daily")) {
                    consumptionCO2 += 800;
                } else if (ans9B.equals("Frequently (3-5 times/week)")) {
                    consumptionCO2 += 500;
                } else if (ans9B.equals("Occasionally (1-2 times/week)")) {
                    consumptionCO2 += 150;
                } else consumptionCO2 += 0;;
            }
        }

        if (q10Button != null) {
            String ans10 = q10Button.getText().toString();

            if (ans10.equals("Never")) {
                consumptionCO2 += 0;
            } else if (ans10.equals("Rarely")) {
                consumptionCO2 += 23.4;
            } else if (ans10.equals("Occasionally")) {
                consumptionCO2 += 70.2;
            } else if (ans10.equals("Frequently")) {
                consumptionCO2 += 140.4;
            }
        }

        return consumptionCO2;
    }

    private double calculateHousingCO2() {
        double consumptionCO2 = 0;

        int housingType = -1;
        /*id for housing type based on excel sheets:
        0- Detached house under 1000 sq. ft.
        1- Detached house 1000-2000 sq. ft.
        2- Detached house over 2000 sq. ft.
        3- Semi-detached house under 1000 sq. ft.
        4- Semi-detached house 1000-2000 sq. ft.
        5- Semi-detached house over 2000 sq. ft.
        6- Townhouse under 1000 sq. ft.
        7- Townhouse 1000-2000 sq. ft.
        8- Townhouse over 2000 sq. ft.
        9- Condo/Apartment 1000 sq. ft.
        10- Condo/Apartment 1000-2000 sq. ft.
        11- Condo/Apartment over 2000 sq. ft.
        Other housing type has similar calculations with townhouse
         */

        int homeHeatType = -1;
        int waterHeatType = -1;
        /*id for heating type for comparison
        0- Natural Gas
        1- Electricity
        2- Oil
        3- Propane
        4- Wood
         */

        int q11Id = question11Group.getCheckedRadioButtonId();
        int q12Id = question12Group.getCheckedRadioButtonId();
        int q13Id = question13Group.getCheckedRadioButtonId();
        int q14Id = question14Group.getCheckedRadioButtonId();
        int q15Id = question15Group.getCheckedRadioButtonId();
        int q16Id = question16Group.getCheckedRadioButtonId();
        int q17Id = question17Group.getCheckedRadioButtonId();

        RadioButton q11Button = findViewById(q11Id);
        RadioButton q12Button = findViewById(q12Id);
        RadioButton q13Button = findViewById(q13Id);
        RadioButton q14Button = findViewById(q14Id);
        RadioButton q15Button = findViewById(q15Id);
        RadioButton q16Button = findViewById(q16Id);
        RadioButton q17Button = findViewById(q17Id);

        if (q11Button != null) {
            String ans11 = q11Button.getText().toString();
            String ans12 = q12Button.getText().toString();
            String ans13 = q13Button.getText().toString();
            String ans14 = q14Button.getText().toString();
            String ans15 = q15Button.getText().toString();
            String ans16 = q16Button.getText().toString();

            //get housing type id
            if (ans11.equals("Detached house")) {
                if (ans13.equals("Under 1000 sq. ft.")) {
                    housingType = 0;
                } else if (ans13.equals("1000-2000 sq. ft.")) {
                    housingType = 1;
                } else if (ans13.equals("Over 2000 sq. ft")) {
                    housingType = 2;
                }
            } else if (ans11.equals("Semi-detached house")) {
                if (ans13.equals("Under 1000 sq. ft.")) {
                    housingType = 3;
                } else if (ans13.equals("1000-2000 sq. ft.")) {
                    housingType = 4;
                } else if (ans13.equals("Over 2000 sq. ft")) {
                    housingType = 5;
                }
            } else if (ans11.equals("Townhouse") || ans11.equals("Other")) {
                if (ans13.equals("Under 1000 sq. ft.")) {
                    housingType = 6;
                } else if (ans13.equals("1000-2000 sq. ft.")) {
                    housingType = 7;
                } else if (ans13.equals("Over 2000 sq. ft")) {
                    housingType = 8;
                }
            } else if (ans11.equals("Condo/Apartment")) {
                if (ans13.equals("Under 1000 sq. ft.")) {
                    housingType = 9;
                } else if (ans13.equals("1000-2000 sq. ft.")) {
                    housingType = 10;
                } else if (ans13.equals("Over 2000 sq. ft")) {
                    housingType = 11;
                }
            }

            //get home heating type
            if (ans14.equals("Natural Gas")) {
                homeHeatType = 0;
            } else if (ans14.equals("Electricity")) {
                homeHeatType = 1;
            } else if (ans14.equals("Oil")) {
                homeHeatType = 2;
            } else if (ans14.equals("Propane")) {
                homeHeatType = 3;
            } else if (ans14.equals("Wood")) {
                homeHeatType = 4;
            }

            //get water heating type
            if (ans16.equals("Natural Gas")) {
                waterHeatType = 0;
            } else if (ans16.equals("Electricity")) {
                waterHeatType = 1;
            } else if (ans16.equals("Oil")) {
                waterHeatType = 2;
            } else if (ans16.equals("Propane")) {
                waterHeatType = 3;
            } else if (ans16.equals("Wood")) {
                waterHeatType = 4;
            }

            //Add 233kg to calculation if home and water heating sources are different
            if (homeHeatType != waterHeatType) {
                consumptionCO2 += 233;
            }

            if (housingType == 0) {
                if (ans15.equals("Under $50")) {
                    switch (ans12) {
                        case "1":
                            if (homeHeatType == 0) {
                                consumptionCO2 += 2400;
                            } else if (homeHeatType == 1) {
                                consumptionCO2 += 200;
                            } else if (homeHeatType == 2) {
                                consumptionCO2 += 2100;
                            } else if (homeHeatType == 3) {
                                consumptionCO2 += 2870;
                            } else if (homeHeatType == 4) {
                                consumptionCO2 += 2170;
                            }
                            break;
                        case "2":
                            if (homeHeatType == 0) {
                                consumptionCO2 += 2600;
                            } else if (homeHeatType == 1) {
                                consumptionCO2 += 250;
                            } else if (homeHeatType == 2) {
                                consumptionCO2 += 2650;
                            } else if (homeHeatType == 3) {
                                consumptionCO2 += 3470;
                            } else if (homeHeatType == 4) {
                                consumptionCO2 += 2370;
                            }
                            break;
                        case "3-4":
                            if (homeHeatType == 0) {
                                consumptionCO2 += 2700;
                            } else if (homeHeatType == 1) {
                                consumptionCO2 += 380;
                            } else if (homeHeatType == 2) {
                                consumptionCO2 += 3200;
                            } else if (homeHeatType == 3) {
                                consumptionCO2 += 4370;
                            } else if (homeHeatType == 4) {
                                consumptionCO2 += 2670;
                            }
                            break;
                        case "5":
                            if (homeHeatType == 0) {
                                consumptionCO2 += 3000;
                            } else if (homeHeatType == 1) {
                                consumptionCO2 += 450;
                            } else if (homeHeatType == 2) {
                                consumptionCO2 += 3700;
                            } else if (homeHeatType == 3) {
                                consumptionCO2 += 5270;
                            } else if (homeHeatType == 4) {
                                consumptionCO2 += 2970;
                            }
                            break;
                    }
                } else if (ans15.equals("$50-$100")) {
                    switch (ans12) {
                        case "1":
                            if (homeHeatType == 0) {
                                consumptionCO2 += 2440;
                            } else if (homeHeatType == 1) {
                                consumptionCO2 += 400;
                            } else if (homeHeatType == 2) {
                                consumptionCO2 += 5200;
                            } else if (homeHeatType == 3) {
                                consumptionCO2 += 4400;
                            } else if (homeHeatType == 4) {
                                consumptionCO2 += 2340;
                            }
                            break;
                        case "2":
                            if (homeHeatType == 0) {
                                consumptionCO2 += 2640;
                            } else if (homeHeatType == 1) {
                                consumptionCO2 += 500;
                            } else if (homeHeatType == 2) {
                                consumptionCO2 += 5400;
                            } else if (homeHeatType == 3) {
                                consumptionCO2 += 4600;
                            } else if (homeHeatType == 4) {
                                consumptionCO2 += 2540;
                            }
                            break;
                        case "3-4":
                            if (homeHeatType == 0) {
                                consumptionCO2 += 2940;
                            } else if (homeHeatType == 1) {
                                consumptionCO2 += 550;
                            } else if (homeHeatType == 2) {
                                consumptionCO2 += 5700;
                            } else if (homeHeatType == 3) {
                                consumptionCO2 += 4900;
                            } else if (homeHeatType == 4) {
                                consumptionCO2 += 2840;
                            }
                            break;
                        case "5":
                            if (homeHeatType == 0) {
                                consumptionCO2 += 3240;
                            } else if (homeHeatType == 1) {
                                consumptionCO2 += 600;
                            } else if (homeHeatType == 2) {
                                consumptionCO2 += 6000;
                            } else if (homeHeatType == 3) {
                                consumptionCO2 += 5200;
                            } else if (homeHeatType == 4) {
                                consumptionCO2 += 3140;
                            }
                            break;
                    }
                } else if (ans15.equals("$100-150")) {
                    switch (ans12) {
                        case "1":
                            if (homeHeatType == 0) {
                                consumptionCO2 += 2610;
                            } else if (homeHeatType == 1) {
                                consumptionCO2 += 1200;
                            } else if (homeHeatType == 2) {
                                consumptionCO2 += 6100;
                            } else if (homeHeatType == 3) {
                                consumptionCO2 += 5400;
                            } else if (homeHeatType == 4) {
                                consumptionCO2 += 2510;
                            }
                            break;
                        case "2":
                            if (homeHeatType == 0) {
                                consumptionCO2 += 2810;
                            } else if (homeHeatType == 1) {
                                consumptionCO2 += 1450;
                            } else if (homeHeatType == 2) {
                                consumptionCO2 += 6250;
                            } else if (homeHeatType == 3) {
                                consumptionCO2 += 5600;
                            } else if (homeHeatType == 4) {
                                consumptionCO2 += 2710;
                            }
                            break;
                        case "3-4":
                            if (homeHeatType == 0) {
                                consumptionCO2 += 3110;
                            } else if (homeHeatType == 1) {
                                consumptionCO2 += 1600;
                            } else if (homeHeatType == 2) {
                                consumptionCO2 += 6700;
                            } else if (homeHeatType == 3) {
                                consumptionCO2 += 5900;
                            } else if (homeHeatType == 4) {
                                consumptionCO2 += 3010;
                            }
                            break;
                        case "5":
                            if (homeHeatType == 0) {
                                consumptionCO2 += 3410;
                            } else if (homeHeatType == 1) {
                                consumptionCO2 += 180;
                            } else if (homeHeatType == 2) {
                                consumptionCO2 += 6950;
                            } else if (homeHeatType == 3) {
                                consumptionCO2 += 6200;
                            } else if (homeHeatType == 4) {
                                consumptionCO2 += 3310;
                            }
                            break;
                    }
                } else if (ans15.equals("$150-$200")) {
                    switch (ans12) {
                        case "1":
                            if (homeHeatType == 0) {
                                consumptionCO2 += 2780;
                            } else if (homeHeatType == 1) {
                                consumptionCO2 += 1700;
                            } else if (homeHeatType == 2) {
                                consumptionCO2 += 7200;
                            } else if (homeHeatType == 3) {
                                consumptionCO2 += 6400;
                            } else if (homeHeatType == 4) {
                                consumptionCO2 += 2680;
                            }
                            break;
                        case "2":
                            if (homeHeatType == 0) {
                                consumptionCO2 += 2980;
                            } else if (homeHeatType == 1) {
                                consumptionCO2 += 1900;
                            } else if (homeHeatType == 2) {
                                consumptionCO2 += 7400;
                            } else if (homeHeatType == 3) {
                                consumptionCO2 += 6600;
                            } else if (homeHeatType == 4) {
                                consumptionCO2 += 2880;
                            }
                            break;
                        case "3-4":
                            if (homeHeatType == 0) {
                                consumptionCO2 += 3280;
                            } else if (homeHeatType == 1) {
                                consumptionCO2 += 2050;
                            } else if (homeHeatType == 2) {
                                consumptionCO2 += 7700;
                            } else if (homeHeatType == 3) {
                                consumptionCO2 += 6900;
                            } else if (homeHeatType == 4) {
                                consumptionCO2 += 3180;
                            }
                            break;
                        case "5":
                            if (homeHeatType == 0) {
                                consumptionCO2 += 3580;
                            } else if (homeHeatType == 1) {
                                consumptionCO2 += 2200;
                            } else if (homeHeatType == 2) {
                                consumptionCO2 += 8000;
                            } else if (homeHeatType == 3) {
                                consumptionCO2 += 7200;
                            } else if (homeHeatType == 4) {
                                consumptionCO2 += 3480;
                            }
                            break;
                    }
                } else if (ans15.equals("Over $200")) {
                    switch (ans12) {
                        case "1":
                            if (homeHeatType == 0) {
                                consumptionCO2 += 3100;
                            } else if (homeHeatType == 1) {
                                consumptionCO2 += 2300;
                            } else if (homeHeatType == 2) {
                                consumptionCO2 += 8200;
                            } else if (homeHeatType == 3) {
                                consumptionCO2 += 740;
                            } else if (homeHeatType == 4) {
                                consumptionCO2 += 3000;
                            }
                            break;
                        case "2":
                            if (homeHeatType == 0) {
                                consumptionCO2 += 3100;
                            } else if (homeHeatType == 1) {
                                consumptionCO2 += 2500;
                            } else if (homeHeatType == 2) {
                                consumptionCO2 += 8400;
                            } else if (homeHeatType == 3) {
                                consumptionCO2 += 7600;
                            } else if (homeHeatType == 4) {
                                consumptionCO2 += 3200;
                            }
                            break;
                        case "3-4":
                            if (homeHeatType == 0) {
                                consumptionCO2 += 3600;
                            } else if (homeHeatType == 1) {
                                consumptionCO2 += 2700;
                            } else if (homeHeatType == 2) {
                                consumptionCO2 += 8700;
                            } else if (homeHeatType == 3) {
                                consumptionCO2 += 7900;
                            } else if (homeHeatType == 4) {
                                consumptionCO2 += 3500;
                            }
                            break;
                        case "5":
                            if (homeHeatType == 0) {
                                consumptionCO2 += 3900;
                            } else if (homeHeatType == 1) {
                                consumptionCO2 += 3000;
                            } else if (homeHeatType == 2) {
                                consumptionCO2 += 9000;
                            } else if (homeHeatType == 3) {
                                consumptionCO2 += 8200;
                            } else if (homeHeatType == 4) {
                                consumptionCO2 += 3800;
                            }
                            break;
                    }
                }
            } else if (housingType == 1) {
                if (ans15.equals("Under $50")) {
                    switch (ans12) {
                        case "1":
                            if (homeHeatType == 0) {
                                consumptionCO2 += 2800;
                            } else if (homeHeatType == 1) {
                                consumptionCO2 += 300;
                            } else if (homeHeatType == 2) {
                                consumptionCO2 += 3800;
                            } else if (homeHeatType == 3) {
                                consumptionCO2 += 3770;
                            } else if (homeHeatType == 4) {
                                consumptionCO2 += 3670;
                            }
                            break;
                        case "2":
                            if (homeHeatType == 0) {
                                consumptionCO2 += 3000;
                            } else if (homeHeatType == 1) {
                                consumptionCO2 += 380;
                            } else if (homeHeatType == 2) {
                                consumptionCO2 += 4000;
                            } else if (homeHeatType == 3) {
                                consumptionCO2 += 4470;
                            } else if (homeHeatType == 4) {
                                consumptionCO2 += 4170;
                            }
                            break;
                        case "3-4":
                            if (homeHeatType == 0) {
                                consumptionCO2 += 3380;
                            } else if (homeHeatType == 1) {
                                consumptionCO2 += 500;
                            } else if (homeHeatType == 2) {
                                consumptionCO2 += 4700;
                            } else if (homeHeatType == 3) {
                                consumptionCO2 += 5670;
                            } else if (homeHeatType == 4) {
                                consumptionCO2 += 4870;
                            }
                            break;
                        case "5":
                            if (homeHeatType == 0) {
                                consumptionCO2 += 3860;
                            } else if (homeHeatType == 1) {
                                consumptionCO2 += 600;
                            } else if (homeHeatType == 2) {
                                consumptionCO2 += 5350;
                            } else if (homeHeatType == 3) {
                                consumptionCO2 += 6570;
                            } else if (homeHeatType == 4) {
                                consumptionCO2 += 5670;
                            }
                            break;
                    }
                } else if (ans15.equals("$50-$100")) {
                    switch (ans12) {
                        case "1":
                            if (homeHeatType == 0) {
                                consumptionCO2 += 5900;
                            } else if (homeHeatType == 1) {
                                consumptionCO2 += 600;
                            } else if (homeHeatType == 2) {
                                consumptionCO2 += 5300;
                            } else if (homeHeatType == 3) {
                                consumptionCO2 += 3940;
                            } else if (homeHeatType == 4) {
                                consumptionCO2 += 3840;
                            }
                            break;
                        case "2":
                            if (homeHeatType == 0) {
                                consumptionCO2 += 6100;
                            } else if (homeHeatType == 1) {
                                consumptionCO2 += 800;
                            } else if (homeHeatType == 2) {
                                consumptionCO2 += 5440;
                            } else if (homeHeatType == 3) {
                                consumptionCO2 += 4640;
                            } else if (homeHeatType == 4) {
                                consumptionCO2 += 4340;
                            }
                            break;
                        case "3-4":
                            if (homeHeatType == 0) {
                                consumptionCO2 += 6400;
                            } else if (homeHeatType == 1) {
                                consumptionCO2 += 1050;
                            } else if (homeHeatType == 2) {
                                consumptionCO2 += 5800;
                            } else if (homeHeatType == 3) {
                                consumptionCO2 += 5740;
                            } else if (homeHeatType == 4) {
                                consumptionCO2 += 5040;
                            }
                            break;
                        case "5":
                            if (homeHeatType == 0) {
                                consumptionCO2 += 6700;
                            } else if (homeHeatType == 1) {
                                consumptionCO2 += 1200;
                            } else if (homeHeatType == 2) {
                                consumptionCO2 += 6100;
                            } else if (homeHeatType == 3) {
                                consumptionCO2 += 6740;
                            } else if (homeHeatType == 4) {
                                consumptionCO2 += 5840;
                            }
                            break;
                    }
                } else if (ans15.equals("$100-150")) {
                    switch (ans12) {
                        case "1":
                            if (homeHeatType == 0) {
                                consumptionCO2 += 6500;
                            } else if (homeHeatType == 1) {
                                consumptionCO2 += 1200;
                            } else if (homeHeatType == 2) {
                                consumptionCO2 += 6200;
                            } else if (homeHeatType == 3) {
                                consumptionCO2 += 7100;
                            } else if (homeHeatType == 4) {
                                consumptionCO2 += 4010;
                            }
                            break;
                        case "2":
                            if (homeHeatType == 0) {
                                consumptionCO2 += 6700;
                            } else if (homeHeatType == 1) {
                                consumptionCO2 += 1450;
                            } else if (homeHeatType == 2) {
                                consumptionCO2 += 6400;
                            } else if (homeHeatType == 3) {
                                consumptionCO2 += 7230;
                            } else if (homeHeatType == 4) {
                                consumptionCO2 += 4510;
                            }
                            break;
                        case "3-4":
                            if (homeHeatType == 0) {
                                consumptionCO2 += 7000;
                            } else if (homeHeatType == 1) {
                                consumptionCO2 += 1600;
                            } else if (homeHeatType == 2) {
                                consumptionCO2 += 6700;
                            } else if (homeHeatType == 3) {
                                consumptionCO2 += 7400;
                            } else if (homeHeatType == 4) {
                                consumptionCO2 += 5210;
                            }
                            break;
                        case "5":
                            if (homeHeatType == 0) {
                                consumptionCO2 += 7300;
                            } else if (homeHeatType == 1) {
                                consumptionCO2 += 1800;
                            } else if (homeHeatType == 2) {
                                consumptionCO2 += 7000;
                            } else if (homeHeatType == 3) {
                                consumptionCO2 += 7550;
                            } else if (homeHeatType == 4) {
                                consumptionCO2 += 6010;
                            }
                            break;
                    }
                } else if (ans15.equals("$150-$200")) {
                    switch (ans12) {
                        case "1":
                            if (homeHeatType == 0) {
                                consumptionCO2 += 7100;
                            } else if (homeHeatType == 1) {
                                consumptionCO2 += 1800;
                            } else if (homeHeatType == 2) {
                                consumptionCO2 += 7200;
                            } else if (homeHeatType == 3) {
                                consumptionCO2 += 4280;
                            } else if (homeHeatType == 4) {
                                consumptionCO2 += 4180;
                            }
                            break;
                        case "2":
                            if (homeHeatType == 0) {
                                consumptionCO2 += 7300;
                            } else if (homeHeatType == 1) {
                                consumptionCO2 += 2000;
                            } else if (homeHeatType == 2) {
                                consumptionCO2 += 7400;
                            } else if (homeHeatType == 3) {
                                consumptionCO2 += 4980;
                            } else if (homeHeatType == 4) {
                                consumptionCO2 += 4680;
                            }
                            break;
                        case "3-4":
                            if (homeHeatType == 0) {
                                consumptionCO2 += 7600;
                            } else if (homeHeatType == 1) {
                                consumptionCO2 += 2250;
                            } else if (homeHeatType == 2) {
                                consumptionCO2 += 7700;
                            } else if (homeHeatType == 3) {
                                consumptionCO2 += 5985;
                            } else if (homeHeatType == 4) {
                                consumptionCO2 += 5380;
                            }
                            break;
                        case "5":
                            if (homeHeatType == 0) {
                                consumptionCO2 += 7900;
                            } else if (homeHeatType == 1) {
                                consumptionCO2 += 2400;
                            } else if (homeHeatType == 2) {
                                consumptionCO2 += 8000;
                            } else if (homeHeatType == 3) {
                                consumptionCO2 += 7080;
                            } else if (homeHeatType == 4) {
                                consumptionCO2 += 6180;
                            }
                            break;
                    }
                } else if (ans15.equals("Over $200")) {
                    switch (ans12) {
                        case "1":
                            if (homeHeatType == 0) {
                                consumptionCO2 += 8300;
                            } else if (homeHeatType == 1) {
                                consumptionCO2 += 2400;
                            } else if (homeHeatType == 2) {
                                consumptionCO2 += 8200;
                            } else if (homeHeatType == 3) {
                                consumptionCO2 += 4600;
                            } else if (homeHeatType == 4) {
                                consumptionCO2 += 4500;
                            }
                            break;
                        case "2":
                            if (homeHeatType == 0) {
                                consumptionCO2 += 8500;
                            } else if (homeHeatType == 1) {
                                consumptionCO2 += 2600;
                            } else if (homeHeatType == 2) {
                                consumptionCO2 += 8400;
                            } else if (homeHeatType == 3) {
                                consumptionCO2 += 5300;
                            } else if (homeHeatType == 4) {
                                consumptionCO2 += 5000;
                            }
                            break;
                        case "3-4":
                            if (homeHeatType == 0) {
                                consumptionCO2 += 8800;
                            } else if (homeHeatType == 1) {
                                consumptionCO2 += 2800;
                            } else if (homeHeatType == 2) {
                                consumptionCO2 += 8700;
                            } else if (homeHeatType == 3) {
                                consumptionCO2 += 6350;
                            } else if (homeHeatType == 4) {
                                consumptionCO2 += 5750;
                            }
                            break;
                        case "5":
                            if (homeHeatType == 0) {
                                consumptionCO2 += 9100;
                            } else if (homeHeatType == 1) {
                                consumptionCO2 += 3100;
                            } else if (homeHeatType == 2) {
                                consumptionCO2 += 9000;
                            } else if (homeHeatType == 3) {
                                consumptionCO2 += 7400;
                            } else if (homeHeatType == 4) {
                                consumptionCO2 += 6500;
                            }
                            break;
                    }
                }
            } else if (housingType == 2) {
                if (ans15.equals("Under $50")) {
                    switch (ans12) {
                        case "1":
                            if (homeHeatType == 0) {
                                consumptionCO2 += 2800;
                            } else if (homeHeatType == 1) {
                                consumptionCO2 += 320;
                            } else if (homeHeatType == 2) {
                                consumptionCO2 += 5400;
                            } else if (homeHeatType == 3) {
                                consumptionCO2 += 5570;
                            } else if (homeHeatType == 4) {
                                consumptionCO2 += 4170;
                            }
                            break;
                        case "2":
                            if (homeHeatType == 0) {
                                consumptionCO2 += 2880;
                            } else if (homeHeatType == 1) {
                                consumptionCO2 += 450;
                            } else if (homeHeatType == 2) {
                                consumptionCO2 += 6200;
                            } else if (homeHeatType == 3) {
                                consumptionCO2 += 6170;
                            } else if (homeHeatType == 4) {
                                consumptionCO2 += 4670;
                            }
                            break;
                        case "3-4":
                            if (homeHeatType == 0) {
                                consumptionCO2 += 3000;
                            } else if (homeHeatType == 1) {
                                consumptionCO2 += 520;
                            } else if (homeHeatType == 2) {
                                consumptionCO2 += 7000;
                            } else if (homeHeatType == 3) {
                                consumptionCO2 += 6970;
                            } else if (homeHeatType == 4) {
                                consumptionCO2 += 5270;
                            }
                            break;
                        case "5":
                            if (homeHeatType == 0) {
                                consumptionCO2 += 3230;
                            } else if (homeHeatType == 1) {
                                consumptionCO2 += 675;
                            } else if (homeHeatType == 2) {
                                consumptionCO2 += 8900;
                            } else if (homeHeatType == 3) {
                                consumptionCO2 += 7970;
                            } else if (homeHeatType == 4) {
                                consumptionCO2 += 6170;
                            }
                            break;
                    }
                } else if (ans15.equals("$50-$100")) {
                    switch (ans12) {
                        case "1":
                            if (homeHeatType == 0) {
                                consumptionCO2 += 3000;
                            } else if (homeHeatType == 1) {
                                consumptionCO2 += 600;
                            } else if (homeHeatType == 2) {
                                consumptionCO2 += 10500;
                            } else if (homeHeatType == 3) {
                                consumptionCO2 += 5740;
                            } else if (homeHeatType == 4) {
                                consumptionCO2 += 4340;
                            }
                            break;
                        case "2":
                            if (homeHeatType == 0) {
                                consumptionCO2 += 3200;
                            } else if (homeHeatType == 1) {
                                consumptionCO2 += 900;
                            } else if (homeHeatType == 2) {
                                consumptionCO2 += 11000;
                            } else if (homeHeatType == 3) {
                                consumptionCO2 += 6340;
                            } else if (homeHeatType == 4) {
                                consumptionCO2 += 4840;
                            }
                            break;
                        case "3-4":
                            if (homeHeatType == 0) {
                                consumptionCO2 += 3500;
                            } else if (homeHeatType == 1) {
                                consumptionCO2 += 1500;
                            } else if (homeHeatType == 2) {
                                consumptionCO2 += 12500;
                            } else if (homeHeatType == 3) {
                                consumptionCO2 += 7240;
                            } else if (homeHeatType == 4) {
                                consumptionCO2 += 5640;
                            }
                            break;
                        case "5":
                            if (homeHeatType == 0) {
                                consumptionCO2 += 3800;
                            } else if (homeHeatType == 1) {
                                consumptionCO2 += 1800;
                            } else if (homeHeatType == 2) {
                                consumptionCO2 += 14000;
                            } else if (homeHeatType == 3) {
                                consumptionCO2 += 8140;
                            } else if (homeHeatType == 4) {
                                consumptionCO2 += 6340;
                            }
                            break;
                    }
                } else if (ans15.equals("$100-150")) {
                    switch (ans12) {
                        case "1":
                            if (homeHeatType == 0) {
                                consumptionCO2 += 3200;
                            } else if (homeHeatType == 1) {
                                consumptionCO2 += 1800;
                            } else if (homeHeatType == 2) {
                                consumptionCO2 += 14000;
                            } else if (homeHeatType == 3) {
                                consumptionCO2 += 5800;
                            } else if (homeHeatType == 4) {
                                consumptionCO2 += 4510;
                            }
                            break;
                        case "2":
                            if (homeHeatType == 0) {
                                consumptionCO2 += 3400;
                            } else if (homeHeatType == 1) {
                                consumptionCO2 += 2100;
                            } else if (homeHeatType == 2) {
                                consumptionCO2 += 15500;
                            } else if (homeHeatType == 3) {
                                consumptionCO2 += 6410;
                            } else if (homeHeatType == 4) {
                                consumptionCO2 += 5010;
                            }
                            break;
                        case "3-4":
                            if (homeHeatType == 0) {
                                consumptionCO2 += 3700;
                            } else if (homeHeatType == 1) {
                                consumptionCO2 += 2300;
                            } else if (homeHeatType == 2) {
                                consumptionCO2 += 16250;
                            } else if (homeHeatType == 3) {
                                consumptionCO2 += 7300;
                            } else if (homeHeatType == 4) {
                                consumptionCO2 += 5710;
                            }
                            break;
                        case "5":
                            if (homeHeatType == 0) {
                                consumptionCO2 += 4000;
                            } else if (homeHeatType == 1) {
                                consumptionCO2 += 2700;
                            } else if (homeHeatType == 2) {
                                consumptionCO2 += 17500;
                            } else if (homeHeatType == 3) {
                                consumptionCO2 += 8230;
                            } else if (homeHeatType == 4) {
                                consumptionCO2 += 6510;
                            }
                            break;
                    }
                } else if (ans15.equals("$150-$200")) {
                    switch (ans12) {
                        case "1":
                            if (homeHeatType == 0) {
                                consumptionCO2 += 3400;
                            } else if (homeHeatType == 1) {
                                consumptionCO2 += 2700;
                            } else if (homeHeatType == 2) {
                                consumptionCO2 += 17500;
                            } else if (homeHeatType == 3) {
                                consumptionCO2 += 5852;
                            } else if (homeHeatType == 4) {
                                consumptionCO2 += 4680;
                            }
                            break;
                        case "2":
                            if (homeHeatType == 0) {
                                consumptionCO2 += 3600;
                            } else if (homeHeatType == 1) {
                                consumptionCO2 += 3100;
                            } else if (homeHeatType == 2) {
                                consumptionCO2 += 18100;
                            } else if (homeHeatType == 3) {
                                consumptionCO2 += 6560;
                            } else if (homeHeatType == 4) {
                                consumptionCO2 += 5180;
                            }
                            break;
                        case "3-4":
                            if (homeHeatType == 0) {
                                consumptionCO2 += 4100;
                            } else if (homeHeatType == 1) {
                                consumptionCO2 += 3400;
                            } else if (homeHeatType == 2) {
                                consumptionCO2 += 20000;
                            } else if (homeHeatType == 3) {
                                consumptionCO2 += 7600;
                            } else if (homeHeatType == 4) {
                                consumptionCO2 += 5980;
                            }
                            break;
                        case "5":
                            if (homeHeatType == 0) {
                                consumptionCO2 += 4400;
                            } else if (homeHeatType == 1) {
                                consumptionCO2 += 3600;
                            } else if (homeHeatType == 2) {
                                consumptionCO2 += 21000;
                            } else if (homeHeatType == 3) {
                                consumptionCO2 += 8300;
                            } else if (homeHeatType == 4) {
                                consumptionCO2 += 6680;
                            }
                            break;
                    }
                } else if (ans15.equals("Over $200")) {
                    switch (ans12) {
                        case "1":
                            if (homeHeatType == 0) {
                                consumptionCO2 += 3600;
                            } else if (homeHeatType == 1) {
                                consumptionCO2 += 3600;
                            } else if (homeHeatType == 2) {
                                consumptionCO2 += 21000;
                            } else if (homeHeatType == 3) {
                                consumptionCO2 += 6100;
                            } else if (homeHeatType == 4) {
                                consumptionCO2 += 5000;
                            }
                            break;
                        case "2":
                            if (homeHeatType == 0) {
                                consumptionCO2 += 3800;
                            } else if (homeHeatType == 1) {
                                consumptionCO2 += 3800;
                            } else if (homeHeatType == 2) {
                                consumptionCO2 += 22000;
                            } else if (homeHeatType == 3) {
                                consumptionCO2 += 7890;
                            } else if (homeHeatType == 4) {
                                consumptionCO2 += 6250;
                            }
                            break;
                        case "3-4":
                            if (homeHeatType == 0) {
                                consumptionCO2 += 4100;
                            } else if (homeHeatType == 1) {
                                consumptionCO2 += 4000;
                            } else if (homeHeatType == 2) {
                                consumptionCO2 += 23500;
                            } else if (homeHeatType == 3) {
                                consumptionCO2 += 7890;
                            } else if (homeHeatType == 4) {
                                consumptionCO2 += 6250;
                            }
                            break;
                        case "5":
                            if (homeHeatType == 0) {
                                consumptionCO2 += 4400;
                            } else if (homeHeatType == 1) {
                                consumptionCO2 += 4200;
                            } else if (homeHeatType == 2) {
                                consumptionCO2 += 25000;
                            } else if (homeHeatType == 3) {
                                consumptionCO2 += 8710;
                            } else if (homeHeatType == 4) {
                                consumptionCO2 += 7000;
                            }
                            break;
                    }
                }
            } else if (housingType == 3) {
                if (ans15.equals("Under $50")) {
                    switch (ans12) {
                        case "1":
                            if (homeHeatType == 0) {
                                consumptionCO2 += 2160;
                            } else if (homeHeatType == 1) {
                                consumptionCO2 += 300;
                            } else if (homeHeatType == 2) {
                                consumptionCO2 += 2500;
                            } else if (homeHeatType == 3) {
                                consumptionCO2 += 2200;
                            } else if (homeHeatType == 4) {
                                consumptionCO2 += 2100;
                            }
                            break;
                        case "2":
                            if (homeHeatType == 0) {
                                consumptionCO2 += 2349;
                            } else if (homeHeatType == 1) {
                                consumptionCO2 += 410;
                            } else if (homeHeatType == 2) {
                                consumptionCO2 += 2592;
                            } else if (homeHeatType == 3) {
                                consumptionCO2 += 2300;
                            } else if (homeHeatType == 4) {
                                consumptionCO2 += 2450;
                            }
                            break;
                        case "3-4":
                            if (homeHeatType == 0) {
                                consumptionCO2 += 2732;
                            } else if (homeHeatType == 1) {
                                consumptionCO2 += 500;
                            } else if (homeHeatType == 2) {
                                consumptionCO2 += 2680;
                            } else if (homeHeatType == 3) {
                                consumptionCO2 += 2450;
                            } else if (homeHeatType == 4) {
                                consumptionCO2 += 2700;
                            }
                            break;
                        case "5":
                            if (homeHeatType == 0) {
                                consumptionCO2 += 3199;
                            } else if (homeHeatType == 1) {
                                consumptionCO2 += 580;
                            } else if (homeHeatType == 2) {
                                consumptionCO2 += 2750;
                            } else if (homeHeatType == 3) {
                                consumptionCO2 += 2600;
                            } else if (homeHeatType == 4) {
                                consumptionCO2 += 3000;
                            }
                            break;
                    }
                } else if (ans15.equals("$50-$100")) {
                    switch (ans12) {
                        case "1":
                            if (homeHeatType == 0) {
                                consumptionCO2 += 2400;
                            } else if (homeHeatType == 1) {
                                consumptionCO2 += 410;
                            } else if (homeHeatType == 2) {
                                consumptionCO2 += 2800;
                            } else if (homeHeatType == 3) {
                                consumptionCO2 += 2600;
                            } else if (homeHeatType == 4) {
                                consumptionCO2 += 3000;
                            }
                            break;
                        case "2":
                            if (homeHeatType == 0) {
                                consumptionCO2 += 2600;
                            } else if (homeHeatType == 1) {
                                consumptionCO2 += 500;
                            } else if (homeHeatType == 2) {
                                consumptionCO2 += 3000;
                            } else if (homeHeatType == 3) {
                                consumptionCO2 += 3100;
                            } else if (homeHeatType == 4) {
                                consumptionCO2 += 3500;
                            }
                            break;
                        case "3-4":
                            if (homeHeatType == 0) {
                                consumptionCO2 += 2900;
                            } else if (homeHeatType == 1) {
                                consumptionCO2 += 560;
                            } else if (homeHeatType == 2) {
                                consumptionCO2 += 3300;
                            } else if (homeHeatType == 3) {
                                consumptionCO2 += 3100;
                            } else if (homeHeatType == 4) {
                                consumptionCO2 += 3500;
                            }
                            break;
                        case "5":
                            if (homeHeatType == 0) {
                                consumptionCO2 += 3200;
                            } else if (homeHeatType == 1) {
                                consumptionCO2 += 600;
                            } else if (homeHeatType == 2) {
                                consumptionCO2 += 3600;
                            } else if (homeHeatType == 3) {
                                consumptionCO2 += 3400;
                            } else if (homeHeatType == 4) {
                                consumptionCO2 += 3800;
                            }
                            break;
                    }
                } else if (ans15.equals("$100-150")) {
                    switch (ans12) {
                        case "1":
                            if (homeHeatType == 0) {
                                consumptionCO2 += 2600;
                            } else if (homeHeatType == 1) {
                                consumptionCO2 += 1210;
                            } else if (homeHeatType == 2) {
                                consumptionCO2 += 3000;
                            } else if (homeHeatType == 3) {
                                consumptionCO2 += 2800;
                            } else if (homeHeatType == 4) {
                                consumptionCO2 += 3200;
                            }
                            break;
                        case "2":
                            if (homeHeatType == 0) {
                                consumptionCO2 += 2800;
                            } else if (homeHeatType == 1) {
                                consumptionCO2 += 1450;
                            } else if (homeHeatType == 2) {
                                consumptionCO2 += 3200;
                            } else if (homeHeatType == 3) {
                                consumptionCO2 += 3000;
                            } else if (homeHeatType == 4) {
                                consumptionCO2 += 3400;
                            }
                            break;
                        case "3-4":
                            if (homeHeatType == 0) {
                                consumptionCO2 += 3100;
                            } else if (homeHeatType == 1) {
                                consumptionCO2 += 1620;
                            } else if (homeHeatType == 2) {
                                consumptionCO2 += 3500;
                            } else if (homeHeatType == 3) {
                                consumptionCO2 += 3300;
                            } else if (homeHeatType == 4) {
                                consumptionCO2 += 3700;
                            }
                            break;
                        case "5":
                            if (homeHeatType == 0) {
                                consumptionCO2 += 3400;
                            } else if (homeHeatType == 1) {
                                consumptionCO2 += 1820;
                            } else if (homeHeatType == 2) {
                                consumptionCO2 += 3800;
                            } else if (homeHeatType == 3) {
                                consumptionCO2 += 3600;
                            } else if (homeHeatType == 4) {
                                consumptionCO2 += 4000;
                            }
                            break;
                    }
                } else if (ans15.equals("$150-$200")) {
                    switch (ans12) {
                        case "1":
                            if (homeHeatType == 0) {
                                consumptionCO2 += 2800;
                            } else if (homeHeatType == 1) {
                                consumptionCO2 += 1700;
                            } else if (homeHeatType == 2) {
                                consumptionCO2 += 3200;
                            } else if (homeHeatType == 3) {
                                consumptionCO2 += 3000;
                            } else if (homeHeatType == 4) {
                                consumptionCO2 += 3400;
                            }
                            break;
                        case "2":
                            if (homeHeatType == 0) {
                                consumptionCO2 += 3000;
                            } else if (homeHeatType == 1) {
                                consumptionCO2 += 1900;
                            } else if (homeHeatType == 2) {
                                consumptionCO2 += 3400;
                            } else if (homeHeatType == 3) {
                                consumptionCO2 += 3200;
                            } else if (homeHeatType == 4) {
                                consumptionCO2 += 3600;
                            }
                            break;
                        case "3-4":
                            if (homeHeatType == 0) {
                                consumptionCO2 += 3300;
                            } else if (homeHeatType == 1) {
                                consumptionCO2 += 2000;
                            } else if (homeHeatType == 2) {
                                consumptionCO2 += 3700;
                            } else if (homeHeatType == 3) {
                                consumptionCO2 += 3500;
                            } else if (homeHeatType == 4) {
                                consumptionCO2 += 3900;
                            }
                            break;
                        case "5":
                            if (homeHeatType == 0) {
                                consumptionCO2 += 3300;
                            } else if (homeHeatType == 1) {
                                consumptionCO2 += 2000;
                            } else if (homeHeatType == 2) {
                                consumptionCO2 += 3700;
                            } else if (homeHeatType == 3) {
                                consumptionCO2 += 3800;
                            } else if (homeHeatType == 4) {
                                consumptionCO2 += 4200;
                            }
                            break;
                    }
                } else if (ans15.equals("Over $200")) {
                    switch (ans12) {
                        case "1":
                            if (homeHeatType == 0) {
                                consumptionCO2 += 3000;
                            } else if (homeHeatType == 1) {
                                consumptionCO2 += 2200;
                            } else if (homeHeatType == 2) {
                                consumptionCO2 += 3400;
                            } else if (homeHeatType == 3) {
                                consumptionCO2 += 3200;
                            } else if (homeHeatType == 4) {
                                consumptionCO2 += 3600;
                            }
                            break;
                        case "2":
                            if (homeHeatType == 0) {
                                consumptionCO2 += 3200;
                            } else if (homeHeatType == 1) {
                                consumptionCO2 += 2500;
                            } else if (homeHeatType == 2) {
                                consumptionCO2 += 3600;
                            } else if (homeHeatType == 3) {
                                consumptionCO2 += 3400;
                            } else if (homeHeatType == 4) {
                                consumptionCO2 += 3800;
                            }
                            break;
                        case "3-4":
                            if (homeHeatType == 0) {
                                consumptionCO2 += 3500;
                            } else if (homeHeatType == 1) {
                                consumptionCO2 += 2600;
                            } else if (homeHeatType == 2) {
                                consumptionCO2 += 3900;
                            } else if (homeHeatType == 3) {
                                consumptionCO2 += 3700;
                            } else if (homeHeatType == 4) {
                                consumptionCO2 += 4100;
                            }
                            break;
                        case "5":
                            if (homeHeatType == 0) {
                                consumptionCO2 += 3800;
                            } else if (homeHeatType == 1) {
                                consumptionCO2 += 3000;
                            } else if (homeHeatType == 2) {
                                consumptionCO2 += 4200;
                            } else if (homeHeatType == 3) {
                                consumptionCO2 += 4000;
                            } else if (homeHeatType == 4) {
                                consumptionCO2 += 4400;
                            }
                            break;
                    }
                }
            } else if (housingType == 4) {
                if (ans15.equals("Under $50")) {
                    switch (ans12) {
                        case "1":
                            if (homeHeatType == 0) {
                                consumptionCO2 += 2443;
                            } else if (homeHeatType == 1) {
                                consumptionCO2 += 300;
                            } else if (homeHeatType == 2) {
                                consumptionCO2 += 3400;
                            } else if (homeHeatType == 3) {
                                consumptionCO2 += 4000;
                            } else if (homeHeatType == 4) {
                                consumptionCO2 += 1500;
                            }
                            break;
                        case "2":
                            if (homeHeatType == 0) {
                                consumptionCO2 += 2727;
                            } else if (homeHeatType == 1) {
                                consumptionCO2 += 410;
                            } else if (homeHeatType == 2) {
                                consumptionCO2 += 3499;
                            } else if (homeHeatType == 3) {
                                consumptionCO2 += 4300;
                            } else if (homeHeatType == 4) {
                                consumptionCO2 += 1800;
                            }
                            break;
                        case "3-4":
                            if (homeHeatType == 0) {
                                consumptionCO2 += 3151;
                            } else if (homeHeatType == 1) {
                                consumptionCO2 += 550;
                            } else if (homeHeatType == 2) {
                                consumptionCO2 += 3599;
                            } else if (homeHeatType == 3) {
                                consumptionCO2 += 4700;
                            } else if (homeHeatType == 4) {
                                consumptionCO2 += 2100;
                            }
                            break;
                        case "5":
                            if (homeHeatType == 0) {
                                consumptionCO2 += 3578;
                            } else if (homeHeatType == 1) {
                                consumptionCO2 += 605;
                            } else if (homeHeatType == 2) {
                                consumptionCO2 += 3700;
                            } else if (homeHeatType == 3) {
                                consumptionCO2 += 4900;
                            } else if (homeHeatType == 4) {
                                consumptionCO2 += 2500;
                            }
                            break;
                    }
                } else if (ans15.equals("$50-$100")) {
                    switch (ans12) {
                        case "1":
                            if (homeHeatType == 0) {
                                consumptionCO2 += 4000;
                            } else if (homeHeatType == 1) {
                                consumptionCO2 += 600;
                            } else if (homeHeatType == 2) {
                                consumptionCO2 += 4000;
                            } else if (homeHeatType == 3) {
                                consumptionCO2 += 5000;
                            } else if (homeHeatType == 4) {
                                consumptionCO2 += 2500;
                            }
                            break;
                        case "2":
                            if (homeHeatType == 0) {
                                consumptionCO2 += 5200;
                            } else if (homeHeatType == 1) {
                                consumptionCO2 += 800;
                            } else if (homeHeatType == 2) {
                                consumptionCO2 += 4600;
                            } else if (homeHeatType == 3) {
                                consumptionCO2 += 6200;
                            } else if (homeHeatType == 4) {
                                consumptionCO2 += 2700;
                            }
                            break;
                        case "3-4":
                            if (homeHeatType == 0) {
                                consumptionCO2 += 6800;
                            } else if (homeHeatType == 1) {
                                consumptionCO2 += 1050;
                            } else if (homeHeatType == 2) {
                                consumptionCO2 += 5100;
                            } else if (homeHeatType == 3) {
                                consumptionCO2 += 7300;
                            } else if (homeHeatType == 4) {
                                consumptionCO2 += 3500;
                            }
                            break;
                        case "5":
                            if (homeHeatType == 0) {
                                consumptionCO2 += 7500;
                            } else if (homeHeatType == 1) {
                                consumptionCO2 += 1200;
                            } else if (homeHeatType == 2) {
                                consumptionCO2 += 6000;
                            } else if (homeHeatType == 3) {
                                consumptionCO2 += 8000;
                            } else if (homeHeatType == 4) {
                                consumptionCO2 += 4000;
                            }
                            break;
                    }
                } else if (ans15.equals("$100-150")) {
                    switch (ans12) {
                        case "1":
                            if (homeHeatType == 0) {
                                consumptionCO2 += 4500;
                            } else if (homeHeatType == 1) {
                                consumptionCO2 += 1200;
                            } else if (homeHeatType == 2) {
                                consumptionCO2 += 6100;
                            } else if (homeHeatType == 3) {
                                consumptionCO2 += 7000;
                            } else if (homeHeatType == 4) {
                                consumptionCO2 += 4100;
                            }
                            break;
                        case "2":
                            if (homeHeatType == 0) {
                                consumptionCO2 += 6000;
                            } else if (homeHeatType == 1) {
                                consumptionCO2 += 1550;
                            } else if (homeHeatType == 2) {
                                consumptionCO2 += 6900;
                            } else if (homeHeatType == 3) {
                                consumptionCO2 += 8000;
                            } else if (homeHeatType == 4) {
                                consumptionCO2 += 4300;
                            }
                            break;
                        case "3-4":
                            if (homeHeatType == 0) {
                                consumptionCO2 += 7800;
                            } else if (homeHeatType == 1) {
                                consumptionCO2 += 1700;
                            } else if (homeHeatType == 2) {
                                consumptionCO2 += 7300;
                            } else if (homeHeatType == 3) {
                                consumptionCO2 += 9100;
                            } else if (homeHeatType == 4) {
                                consumptionCO2 += 4850;
                            }
                            break;
                        case "5":
                            if (homeHeatType == 0) {
                                consumptionCO2 += 8500;
                            } else if (homeHeatType == 1) {
                                consumptionCO2 += 1800;
                            } else if (homeHeatType == 2) {
                                consumptionCO2 += 8500;
                            } else if (homeHeatType == 3) {
                                consumptionCO2 += 10000;
                            } else if (homeHeatType == 4) {
                                consumptionCO2 += 5500;
                            }
                            break;
                    }
                } else if (ans15.equals("$150-$200")) {
                    switch (ans12) {
                        case "1":
                            if (homeHeatType == 0) {
                                consumptionCO2 += 5000;
                            } else if (homeHeatType == 1) {
                                consumptionCO2 += 1800;
                            } else if (homeHeatType == 2) {
                                consumptionCO2 += 8000;
                            } else if (homeHeatType == 3) {
                                consumptionCO2 += 9000;
                            } else if (homeHeatType == 4) {
                                consumptionCO2 += 5500;
                            }
                            break;
                        case "2":
                            if (homeHeatType == 0) {
                                consumptionCO2 += 6500;
                            } else if (homeHeatType == 1) {
                                consumptionCO2 += 2000;
                            } else if (homeHeatType == 2) {
                                consumptionCO2 += 8800;
                            } else if (homeHeatType == 3) {
                                consumptionCO2 += 10200;
                            } else if (homeHeatType == 4) {
                                consumptionCO2 += 6000;
                            }
                            break;
                        case "3-4":
                            if (homeHeatType == 0) {
                                consumptionCO2 += 8800;
                            } else if (homeHeatType == 1) {
                                consumptionCO2 += 2250;
                            } else if (homeHeatType == 2) {
                                consumptionCO2 += 9200;
                            } else if (homeHeatType == 3) {
                                consumptionCO2 += 11000;
                            } else if (homeHeatType == 4) {
                                consumptionCO2 += 6800;
                            }
                            break;
                        case "5":
                            if (homeHeatType == 0) {
                                consumptionCO2 += 10000;
                            } else if (homeHeatType == 1) {
                                consumptionCO2 += 2400;
                            } else if (homeHeatType == 2) {
                                consumptionCO2 += 10550;
                            } else if (homeHeatType == 3) {
                                consumptionCO2 += 12000;
                            } else if (homeHeatType == 4) {
                                consumptionCO2 += 7100;
                            }
                            break;
                    }
                } else if (ans15.equals("Over $200")) {
                    switch (ans12) {
                        case "1":
                            if (homeHeatType == 0) {
                                consumptionCO2 += 6000;
                            } else if (homeHeatType == 1) {
                                consumptionCO2 += 2400;
                            } else if (homeHeatType == 2) {
                                consumptionCO2 += 10550;
                            } else if (homeHeatType == 3) {
                                consumptionCO2 += 12000;
                            } else if (homeHeatType == 4) {
                                consumptionCO2 += 7220;
                            }
                            break;
                        case "2":
                            if (homeHeatType == 0) {
                                consumptionCO2 += 7800;
                            } else if (homeHeatType == 1) {
                                consumptionCO2 += 2500;
                            } else if (homeHeatType == 2) {
                                consumptionCO2 += 10900;
                            } else if (homeHeatType == 3) {
                                consumptionCO2 += 13200;
                            } else if (homeHeatType == 4) {
                                consumptionCO2 += 8000;
                            }
                            break;
                        case "3-4":
                            if (homeHeatType == 0) {
                                consumptionCO2 += 9800;
                            } else if (homeHeatType == 1) {
                                consumptionCO2 += 2800;
                            } else if (homeHeatType == 2) {
                                consumptionCO2 += 11200;
                            } else if (homeHeatType == 3) {
                                consumptionCO2 += 14100;
                            } else if (homeHeatType == 4) {
                                consumptionCO2 += 8600;
                            }
                            break;
                        case "5":
                            if (homeHeatType == 0) {
                                consumptionCO2 += 11000;
                            } else if (homeHeatType == 1) {
                                consumptionCO2 += 3200;
                            } else if (homeHeatType == 2) {
                                consumptionCO2 += 12000;
                            } else if (homeHeatType == 3) {
                                consumptionCO2 += 15000;
                            } else if (homeHeatType == 4) {
                                consumptionCO2 += 9100;
                            }
                            break;
                    }
                }
            } else if (housingType == 5) {
                if (ans15.equals("Under $50")) {
                    switch (ans12) {
                        case "1":
                            if (homeHeatType == 0) {
                                consumptionCO2 += 2821;
                            } else if (homeHeatType == 1) {
                                consumptionCO2 += 300;
                            } else if (homeHeatType == 2) {
                                consumptionCO2 += 3820;
                            } else if (homeHeatType == 3) {
                                consumptionCO2 += 4370;
                            } else if (homeHeatType == 4) {
                                consumptionCO2 += 3970;
                            }
                            break;
                        case "2":
                            if (homeHeatType == 0) {
                                consumptionCO2 += 3010;
                            } else if (homeHeatType == 1) {
                                consumptionCO2 += 560;
                            } else if (homeHeatType == 2) {
                                consumptionCO2 += 4000;
                            } else if (homeHeatType == 3) {
                                consumptionCO2 += 4870;
                            } else if (homeHeatType == 4) {
                                consumptionCO2 += 4470;
                            }
                            break;
                        case "3-4":
                            if (homeHeatType == 0) {
                                consumptionCO2 += 3261;
                            } else if (homeHeatType == 1) {
                                consumptionCO2 += 890;
                            } else if (homeHeatType == 2) {
                                consumptionCO2 += 4307;
                            } else if (homeHeatType == 3) {
                                consumptionCO2 += 5670;
                            } else if (homeHeatType == 4) {
                                consumptionCO2 += 5270;
                            }
                            break;
                        case "5":
                            if (homeHeatType == 0) {
                                consumptionCO2 += 3578;
                            } else if (homeHeatType == 1) {
                                consumptionCO2 += 1000;
                            } else if (homeHeatType == 2) {
                                consumptionCO2 += 4400;
                            } else if (homeHeatType == 3) {
                                consumptionCO2 += 6370;
                            } else if (homeHeatType == 4) {
                                consumptionCO2 += 5970;
                            }
                            break;
                    }
                } else if (ans15.equals("$50-$100")) {
                    switch (ans12) {
                        case "1":
                            if (homeHeatType == 0) {
                                consumptionCO2 += 7500;
                            } else if (homeHeatType == 1) {
                                consumptionCO2 += 1200;
                            } else if (homeHeatType == 2) {
                                consumptionCO2 += 5500;
                            } else if (homeHeatType == 3) {
                                consumptionCO2 += 4540;
                            } else if (homeHeatType == 4) {
                                consumptionCO2 += 4140;
                            }
                            break;
                        case "2":
                            if (homeHeatType == 0) {
                                consumptionCO2 += 8800;
                            } else if (homeHeatType == 1) {
                                consumptionCO2 += 1400;
                            } else if (homeHeatType == 2) {
                                consumptionCO2 += 6000;
                            } else if (homeHeatType == 3) {
                                consumptionCO2 += 5040;
                            } else if (homeHeatType == 4) {
                                consumptionCO2 += 4640;
                            }
                            break;
                        case "3-4":
                            if (homeHeatType == 0) {
                                consumptionCO2 += 10800;
                            } else if (homeHeatType == 1) {
                                consumptionCO2 += 1650;
                            } else if (homeHeatType == 2) {
                                consumptionCO2 += 7200;
                            } else if (homeHeatType == 3) {
                                consumptionCO2 += 5840;
                            } else if (homeHeatType == 4) {
                                consumptionCO2 += 5340;
                            }
                            break;
                        case "5":
                            if (homeHeatType == 0) {
                                consumptionCO2 += 12500;
                            } else if (homeHeatType == 1) {
                                consumptionCO2 += 1800;
                            } else if (homeHeatType == 2) {
                                consumptionCO2 += 8500;
                            } else if (homeHeatType == 3) {
                                consumptionCO2 += 6540;
                            } else if (homeHeatType == 4) {
                                consumptionCO2 += 6140;
                            }
                            break;
                    }
                } else if (ans15.equals("$100-150")) {
                    switch (ans12) {
                        case "1":
                            if (homeHeatType == 0) {
                                consumptionCO2 += 10000;
                            } else if (homeHeatType == 1) {
                                consumptionCO2 += 1800;
                            } else if (homeHeatType == 2) {
                                consumptionCO2 += 8500;
                            } else if (homeHeatType == 3) {
                                consumptionCO2 += 4710;
                            } else if (homeHeatType == 4) {
                                consumptionCO2 += 4310;
                            }
                            break;
                        case "2":
                            if (homeHeatType == 0) {
                                consumptionCO2 += 12000;
                            } else if (homeHeatType == 1) {
                                consumptionCO2 += 2000;
                            } else if (homeHeatType == 2) {
                                consumptionCO2 += 9200;
                            } else if (homeHeatType == 3) {
                                consumptionCO2 += 5210;
                            } else if (homeHeatType == 4) {
                                consumptionCO2 += 4810;
                            }
                            break;
                        case "3-4":
                            if (homeHeatType == 0) {
                                consumptionCO2 += 14000;
                            } else if (homeHeatType == 1) {
                                consumptionCO2 += 2300;
                            } else if (homeHeatType == 2) {
                                consumptionCO2 += 10200;
                            } else if (homeHeatType == 3) {
                                consumptionCO2 += 6010;
                            } else if (homeHeatType == 4) {
                                consumptionCO2 += 5610;
                            }
                            break;
                        case "5":
                            if (homeHeatType == 0) {
                                consumptionCO2 += 15000;
                            } else if (homeHeatType == 1) {
                                consumptionCO2 += 2400;
                            } else if (homeHeatType == 2) {
                                consumptionCO2 += 11000;
                            } else if (homeHeatType == 3) {
                                consumptionCO2 += 6710;
                            } else if (homeHeatType == 4) {
                                consumptionCO2 += 6310;
                            }
                            break;
                    }
                } else if (ans15.equals("$150-$200")) {
                    switch (ans12) {
                        case "1":
                            if (homeHeatType == 0) {
                                consumptionCO2 += 12500;
                            } else if (homeHeatType == 1) {
                                consumptionCO2 += 2400;
                            } else if (homeHeatType == 2) {
                                consumptionCO2 += 11000;
                            } else if (homeHeatType == 3) {
                                consumptionCO2 += 4880;
                            } else if (homeHeatType == 4) {
                                consumptionCO2 += 4480;
                            }
                            break;
                        case "2":
                            if (homeHeatType == 0) {
                                consumptionCO2 += 14200;
                            } else if (homeHeatType == 1) {
                                consumptionCO2 += 2600;
                            } else if (homeHeatType == 2) {
                                consumptionCO2 += 12000;
                            } else if (homeHeatType == 3) {
                                consumptionCO2 += 5380;
                            } else if (homeHeatType == 4) {
                                consumptionCO2 += 4980;
                            }
                            break;
                        case "3-4":
                            if (homeHeatType == 0) {
                                consumptionCO2 += 16000;
                            } else if (homeHeatType == 1) {
                                consumptionCO2 += 2820;
                            } else if (homeHeatType == 2) {
                                consumptionCO2 += 13500;
                            } else if (homeHeatType == 3) {
                                consumptionCO2 += 6180;
                            } else if (homeHeatType == 4) {
                                consumptionCO2 += 5780;
                            }
                            break;
                        case "5":
                            if (homeHeatType == 0) {
                                consumptionCO2 += 17500;
                            } else if (homeHeatType == 1) {
                                consumptionCO2 += 3000;
                            } else if (homeHeatType == 2) {
                                consumptionCO2 += 14000;
                            } else if (homeHeatType == 3) {
                                consumptionCO2 += 6880;
                            } else if (homeHeatType == 4) {
                                consumptionCO2 += 6480;
                            }
                            break;
                    }
                } else if (ans15.equals("Over $200")) {
                    switch (ans12) {
                        case "1":
                            if (homeHeatType == 0) {
                                consumptionCO2 += 15000;
                            } else if (homeHeatType == 1) {
                                consumptionCO2 += 3000;
                            } else if (homeHeatType == 2) {
                                consumptionCO2 += 14000;
                            } else if (homeHeatType == 3) {
                                consumptionCO2 += 5200;
                            } else if (homeHeatType == 4) {
                                consumptionCO2 += 4800;
                            }
                            break;
                        case "2":
                            if (homeHeatType == 0) {
                                consumptionCO2 += 16800;
                            } else if (homeHeatType == 1) {
                                consumptionCO2 += 3500;
                            } else if (homeHeatType == 2) {
                                consumptionCO2 += 14800;
                            } else if (homeHeatType == 3) {
                                consumptionCO2 += 5700;
                            } else if (homeHeatType == 4) {
                                consumptionCO2 += 5300;
                            }
                            break;
                        case "3-4":
                            if (homeHeatType == 0) {
                                consumptionCO2 += 18200;
                            } else if (homeHeatType == 1) {
                                consumptionCO2 += 4000;
                            } else if (homeHeatType == 2) {
                                consumptionCO2 += 15500;
                            } else if (homeHeatType == 3) {
                                consumptionCO2 += 6500;
                            } else if (homeHeatType == 4) {
                                consumptionCO2 += 6150;
                            }
                            break;
                        case "5":
                            if (homeHeatType == 0) {
                                consumptionCO2 += 19000;
                            } else if (homeHeatType == 1) {
                                consumptionCO2 += 4500;
                            } else if (homeHeatType == 2) {
                                consumptionCO2 += 16000;
                            } else if (homeHeatType == 3) {
                                consumptionCO2 += 7200;
                            } else if (homeHeatType == 4) {
                                consumptionCO2 += 6800;
                            }
                            break;
                    }
                }
            } else if (housingType == 6) {
                if (ans15.equals("Under $50")) {
                    switch (ans12) {
                        case "1":
                            if (homeHeatType == 0) {
                                consumptionCO2 += 1971;
                            } else if (homeHeatType == 1) {
                                consumptionCO2 += 300;
                            } else if (homeHeatType == 2) {
                                consumptionCO2 += 2400;
                            } else if (homeHeatType == 3) {
                                consumptionCO2 += 1500;
                            } else if (homeHeatType == 4) {
                                consumptionCO2 += 2000;
                            }
                            break;
                        case "2":
                            if (homeHeatType == 0) {
                                consumptionCO2 += 2160;
                            } else if (homeHeatType == 1) {
                                consumptionCO2 += 410;
                            } else if (homeHeatType == 2) {
                                consumptionCO2 += 2523;
                            } else if (homeHeatType == 3) {
                                consumptionCO2 += 1850;
                            } else if (homeHeatType == 4) {
                                consumptionCO2 += 2250;
                            }
                            break;
                        case "3-4":
                            if (homeHeatType == 0) {
                                consumptionCO2 += 2500;
                            } else if (homeHeatType == 1) {
                                consumptionCO2 += 500;
                            } else if (homeHeatType == 2) {
                                consumptionCO2 += 2600;
                            } else if (homeHeatType == 3) {
                                consumptionCO2 += 2100;
                            } else if (homeHeatType == 4) {
                                consumptionCO2 += 2500;
                            }
                            break;
                        case "5":
                            if (homeHeatType == 0) {
                                consumptionCO2 += 2800;
                            } else if (homeHeatType == 1) {
                                consumptionCO2 += 550;
                            } else if (homeHeatType == 2) {
                                consumptionCO2 += 2720;
                            } else if (homeHeatType == 3) {
                                consumptionCO2 += 2500;
                            } else if (homeHeatType == 4) {
                                consumptionCO2 += 2600;
                            }
                            break;
                    }
                } else if (ans15.equals("$50-$100")) {
                    switch (ans12) {
                        case "1":
                            if (homeHeatType == 0) {
                                consumptionCO2 += 2800;
                            } else if (homeHeatType == 1) {
                                consumptionCO2 += 500;
                            } else if (homeHeatType == 2) {
                                consumptionCO2 += 2800;
                            } else if (homeHeatType == 3) {
                                consumptionCO2 += 2500;
                            } else if (homeHeatType == 4) {
                                consumptionCO2 += 2800;
                            }
                            break;
                        case "2":
                            if (homeHeatType == 0) {
                                consumptionCO2 += 2910;
                            } else if (homeHeatType == 1) {
                                consumptionCO2 += 550;
                            } else if (homeHeatType == 2) {
                                consumptionCO2 += 3100;
                            } else if (homeHeatType == 3) {
                                consumptionCO2 += 2800;
                            } else if (homeHeatType == 4) {
                                consumptionCO2 += 3000;
                            }
                            break;
                        case "3-4":
                            if (homeHeatType == 0) {
                                consumptionCO2 += 3000;
                            } else if (homeHeatType == 1) {
                                consumptionCO2 += 580;
                            } else if (homeHeatType == 2) {
                                consumptionCO2 += 3250;
                            } else if (homeHeatType == 3) {
                                consumptionCO2 += 3400;
                            } else if (homeHeatType == 4) {
                                consumptionCO2 += 3300;
                            }
                            break;
                        case "5":
                            if (homeHeatType == 0) {
                                consumptionCO2 += 3200;
                            } else if (homeHeatType == 1) {
                                consumptionCO2 += 600;
                            } else if (homeHeatType == 2) {
                                consumptionCO2 += 3500;
                            } else if (homeHeatType == 3) {
                                consumptionCO2 += 3800;
                            } else if (homeHeatType == 4) {
                                consumptionCO2 += 3400;
                            }
                            break;
                    }
                } else if (ans15.equals("$100-150")) {
                    switch (ans12) {
                        case "1":
                            if (homeHeatType == 0) {
                                consumptionCO2 += 3000;
                            } else if (homeHeatType == 1) {
                                consumptionCO2 += 1000;
                            } else if (homeHeatType == 2) {
                                consumptionCO2 += 3600;
                            } else if (homeHeatType == 3) {
                                consumptionCO2 += 3000;
                            } else if (homeHeatType == 4) {
                                consumptionCO2 += 3000;
                            }
                            break;
                        case "2":
                            if (homeHeatType == 0) {
                                consumptionCO2 += 3210;
                            } else if (homeHeatType == 1) {
                                consumptionCO2 += 1250;
                            } else if (homeHeatType == 2) {
                                consumptionCO2 += 3750;
                            } else if (homeHeatType == 3) {
                                consumptionCO2 += 3500;
                            } else if (homeHeatType == 4) {
                                consumptionCO2 += 3300;
                            }
                            break;
                        case "3-4":
                            if (homeHeatType == 0) {
                                consumptionCO2 += 3500;
                            } else if (homeHeatType == 1) {
                                consumptionCO2 += 1320;
                            } else if (homeHeatType == 2) {
                                consumptionCO2 += 3900;
                            } else if (homeHeatType == 3) {
                                consumptionCO2 += 4100;
                            } else if (homeHeatType == 4) {
                                consumptionCO2 += 3520;
                            }
                            break;
                        case "5":
                            if (homeHeatType == 0) {
                                consumptionCO2 += 3800;
                            } else if (homeHeatType == 1) {
                                consumptionCO2 += 1420;
                            } else if (homeHeatType == 2) {
                                consumptionCO2 += 4050;
                            } else if (homeHeatType == 3) {
                                consumptionCO2 += 5000;
                            } else if (homeHeatType == 4) {
                                consumptionCO2 += 3800;
                            }
                            break;
                    }
                } else if (ans15.equals("$150-$200")) {
                    switch (ans12) {
                        case "1":
                            if (homeHeatType == 0) {
                                consumptionCO2 += 4000;
                            } else if (homeHeatType == 1) {
                                consumptionCO2 += 1600;
                            } else if (homeHeatType == 2) {
                                consumptionCO2 += 4500;
                            } else if (homeHeatType == 3) {
                                consumptionCO2 += 3700;
                            } else if (homeHeatType == 4) {
                                consumptionCO2 += 3330;
                            }
                            break;
                        case "2":
                            if (homeHeatType == 0) {
                                consumptionCO2 += 5500;
                            } else if (homeHeatType == 1) {
                                consumptionCO2 += 1750;
                            } else if (homeHeatType == 2) {
                                consumptionCO2 += 4600;
                            } else if (homeHeatType == 3) {
                                consumptionCO2 += 4500;
                            } else if (homeHeatType == 4) {
                                consumptionCO2 += 3500;
                            }
                            break;
                        case "3-4":
                            if (homeHeatType == 0) {
                                consumptionCO2 += 6200;
                            } else if (homeHeatType == 1) {
                                consumptionCO2 += 1900;
                            } else if (homeHeatType == 2) {
                                consumptionCO2 += 4800;
                            } else if (homeHeatType == 3) {
                                consumptionCO2 += 5200;
                            } else if (homeHeatType == 4) {
                                consumptionCO2 += 3720;
                            }
                            break;
                        case "5":
                            if (homeHeatType == 0) {
                                consumptionCO2 += 8000;
                            } else if (homeHeatType == 1) {
                                consumptionCO2 += 2000;
                            } else if (homeHeatType == 2) {
                                consumptionCO2 += 5100;
                            } else if (homeHeatType == 3) {
                                consumptionCO2 += 5800;
                            } else if (homeHeatType == 4) {
                                consumptionCO2 += 4000;
                            }
                            break;
                    }
                } else if (ans15.equals("Over $200")) {
                    switch (ans12) {
                        case "1":
                            if (homeHeatType == 0) {
                                consumptionCO2 += 8000;
                            } else if (homeHeatType == 1) {
                                consumptionCO2 += 2000;
                            } else if (homeHeatType == 2) {
                                consumptionCO2 += 5000;
                            } else if (homeHeatType == 3) {
                                consumptionCO2 += 5800;
                            } else if (homeHeatType == 4) {
                                consumptionCO2 += 3500;
                            }
                            break;
                        case "2":
                            if (homeHeatType == 0) {
                                consumptionCO2 += 9500;
                            } else if (homeHeatType == 1) {
                                consumptionCO2 += 2100;
                            } else if (homeHeatType == 2) {
                                consumptionCO2 += 5200;
                            } else if (homeHeatType == 3) {
                                consumptionCO2 += 6800;
                            } else if (homeHeatType == 4) {
                                consumptionCO2 += 3700;
                            }
                            break;
                        case "3-4":
                            if (homeHeatType == 0) {
                                consumptionCO2 += 10200;
                            } else if (homeHeatType == 1) {
                                consumptionCO2 += 2300;
                            } else if (homeHeatType == 2) {
                                consumptionCO2 += 5300;
                            } else if (homeHeatType == 3) {
                                consumptionCO2 += 7300;
                            } else if (homeHeatType == 4) {
                                consumptionCO2 += 4000;
                            }
                            break;
                        case "5":
                            if (homeHeatType == 0) {
                                consumptionCO2 += 12000;
                            } else if (homeHeatType == 1) {
                                consumptionCO2 += 3000;
                            } else if (homeHeatType == 2) {
                                consumptionCO2 += 5600;
                            } else if (homeHeatType == 3) {
                                consumptionCO2 += 7800;
                            } else if (homeHeatType == 4) {
                                consumptionCO2 += 4300;
                            }
                            break;
                    }
                }
            } else if (housingType == 7) {
                if (ans15.equals("Under $50")) {
                    switch (ans12) {
                        case "1":
                            if (homeHeatType == 0) {
                                consumptionCO2 += 2443;
                            } else if (homeHeatType == 1) {
                                consumptionCO2 += 300;
                            } else if (homeHeatType == 2) {
                                consumptionCO2 += 2590;
                            } else if (homeHeatType == 3) {
                                consumptionCO2 += 3170;
                            } else if (homeHeatType == 4) {
                                consumptionCO2 += 1400;
                            }
                            break;
                        case "2":
                            if (homeHeatType == 0) {
                                consumptionCO2 += 2750;
                            } else if (homeHeatType == 1) {
                                consumptionCO2 += 380;
                            } else if (homeHeatType == 2) {
                                consumptionCO2 += 2620;
                            } else if (homeHeatType == 3) {
                                consumptionCO2 += 3770;
                            } else if (homeHeatType == 4) {
                                consumptionCO2 += 1560;
                            }
                            break;
                        case "3-4":
                            if (homeHeatType == 0) {
                                consumptionCO2 += 3111;
                            } else if (homeHeatType == 1) {
                                consumptionCO2 += 500;
                            } else if (homeHeatType == 2) {
                                consumptionCO2 += 2730;
                            } else if (homeHeatType == 3) {
                                consumptionCO2 += 4670;
                            } else if (homeHeatType == 4) {
                                consumptionCO2 += 1900;
                            }
                            break;
                        case "5":
                            if (homeHeatType == 0) {
                                consumptionCO2 += 3580;
                            } else if (homeHeatType == 1) {
                                consumptionCO2 += 590;
                            } else if (homeHeatType == 2) {
                                consumptionCO2 += 2800;
                            } else if (homeHeatType == 3) {
                                consumptionCO2 += 5570;
                            } else if (homeHeatType == 4) {
                                consumptionCO2 += 2200;
                            }
                            break;
                    }
                } else if (ans15.equals("$50-$100")) {
                    switch (ans12) {
                        case "1":
                            if (homeHeatType == 0) {
                                consumptionCO2 += 4000;
                            } else if (homeHeatType == 1) {
                                consumptionCO2 += 550;
                            } else if (homeHeatType == 2) {
                                consumptionCO2 += 3800;
                            } else if (homeHeatType == 3) {
                                consumptionCO2 += 5600;
                            } else if (homeHeatType == 4) {
                                consumptionCO2 += 2400;
                            }
                            break;
                        case "2":
                            if (homeHeatType == 0) {
                                consumptionCO2 += 5000;
                            } else if (homeHeatType == 1) {
                                consumptionCO2 += 700;
                            } else if (homeHeatType == 2) {
                                consumptionCO2 += 4320;
                            } else if (homeHeatType == 3) {
                                consumptionCO2 += 5940;
                            } else if (homeHeatType == 4) {
                                consumptionCO2 += 2600;
                            }
                            break;
                        case "3-4":
                            if (homeHeatType == 0) {
                                consumptionCO2 += 6500;
                            } else if (homeHeatType == 1) {
                                consumptionCO2 += 950;
                            } else if (homeHeatType == 2) {
                                consumptionCO2 += 4800;
                            } else if (homeHeatType == 3) {
                                consumptionCO2 += 6140;
                            } else if (homeHeatType == 4) {
                                consumptionCO2 += 3300;
                            }
                            break;
                        case "5":
                            if (homeHeatType == 0) {
                                consumptionCO2 += 7300;
                            } else if (homeHeatType == 1) {
                                consumptionCO2 += 1100;
                            } else if (homeHeatType == 2) {
                                consumptionCO2 += 5500;
                            } else if (homeHeatType == 3) {
                                consumptionCO2 += 6340;
                            } else if (homeHeatType == 4) {
                                consumptionCO2 += 3800;
                            }
                            break;
                    }
                } else if (ans15.equals("$100-150")) {
                    switch (ans12) {
                        case "1":
                            if (homeHeatType == 0) {
                                consumptionCO2 += 4300;
                            } else if (homeHeatType == 1) {
                                consumptionCO2 += 1200;
                            } else if (homeHeatType == 2) {
                                consumptionCO2 += 5000;
                            } else if (homeHeatType == 3) {
                                consumptionCO2 += 6000;
                            } else if (homeHeatType == 4) {
                                consumptionCO2 += 4000;
                            }
                            break;
                        case "2":
                            if (homeHeatType == 0) {
                                consumptionCO2 += 5500;
                            } else if (homeHeatType == 1) {
                                consumptionCO2 += 1350;
                            } else if (homeHeatType == 2) {
                                consumptionCO2 += 5800;
                            } else if (homeHeatType == 3) {
                                consumptionCO2 += 6200;
                            } else if (homeHeatType == 4) {
                                consumptionCO2 += 4310;
                            }
                            break;
                        case "3-4":
                            if (homeHeatType == 0) {
                                consumptionCO2 += 6800;
                            } else if (homeHeatType == 1) {
                                consumptionCO2 += 1520;
                            } else if (homeHeatType == 2) {
                                consumptionCO2 += 6200;
                            } else if (homeHeatType == 3) {
                                consumptionCO2 += 6420;
                            } else if (homeHeatType == 4) {
                                consumptionCO2 += 4600;
                            }
                            break;
                        case "5":
                            if (homeHeatType == 0) {
                                consumptionCO2 += 8340;
                            } else if (homeHeatType == 1) {
                                consumptionCO2 += 1700;
                            } else if (homeHeatType == 2) {
                                consumptionCO2 += 6100;
                            } else if (homeHeatType == 3) {
                                consumptionCO2 += 6500;
                            } else if (homeHeatType == 4) {
                                consumptionCO2 += 5100;
                            }
                            break;
                    }
                } else if (ans15.equals("$150-$200")) {
                    switch (ans12) {
                        case "1":
                            if (homeHeatType == 0) {
                                consumptionCO2 += 4800;
                            } else if (homeHeatType == 1) {
                                consumptionCO2 += 1700;
                            } else if (homeHeatType == 2) {
                                consumptionCO2 += 5350;
                            } else if (homeHeatType == 3) {
                                consumptionCO2 += 3680;
                            } else if (homeHeatType == 4) {
                                consumptionCO2 += 3800;
                            }
                            break;
                        case "2":
                            if (homeHeatType == 0) {
                                consumptionCO2 += 6300;
                            } else if (homeHeatType == 1) {
                                consumptionCO2 += 1900;
                            } else if (homeHeatType == 2) {
                                consumptionCO2 += 5500;
                            } else if (homeHeatType == 3) {
                                consumptionCO2 += 4280;
                            } else if (homeHeatType == 4) {
                                consumptionCO2 += 3800;
                            }
                            break;
                        case "3-4":
                            if (homeHeatType == 0) {
                                consumptionCO2 += 8500;
                            } else if (homeHeatType == 1) {
                                consumptionCO2 += 2150;
                            } else if (homeHeatType == 2) {
                                consumptionCO2 += 5720;
                            } else if (homeHeatType == 3) {
                                consumptionCO2 += 5180;
                            } else if (homeHeatType == 4) {
                                consumptionCO2 += 4220;
                            }
                            break;
                        case "5":
                            if (homeHeatType == 0) {
                                consumptionCO2 += 9000;
                            } else if (homeHeatType == 1) {
                                consumptionCO2 += 2400;
                            } else if (homeHeatType == 2) {
                                consumptionCO2 += 5900;
                            } else if (homeHeatType == 3) {
                                consumptionCO2 += 6080;
                            } else if (homeHeatType == 4) {
                                consumptionCO2 += 4400;
                            }
                            break;
                    }
                } else if (ans15.equals("Over $200")) {
                    switch (ans12) {
                        case "1":
                            if (homeHeatType == 0) {
                                consumptionCO2 += 9500;
                            } else if (homeHeatType == 1) {
                                consumptionCO2 += 2500;
                            } else if (homeHeatType == 2) {
                                consumptionCO2 += 5370;
                            } else if (homeHeatType == 3) {
                                consumptionCO2 += 6000;
                            } else if (homeHeatType == 4) {
                                consumptionCO2 += 4000;
                            }
                            break;
                        case "2":
                            if (homeHeatType == 0) {
                                consumptionCO2 += 10100;
                            } else if (homeHeatType == 1) {
                                consumptionCO2 += 2780;
                            } else if (homeHeatType == 2) {
                                consumptionCO2 += 5500;
                            } else if (homeHeatType == 3) {
                                consumptionCO2 += 6600;
                            } else if (homeHeatType == 4) {
                                consumptionCO2 += 4640;
                            }
                            break;
                        case "3-4":
                            if (homeHeatType == 0) {
                                consumptionCO2 += 11200;
                            } else if (homeHeatType == 1) {
                                consumptionCO2 += 3000;
                            } else if (homeHeatType == 2) {
                                consumptionCO2 += 5800;
                            } else if (homeHeatType == 3) {
                                consumptionCO2 += 6800;
                            } else if (homeHeatType == 4) {
                                consumptionCO2 += 5000;
                            }
                            break;
                        case "5":
                            if (homeHeatType == 0) {
                                consumptionCO2 += 14000;
                            } else if (homeHeatType == 1) {
                                consumptionCO2 += 3500;
                            } else if (homeHeatType == 2) {
                                consumptionCO2 += 6200;
                            } else if (homeHeatType == 3) {
                                consumptionCO2 += 7400;
                            } else if (homeHeatType == 4) {
                                consumptionCO2 += 5340;
                            }
                            break;
                    }
                }
            } else if (housingType == 8) {
                if (ans15.equals("Under $50")) {
                    switch (ans12) {
                        case "1":
                            if (homeHeatType == 0) {
                                consumptionCO2 += 2822;
                            } else if (homeHeatType == 1) {
                                consumptionCO2 += 300;
                            } else if (homeHeatType == 2) {
                                consumptionCO2 += 2810;
                            } else if (homeHeatType == 3) {
                                consumptionCO2 += 3340;
                            } else if (homeHeatType == 4) {
                                consumptionCO2 += 3800;
                            }
                            break;
                        case "2":
                            if (homeHeatType == 0) {
                                consumptionCO2 += 3010;
                            } else if (homeHeatType == 1) {
                                consumptionCO2 += 560;
                            } else if (homeHeatType == 2) {
                                consumptionCO2 += 3000;
                            } else if (homeHeatType == 3) {
                                consumptionCO2 += 3940;
                            } else if (homeHeatType == 4) {
                                consumptionCO2 += 4070;
                            }
                            break;
                        case "3-4":
                            if (homeHeatType == 0) {
                                consumptionCO2 += 3300;
                            } else if (homeHeatType == 1) {
                                consumptionCO2 += 890;
                            } else if (homeHeatType == 2) {
                                consumptionCO2 += 3468;
                            } else if (homeHeatType == 3) {
                                consumptionCO2 += 4840;
                            } else if (homeHeatType == 4) {
                                consumptionCO2 += 5000;
                            }
                            break;
                        case "5":
                            if (homeHeatType == 0) {
                                consumptionCO2 += 3600;
                            } else if (homeHeatType == 1) {
                                consumptionCO2 += 1000;
                            } else if (homeHeatType == 2) {
                                consumptionCO2 += 3760;
                            } else if (homeHeatType == 3) {
                                consumptionCO2 += 5740;
                            } else if (homeHeatType == 4) {
                                consumptionCO2 += 5600;
                            }
                            break;
                    }
                } else if (ans15.equals("$50-$100")) {
                    switch (ans12) {
                        case "1":
                            if (homeHeatType == 0) {
                                consumptionCO2 += 3600;
                            } else if (homeHeatType == 1) {
                                consumptionCO2 += 1200;
                            } else if (homeHeatType == 2) {
                                consumptionCO2 += 4300;
                            } else if (homeHeatType == 3) {
                                consumptionCO2 += 5900;
                            } else if (homeHeatType == 4) {
                                consumptionCO2 += 3500;
                            }
                            break;
                        case "2":
                            if (homeHeatType == 0) {
                                consumptionCO2 += 3840;
                            } else if (homeHeatType == 1) {
                                consumptionCO2 += 1380;
                            } else if (homeHeatType == 2) {
                                consumptionCO2 += 4900;
                            } else if (homeHeatType == 3) {
                                consumptionCO2 += 6330;
                            } else if (homeHeatType == 4) {
                                consumptionCO2 += 3930;
                            }
                            break;
                        case "3-4":
                            if (homeHeatType == 0) {
                                consumptionCO2 += 3900;
                            } else if (homeHeatType == 1) {
                                consumptionCO2 += 1600;
                            } else if (homeHeatType == 2) {
                                consumptionCO2 += 5320;
                            } else if (homeHeatType == 3) {
                                consumptionCO2 += 6440;
                            } else if (homeHeatType == 4) {
                                consumptionCO2 += 4360;
                            }
                            break;
                        case "5":
                            if (homeHeatType == 0) {
                                consumptionCO2 += 5100;
                            } else if (homeHeatType == 1) {
                                consumptionCO2 += 1750;
                            } else if (homeHeatType == 2) {
                                consumptionCO2 += 5800;
                            } else if (homeHeatType == 3) {
                                consumptionCO2 += 6900;
                            } else if (homeHeatType == 4) {
                                consumptionCO2 += 5000;
                            }
                            break;
                    }
                } else if (ans15.equals("$100-150")) {
                    switch (ans12) {
                        case "1":
                            if (homeHeatType == 0) {
                                consumptionCO2 += 5000;
                            } else if (homeHeatType == 1) {
                                consumptionCO2 += 1800;
                            } else if (homeHeatType == 2) {
                                consumptionCO2 += 5300;
                            } else if (homeHeatType == 3) {
                                consumptionCO2 += 3510;
                            } else if (homeHeatType == 4) {
                                consumptionCO2 += 4100;
                            }
                            break;
                        case "2":
                            if (homeHeatType == 0) {
                                consumptionCO2 += 6200;
                            } else if (homeHeatType == 1) {
                                consumptionCO2 += 2000;
                            } else if (homeHeatType == 2) {
                                consumptionCO2 += 5690;
                            } else if (homeHeatType == 3) {
                                consumptionCO2 += 4110;
                            } else if (homeHeatType == 4) {
                                consumptionCO2 += 4500;
                            }
                            break;
                        case "3-4":
                            if (homeHeatType == 0) {
                                consumptionCO2 += 7000;
                            } else if (homeHeatType == 1) {
                                consumptionCO2 += 2200;
                            } else if (homeHeatType == 2) {
                                consumptionCO2 += 6250;
                            } else if (homeHeatType == 3) {
                                consumptionCO2 += 5010;
                            } else if (homeHeatType == 4) {
                                consumptionCO2 += 4780;
                            }
                            break;
                        case "5":
                            if (homeHeatType == 0) {
                                consumptionCO2 += 8000;
                            } else if (homeHeatType == 1) {
                                consumptionCO2 += 2300;
                            } else if (homeHeatType == 2) {
                                consumptionCO2 += 6500;
                            } else if (homeHeatType == 3) {
                                consumptionCO2 += 5910;
                            } else if (homeHeatType == 4) {
                                consumptionCO2 += 5360;
                            }
                            break;
                    }
                } else if (ans15.equals("$150-$200")) {
                    switch (ans12) {
                        case "1":
                            if (homeHeatType == 0) {
                                consumptionCO2 += 8000;
                            } else if (homeHeatType == 1) {
                                consumptionCO2 += 2400;
                            } else if (homeHeatType == 2) {
                                consumptionCO2 += 5440;
                            } else if (homeHeatType == 3) {
                                consumptionCO2 += 3800;
                            } else if (homeHeatType == 4) {
                                consumptionCO2 += 4200;
                            }
                            break;
                        case "2":
                            if (homeHeatType == 0) {
                                consumptionCO2 += 8300;
                            } else if (homeHeatType == 1) {
                                consumptionCO2 += 2500;
                            } else if (homeHeatType == 2) {
                                consumptionCO2 += 5600;
                            } else if (homeHeatType == 3) {
                                consumptionCO2 += 4500;
                            } else if (homeHeatType == 4) {
                                consumptionCO2 += 4640;
                            }
                            break;
                        case "3-4":
                            if (homeHeatType == 0) {
                                consumptionCO2 += 9000;
                            } else if (homeHeatType == 1) {
                                consumptionCO2 += 2650;
                            } else if (homeHeatType == 2) {
                                consumptionCO2 += 5800;
                            } else if (homeHeatType == 3) {
                                consumptionCO2 += 5380;
                            } else if (homeHeatType == 4) {
                                consumptionCO2 += 5000;
                            }
                            break;
                        case "5":
                            if (homeHeatType == 0) {
                                consumptionCO2 += 9500;
                            } else if (homeHeatType == 1) {
                                consumptionCO2 += 2800;
                            } else if (homeHeatType == 2) {
                                consumptionCO2 += 6000;
                            } else if (homeHeatType == 3) {
                                consumptionCO2 += 6200;
                            } else if (homeHeatType == 4) {
                                consumptionCO2 += 5400;
                            }
                            break;
                    }
                } else if (ans15.equals("Over $200")) {
                    switch (ans12) {
                        case "1":
                            if (homeHeatType == 0) {
                                consumptionCO2 += 9500;
                            } else if (homeHeatType == 1) {
                                consumptionCO2 += 2800;
                            } else if (homeHeatType == 2) {
                                consumptionCO2 += 5670;
                            } else if (homeHeatType == 3) {
                                consumptionCO2 += 6200;
                            } else if (homeHeatType == 4) {
                                consumptionCO2 += 4300;
                            }
                            break;
                        case "2":
                            if (homeHeatType == 0) {
                                consumptionCO2 += 1010;
                            } else if (homeHeatType == 1) {
                                consumptionCO2 += 3000;
                            } else if (homeHeatType == 2) {
                                consumptionCO2 += 5800;
                            } else if (homeHeatType == 3) {
                                consumptionCO2 += 6900;
                            } else if (homeHeatType == 4) {
                                consumptionCO2 += 4700;
                            }
                            break;
                        case "3-4":
                            if (homeHeatType == 0) {
                                consumptionCO2 += 10300;
                            } else if (homeHeatType == 1) {
                                consumptionCO2 += 3800;
                            } else if (homeHeatType == 2) {
                                consumptionCO2 += 6100;
                            } else if (homeHeatType == 3) {
                                consumptionCO2 += 7500;
                            } else if (homeHeatType == 4) {
                                consumptionCO2 += 5100;
                            }
                            break;
                        case "5":
                            if (homeHeatType == 0) {
                                consumptionCO2 += 11000;
                            } else if (homeHeatType == 1) {
                                consumptionCO2 += 4300;
                            } else if (homeHeatType == 2) {
                                consumptionCO2 += 6350;
                            } else if (homeHeatType == 3) {
                                consumptionCO2 += 7850;
                            } else if (homeHeatType == 4) {
                                consumptionCO2 += 5500;
                            }
                            break;
                    }
                }
            } //housing 9,10,11
        }

        if (q17Button != null) {
            String ans17 = q17Button.getText().toString();

            if (ans17.equals("Yes, primarily")) {
                consumptionCO2 -= 6000;
            } else if (ans17.equals("Yes, partially")) {
                consumptionCO2 -= 4000;
            }
        }

        return consumptionCO2;
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