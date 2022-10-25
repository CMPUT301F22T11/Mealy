package com.example.mealy;

import android.annotation.SuppressLint;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

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

    Button Save;
    EditText IngredientName;
    EditText IngredientQuantity;



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
        SaveButton(view);
        IngredientNameTextView(view);
        QuantityTextView(view);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        final String TAG = "!!!";
        final CollectionReference collectionReference = db.collection("Ingredients");

        Save.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("AHAHA", "This is my message");

                final String ingredientName = IngredientName.getText().toString();
                final String categoryName = categorySpinner.getSelectedItem().toString();
                final String ingredientQuantity = IngredientQuantity.getText().toString();
                final String unit = quantityUnits.getSelectedItem().toString();
                HashMap<String, String> data = new HashMap<>();
                if (ingredientName.length()>0 &&
                        (categoryName != "Select A Category") &&
                        IngredientQuantity.length()>0 &&
                        (unit != "Select unit")
                        ) { //&& categoryName.length()>0
                    data.put("Category", categoryName);
                    data.put("Quantity", ingredientQuantity);
                    data.put("Quantity Unit", unit);
                    collectionReference
                            .document(ingredientName)
                            .set(data)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    // These are a method which gets executed when the task is succeeded
                                    Log.d(TAG, "Data has been added successfully!");
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    // These are a method which gets executed if there’s any problem
                                    Log.d(TAG, "Data could not be added!" + e.toString());
                                }
                            });
                    IngredientName.setText("");
                    IngredientQuantity.setText("");
                }
                // figure out how to destroy the fragment and insert it here
            }
        });

        return view;
    }

    private void InitializeCategorySpinner() {
        categorySpinner = (Spinner) view.findViewById(R.id.categoryDropdown);
        categories = new String[]{"Select A Category", "Raw Food", "Meat", "Spice", "Fluid", "Other"};
        categoryAdapter = new ArrayAdapter<CharSequence>(getContext(), android.R.layout.simple_spinner_dropdown_item, categories);
        categorySpinner.setAdapter(categoryAdapter);
    }

    private void quantityUnitsSpinner() {
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

    private void SaveButton(View view) {
        Save = (Button) view.findViewById(R.id.addIngredient);
    }

    private void IngredientNameTextView(View view) {
        IngredientName = (EditText) view.findViewById(R.id.ingredientName);
    }

    private void QuantityTextView(View view) {
        IngredientQuantity = (EditText) view.findViewById(R.id.quantity);
    }
}
