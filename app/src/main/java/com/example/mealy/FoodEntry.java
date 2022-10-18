package com.example.mealy;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RadioGroup;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class FoodEntry extends DialogFragment{

    Spinner categorySpinner;
    Spinner quantityUnits;
    ArrayAdapter<CharSequence> categoryAdapter;
    ArrayAdapter<CharSequence> unitsAdapter;
    String[] categories;
    String[] whole;
    String[] weight;
    String[] volume;
    String[] current;
    RadioGroup unitsRadioGroup;


    public FoodEntry(){

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.food_entry, container, false);
        InitializeCategorySpinner(view);
        quantityUnitsSpinner(view);


        return view;
    }

    private void InitializeCategorySpinner(View view) {
        categorySpinner = (Spinner) view.findViewById(R.id.categoryDropdown);
        categories = new String[]{"Select A Category", "Raw Food", "Meat", "Spice", "Fluid", "Other"};
        categoryAdapter = new ArrayAdapter<CharSequence>(getContext(), android.R.layout.simple_spinner_dropdown_item, categories);
        categorySpinner.setAdapter(categoryAdapter);
    }

    private void quantityUnitsSpinner(View view) {
        quantityUnits = (Spinner) view.findViewById(R.id.quantityDropdown);
        unitsRadioGroup = (RadioGroup) view.findViewById(R.id.quantityType);
        whole = new String[]{"Select unit", "single", "Dozen", "Five Pack"};
        weight = new String[]{"Select unit", "lb", "kg", "g"};
        volume = new String[]{"Select unit", "L", "ml", "oz"};
        current = whole;
        unitsAdapter = new ArrayAdapter<CharSequence>(getContext(), android.R.layout.simple_spinner_dropdown_item, current);
        quantityUnits.setAdapter(unitsAdapter);
        unitsAdapter.setNotifyOnChange(true);

        unitsRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                switch (checkedId) {
                    case R.id.whole:
                        current = whole;
                        unitsAdapter = new ArrayAdapter<CharSequence>(getContext(), android.R.layout.simple_spinner_dropdown_item, current);
                        quantityUnits.setAdapter(unitsAdapter);
                        break;
                    case R.id.weight:
                        current = weight;
                        unitsAdapter = new ArrayAdapter<CharSequence>(getContext(), android.R.layout.simple_spinner_dropdown_item, current);
                        quantityUnits.setAdapter(unitsAdapter);
                        break;
                    case R.id.volume:
                        current = volume;
                        unitsAdapter = new ArrayAdapter<CharSequence>(getContext(), android.R.layout.simple_spinner_dropdown_item, current);
                        quantityUnits.setAdapter(unitsAdapter);
                        break;
                    default:
                        Log.wtf(String.valueOf(checkedId), "it's 3");

                }
            }
        });



    }
}
