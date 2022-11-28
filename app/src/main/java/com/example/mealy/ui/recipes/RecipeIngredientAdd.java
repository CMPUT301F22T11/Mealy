package com.example.mealy.ui.recipes;

import android.annotation.SuppressLint;
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
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import com.example.mealy.R;
import com.example.mealy.functions.DeletableSpinnerArrayAdapter;
import com.example.mealy.functions.Firestore;
import com.example.mealy.functions.General;
import com.example.mealy.functions.Validate;
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
 * This class contains the DialogFragment that allows the user to create/edit a RecipeIngredient entry.
 * After hitting the "Save" button, the fragment passes the created (or edited) Recipe Ingredient
 * object back to the RecipeEntry fragment, and ends.
 */
public class RecipeIngredientAdd extends DialogFragment {
    private final RecipeIngredientAdd fragment = this;
    Spinner categorySpinner;
    AutoCompleteTextView ingredientName;
    Spinner quantityUnits;
    DeletableSpinnerArrayAdapter categoryAdapter;
    ArrayAdapter<String> ingredientAdapter;
    ArrayAdapter<CharSequence> unitsAdapter;
    String[] whole;
    String[] weight;
    String[] volume;
    String[] current;
    String unitCategory;
    String oldIngredient;
    RadioGroup unitsRadioGroup;
    RadioButton wholeButton;
    RadioButton weightButton;
    RadioButton volumeButton;

    Button Save;
    EditText RecipeIngredientAmount;
    EditText RecipeIngredientDescription;
    EditText RecipeIngredientCategory;
    TextView Title;

    ArrayList<String> Category = new ArrayList<String>();
    HashMap<String, Map<String, Object>> Ingredients = new HashMap<>();
    Map<String, Object> categoryData;
    ArrayList<String> IngredientNames = new ArrayList<String>();
    RecipeIngredient ingredient;
    ArrayList<RecipeIngredient> listOfIngredients;
    ArrayList<String> savedIngredients = new ArrayList<String>();;



    FirebaseFirestore db = FirebaseFirestore.getInstance();


    CollectionReference collectionReference;



    private static final String TAG = "DocSnippets";

    boolean categoryNewSelected = false, nameNewSelected = false, edit;

    View view;

    public RecipeIngredientAdd(ArrayList<RecipeIngredient> listOfIngredients) {
        edit = false;
        this.listOfIngredients = listOfIngredients;
        // Constructor: TODO
    }

    public RecipeIngredientAdd(RecipeIngredient ingredient, ArrayList<RecipeIngredient> listOfIngredients) {
        edit = true;
        this.ingredient = ingredient;
        this.oldIngredient = ingredient.getTitle();
        this.listOfIngredients = listOfIngredients;
        // Constructor: TODO
    }

    /**
     * This is the override for the oncreate
     * @param savedInstanceState
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * Creates the constructed view for the user to interact with. Initialize all XML elements (TextViews, Spinners, EditTexts, etc).
     *
     * @param inflater LayoutInflater object used to inflate any views in the fragment
     * @param container May be used to generate LayoutParams of the view. Otherwise, this is NULL
     * @param savedInstanceState This crated fragment may be re-constructed from a previous saved state, stored in this parameter. Otherwise, this is NULL.
     *
     * @return Returns the view created.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        // Inflates View
        view = inflater.inflate(R.layout.recipe_ingredient_add, container, false);

        for (RecipeIngredient i: listOfIngredients) {
            savedIngredients.add(i.getTitle());
        }
        if (edit) {
            savedIngredients.remove(oldIngredient);
        }



        // Initializes Interface
        InitializeNameSpinner();
        InitializeCategorySpinner();
        InitializeUnitSpinner();
        InitializeSaveButton();
        InitializeTextViews();
        readCategoryFirebase();
        GetIngredientNames();
        
        if (edit) {
            EditMode();
        }
        



        return view;
    }

    private void EditMode() {
        Title.setText("Edit Ingredient");
        ingredientName.setText(ingredient.getTitle());
        RecipeIngredientAmount.setText(ingredient.getAmount());
        RecipeIngredientDescription.setText(ingredient.getDescription());

        // this sets the button to true if its equal to the ingredient unit category
        wholeButton.setChecked(true); // true in case the field is empty
        weightButton.setChecked(ingredient.getUnitCategory().equals("Weight"));
        volumeButton.setChecked(ingredient.getUnitCategory().equals("Volume"));

        // sets spinners to their appropriate value. Goes to default value if item is not in spinner
        quantityUnits.setSelection(Arrays.asList(current).indexOf(ingredient.getUnit()));


    }

    /**
     * Initialize an addSnapshotListener that retrieves all current entries of Ingredients, alongside the category in which
     * each ingredient is in, from the Firebase.
     */


    /**
     * Initializes the spinner for selecting the name
     */
    private void InitializeNameSpinner() {
        ingredientName = view.findViewById(R.id.r_ingredient_name_text);
        ingredientAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, IngredientNames);
        ingredientAdapter.setNotifyOnChange(true);
        ingredientName.setAdapter(ingredientAdapter);

