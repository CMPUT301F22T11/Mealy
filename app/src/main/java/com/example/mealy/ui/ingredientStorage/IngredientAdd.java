package com.example.mealy.ui.ingredientStorage;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import com.example.mealy.R;
import com.example.mealy.functions.DeletableSpinnerArrayAdapter;
import com.example.mealy.functions.General;
import com.example.mealy.functions.Validate;
import com.example.mealy.functions.Firestore;
import com.example.mealy.functions.DateFunc;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Source;

/**
 * Fragment for users to input info about an ingredient
 */
public class IngredientAdd extends DialogFragment {
    private final IngredientAdd fragment = this;
    Spinner categorySpinner;
    Spinner quantityUnits;
    Spinner locationSpinner;
    DeletableSpinnerArrayAdapter categoryAdapter;
    DeletableSpinnerArrayAdapter locationAdapter;
    ArrayAdapter<String> ingredientAdapter;
    ArrayAdapter<CharSequence> unitsAdapter;
    ArrayList<String> Location = new ArrayList<>();
    ArrayList<String> Category = new ArrayList<>();
    ArrayList<String> Ingredients = new ArrayList<>();
    ArrayList<String> RecipeIngredients = new ArrayList<>();
    HashMap<String, Map<String, Object>> RecipeIngredientsMaps = new HashMap<>();

    String[] whole;
    String[] weight;
    String[] volume;
    String[] current;
    RadioGroup unitsRadioGroup;
    RadioButton wholeButton;
    RadioButton weightButton;
    RadioButton volumeButton;
    String unitCategory;
    Map<String, Object> categoryData;
    Map<String, Object> locationData;

    DatePickerDialog datePickerDialog;
    Button ExpiryDate;

    Button Save;
    AutoCompleteTextView IngredientName;
    EditText IngredientQuantity;
    EditText DescriptionText;
    EditText AddCategory;
    EditText AddLocation;
    TextView Title;

    View view;

    Ingredient ingredient;
    boolean edit;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    /**
     * if no ingredient is provided, it's assumed you want to create a new ingredient
     */
    public IngredientAdd() {
        edit = false;
    }

    /**
     * if an ingredient is provided, it's assumed you want to edit an ingredient
     * @param ingredient ingredient you want to edit
     */
    public IngredientAdd(Ingredient ingredient) {
        this.ingredient = ingredient;
        edit = true;
    }

