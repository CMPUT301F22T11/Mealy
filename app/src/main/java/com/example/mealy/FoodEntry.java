package com.example.mealy;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.HashMap;
import java.util.Objects;

import com.example.mealy.functions.Validate;
import com.example.mealy.functions.Firestore;

public class FoodEntry extends DialogFragment {
    private final FoodEntry fragment = this;
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

    Button Save;
    EditText IngredientName;
    EditText IngredientQuantity;
    EditText ExpiryDate;

    View view;


    public FoodEntry() {
        // Constructor: TODO
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflates View
        view = inflater.inflate(R.layout.food_entry, container, false);

        // Initializes Interface
        InitializeCategorySpinner();
        InitializeQuantityUnitsSpinner();
        InitializeSaveButton();
        InitializeTextViews();

        //StoreToFirestore();


        return view;
    }

    private void InitializeCategorySpinner() {
        categorySpinner = (Spinner) view.findViewById(R.id.categoryDropdown);
        categories = new String[]{"Select A Category", "Raw Food", "Meat", "Spice", "Fluid", "Other"};
        categoryAdapter = new ArrayAdapter<CharSequence>(getContext(), android.R.layout.simple_spinner_dropdown_item, categories);
        categorySpinner.setAdapter(categoryAdapter);
    }

    private void InitializeQuantityUnitsSpinner() {
        quantityUnits = (Spinner) view.findViewById(R.id.quantityDropdown);
        unitsRadioGroup = (RadioGroup) view.findViewById(R.id.quantityType);
        whole = new String[]{"Select Unit", "single", "Dozen", "Five Pack"};
        weight = new String[]{"Select Unit", "lb", "kg", "g"};
        volume = new String[]{"Select Unit", "L", "ml", "oz"};
        current = whole;
        unitsAdapter = new ArrayAdapter<CharSequence>(getContext(), android.R.layout.simple_spinner_dropdown_item, current);
        quantityUnits.setAdapter(unitsAdapter);
        unitsAdapter.setNotifyOnChange(true);

        unitsRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @SuppressLint("NonConstantResourceId")
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
                        Log.wtf("This shouldn't happen", String.valueOf(checkedId));

                }
            }
        });
    }

    private void InitializeSaveButton() {
        Save = (Button) view.findViewById(R.id.addIngredient);

        Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("AHAHA", "This is my message");

                if (ValidData()) {
                    String ingredientName = GetIngredientName();
                    HashMap<String, String> data = GetData();
                    requireActivity().getSupportFragmentManager().beginTransaction().remove(fragment).commit();
                    Firestore.StoreToFirestore("Ingredients", ingredientName, data);
                }

                // figure out how to destroy the fragment and insert it here
            }
        });
    }

    private void InitializeTextViews() {
        IngredientName = (EditText) view.findViewById(R.id.ingredientName);
        IngredientQuantity = (EditText) view.findViewById(R.id.quantity);
        ExpiryDate = (EditText) view.findViewById(R.id.expiryDate);
    }

    private HashMap<String, String> GetData(){

        HashMap<String, String> data = new HashMap<>();

        String categoryName = categorySpinner.getSelectedItem().toString();
        String ingredientQuantity = IngredientQuantity.getText().toString();
        String unit = quantityUnits.getSelectedItem().toString();
        String expiryDate = ExpiryDate.getText().toString();

        data.put("Category", categoryName);
        data.put("Quantity", ingredientQuantity);
        data.put("Quantity Unit", unit);
        data.put("Expiry Date", expiryDate);

        return data;
    }


    private String GetIngredientName() {
        return IngredientName.getText().toString();
    }


    private boolean ValidData(){
        String ingredientName = GetIngredientName();
        HashMap<String, String> data = GetData();
        /*
        Keys:

         */


        boolean isValid = true;

        if (Validate.IsEmpty(ingredientName)) {
            IngredientName.setError("Ingredient Name cant be empty");
            isValid =  false;
        }

        if (Validate.IsEmpty(data.get("Category")) || Objects.equals(data.get("Category"), "Select A Category")) {
            TextView errorText = (TextView) categorySpinner.getSelectedView();
            errorText.setError("");
            errorText.setTextColor(Color.RED);
            errorText.setText("Select A Category");
            isValid =  false;
        }

        if (Validate.IsEmpty(data.get("Quantity"))) {
            IngredientQuantity.setError("Please Input Quantity");
            isValid =  false;
        }

        if (Validate.IsEmpty(data.get("Quantity Unit")) || Objects.equals(data.get("Quantity Unit"), "Select Unit")) {
            TextView errorText = (TextView) quantityUnits.getSelectedView();
            errorText.setError("");
            errorText.setTextColor(Color.RED);
            errorText.setText("Select Unit");
            isValid =  false;
        }
        if (Validate.ValidDate(data.get("Quantity Unit")) || Objects.equals(data.get("Quantity Unit"), "Select Unit")) {
            TextView errorText = (TextView) quantityUnits.getSelectedView();
            errorText.setError("");
            errorText.setTextColor(Color.RED);
            errorText.setText("Select Unit");
            isValid =  false;
        }

        // TODO Validate expiry date


        return isValid;

    }
}