        ingredientName.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                InputMethodManager in = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                in.hideSoftInputFromWindow(view.getWindowToken(), 0);
                Map<String, Object> currentIngredient = Ingredients.get(ingredientName.getText().toString());
                Log.wtf("idk", currentIngredient.toString());

                RecipeIngredientDescription.setText((String) currentIngredient.get("Description"));

                String unit = (String) currentIngredient.get("Unit Category");
                unit = General.blankIfVoid(unit);
                wholeButton.setChecked(true); // true in case the field is empty
                weightButton.setChecked(unit.equals("Weight"));
                volumeButton.setChecked(unit.equals("Volume"));

                unit = (String) currentIngredient.get("Quantity Unit");
                unit = General.blankIfVoid(unit);
                Log.wtf("idk", unit);
                quantityUnits.setSelection(Arrays.asList(current).indexOf(unit));

                if (Category.contains((String) currentIngredient.get("Category"))) {
                    categorySpinner.setSelection(Category.indexOf(General.blankIfVoid((String) currentIngredient.get("Category"))));
                }
            }
        });
    }

    /**
     * Initializes the spinner for selecting a category
     */
    private void InitializeCategorySpinner() {
        categorySpinner = view.findViewById(R.id.r_ingredient_category_dropdown);
        categoryAdapter = new DeletableSpinnerArrayAdapter(getContext(), Category, "Category");
        categorySpinner.setAdapter(categoryAdapter);

        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            /**
             * For an item that is selected on the spinner, if the user selects the last entry ('New Category'), reveal the
             * EditText element that allows the user to enter in a new category.
             * @param parentView The adapterView where the click occurred
             * @param selectedItemView The current view of the app
             * @param position Returns the index of the category that was selected
             * @param id THe row id of the category that was clicked
             */
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                if (position == 1) {
                    categoryNewSelected = true;
                    RecipeIngredientCategory.setVisibility(View.VISIBLE);
                }
                else {
                    categoryNewSelected = false;
                    RecipeIngredientCategory.setVisibility(View.GONE);
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
        quantityUnits = view.findViewById(R.id.r_ingredient_unit_dropdown);
        unitsRadioGroup = view.findViewById(R.id.r_ingredient_quantityType);
        wholeButton = view.findViewById(R.id.r_ingredient_quantity_whole);
        volumeButton = view.findViewById(R.id.r_ingredient_quantity_volume);
        weightButton = view.findViewById(R.id.r_ingredient_quantity_weight);

        whole = new String[]{ "Single", "Dozen", "Five Pack"};
        weight = new String[]{"Select Unit", "lb", "kg", "g"};
        volume = new String[]{"Select Unit", "L", "ml", "oz"};
        current = whole;
        unitCategory = "Whole";
        unitsAdapter = new ArrayAdapter<CharSequence>(getContext(), R.layout.spinner_layout, current);
        unitsAdapter.setDropDownViewResource(R.layout.spinner_dropdown);

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
                        unitsAdapter = new ArrayAdapter<CharSequence>(getContext(), R.layout.spinner_layout, current);
                        unitsAdapter.setDropDownViewResource(R.layout.spinner_dropdown);
                        quantityUnits.setAdapter(unitsAdapter);
                        unitCategory = "Whole";
                        break;
                    case R.id.r_ingredient_quantity_weight:
                        current = weight;
                        unitsAdapter = new ArrayAdapter<CharSequence>(getContext(), R.layout.spinner_layout, current);
                        unitsAdapter.setDropDownViewResource(R.layout.spinner_dropdown);
                        quantityUnits.setAdapter(unitsAdapter);
                        unitCategory = "Weight";
                        break;
                    case R.id.r_ingredient_quantity_volume:
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
     * Initializes the save button.
     */
    private void InitializeSaveButton() {
        Save = view.findViewById(R.id.add_r_ingredient);

        Save.setOnClickListener(new View.OnClickListener() {
            /**
             * Once the save button is clicked, get the user's inputs for all the different fields, and use it to create a
             * new Recipe Ingredient. Using Parcelable, the Recipe Ingredient gets passed back to the Recipe DialogFragment.
             *
             * @param view returns the current view of the activity
             */
            @Override
            public void onClick(View view) {
                Log.d("AHAHA", "This is my message");

                if (ValidData()) {
                    String name, category;

                    if (RecipeIngredientCategory.getVisibility() == View.VISIBLE){
                        if (!categoryData.containsValue(RecipeIngredientCategory.getText().toString())) {
                            categoryData.put(String.valueOf(categoryData.size()+1), RecipeIngredientCategory.getText().toString());
                            Firestore.storeToFirestore("Spinner","Category", categoryData);
                        }
                    }

                    name = ingredientName.getText().toString();
                    String recipeIngredientCategory = categorySpinner.getSelectedItem().toString();
                    String ingredientCategoryNew = RecipeIngredientCategory.getText().toString();
                    String recipeIngredientAmount = RecipeIngredientAmount.getText().toString();
                    String recipeIngredientUnit = quantityUnits.getSelectedItem().toString();
                    String recipeIngredientDescription = RecipeIngredientDescription.getText().toString();


                    if (categoryNewSelected) {
                        category = ingredientCategoryNew;
                    }
                    else {
                        category = recipeIngredientCategory;
                    }

                    getParentFragmentManager().beginTransaction().remove(fragment).commit();

                    RecipeIngredient thisIngredient = new RecipeIngredient(name, recipeIngredientDescription, recipeIngredientAmount, recipeIngredientUnit, category, unitCategory);

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
        Title = view.findViewById(R.id.r_ingredient_title);
        RecipeIngredientAmount = view.findViewById(R.id.r_ingredient_amount_text);
        RecipeIngredientDescription = view.findViewById(R.id.r_ingredient_description_text);
        RecipeIngredientCategory = view.findViewById(R.id.r_ingredient_category_text);
    }

    /**
     * Gets the name for the Recipe Ingredient the user gave for this current instacne of a Recipe Ingredient.
     *
     * @return Returns the name of the recipe ingredient that was created.
     */
    private String GetRecipeIngredientName() {
        return ingredientName.getText().toString();
    }

    /**
     * Checks if the user has filled in all required fields. If not, then it returns an error prompting the user to
     * fill in said fields.
     *
     * @return returns true if the data is valid. False otherwise.
     */
    private boolean ValidData(){

        String recipeIngredientNew = ingredientName.getText().toString();
        String recipeIngredientName = ingredientName.getText().toString();
        String recipeIngredientCategory = categorySpinner.getSelectedItem().toString();
        String ingredientCategoryNew = RecipeIngredientCategory.getText().toString();
        String recipeIngredientAmount = RecipeIngredientAmount.getText().toString();
        String recipeIngredientUnit = quantityUnits.getSelectedItem().toString();
        String recipeIngredientDescription = RecipeIngredientDescription.getText().toString();


        boolean isValid = true;

        if (Validate.isEmpty(recipeIngredientNew)  && nameNewSelected == true) {
            ingredientName.setError("Please give an ingredient name");
            isValid =  false;
        }

        if ((Validate.isEmpty(recipeIngredientCategory) || Objects.equals(recipeIngredientCategory, "Select Category")) && categoryNewSelected == false) {
            TextView errorText = (TextView) categorySpinner.getSelectedView();
            errorText.setError("");
            errorText.setTextColor(Color.RED);
            errorText.setText("Please select a category");
            isValid =  false;
        }

        // checks if ingredientName is empty
        if (Validate.isEmpty(recipeIngredientName)) {
            ingredientName.setError("Ingredient Name cant be empty");
            isValid = false;
        }

        if (savedIngredients.contains(ingredientName.getText().toString())) {
            ingredientName.setError("This ingredient already exists");
            isValid = false;
        }

        if (Validate.isEmpty(recipeIngredientAmount)) {
            RecipeIngredientAmount.setError("Please input amount");
            isValid =  false;
        }

        if (Validate.isEmpty(recipeIngredientUnit) || Objects.equals(recipeIngredientUnit, "Select Unit")) {
            TextView errorText = (TextView) quantityUnits.getSelectedView();
            errorText.setError("");
            errorText.setTextColor(Color.RED);
            errorText.setText("Select unit");
            isValid =  false;
        }

        if (Validate.isEmpty(recipeIngredientDescription)) {
            RecipeIngredientDescription.setError("Ingredient description cannot be empty.");
            isValid =  false;
        }

        if (Validate.isEmpty(ingredientCategoryNew) && categoryNewSelected == true) {
            RecipeIngredientCategory.setError("Please give a new category.");
            isValid =  false;
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



    private void GetIngredientNames() {

        CollectionReference ingredientReference = db.collection("Ingredients");
        ingredientReference.addSnapshotListener(new EventListener<QuerySnapshot>() {

            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable
                    FirebaseFirestoreException error) {
                for(QueryDocumentSnapshot doc: queryDocumentSnapshots) {
                    String ingredient = doc.getId();
                    if (!IngredientNames.contains(ingredient)) {
                        IngredientNames.add(ingredient);
                        Ingredients.put(ingredient, doc.getData());
                    }
                }
            }
        });

        CollectionReference recipeIngredientReference = db.collection("RecipeIngredients");
        recipeIngredientReference.addSnapshotListener(new EventListener<QuerySnapshot>() {

            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable
                    FirebaseFirestoreException error) {

                String name;
                for(QueryDocumentSnapshot doc: queryDocumentSnapshots) {
                    name = (String) doc.getData().get("Name");
                    if (!IngredientNames.contains(name)) {
                        Ingredients.put(name, doc.getData());
                        IngredientNames.add(name);
                    }
                }
                IngredientNames.removeAll(savedIngredients);
            }
        });

    }
}
