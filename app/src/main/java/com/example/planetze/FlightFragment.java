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

public class FlightFragment extends Fragment {

    private String distance, length;
    private TextView tvDistance, tvLength;
    private Spinner lengthSpinner;
    private ArrayAdapter<CharSequence> lengthAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_flight, container, false);

        lengthSpinner = view.findViewById(R.id.spLength);
        lengthAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.length, R.layout.spinner);
        lengthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        lengthSpinner.setAdapter(lengthAdapter);
        lengthSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                length = lengthSpinner.getSelectedItem().toString();
//                int parentId = parent.getId();
//                if (parentId == R.id.spMealType) {
//                    switch(category) {
//                        case "Short-haul (less than 1,500 km)":
//                            break;
//                        case "Long-haul (more than 1,500 km)":
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