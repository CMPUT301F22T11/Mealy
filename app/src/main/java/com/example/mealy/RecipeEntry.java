package com.example.mealy;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.mealy.functions.Firestore;
import com.example.mealy.functions.General;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

public class RecipeEntry extends DialogFragment {
    private final RecipeEntry fragment = this;
    EditText RecipeName;
    EditText PrepTime;
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

    public RecipeEntry(){
        // constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

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

        return view;
    }

    private void InitializeCategorySpinner() {
        CategorySpinner = view.findViewById(R.id.Recipe_Entry_CategoryDropdown);
        RecipeCategories = new String[]{"Select A Category", "Breakfast", "Lunch", "Dinner", "Other"};
        categoryAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, RecipeCategories);
        CategorySpinner.setAdapter(categoryAdapter);
    }

    private void InitializeSaveButton() {
        Save = (Button) view.findViewById(R.id.Recipe_Entry_saveButton);

        Save.setOnClickListener(view -> {
            // add data to firestore
            // validate data
            String RecipeName = GetRecipeName();
            HashMap<String, String> data = GetData();
            requireActivity().getSupportFragmentManager().beginTransaction().remove(fragment).commit();
            Firestore.StoreToFirestore("Recipe", RecipeName, data);
            uploadImage();

        });
    }

    private void InitializeEditText() {
        RecipeName = view.findViewById(R.id.Recipe_Entry_RecipeName);
        PrepTime = view.findViewById(R.id.Recipe_Entry_prepTime);
        Servings = view.findViewById(R.id.Recipe_Entry_Servings);
        Comments = view.findViewById(R.id.Recipe_Entry_Comments);
        AddIngredient = view.findViewById(R.id.Recipe_Entry_addIngredientToRecipe);
        IVPreviewImage = view.findViewById(R.id.IVPreviewImage);
    }

    private void InitializeAddPhoto() {
        AddPhoto = view.findViewById(R.id.Recipe_Entry_AddImage);
        AddPhoto.setOnClickListener(view -> imageChooser());
    }



    private HashMap<String, String> GetData() {

        HashMap<String, String> data = new HashMap<>();

        String RecipeNameText = RecipeName.getText().toString();
        String PrepTimeText = PrepTime.getText().toString();
        String ServingsText = Servings.getText().toString();
        String CategoryText = CategorySpinner.getSelectedItem().toString();
        String CommentsText = Comments.getText().toString();

        // Figure out how to attach image
        // Figure out how to attach ingredient

        data.put("Recipe Name", RecipeNameText);
        data.put("Preparation Time", PrepTimeText);
        data.put("Servings", ServingsText);
        data.put("Category", CategoryText);
        data.put("Comments", CommentsText);

        return data;
    }

    private String GetRecipeName() {
        return RecipeName.getText().toString();
    }

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
}


