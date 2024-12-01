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

public class PublicTransportationFragment extends Fragment {

    private String type, distance;
    private TextView tvType, tvDistance;
    private Spinner typeSpinner;
    private ArrayAdapter<CharSequence> typeAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_public_transportation, container, false);

        typeSpinner = view.findViewById(R.id.spType);
        typeAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.publicTransportation, R.layout.spinner);
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeSpinner.setAdapter(typeAdapter);
        typeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                type = typeSpinner.getSelectedItem().toString();
//                int parentId = parent.getId();
//                if (parentId == R.id.spMealType) {
//                    switch(category) {
//                        case "Bus":
//                            break;
//                        case "Train":
//                            break;
//                        case "Subway":
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