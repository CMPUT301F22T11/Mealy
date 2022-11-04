package com.example.mealy;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import com.example.mealy.functions.Validate;
import com.example.mealy.functions.Firestore;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;

import org.checkerframework.checker.units.qual.A;


/**
 * This class contains the DIalogFragment that allows the user to create/edit a RecipeIngredient entry.
 * After hitting the "Save" button, the fragment passes the created (or edited) Recipe Ingredient
 * object back to the RecipeEntry fragment, and ends.
 */
public class RecipeIngredientAdd extends DialogFragment {
    private final RecipeIngredientAdd fragment = this;
    Spinner categorySpinner;
    Spinner nameSpinner;
    Spinner quantityUnits;
    ArrayAdapter<String> categoryAdapter;
    ArrayAdapter<String> nameAdapter;
    ArrayAdapter<CharSequence> unitsAdapter;
    String[] categories;
    String[] whole;
    String[] weight;
    String[] volume;
    String[] current;
    String[] names;
    RadioGroup unitsRadioGroup;

    Button Save;
    EditText RecipeIngredientName;
    EditText RecipeIngredientAmount;
    EditText RecipeIngredientDescription;
    EditText RecipeIngredientCategory;

    ArrayList<String> thisIngredient = new ArrayList<String>();
    ArrayList<String> thisCategory = new ArrayList<String>();


    FirebaseFirestore db = FirebaseFirestore.getInstance();


    CollectionReference collectionReference;



    private static final String TAG = "DocSnippets";

    boolean categoryNewSelected = false, nameNewSelected = false;

    View view;

    public RecipeIngredientAdd() {
        // Constructor: TODO
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * Creates the constructed view for the user to interact with. Initialize all XML elements (TextViews, Spinners, EditTexts, etc).
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        // Inflates View
        view = inflater.inflate(R.layout.recipe_ingredient, container, false);
        // Initializes Interface
        InitializeNameSpinner();
        InitializeCategorySpinner();
        InitializeGetAll();
        InitializeUnitSpinner();
        InitializeSaveButton();
        InitializeTextViews();



        return view;
    }

