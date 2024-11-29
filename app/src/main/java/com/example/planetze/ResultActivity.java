package com.example.planetze;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class ResultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            double totalCO2 = extras.getDouble("totalCO2");
            double transportationCO2 = extras.getDouble("transportationCO2");
            double foodCO2 = extras.getDouble("foodCO2");
            double housingCO2 = extras.getDouble("housingCO2");
            double consumptionCO2 = extras.getDouble("consumptionCO2");

            TextView totalCO2View = findViewById(R.id.totalCO2View);
            TextView breakdownView = findViewById(R.id.breakdownView);

            totalCO2View.setText(String.format("Total Annual Carbon Footprint: %.2f tons of CO2e/year", totalCO2));
            breakdownView.setText(String.format(
                    "Breakdown:\n" +
                            "Transportation: %.2f tons of CO2e/year\n" +
                            "Food: %.2f tons of CO2e/year\n" +
                            "Housing: %.2f tons of CO2e/year\n" +
                            "Consumption: %.2f tons of CO2e/year",
                    transportationCO2, foodCO2, housingCO2, consumptionCO2
            ));
        }
    }
}