package com.example.mealy.ui.recipes;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentResultListener;

import com.example.mealy.MainActivity;
import com.example.mealy.R;
import com.example.mealy.functions.Firestore;
import com.example.mealy.functions.General;
import com.example.mealy.functions.Validate;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;

/**
 * This is the recipe entry class which creates a fragment for the user to enter
 * in a recipe to be added to the database.
 */
public class RecipeEntry extends DialogFragment {
    private final RecipeEntry fragment = this;
    EditText RecipeName;
    EditText PrepTime;
    EditText PrepTimeMin;
    EditText Servings;
    Spinner CategorySpinner;
    EditText Comments;
    ImageButton AddPhoto;
    ImageButton AddIngredient;
    Button Save;
    ImageView IVPreviewImage;

    private Uri filePath;
    int SELECT_PICTURE = 200;
    FirebaseStorage storage;
    StorageReference storageReference;

    Context applicationContext;

    String[] RecipeCategories;

    ArrayAdapter<CharSequence> categoryAdapter;

    ArrayAdapter<RecipeIngredient>recipeIngredientAdapter;

    ArrayList<RecipeIngredient> listOfIngredients = new ArrayList<>();

    ListView ingredientList;

    boolean ingredientClicked = false;

    int ingredientIndex;

    View view;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    CollectionReference collectionReference;

    Recipe recipe;
    boolean edit;

    /**
     * This is the constructor for Recipe Entry
     */
    public RecipeEntry(){
        // constructor
        edit = false;
    }

    /**
     * This is the constructor for Recipe Entry to edit
     * @param recipe
     */
    public RecipeEntry(Recipe recipe) {
        this.recipe = recipe;
        edit = true;
    }

