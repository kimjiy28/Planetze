package com.example.planetze;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MealFragment extends Fragment {

    // Firebase
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    // DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users").child(user.getUid());
    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users").
            child("4U6brQiObtULMg94EPA2zN6nH2h1");

    // Fields to Record
    private static String date;
    private String mealType, serving;
    private double dailyEmission;

    // Views
    private TextView tvMealTypeSpinner, tvServingSpinner;
    private Spinner mealTypeSpinner, servingSpinner;
    private ArrayAdapter<CharSequence> mealTypeAdapter, servingAdapter;
    private Button submitButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_meal, container, false);

        // Views Initialization
        mealTypeSpinner = view.findViewById(R.id.spMealType);
        mealTypeAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.mealType, R.layout.spinner);
        submitButton = view.findViewById(R.id.submitButton);

        mealTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mealTypeSpinner.setAdapter(mealTypeAdapter);
        mealTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mealType = mealTypeSpinner.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        servingSpinner = view.findViewById(R.id.spServing);
        servingAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.serving, R.layout.spinner);
        servingAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        servingSpinner.setAdapter(servingAdapter);

        servingSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                serving = servingSpinner.getSelectedItem().toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        submitButton.setOnClickListener(v -> {

            date = RecordActivity.getDate();

            // Errors Handling
            if (date == null
                    || mealType.equals("Select Meal Type")
                    || serving.equals("Select Number of Servings Consumed")) {
                Toast.makeText(getActivity(), "Please enter the required field",
                        Toast.LENGTH_LONG).show();
            } else {
                // Update Activity Details
                Food record = new Food(mealType, Integer.parseInt(serving));
                reference.child(date).child("activities").push().setValue(record);
                Toast.makeText(getActivity(), "Activity Recorded", Toast.LENGTH_LONG).show();
                reference.child(date).child("dailyEmission")
                        .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.getValue() != null) {
                            dailyEmission = snapshot.getValue(Double.class);
                            mealTypeSpinner.setSelection(0);
                            servingSpinner.setSelection(0);
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
                reference.child(date).child("dailyEmission").setValue(dailyEmission + record.emission);
            }
        });

        return view;
    }

}

