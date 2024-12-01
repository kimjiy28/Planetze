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

public class MealFragment extends Fragment {

    private String mealType, serving;
    private TextView tvMealTypeSpinner, tvServingSpinner;
    private Spinner mealTypeSpinner, servingSpinner;
    private ArrayAdapter<CharSequence> mealTypeAdapter, servingAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_meal, container, false);

        mealTypeSpinner = view.findViewById(R.id.spMealType);
        mealTypeAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.mealType, R.layout.spinner);
        mealTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mealTypeSpinner.setAdapter(mealTypeAdapter);
        mealTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mealType = mealTypeSpinner.getSelectedItem().toString();
//                int parentId = parent.getId();
//                if (parentId == R.id.spMealType) {
//                    switch(category) {
//                        case "beef":
//                            break;
//                        case "transportation":
//                            break;
//                        case "food":
//                            break;
//                        case "consumption":
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

        servingSpinner = view.findViewById(R.id.spServing);
        servingAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.consumptionList, R.layout.spinner);
        servingAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        servingSpinner.setAdapter(servingAdapter);

        servingSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                serving = servingSpinner.getSelectedItem().toString();
//                switch (serving) {
//                    case "0":
//                        break;
//                    default:
//                        break;
//                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        return view;
    }
}