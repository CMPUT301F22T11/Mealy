package com.example.mealy.ui.recipes;

import static android.content.ContentValues.TAG;

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
import android.util.Log;
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
import com.example.mealy.functions.DeletableSpinnerArrayAdapter;
import com.example.mealy.functions.Firestore;
import com.example.mealy.functions.General;
import com.example.mealy.functions.Validate;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
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
    EditText NewCategory;
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
    ArrayList<String> RecipeCategories;
    DeletableSpinnerArrayAdapter categoryAdapter;
    ArrayAdapter<RecipeIngredient>recipeIngredientAdapter;
    ArrayList<RecipeIngredient> listOfIngredients = new ArrayList<>();
    Map<String, Object> categoryData;
    ListView ingredientList;
    ArrayList<RecipeIngredient> oldIngredients = new ArrayList<>();

    boolean ingredientClicked = false;
    int ingredientIndex;

    View view;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference collectionReference;

    String oldRecipName = "";
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


    private void onCreateFragment() {
        getParentFragmentManager().setFragmentResultListener("requestKey", this, new FragmentResultListener() {
            /**
             * Asks for an instance of recipe ingredient that was created, and returns it be adding it to the list of
             * recipe ingredients.
             * @param requestKey
             * @param bundle bundle that stores the data of the recipe ingredient
             */
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle bundle) {

                RecipeIngredient thisIngredient = bundle.getParcelable("RecipeIngredient");
                Log.wtf("test", "got here wtf");
                if (ingredientClicked == false) {
                    listOfIngredients.add(thisIngredient);
                    recipeIngredientAdapter.notifyDataSetChanged();
                    Log.wtf("test", listOfIngredients.toString());
//                    recipeIngredientAdapter.add(thisIngredient);
                }
                else {
                    listOfIngredients.set(ingredientIndex, thisIngredient);
                    recipeIngredientAdapter.notifyDataSetChanged();
                    Log.wtf("test2", listOfIngredients.toString());

                }
            }
        });
        Log.wtf("test", getParentFragmentManager().toString());
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
        readCategoryFirebase();

        // for image upload
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        onCreateFragment();

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
                    new AlertDialog.Builder(getContext(), R.style.MyDialogTheme)
                            .setMessage("What do you want to do with this ingredient?")
                            .setPositiveButton("Edit", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    ingredientIndex = i;
                                    ingredientClicked = false;
                                    if (edit) {
                                        new RecipeIngredientAdd(listOfIngredients.get(i), listOfIngredients).show(getParentFragmentManager(), "RecipeIngredient");
                                    }
                                    else {
                                        new RecipeIngredientAdd(listOfIngredients).show(getActivity().getSupportFragmentManager(), "RecipeIngredient");
                                    }
                                    ingredientClicked = true;

                                }
                            })
                            .setNegativeButton("Delete", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    listOfIngredients.remove(i);
                                    recipeIngredientAdapter.notifyDataSetChanged();

                                }
                            })
                            .show();
            }
        });
    }
    /**
     * This function initializes the category spinner in the recipe entry fragment
     */
    private void InitializeCategorySpinner() {
        CategorySpinner = view.findViewById(R.id.Recipe_Entry_CategoryDropdown);
        NewCategory = view.findViewById(R.id.newRecipeCategory);
        RecipeCategories = new ArrayList<String>();
        RecipeCategories.add("Select Category");
        RecipeCategories.add("Add Category");
        categoryAdapter = new DeletableSpinnerArrayAdapter(getContext(), RecipeCategories, "RecipeCategories");
        CategorySpinner.setAdapter(categoryAdapter);

        CategorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if (position == 1) {
                    NewCategory.setVisibility(View.VISIBLE);

                } else {
                    NewCategory.setVisibility(View.GONE);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                CategorySpinner.setSelection(0);
            }
        });
    }

    /**
     * This function initializes the save button in the recipe entry fragment
     */
    private void InitializeSaveButton() {
        Save = view.findViewById(R.id.Recipe_Entry_saveButton);

        Save.setOnClickListener(view -> {
            // add data to firestore
            // validate data
            if (ValidData()) {
                String RecipeName = GetRecipeName();
                HashMap<String, String> data = GetData();

                if (NewCategory.getVisibility() == View.VISIBLE){
                        if (categoryData == null) {
                            RecipeCategories.add(NewCategory.getText().toString());
                            categoryData = General.listToMap(RecipeCategories);
                        } else {
                            if (!categoryData.containsValue(NewCategory.getText().toString())) {
                                categoryData.put(String.valueOf(categoryData.size() + 1), NewCategory.getText().toString());
                            }
                        }
                        Firestore.storeToFirestore("Spinner","RecipeCategories", categoryData);
                }

                if (edit) {
                    getParentFragmentManager().beginTransaction().remove(fragment).commit();
//                    db.collection("Recipe").document(RecipeName).delete();
                    Firestore.deleteFromFirestore("Recipe", oldRecipName);
                    for (int i = 0; i < oldIngredients.size(); i++) {
                        String ingredientName = oldIngredients.get(i).getTitle();
                        String thisIngredientID = ingredientName + oldRecipName;
                        Firestore.deleteFromFirestore("RecipeIngredients", thisIngredientID);
                    }
                } else {
                    getParentFragmentManager().beginTransaction().remove(fragment).commit();

                }
                Firestore.storeToFirestore("Recipe", RecipeName, data);
                for (int i = 0; i < listOfIngredients.size(); i++) {
                    HashMap<String, String> thisData = GetDataIngredient(i);
                    String ingredientID = listOfIngredients.get(i).getTitle() + RecipeName;
                    Firestore.storeToFirestore("RecipeIngredients", ingredientID, thisData);
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
                new RecipeIngredientAdd(listOfIngredients).show(getParentFragmentManager(), "RecipeIngredient");
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

        String CategoryText;
        if (NewCategory.getVisibility() == View.VISIBLE) {
            CategoryText = NewCategory.getText().toString();
        } else {
            CategoryText = CategorySpinner.getSelectedItem().toString();
        }

        String RecipeNameText = RecipeName.getText().toString();
        String PrepTimeText = PrepTime.getText().toString();
        String PrepTimeMinText = PrepTimeMin.getText().toString();
        String ServingsText = Servings.getText().toString();
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
        String unitCategory = listOfIngredients.get(index).getUnitCategory();
        String recipeName = RecipeName.getText().toString();
        String recipIngredientId = name + recipeName;


        // Figure out how to attach image
        // Figure out how to attach ingredient

        thisData.put("ID", recipIngredientId);
        thisData.put("Name", name);
        thisData.put("Description", description);
        thisData.put("Amount", amount);
        thisData.put("Unit", unit);
        thisData.put("Category", category);
        thisData.put("Recipe Name", recipeName);
        thisData.put("Unit Category", unitCategory);

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
                    params.width = General.dpToPx(48, getContext());
                    params.height = General.dpToPx(48, getContext());
                    params.leftMargin = General.dpToPx(16, getContext());
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
        oldRecipName = recipe.getTitle();
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
                    String ingredient = (String) doc.getData().get("Name");
                    String category = (String) doc.getData().get("Category");
                    String amount = (String) doc.getData().get("Amount");
                    String desc = (String) doc.getData().get("Description");
                    String unit = (String) doc.getData().get("Unit");
                    String unitCategory = (String) doc.getData().get("Unit Category");
                    RecipeIngredient thisRepIn = new RecipeIngredient(ingredient, desc, amount, unit, category, unitCategory);
                    listOfIngredients.add(thisRepIn);
                    recipeIngredientAdapter.notifyDataSetChanged();
                    oldIngredients.add(thisRepIn);
                }

            }
        }
        });
    }

    public void readCategoryFirebase() {
        String CollectionName = "Spinner";
        String documentName = "RecipeCategories";
        DocumentReference docRef = db.collection(CollectionName).document(documentName);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        categoryData = document.getData();
                        Log.wtf("blah", categoryData.toString());
                        RecipeCategories = General.mapToArrayList(categoryData);
                        categoryAdapter = new DeletableSpinnerArrayAdapter(getContext(), RecipeCategories, documentName);
                        CategorySpinner.setAdapter(categoryAdapter);

                        if (edit && recipe!=null) CategorySpinner.setSelection(RecipeCategories.indexOf(recipe.getCategory()));

                        Log.d(TAG, "DocumentSnapshot data: " + categoryData);
                    } else {
                        Log.d(TAG, "No such document");
                        assert RecipeCategories != null;
                        Firestore.storeToFirestore("Spinner", documentName, General.listToMap(RecipeCategories));
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
                        RecipeCategories = General.mapToArrayList(categoryData);
                        categoryAdapter = new DeletableSpinnerArrayAdapter(getContext(), RecipeCategories, documentName);
                        CategorySpinner.setAdapter(categoryAdapter);
                    }
                }
            }
        });
    }
}


