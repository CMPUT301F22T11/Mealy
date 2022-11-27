package com.example.mealy.ui.home;
import android.annotation.SuppressLint;
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
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import com.example.mealy.R;
import com.example.mealy.functions.Validate;
import com.example.mealy.ui.ingredientStorage.Ingredient;
import com.example.mealy.ui.recipes.Recipe;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


/**
 * This class contains the DialogFragment that allows the user to create/edit a RecipeIngredient entry.
 * After hitting the "Save" button, the fragment passes the created (or edited) Recipe Ingredient
 * object back to the RecipeEntry fragment, and ends.
 */
public class IRAdd extends DialogFragment {
    private final IRAdd fragment = this;
    Spinner IRSpinner;
    ArrayAdapter<String> IRAdapter;



    Button Save;
    EditText RecipeIngredientName;

    ArrayList<String> IngredientRecipeList = new ArrayList<String>();

    Map<String, String> IRMap = new HashMap<>();

    ArrayList<Ingredient> listIngredient = new ArrayList<Ingredient>();

    ArrayList<Recipe> listRecipe = new ArrayList<Recipe>();

    FirebaseFirestore db = FirebaseFirestore.getInstance();


    CollectionReference collectionReference;



    private static final String TAG = "DocSnippets";

    boolean categoryNewSelected = false, nameNewSelected = false, edit = false;

    View view;

    public IRAdd() {
        edit = false;
        // Constructor: TODO
    }

//    public IRAdd(RecipeIngredient i) {
//        edit = true;
//        // Constructor: TODO
//    }

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
        view = inflater.inflate(R.layout.add_ir, container, false);
        // Initializes Interface
        InitializeIRSpinner();
        InitializeGetAll();
        InitializeSaveButton();



        return view;
    }

    /**
     * Initialize an addSnapshotListener that retrieves all current entries of Ingredients, alongside the category in which
     * each ingredient is in, from the Firebase.
     */
    private void InitializeGetAll() {
        collectionReference = db.collection("Ingredients");
        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
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
                    String category = (String) doc.getData().get("Category");
                    String desc = (String) doc.getData().get("Description");
                    String amount = (String) doc.getData().get("Quantity");
                    String unitCategory = (String) doc.getData().get("Unit Category");
                    String unit = (String) doc.getData().get("Quantity Unit");
                    String location = (String) doc.getData().get("Location");
                    String exp = (String) doc.getData().get("Expiry Date");
                    IngredientRecipeList.add(ingredient);
                    IRMap.put(ingredient, "I");
                    Ingredient newIn = new Ingredient(ingredient, desc, amount, unit, unitCategory, category, location, exp);
                    listIngredient.add(newIn);
                }
                IRAdapter.notifyDataSetChanged();
            }
        });

        collectionReference = db.collection("Recipe");
        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
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
                    String recipe = (String) doc.getId();
                    String comments = (String) doc.getData().get("Comments");
                    String category = (String) doc.getData().get("Category");
                    String prepTime = (String) doc.getData().get("Preparation Time");
                    String prepTimeMin = (String) doc.getData().get("Preparation Time Min");
                    String servings = (String) doc.getData().get("Servings");
                    IngredientRecipeList.add(recipe);
                    IRMap.put(recipe, "R");
                    List thisList = new ArrayList<>();
                    Recipe recip = new Recipe(recipe, comments, Integer.parseInt(servings), Integer.parseInt(prepTime), Integer.parseInt(prepTimeMin), category, thisList);
                    listRecipe.add(recip);
                }
                IRAdapter.notifyDataSetChanged();
            }
        });
    }

    /**
     * Initializes the spinner for selecting the name
     */
    private void InitializeIRSpinner() {
        IRSpinner = (Spinner) view.findViewById(R.id.ir_dropdown);
        IRAdapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_layout, IngredientRecipeList);
        IRAdapter.setDropDownViewResource(R.layout.spinner_dropdown);
        IRSpinner.setAdapter(IRAdapter);

    }


    /**
     * Initializes the save button.
     */
    private void InitializeSaveButton() {
        Save = (Button) view.findViewById(R.id.add_ir);

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

                    String IRName = IRSpinner.getSelectedItem().toString();
                    getParentFragmentManager().beginTransaction().remove(fragment).commit();


                    for (Map.Entry<String, String> entry : IRMap.entrySet()) {
                        if (entry.getKey() == IRName && entry.getValue() == "I") {


                            for (Ingredient x : listIngredient) {
                                if (x.getName().equals(IRName)) {
                                    Ingredient putIng = x;
                                    Bundle bundle = new Bundle();
                                    bundle.putParcelable("IR", putIng);
                                    getParentFragmentManager().setFragmentResult("requestKey", bundle);
                                }
                            }

                        }
                        else if (entry.getKey() == IRName && entry.getValue() == "R") {

                            for (Recipe x : listRecipe) {
                                if (x.getTitle().equals(IRName)) {
                                    Bundle bundle = new Bundle();
                                    Recipe putRec = x;
                                    bundle.putParcelable("IR", putRec);
                                    getParentFragmentManager().setFragmentResult("requestKey", bundle);
                                }
                            }
                        }
                        // Do things with the list
                    }
                }


            }
        });
    }



    /**
     * Gets the name for the Recipe Ingredient the user gave for this current instacne of a Recipe Ingredient.
     *
     * @return Returns the name of the recipe ingredient that was created.
     */
    private String GetRecipeIngredientName() {
        return RecipeIngredientName.getText().toString();
    }

    /**
     * Checks if the user has filled in all required fields. If not, then it returns an error prompting the user to
     * fill in said fields.
     *
     * @return returns true if the data is valid. False otherwise.
     */
    private boolean ValidData(){

        String IRName = IRSpinner.getSelectedItem().toString();

        boolean isValid = true;

        if (Validate.isEmpty(IRName)) {
            RecipeIngredientName.setError("Please select recipe/ingredient");
            isValid =  false;
        }

        return isValid;

    }
}