    /**
     * Initialize an addSnapshotListener that retrieves all current entries of Ingredients, alongside the category in which
     * each ingredient is in, from the Firebase.
     */
    private void InitializeGetAll() {
        collectionReference = db.collection("Ingredients");
        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            /**
             * Retrieve entries of Ingredients and categories from the firebase, and notify the nameAdapter and categoryAdapter
             * that was created for each respective lists.
             */
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable
                    FirebaseFirestoreException error) {
                thisIngredient.add("Select an Ingredient:");
                thisCategory.add("Select a Category:");

                for(QueryDocumentSnapshot doc: queryDocumentSnapshots) {
                    String ingredient = (String) doc.getId();
                    thisIngredient.add(ingredient); // Adding the cities and provinces from FireStore
                    String category = (String) doc.getData().get("Category");
                    boolean hasCategory = false;
                    for (String curCategory : thisCategory) {
                        if (category == curCategory) {
                            hasCategory = true;
                        }
                    }
                    if (hasCategory == false) {
                        thisCategory.add(category);
                    }
                }
                thisIngredient.add("New Ingredient");
                thisCategory.add("New Category");
                nameAdapter.notifyDataSetChanged();
                categoryAdapter.notifyDataSetChanged();
            }
        });
    }

    /**
     * Initializes the spinner for selecting the name
     */
    private void InitializeNameSpinner() {
        nameSpinner = (Spinner) view.findViewById(R.id.r_ingredient_name_dropdown);


        nameAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, thisIngredient);
        nameSpinner.setAdapter(nameAdapter);

        nameSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            /**
             * For an item is selected on the spinner, if the user selects the last entry ('New Ingredient'), reveal the
             * EditText element that allows the user to enter in a new ingredient entry.
             */
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                if (position == thisIngredient.size()-1) {
                    nameNewSelected = true;
                    RecipeIngredientName.setVisibility(View.VISIBLE);
                }
                else {
                    nameNewSelected = false;
                    RecipeIngredientName.setVisibility(View.INVISIBLE);
                }


            }

            /**
             * If nothing gets selected for the spinner, then make the EditText invisible.
             */
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                nameNewSelected = false;
                RecipeIngredientName.setVisibility(View.INVISIBLE);
            }

        });

    }

    /**
     * Initializes the spinner for selecting a category
     */
    private void InitializeCategorySpinner() {
        categorySpinner = (Spinner) view.findViewById(R.id.r_ingredient_category_dropdown);
        categoryAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, thisCategory);
        categorySpinner.setAdapter(categoryAdapter);

        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            /**
             * For an item that is selected on the spinner, if the user selects the last entry ('New Category'), reveal the
             * EditText element that allows the user to enter in a new ingredient entry.
             */
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                if (position == thisCategory.size()-1) {
                    categoryNewSelected = true;
                    RecipeIngredientCategory.setVisibility(View.VISIBLE);
                }
                else {
                    categoryNewSelected = false;
                    RecipeIngredientCategory.setVisibility(View.INVISIBLE);
                }

            }
            /**
             * If nothing gets selected for the spinner, then make the EditText invisible.
             */
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                categoryNewSelected = false;
                RecipeIngredientCategory.setVisibility(View.INVISIBLE);
            }

        });
    }

    /**
     * Initializes the spinner for units.
     */
    private void InitializeUnitSpinner() {
        quantityUnits = (Spinner) view.findViewById(R.id.r_ingredient_unit_dropdown);
        unitsRadioGroup = (RadioGroup) view.findViewById(R.id.r_ingredient_quantityType);
        whole = new String[]{ "single", "Dozen", "Five Pack"};
        weight = new String[]{"Select Unit", "lb", "kg", "g"};
        volume = new String[]{"Select Unit", "L", "ml", "oz"};
        current = whole;
        unitsAdapter = new ArrayAdapter<CharSequence>(getContext(), android.R.layout.simple_spinner_dropdown_item, current);
        quantityUnits.setAdapter(unitsAdapter);
        unitsAdapter.setNotifyOnChange(true);

        unitsRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            /**
             * Changes the selection for the spinner, based on the selection of the radio group.
             */
            @SuppressLint("NonConstantResourceId")
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                switch (checkedId) {
                    case R.id.r_ingredient_quantity_whole:
                        current = whole;
                        unitsAdapter = new ArrayAdapter<CharSequence>(getContext(), android.R.layout.simple_spinner_dropdown_item, current);
                        quantityUnits.setAdapter(unitsAdapter);
                        break;
                    case R.id.r_ingredient_quantity_weight:
                        current = weight;
                        unitsAdapter = new ArrayAdapter<CharSequence>(getContext(), android.R.layout.simple_spinner_dropdown_item, current);
                        quantityUnits.setAdapter(unitsAdapter);
                        break;
                    case R.id.r_ingredient_quantity_volume:
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
     * Initializes the save button.
     */
    private void InitializeSaveButton() {
        Save = (Button) view.findViewById(R.id.add_r_ingredient);

        Save.setOnClickListener(new View.OnClickListener() {
            /**
             * Once the save button is clicked, get the user's inputs for all the different fields, and use it to create a
             * new Recipe Ingredient. Using Parcelable, the Recipe Ingredient gets passed back to the Recipe DialogFragment.
             */
            @Override
            public void onClick(View view) {
                Log.d("AHAHA", "This is my message");

                if (ValidData()) {

                    String ingredientNameNew = GetRecipeIngredientName();
                    String ingredientName = nameSpinner.getSelectedItem().toString();

                    requireActivity().getSupportFragmentManager().beginTransaction().remove(fragment).commit();

                    String name, category;
                    String recipeIngredientCategory = categorySpinner.getSelectedItem().toString();
                    String ingredientCategoryNew = RecipeIngredientCategory.getText().toString();
                    String recipeIngredientAmount = RecipeIngredientAmount.getText().toString();
                    String recipeIngredientUnit = quantityUnits.getSelectedItem().toString();
                    String recipeIngredientDescription = RecipeIngredientDescription.getText().toString();
                    if (nameNewSelected == true) {
                        name = ingredientNameNew;
                    }
                    else {
                        name = ingredientName;
                    }
                    if (categoryNewSelected == true) {
                        category = ingredientCategoryNew;
                    }
                    else {
                        category = recipeIngredientCategory;
                    }

                    RecipeIngredient thisIngredient = new RecipeIngredient(name, recipeIngredientDescription, recipeIngredientAmount, recipeIngredientUnit, category);

                    Bundle bundle = new Bundle();
                    bundle.putParcelable("RecipeIngredient", thisIngredient);

                    getParentFragmentManager().setFragmentResult("requestKey", bundle);

                }


            }
        });
    }


    /**
     * Initializes all the EditText elements.
     */
    private void InitializeTextViews() {
        RecipeIngredientName = (EditText) view.findViewById(R.id.r_ingredient_name_text);
        RecipeIngredientAmount = (EditText) view.findViewById(R.id.r_ingredient_amount_text);
        RecipeIngredientDescription = (EditText) view.findViewById(R.id.r_ingredient_description_text);
        RecipeIngredientCategory = (EditText) view.findViewById(R.id.r_ingredient_category_text);
    }

    /**
     * Gets the name for the Recipe Ingredient the user gave for this current instacne of a Recipe Ingredient.
     */
    private String GetRecipeIngredientName() {
        return RecipeIngredientName.getText().toString();
    }

    /**
     * Checks if the user has filled in all required fields. If not, then it returns an error prompting the user to
     * fill in said fields.
     */
    private boolean ValidData(){

        String recipeIngredientNew = RecipeIngredientName.getText().toString();
        String recipeIngredientName = nameSpinner.getSelectedItem().toString();
        String recipeIngredientCategory = categorySpinner.getSelectedItem().toString();
        String ingredientCategoryNew = RecipeIngredientCategory.getText().toString();
        String recipeIngredientAmount = RecipeIngredientAmount.getText().toString();
        String recipeIngredientUnit = quantityUnits.getSelectedItem().toString();
        String recipeIngredientDescription = RecipeIngredientDescription.getText().toString();


        boolean isValid = true;

        if (Validate.IsEmpty(recipeIngredientNew)  && nameNewSelected == true) {
            RecipeIngredientName.setError("Please give an ingredient name");
            isValid =  false;
        }

        if ((Validate.IsEmpty(recipeIngredientCategory) || Objects.equals(recipeIngredientCategory, "Select a Category:")) && categoryNewSelected == false) {
            TextView errorText = (TextView) categorySpinner.getSelectedView();
            errorText.setError("");
            errorText.setTextColor(Color.RED);
            errorText.setText("Please select a category");
            isValid =  false;
        }

        if ((Validate.IsEmpty(recipeIngredientName) || Objects.equals(recipeIngredientName, "Select an Ingredient:")) &&  nameNewSelected == false) {
            TextView errorText = (TextView) nameSpinner.getSelectedView();
            errorText.setError("");
            errorText.setTextColor(Color.RED);
            errorText.setText("Please select a name");
            isValid =  false;
        }

        if (Validate.IsEmpty(recipeIngredientAmount)) {
            RecipeIngredientAmount.setError("Please input amount");
            isValid =  false;
        }

        if (Validate.IsEmpty(recipeIngredientUnit) || Objects.equals(recipeIngredientUnit, "Select Unit")) {
            TextView errorText = (TextView) quantityUnits.getSelectedView();
            errorText.setError("");
            errorText.setTextColor(Color.RED);
            errorText.setText("Select unit");
            isValid =  false;
        }

        if (Validate.IsEmpty(recipeIngredientDescription)) {
            RecipeIngredientDescription.setError("Ingredient description cannot be empty.");
            isValid =  false;
        }

        if (Validate.IsEmpty(ingredientCategoryNew) && categoryNewSelected == true) {
            RecipeIngredientCategory.setError("Please give a new category.");
            isValid =  false;
        }

        return isValid;

    }
}