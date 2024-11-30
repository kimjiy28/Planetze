package com.example.planetze;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

public class EnergyBillsFragment extends Fragment {

    private String type, bill;
    private TextView tvType, tvBill;
    private Spinner typeSpinner;
    private ArrayAdapter<CharSequence> typeAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_energy_bills, container, false);

        typeSpinner = view.findViewById(R.id.spType);
        typeAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.energyBills, R.layout.spinner);
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeSpinner.setAdapter(typeAdapter);
        typeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                type = typeSpinner.getSelectedItem().toString();
//                int parentId = parent.getId();
//                if (parentId == R.id.spMealType) {
//                    switch(category) {
//                        case "Electricity":
//                            break;
//                        case "Gas":
//                            break;
//                        case "Water":
//                            break;
//                        default:
//                            break;
//                    }
//                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        return view;
    }
}