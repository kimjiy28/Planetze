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

public class EnergyBillsFragment extends Fragment {

    // Firebase
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users").child(user.getUid());

    // Fields to Record
    private static String date;
    private String type, bill;
    private double dailyEmission;

    // Views
    private TextView tvBill;
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
        View view = inflater.inflate(R.layout.fragment_energy_bills, container, false);

        // Views initialization
        typeSpinner = view.findViewById(R.id.spType);
        typeAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.energyBills, R.layout.spinner);
        tvBill = view.findViewById(R.id.txBill);
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
            bill = tvBill.getText().toString();

            // Errors Handling
            if (date == null || type.equals("Select Your Energy Bill") || bill.isEmpty()) {
                Toast.makeText(getActivity(), "Please enter the required field",
                        Toast.LENGTH_LONG).show();
            } else {
                // Update Activity Details
                EnergyBills record = new EnergyBills(type, Integer.parseInt(bill));
                reference.child(date).child("activities").push().setValue(record);
                Toast.makeText(getActivity(), "Activity Recorded", Toast.LENGTH_LONG).show();
                // Update Total Daily Emission
                reference.child(date).child("dailyEmission")
                        .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.getValue() != null) {
                                    dailyEmission = snapshot.getValue(Double.class);
                                    tvBill.setText("");
                                    typeSpinner.setSelection(0);
                                    Log.d("Fetched", "Current Daily Emission / " + dailyEmission);
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