    /**
     * Creates the view to add/edit ingredients
     * @param inflater inflater for view
     * @param container container for view
     * @param savedInstanceState idk tbh
     * @return the view
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflates View
        view = inflater.inflate(R.layout.ingredient_add, container, false);

        InitializeIngredientName();
        GetIngredientNames(); // Gets all the ingredient names to avoid duplicates
        // Initializes Interface
        InitializeCategorySpinner();
        InitializeQuantityUnitsSpinner();
        InitializeSaveButton();
        InitializeLocationSpinner();
        InitializeTextViews();
        InitializeDatePicker();

        if (edit) {
            EditMode();
        }

        return view;
    }

    private void InitializeIngredientName() {
        IngredientName = view.findViewById(R.id.ingredientName);
        ingredientAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, RecipeIngredients);
        ingredientAdapter.setNotifyOnChange(true);
        IngredientName.setAdapter(ingredientAdapter);

        IngredientName.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                InputMethodManager in = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                in.hideSoftInputFromWindow(view.getWindowToken(), 0);
                Map<String, Object> currentIngredient = RecipeIngredientsMaps.get(RecipeIngredients.get(i));

                DescriptionText.setText(General.blankIfVoid((String) currentIngredient.get("Description")));
                wholeButton.setChecked(true); // true in case the field is empty

                String unit = (String) currentIngredient.get("Unit Category");
                unit = General.blankIfVoid(unit);
                wholeButton.setChecked(true);
                weightButton.setChecked(unit.equals("Weight"));
                volumeButton.setChecked(unit.equals("Volume"));

                unit = (String) currentIngredient.get("Unit");
                unit = General.blankIfVoid(unit);

                quantityUnits.setSelection(Arrays.asList(current).indexOf(unit));

                if (Category.contains((String) currentIngredient.get("Category"))) {
                    categorySpinner.setSelection(Category.indexOf(General.blankIfVoid((String) currentIngredient.get("Category"))));
                }
            }
        });
    }


    /**
     * Sets up the spinner for the Category selection. Sets the values and adapter
     */
    private void InitializeCategorySpinner() {
        categorySpinner = (Spinner) view.findViewById(R.id.categoryDropdown);
        //categories = new String[]{"Select Category", "Add Category", "Raw Food", "Meat", "Spice", "Fluid", "Other"};
        Category.add("Select Category");
        Category.add("Add Category");
        categoryAdapter = new DeletableSpinnerArrayAdapter(getContext(), Category, "Category");
        //categoryAdapter.setDropDownViewResource(R.layout.spinner_dropdown);
        categorySpinner.setAdapter(categoryAdapter);
        AddCategory = view.findViewById(R.id.newCategory);
        readCategoryFirebase();

        // Todo let user add categories

        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if (position == 1) {
                    AddCategory.setVisibility(View.VISIBLE);

                } else {
                    AddCategory.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                categorySpinner.setSelection(0);
            }
        });

    }

    /**
     * Sets up the spinner for location. Sets the values and adapter
     */

    private void InitializeLocationSpinner() {
        locationSpinner = (Spinner) view.findViewById(R.id.locationDropdown);
        //locations = new String[]{"Select Location","Add Location", "Pantry", "Fridge", "Freezer"};
        Location.add("Select Location");
        Location.add("Add Location");
        locationAdapter = new DeletableSpinnerArrayAdapter(getContext(), Location, "Location");
        locationAdapter.setNotifyOnChange(true);
        locationSpinner.setAdapter(locationAdapter);
        AddLocation = view.findViewById(R.id.newLocation);
        readLocationFirebase();

        locationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if (position == 1) {
                    AddLocation.setVisibility(View.VISIBLE);

                } else {
                    AddLocation.setVisibility(View.GONE);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                locationSpinner.setSelection(0);
            }
        });

    }

    /**
     * Sets up the quantity for units spinner. Sets onCheckedChangeListener for the unit type radio group. Changes spinner
     * values accordingly
     */
    private void InitializeQuantityUnitsSpinner() {
        quantityUnits = (Spinner) view.findViewById(R.id.quantityDropdown);
        unitsRadioGroup = (RadioGroup) view.findViewById(R.id.quantityType);
        wholeButton = view.findViewById(R.id.whole);
        volumeButton = view.findViewById(R.id.volume);
        weightButton = view.findViewById(R.id.weight);
        whole = new String[]{"Select Unit", "single", "Dozen", "Five Pack"};
        weight = new String[]{"Select Unit", "lb", "kg", "g", "oz"};
        volume = new String[]{"Select Unit", "L", "ml", "fl oz"};
        current = whole;
        unitCategory = "Whole";
        unitsAdapter = new ArrayAdapter<CharSequence>(getContext(), R.layout.spinner_layout, current);
        unitsAdapter.setDropDownViewResource(R.layout.spinner_dropdown);
        quantityUnits.setAdapter(unitsAdapter);
        unitsAdapter.setNotifyOnChange(true);

        unitsRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                switch (checkedId) {
                    case R.id.whole:
                        current = whole;
                        unitsAdapter = new ArrayAdapter<CharSequence>(getContext(), R.layout.spinner_layout, current);
                        unitsAdapter.setDropDownViewResource(R.layout.spinner_dropdown);
                        quantityUnits.setAdapter(unitsAdapter);
                        unitCategory = "Whole";
                        break;
                    case R.id.weight:
                        current = weight;
                        unitsAdapter = new ArrayAdapter<CharSequence>(getContext(), R.layout.spinner_layout, current);
                        unitsAdapter.setDropDownViewResource(R.layout.spinner_dropdown);
                        quantityUnits.setAdapter(unitsAdapter);
                        unitCategory = "Weight";
                        break;
                    case R.id.volume:
                        current = volume;
                        unitsAdapter = new ArrayAdapter<CharSequence>(getContext(), R.layout.spinner_layout, current);
                        unitsAdapter.setDropDownViewResource(R.layout.spinner_dropdown);
                        quantityUnits.setAdapter(unitsAdapter);
                        unitCategory = "Volume";
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
                final String collection = "Ingredients";

                if (ValidData()) {
                    String ingredientName = GetIngredientName();
                    HashMap<String, String> data = GetData();

                    if (AddCategory.getVisibility() == View.VISIBLE){
                        if (!categoryData.containsValue(AddCategory.getText().toString())) {
                            categoryData.put(String.valueOf(categoryData.size()+1), AddCategory.getText().toString());
                            Firestore.storeToFirestore("Spinner","Category", categoryData);
                        }
                    }

                    if (AddLocation.getVisibility() == View.VISIBLE){
                        if(!locationData.containsValue(AddLocation.getText().toString())) {
                            locationData.put(String.valueOf(locationData.size() + 1), AddLocation.getText().toString());
                            Firestore.storeToFirestore("Spinner", "Location", locationData);
                        }
                    }

                    if (edit) {
                        getParentFragmentManager().beginTransaction().remove(fragment).commit();
                        Firestore.deleteFromFirestore(collection, ingredient.getName());
                    }
                    else {
                        requireActivity().getSupportFragmentManager().beginTransaction().remove(fragment).commit();
                    }
                    Firestore.storeToFirestore(collection, ingredientName, data);
                }
            }
        });
    }

    /**
     * Initializes the textViews
     */
    private void InitializeTextViews() {
        IngredientQuantity = (EditText) view.findViewById(R.id.quantity);
        DescriptionText = (EditText) view.findViewById(R.id.descriptionText);
        Title = (TextView) view.findViewById(R.id.title);
    }

    /**
     * Sets default values to ingredient values that we need to edit
     */
    private void EditMode() {
        Title.setText("Edit Ingredient");
        IngredientName.setText(ingredient.getName());
        IngredientQuantity.setText(ingredient.getAmount());
        DescriptionText.setText(ingredient.getDescription());


        // this sets the button to true if its equal to the ingredient unit category
        wholeButton.setChecked(true); // true in case the field is empty
        weightButton.setChecked(ingredient.getUnitCategory().equals("Weight"));
        volumeButton.setChecked(ingredient.getUnitCategory().equals("Volume"));

        // sets spinners to their appropriate value. Goes to default value if item is not in spinner
        quantityUnits.setSelection(Arrays.asList(current).indexOf(ingredient.getUnit()));
        //categorySpinner.setSelection(Arrays.asList(Categories).indexOf(ingredient.getCategory()));
        //locationSpinner.setSelection(Arrays.asList(locations).indexOf(ingredient.getLocation()));
        ExpiryDate.setText(DateFunc.makeDateString(ingredient.getExpiryDate()));
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
        String categoryName;
        String location;

        if (AddCategory.getVisibility() == View.VISIBLE) {
            categoryName = AddCategory.getText().toString();
        } else {
            categoryName = categorySpinner.getSelectedItem().toString();
        }

        if (AddLocation.getVisibility() == View.VISIBLE) {
            location = AddLocation.getText().toString();
        } else {
            location = locationSpinner.getSelectedItem().toString();
        }

        String ingredientQuantity = IngredientQuantity.getText().toString();
        String unit = quantityUnits.getSelectedItem().toString();
        String expiryDate = DateFunc.makeStringDate(ExpiryDate.getText().toString());
        String description = DescriptionText.getText().toString();

        data.put("Category", categoryName);
        data.put("Quantity", ingredientQuantity);
        data.put("Quantity Unit", unit);
        data.put("Unit Category", unitCategory);
        data.put("Expiry Date", expiryDate);
        data.put("Description", description);
        data.put("Location", location);

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
                ExpiryDate.setText(DateFunc.makeIntString(day, month, year));
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

        // gets all the user's inputted data
        String ingredientName = IngredientName.getText().toString();
        String categoryName = categorySpinner.getSelectedItem().toString();
        String ingredientQuantity = IngredientQuantity.getText().toString();
        String unit = quantityUnits.getSelectedItem().toString();
        String expiryDate = ExpiryDate.getText().toString();
        String location = locationSpinner.getSelectedItem().toString();
        String description = DescriptionText.getText().toString();

        boolean isValid = true; // is converted to false if any item is false

        // checks if ingredientName is empty
        if (Validate.isEmpty(ingredientName)) {
            IngredientName.setError("Ingredient Name cant be empty");
            isValid = false;
        }
        // checks if categoryName is empty or equal to the default text
        if (Validate.isEmpty(categoryName) || Objects.equals(categoryName, "Select Category")) {
            // if it's empty it shows a red exclamation mark and turns spinner text red
            TextView errorText = (TextView) categorySpinner.getSelectedView();
            errorText.setError("");
            errorText.setTextColor(Color.RED);
            errorText.setText("Select Category");
            isValid = false;
        }

        // checks if add category is empty
        if (AddCategory.getVisibility() == View.VISIBLE) {
            if (Validate.isEmpty(AddCategory.getText().toString())){
                AddCategory.setError("Please Input a New Category");
                isValid = false;
            }
        }

        if (AddLocation.getVisibility() == View.VISIBLE) {
            if (Validate.isEmpty(AddLocation.getText().toString())){
                AddLocation.setError("Please Input a New Location");
                isValid = false;
            }
        }

        // checks if ingredientQuantity is empty
        if (Validate.isEmpty(ingredientQuantity)) {
            IngredientQuantity.setError("Please Input Quantity");
            isValid = false;
        }
        // checks if ingredientQuantity is actually a number and if it's less than 0
        try {
            if (Float.parseFloat(ingredientQuantity) <= 0) {
                IngredientQuantity.setError("Can't have 0 or negative quantities");
                isValid = false;
            }
        } catch (Exception e) {
            IngredientQuantity.setError("Invalid Number");
            isValid = false;
        }

        // checks if the user selected a unit of measurement
        if (Validate.isEmpty(unit) || Objects.equals(unit, "Select Unit")) {
            // if it's empty it shows a red exclamation mark and turns spinner text red
            TextView errorText = (TextView) quantityUnits.getSelectedView();
            errorText.setError("");
            errorText.setTextColor(Color.RED);
            errorText.setText("Select Unit");
            isValid = false;
        }

        // checks if the location is empty
        if (Validate.isEmpty(location) || Objects.equals(location, "Select Location")) {
            // if it's empty it shows a red exclamation mark and turns spinner text red
            TextView errorText = (TextView) locationSpinner.getSelectedView();
            errorText.setError("");
            errorText.setTextColor(Color.RED);
            errorText.setText("Select Location");
            isValid = false;
        }

        // checks if the description is empty
        if (Validate.isEmpty(description)) {
            DescriptionText.setError("Please write a description");
            isValid = false;
        }

        // checks if the user inputted a valid expiry date.
        if (expiryDate.equals("Expiry Date") || Validate.isEmpty(expiryDate)){
            // TODO check if the date is valid, don't rely on the date picker to be valid
            ExpiryDate.setError("");
            isValid = false;
        }

        return isValid;

    }

    public void readCategoryFirebase() {
        String CollectionName = "Spinner";
        String documentName = "Category";
        DocumentReference docRef = db.collection(CollectionName).document(documentName);
        Source source = Source.CACHE;
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        categoryData = document.getData();
                        Category = General.mapToArrayList(categoryData);
                        categoryAdapter = new DeletableSpinnerArrayAdapter(getContext(), Category, "Category");
                        categorySpinner.setAdapter(categoryAdapter);
                        if (edit && ingredient!=null) categorySpinner.setSelection(Category.indexOf(ingredient.getCategory()));

                        Log.d(TAG, "DocumentSnapshot data: " + categoryData);
                    } else {
                        Log.d(TAG, "No such document");
                        assert Category != null;
                        Firestore.storeToFirestore("Spinner", documentName, General.listToMap(Category));
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });

        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot doc, @Nullable FirebaseFirestoreException error) {
                if (doc != null) {
                    if ((doc.getData() != null) && (getContext() != null))
                    {
                        categoryData = doc.getData();
                        Category = General.mapToArrayList(categoryData);
                        categoryAdapter = new DeletableSpinnerArrayAdapter(getContext(), Category, "Category");
                        categorySpinner.setAdapter(categoryAdapter);
                    }
                }
            }
        });

    }

    public void readLocationFirebase() {
        String CollectionName = "Spinner";
        String documentName = "Location";
        DocumentReference docRef = db.collection(CollectionName).document(documentName);
        Source source = Source.CACHE;
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        locationData = document.getData();
                        Location = General.mapToArrayList(locationData);
                        locationAdapter = new DeletableSpinnerArrayAdapter(getContext(), Location, "Location");
                        locationSpinner.setAdapter(locationAdapter);
                        if (edit && ingredient!=null) {locationSpinner.setSelection(Location.indexOf(ingredient.getLocation()));}

                        Log.d(TAG, "DocumentSnapshot data: " + locationData);
                    } else {
                        Log.d(TAG, "No such document");
                        assert Location != null;
                        Firestore.storeToFirestore("Spinner", documentName, General.listToMap(Location));
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });

        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot doc, @Nullable FirebaseFirestoreException error) {
                if (doc != null) {
                    if ((doc.getData() != null) && (getContext() != null))
                    {
                        locationData = doc.getData();
                        Location = General.mapToArrayList(locationData);
                        locationAdapter = new DeletableSpinnerArrayAdapter(getContext(), Location, "Location");
                        locationSpinner.setAdapter(locationAdapter);
                    }
                }
            }
        });
    }

    private void GetIngredientNames() {
        CollectionReference ingredientReference = db.collection("Ingredients");
        ingredientReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            /**
             * Retrieve entries of Ingredients and categories from the firebase, and notify the nameAdapter and categoryAdapter
             * that was created for each respective lists.
             *
             * @param queryDocumentSnapshots returns each document within the collection
             * @param error
             */
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable
                    FirebaseFirestoreException error) {

                for(QueryDocumentSnapshot doc: queryDocumentSnapshots) {
                    String ingredient = (String) doc.getId();
                    Ingredients.add(ingredient);
                }
            }
        });

        CollectionReference recipeIngredientReference = db.collection("RecipeIngredients");
        recipeIngredientReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            /**
             * Retrieve entries of Ingredients and categories from the firebase, and notify the nameAdapter and categoryAdapter
             * that was created for each respective lists.
             *
             * @param queryDocumentSnapshots returns each document within the collection
             * @param error
             */
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable
                    FirebaseFirestoreException error) {

                for(QueryDocumentSnapshot doc: queryDocumentSnapshots) {
                    if (!RecipeIngredientsMaps.containsKey((String) doc.getData().get("Name"))) {
                        RecipeIngredientsMaps.put((String) doc.getData().get("Name"), doc.getData());
                        RecipeIngredients.add((String) doc.getData().get("Name"));
                    }
                }
            }
        });

    }
}