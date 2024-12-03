package com.example.planetze;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

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

public class PersonalVehicleFragment extends Fragment {

    // Firebase
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    // DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users").child(user.getUid());
    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users").
            child("4U6brQiObtULMg94EPA2zN6nH2h1");

    // Fields to Record
    private static String date;
    private String distance, type;
    private double dailyEmission;

    // Views
    private TextView tvDistance, tvType;
    private Spinner typeSpinner;
    private ArrayAdapter<CharSequence> typeAdapter;
    private Button submitButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_personal_vehicle, container, false);

        // Views Initialization
        typeSpinner = view.findViewById(R.id.spType);
        typeAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.vehicle, R.layout.spinner);
        tvDistance = view.findViewById(R.id.txDistance);
        submitButton = view.findViewById(R.id.submitButton);

        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeSpinner.setAdapter(typeAdapter);
        typeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                type = typeSpinner.getSelectedItem().toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        submitButton.setOnClickListener(v -> {

            date = RecordActivity.getDate();
            distance = tvDistance.getText().toString();

            // Errors Handling
            if (date == null || distance.isEmpty()) {
                Toast.makeText(getActivity(), "Please enter the required field",
                        Toast.LENGTH_LONG).show();
            } else {
                // Update Activity Details
                PersonalVehicle record = new PersonalVehicle(Integer.parseInt(distance), type);
                reference.child(date).child("activities").push().setValue(record);
                Toast.makeText(getActivity(), "Activity Recorded", Toast.LENGTH_LONG).show();
                // Update Total Daily Emission
                reference.child(date).child("dailyEmission")
                        .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.getValue() != null) {
                                    dailyEmission = snapshot.getValue(Double.class);
                                    typeSpinner.setSelection(0);
                                    tvDistance.setText("");
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