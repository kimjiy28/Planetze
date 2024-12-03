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

public class PublicTransportationFragment extends Fragment {

    // Firebase
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    // DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users").child(user.getUid());
    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users").
            child("4U6brQiObtULMg94EPA2zN6nH2h1");

    // Fields to Record
    private static String date;
    private String type, hour;
    private double dailyEmission;

    // Views
    private TextView tvType, tvHour;
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
        View view = inflater.inflate(R.layout.fragment_public_transportation, container, false);

        // Views Initialization
        typeSpinner = view.findViewById(R.id.spType);
        typeAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.publicTransportation, R.layout.spinner);
        tvHour = view.findViewById(R.id.txHours);
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
            hour = tvHour.getText().toString();

            // Errors Handling
            if (date == null
                    || type.equals("Select Type of Public Transportation")
                    || hour.isEmpty()) {
                Toast.makeText(getActivity(), "Please enter the required field",
                        Toast.LENGTH_LONG).show();
            } else {
                // Update Activity Details
                PublicTransportation record = new PublicTransportation(type, Integer.parseInt(hour));
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
                                    tvHour.setText("");
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