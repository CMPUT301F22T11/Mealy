package com.example.mealy;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.mealy.functions.Firestore;
import com.example.mealy.functions.General;
import com.example.mealy.functions.Validate;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
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


    View view;

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

        // for image upload
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        if (edit) {
            EditMode();
        }

        return view;
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
                    Firestore.DeleteFromFirestore("Recipe", RecipeName);
                } else {
                    requireActivity().getSupportFragmentManager().beginTransaction().remove(fragment).commit();
                }
                Firestore.StoreToFirestore("Recipe", RecipeName, data);
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
    }
}


