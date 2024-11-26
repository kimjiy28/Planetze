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