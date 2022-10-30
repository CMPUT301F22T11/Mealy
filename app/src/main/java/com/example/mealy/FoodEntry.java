package com.example.mealy;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Objects;

import com.example.mealy.functions.Validate;
import com.example.mealy.functions.Firestore;
import com.example.mealy.functions.DateFunc;

public class FoodEntry extends DialogFragment {
    private final FoodEntry fragment = this;
    Spinner categorySpinner;
    Spinner quantityUnits;
    Spinner locationSpinner;
    ArrayAdapter<CharSequence> categoryAdapter;
    ArrayAdapter<CharSequence> unitsAdapter;
    ArrayAdapter<CharSequence> locationAdapter;
    String[] categories;
    String[] whole;
    String[] weight;
    String[] volume;
    String[] current;
    String[] locations;
    RadioGroup unitsRadioGroup;

    DatePickerDialog datePickerDialog;
    Button ExpiryDate;

    Button Save;
    EditText IngredientName;
    EditText IngredientQuantity;
    EditText DescriptionText;

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
        InitializeLocationSpinner();
        InitializeTextViews();
        InitializeDatePicker();

        return view;
    }

    /**
     * Sets up the spinner for the Category selection. Sets the values and adapter
     */
    private void InitializeCategorySpinner() {
        categorySpinner = (Spinner) view.findViewById(R.id.categoryDropdown);
        categories = new String[]{"Select Category", "Raw Food", "Meat", "Spice", "Fluid", "Other"};
        categoryAdapter = new ArrayAdapter<CharSequence>(getContext(), android.R.layout.simple_spinner_dropdown_item, categories);
        categorySpinner.setAdapter(categoryAdapter);

        // Todo, add categories and set default values for each category
    }

    /**
     * Sets up the spinner for location. Sets the values and adapter
     */

    private void InitializeLocationSpinner() {
        locationSpinner = (Spinner) view.findViewById(R.id.locationDropdown);
        locations = new String[]{"Select Location", "Pantry", "Fridge", "Freezer"};
        locationAdapter = new ArrayAdapter<CharSequence>(getContext(), android.R.layout.simple_spinner_dropdown_item, locations);
        locationSpinner.setAdapter(locationAdapter);

        // Todo, add locations
    }

    /**
     * Sets up the quantity for units spinner. Sets onCheckedChangeListener for the unit type radio group. Changes spinner
     * values accordingly
     */

    private void InitializeQuantityUnitsSpinner() {
        quantityUnits = (Spinner) view.findViewById(R.id.quantityDropdown);
        unitsRadioGroup = (RadioGroup) view.findViewById(R.id.quantityType);
        whole = new String[]{"Select Unit", "single", "Dozen", "Five Pack"};
        weight = new String[]{"Select Unit", "lb", "kg", "g", "oz"};
        volume = new String[]{"Select Unit", "L", "ml", "fl oz"};
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

    /**
     * Initialises save button, sets on click listener to store data into Firestore
     */
    private void InitializeSaveButton() {
        Save = (Button) view.findViewById(R.id.addIngredient);

        Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (ValidData()) {
                    String ingredientName = GetIngredientName();
                    HashMap<String, String> data = GetData();
                    requireActivity().getSupportFragmentManager().beginTransaction().remove(fragment).commit();
                    Firestore.StoreToFirestore("Ingredients", ingredientName, data);
                }
            }
        });
    }

    /**
     * Initializes the textViews
     */
    private void InitializeTextViews() {
        IngredientName = (EditText) view.findViewById(R.id.ingredientName);
        IngredientQuantity = (EditText) view.findViewById(R.id.quantity);
        DescriptionText = (EditText) view.findViewById(R.id.descriptionText);
    }

    /**
     * Takes all inputted data (except for ingredient name) and stores it into a HashMap
     * Hashmap:
     * Key            Value
     * Category       categoryName
     * Quantity       ingredientQuantity
     * Quantity Unit  unit
     * Expiry Date    expiryDate
     * Description    description
     *
     * @return HashMap of the data inputted (except for ingredient name)
     */
    private HashMap<String, String> GetData() {

        HashMap<String, String> data = new HashMap<>();

        String categoryName = categorySpinner.getSelectedItem().toString();
        String ingredientQuantity = IngredientQuantity.getText().toString();
        String unit = quantityUnits.getSelectedItem().toString();
        String expiryDate = DateFunc.MakeStringDate(ExpiryDate.getText().toString());
        String description = DescriptionText.getText().toString();

        data.put("Category", categoryName);
        data.put("Quantity", ingredientQuantity);
        data.put("Quantity Unit", unit);
        data.put("Expiry Date", expiryDate);
        data.put("Description", description);

        return data;
    }

    /**
     * Gets ingredient name
     *
     * @return Ingredient name
     */
    private String GetIngredientName() {
        return IngredientName.getText().toString();
    }

    private void InitializeDatePicker() {
        ExpiryDate = view.findViewById(R.id.datePickerButton);

        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener()
        {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day)
            {
                // months index from 0-11
                month = month + 1;
                ExpiryDate.setText(DateFunc.MakeIntString(day, month, year));
            }
        };

        // create the date from whatever was input by the user
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        int style = AlertDialog.THEME_HOLO_LIGHT;

        datePickerDialog = new DatePickerDialog(getContext(), style, dateSetListener, year, month, day);
        // the minimum expiration date is today
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() + 86400000);

        ExpiryDate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                datePickerDialog.show();

            }
        });
    }

    /**
     * Checks if a user inputted the data properly
     *
     * @return true if all data is inputted properly
     */
    private boolean ValidData() {

        String ingredientName = IngredientName.getText().toString();
        String categoryName = categorySpinner.getSelectedItem().toString();
        String ingredientQuantity = IngredientQuantity.getText().toString();
        String unit = quantityUnits.getSelectedItem().toString();
        String expiryDate = ExpiryDate.getText().toString();
        String location = locationSpinner.getSelectedItem().toString();
        String description = DescriptionText.getText().toString();

        boolean isValid = true;

        if (Validate.IsEmpty(ingredientName)) {
            IngredientName.setError("Ingredient Name cant be empty");
            isValid = false;
        }

        if (Validate.IsEmpty(categoryName) || Objects.equals(categoryName, "Select Category")) {
            TextView errorText = (TextView) categorySpinner.getSelectedView();
            errorText.setError("");
            errorText.setTextColor(Color.RED);
            errorText.setText("Select Category");
            isValid = false;
        }

        if (Validate.IsEmpty(ingredientQuantity)) {
            IngredientQuantity.setError("Please Input Quantity");
            isValid = false;
        }

        if (Validate.IsEmpty(unit) || Objects.equals(unit, "Select Unit")) {
            TextView errorText = (TextView) quantityUnits.getSelectedView();
            errorText.setError("");
            errorText.setTextColor(Color.RED);
            errorText.setText("Select Unit");
            isValid = false;
        }

        if (Validate.IsEmpty(location) || Objects.equals(location, "Select Location")) {
            TextView errorText = (TextView) locationSpinner.getSelectedView();
            errorText.setError("");
            errorText.setTextColor(Color.RED);
            errorText.setText("Select Location");
            isValid = false;
        }

        if (Validate.IsEmpty(description)) {
            DescriptionText.setError("Please write a description");
            isValid = false;
        }

        if (expiryDate.equals("Expiry Date")){
            ExpiryDate.setError("");
            isValid = false;
        }

        return isValid;

    }


}