    /**
     * This is the override for the oncreate
     * @param savedInstanceState
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getParentFragmentManager().setFragmentResultListener("requestKey", this, new FragmentResultListener() {
            /**
             * Asks for an instance of recipe ingredient that was created, and returns it be adding it to the list of
             * recipe ingredients.
             * @param requestKey
             * @param bundle bundle that stores the data of the recipe ingredient
             */
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle bundle) {

                RecipeIngredient thisIngredient = (RecipeIngredient)  bundle.getParcelable("RecipeIngredient");
                if (ingredientClicked == false) {
                    recipeIngredientAdapter.add(thisIngredient);
                }
                else {
                    listOfIngredients.set(ingredientIndex, thisIngredient);
                    recipeIngredientAdapter.notifyDataSetChanged();

                }
            }
        });
    }

    /**
     * This is the override for the onCreateView for Recipe Entry
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return returns the view
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.recipe_entry, container, false);

        applicationContext = MainActivity.getContextOfApplication();

        // Initialize interfaces
        InitializeCategorySpinner();
        InitializeEditText();
        InitializeAddPhoto();
        InitializeSaveButton();
        InitializeIngredientList();

        // for image upload
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        if (edit) {
            EditMode();
        }



        return view;
    }

    /**
     * This function initializes the recipe ingredient list that will store all the instance of the
     * recipe ingredient class for this recipe.
     */
    private void InitializeIngredientList() {
        recipeIngredientAdapter = new RecipeIngredientList(this.getActivity(), listOfIngredients);

        ingredientList = view.findViewById(R.id.ingredient_list);
        ingredientList.setAdapter(recipeIngredientAdapter);

        ingredientList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            /**
             * This function is called when the user wants to perform an action on the recipe ingredient. When the user selects an entry,
             * an Alert Dialog pops up. Then, the user can either choose to edit the ingredient, or remove it.
             * @param adapterView The adapterView where the click occurred
             * @param view The current view of the app
             * @param i Returns the index of the recipe ingredient that was selected
             * @param l THe row id of the recipe ingredient that was clicked
             */
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    new AlertDialog.Builder(getContext())
                            .setTitle("Recipe Ingredient")
                            .setMessage("Select an action")
                            .setPositiveButton("Edit", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    ingredientIndex = i;
                                    new RecipeIngredientAdd().show(getActivity().getSupportFragmentManager(), "RecipeIngredient");
                                    ingredientClicked = true;

                                }
                            })
                            .setNegativeButton("Delete", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    listOfIngredients.remove(i);
                                    recipeIngredientAdapter.notifyDataSetChanged();

                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
            }
        });
    }
    /**
     * This function initializes the category spinner in the recipe entry fragment
     */
    private void InitializeCategorySpinner() {
        CategorySpinner = view.findViewById(R.id.Recipe_Entry_CategoryDropdown);
        RecipeCategories = new String[]{"Select A Category", "Breakfast", "Lunch", "Dinner", "Other"};
        categoryAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, RecipeCategories);
        CategorySpinner.setAdapter(categoryAdapter);
    }

    /**
     * This function initializes the save button in the recipe entry fragment
     */
    private void InitializeSaveButton() {
        Save = (Button) view.findViewById(R.id.Recipe_Entry_saveButton);

        Save.setOnClickListener(view -> {
            // add data to firestore
            // validate data
            if (ValidData()) {
                String RecipeName = GetRecipeName();
                HashMap<String, String> data = GetData();

                if (edit) {
                    getParentFragmentManager().beginTransaction().remove(fragment).commit();
                    Firestore.deleteFromFirestore("Recipe", RecipeName);
                } else {
                    requireActivity().getSupportFragmentManager().beginTransaction().remove(fragment).commit();

                }
                Firestore.storeToFirestore("Recipe", RecipeName, data);
                for (int i = 0; i < listOfIngredients.size(); i++) {
                    HashMap<String, String> thisData = GetDataIngredient(i);
                    String ingredientName = listOfIngredients.get(i).getTitle();
                    Firestore.storeToFirestore("RecipeIngredients", ingredientName, thisData);
                }
                uploadImage();
            }

        });
    }

    /**
     * This function initializes all the edit texts in the recipe entry fragment
     */
    private void InitializeEditText() {
        RecipeName = view.findViewById(R.id.Recipe_Entry_RecipeName);
        PrepTime = view.findViewById(R.id.Recipe_Entry_prepTimeHour);
        PrepTimeMin = view.findViewById(R.id.Recipe_Entry_prepTimeMin);
        Servings = view.findViewById(R.id.Recipe_Entry_Servings);
        Comments = view.findViewById(R.id.Recipe_Entry_Comments);
        AddIngredient = view.findViewById(R.id.Recipe_Entry_addIngredientToRecipe);
        IVPreviewImage = view.findViewById(R.id.IVPreviewImage);

        AddIngredient.setOnClickListener(new View.OnClickListener() {

            /**
             * This function initiates the RecipeIngredient fragment to be used to create an
             * instance of a recipe ingredient class.
             * @param view give the current view of the app
             */
            @Override
            public void onClick(View view) {

                ingredientClicked = false;
                new RecipeIngredientAdd().show(getActivity().getSupportFragmentManager(), "test");

            }
        });
    }

    /**
     * THis function initializes the add photo button in the recipe entry fragment
     */
    private void InitializeAddPhoto() {
        AddPhoto = view.findViewById(R.id.Recipe_Entry_AddImage);
        AddPhoto.setOnClickListener(view -> imageChooser());
    }


    /**
     * This function builds a hash map from the data that is pulled from the entry boxes
     * @return data as a hash map to be uploaded on firebase
     */
    private HashMap<String, String> GetData() {

        HashMap<String, String> data = new HashMap<>();

        String RecipeNameText = RecipeName.getText().toString();
        String PrepTimeText = PrepTime.getText().toString();
        String PrepTimeMinText = PrepTimeMin.getText().toString();
        String ServingsText = Servings.getText().toString();
        String CategoryText = CategorySpinner.getSelectedItem().toString();
        String CommentsText = Comments.getText().toString();

        // Figure out how to attach image
        // Figure out how to attach ingredient

        data.put("Recipe Name", RecipeNameText);
        data.put("Preparation Time", PrepTimeText);
        data.put("Preparation Time Min", PrepTimeMinText);
        data.put("Servings", ServingsText);
        data.put("Category", CategoryText);
        data.put("Comments", CommentsText);

        return data;
    }

    /**
     * This function builds a hashmap of an instance of the recipe ingredient created within the
     * list of recipe ingredients, specified by the index.
     * @param index Get the index of the ingredient in the list.
     * @return Returns the recipe ingredient, with its attributes, as a hashmap. Used to upload to the firebase.
     */
    private HashMap<String, String> GetDataIngredient(int index) {

        HashMap<String, String> thisData = new HashMap<>();

        String name = listOfIngredients.get(index).getTitle();
        String description = listOfIngredients.get(index).getDescription();
        String amount = listOfIngredients.get(index).getAmount();
        String unit = listOfIngredients.get(index).getUnit();
        String category = listOfIngredients.get(index).getCategory();
        String recipeName = RecipeName.getText().toString();

        // Figure out how to attach image
        // Figure out how to attach ingredient

        thisData.put("Name", name);
        thisData.put("Description", description);
        thisData.put("Amount", amount);
        thisData.put("Unit", unit);
        thisData.put("Category", category);
        thisData.put("Recipe Name", recipeName);

        return thisData;
    }

    /**
     * This funciton returns the recipe name from the recipe name box
     * @return
     */
    private String GetRecipeName() {
        return RecipeName.getText().toString();
    }

    /**
     * This funciton allows the user to choose a photo from their gallery
     */
    //https://www.geeksforgeeks.org/how-to-select-an-image-from-gallery-in-android/
    void imageChooser() {

        // create an instance of the
        // intent of the type image
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);

        // pass the constant to compare it
        // with the returned requestCode
        startActivityForResult(Intent.createChooser(i, "Select Picture"), SELECT_PICTURE);
    }

    /**
     * This function converts the image to make it uploadable to the firebase storage
     * @param requestCode make sure that the image wants to be uploaded
     * @param resultCode if the image was successfully uploaded
     * @param data bit data of time image
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {

            // compare the resultCode with the
            // SELECT_PICTURE constant
            if (requestCode == SELECT_PICTURE) {
                // Get the url of the image from data
                Uri selectedImageUri = data.getData();
                if (null != selectedImageUri) {
                    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) IVPreviewImage.getLayoutParams();
                    params.width = General.dpToPx(200, getContext());
                    params.height = General.dpToPx(200, getContext());
                    IVPreviewImage.setLayoutParams(params);
                    // update the preview image in the layout
                    IVPreviewImage.setImageURI(selectedImageUri);

                    // for uploading image
                    filePath = data.getData();
                    try {

                        // Setting image on image view using Bitmap
                        Bitmap bitmap = MediaStore
                                .Images
                                .Media
                                .getBitmap(
                                        applicationContext.getContentResolver(),
                                        filePath);
                        IVPreviewImage.setImageBitmap(bitmap);
                    }

                    catch (IOException e) {
                        // Log the exception
                        e.printStackTrace();
                    }

                }
            }
        }
    }

    /**
     * This function uploads the image up to the firebase storage
     */
    private void uploadImage()
    {
        if (filePath != null) {

//            // Code for showing progressDialog while uploading
//            ProgressDialog progressDialog
//                    = new ProgressDialog(applicationContext);
//            progressDialog.setTitle("Uploading...");
//            progressDialog.show();

            // Defining the child of storageReference
            StorageReference ref
                    = storageReference
                    .child(
                            "Recipe_Image/"
                                    + GetRecipeName());

            // adding listeners on upload
            // or failure of image
            ref.putFile(filePath)
                    .addOnSuccessListener(
                            taskSnapshot -> {

                                // Image uploaded successfully
                                // Dismiss dialog
//                                progressDialog.dismiss();
                                Toast
                                        .makeText(applicationContext,
                                                "Image Uploaded!!",
                                                Toast.LENGTH_SHORT)
                                        .show();
                            })

                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e)
                        {

                            // Error, Image not uploaded
//                            progressDialog.dismiss();
                            Toast
                                    .makeText(applicationContext,
                                            "Failed " + e.getMessage(),
                                            Toast.LENGTH_SHORT)
                                    .show();
                        }
                    });
        }
    }

    /**
     * Checks if a user inputted the data properly
     *
     * @return true if all data is inputted properly
     */
    private boolean ValidData() {

        String recipeName = RecipeName.getText().toString();
        String prepTime = PrepTime.getText().toString();
        String prepTimeMin = PrepTimeMin.getText().toString();
        String servingSize = Servings.getText().toString();
        String categoryType = CategorySpinner.getSelectedItem().toString();
        String comments = Comments.getText().toString();
        // validate image

        boolean isValid = true;

        if (Validate.isEmpty(recipeName)) {
            RecipeName.setError("Can't be empty");
            isValid = false;
        }

        if (Validate.isEmpty(categoryType) || Objects.equals(categoryType, "Select Category")) {
            TextView errorText = (TextView) CategorySpinner.getSelectedView();
            errorText.setError("");
            errorText.setTextColor(Color.RED);
            errorText.setText("Select Category");
            isValid = false;
        }

        if (Validate.isEmpty(prepTime)) {
            PrepTime.setError("Can't be empty");
            isValid = false;
        }

        if (Validate.isEmpty(prepTimeMin)) {
            PrepTime.setError("Can't be empty");
            isValid = false;
        }

        try {
            if (Float.parseFloat(servingSize) <= 0) {
                Servings.setError("Can't have 0 or negative quantities");
                isValid = false;
            }
        } catch (Exception e) {
            Servings.setError("Invalid Number");
            isValid = false;
        }

        if (Validate.isEmpty(comments)) {
            Comments.setError("Please write a comment");
            isValid = false;
        }

        if (listOfIngredients.size() == 0) {
            Toast errorToast;
            errorToast = Toast.makeText(this.getActivity(), "Error, please add an ingredient to the recipe", Toast.LENGTH_SHORT);
            errorToast.show();
            isValid = false;
        }

        return isValid;

    }

    /**
     * Sets default values to ingredient values that we need to edit
     */
    private void EditMode() {
        RecipeName.setText(recipe.getTitle());
        PrepTime.setText(Integer.toString(recipe.getPreptimeHours()));
        PrepTimeMin.setText(Integer.toString(recipe.getPreptimeMins()));
        Servings.setText(Integer.toString(recipe.getServings()));
        Comments.setText(recipe.getComments());
        IVPreviewImage.setImageBitmap(recipe.getBitmap());

        // sets spinners to their appropriate value. Goes to default value if item is not in spinner
        CategorySpinner.setSelection(Arrays.asList(RecipeCategories).indexOf(recipe.getCategory()));
        collectionReference = db.collection("RecipeIngredients");
        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {

            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable
                    FirebaseFirestoreException error) {

                for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                    String recipeName = (String) doc.getData().get("Recipe Name");
                    if (recipeName.equals(recipe.getTitle())) {
                        String ingredient = (String) doc.getId();
                        String category = (String) doc.getData().get("Category");
                        String amount = (String) doc.getData().get("Amount");
                        String desc = (String) doc.getData().get("Description");
                        String unit = (String) doc.getData().get("Unit");
                        String unitCategory = (String) doc.getData().get("Unit Category");
                        RecipeIngredient thisRepIn = new RecipeIngredient(ingredient, desc, amount, unit, category, unitCategory);
                        recipeIngredientAdapter.add(thisRepIn);

                    }

                }
            }
        });
    }
}


