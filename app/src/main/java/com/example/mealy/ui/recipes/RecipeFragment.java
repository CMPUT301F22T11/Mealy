package com.example.mealy.ui.recipes;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.mealy.DisplayRecipeInfo;
import com.example.mealy.Ingredient;
import com.example.mealy.R;
import com.example.mealy.Recipe;
import com.example.mealy.RecipeList;
import com.example.mealy.comparators.recipes.CompareRecipes;
import com.example.mealy.databinding.FragmentNotificationsBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.time.Instant;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * This fragment represents the recipe screen.
 * Here, we have a ListView for our recipe and all its items,
 * as well as a few extra UI elements for sorting.
 */
// This is where I will temporarily put my RecipeList view
public class RecipeFragment extends Fragment {

    private FragmentNotificationsBinding binding;

    private Spinner sortSpinner; // for selecting sorting category
    private ListView recipeListView; // list of recipes
    private ConstraintLayout recipeEntryBox; // each individual recipe contained in a box
    private Button flipButton; // for flipping the recipe items

    DialogFragment recipeOptions;

    final String TAG = "Logging";

    int recipeIndex;
    int asc = 1; // for sort order

    @RequiresApi(api = Build.VERSION_CODES.O)
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState)
    {

        RecipeViewModel notificationsViewModel =
                new ViewModelProvider(this).get(RecipeViewModel.class);

        binding = FragmentNotificationsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        asc = 1;
        // have some flip button to change sort order

        // assign UI elements
        sortSpinner = root.findViewById(R.id.recipesort);
        recipeListView = root.findViewById(R.id.recipestorage);
        flipButton = root.findViewById(R.id.flip_recipe_sort);

        // create sorting spinner with sort categories
        ArrayList<String> options = new ArrayList<>(Arrays.asList("Title", "Prep Time", "Servings", "Category"));
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item, options);
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        sortSpinner.setAdapter(adapter);

        // list that will store all our recipe objects
        ArrayList<Recipe> recipeArrayList = new ArrayList<>();

        // Create the adapter and set it to the arraylist
        RecipeList recipeAdapter = new RecipeList(getActivity(), recipeArrayList);

        // set flip button on click
        flipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                asc *= -1;

                int selection = sortSpinner.getSelectedItemPosition();
                String selectedItem = sortSpinner.getItemAtPosition(selection).toString();
                CompareRecipes compare = new CompareRecipes(selectedItem, asc);

                Collections.sort(recipeArrayList, compare.returnComparator());
                recipeAdapter.notifyDataSetChanged();

            }
        });

        // add sample ingredient for recipe list (change later)
        Ingredient sample = new Ingredient("sample",
                "sample_text",
                "100",
                "kg",
                "protein",
                "fried",
                "pantry",
                "2025-12-13");

        List ingredientList = new ArrayList();
        ingredientList.add(sample);

        // PULL FROM FIREBASE
        // https://www.youtube.com/watch?v=xzCsJF9WtPU
        // get recipe table
        FirebaseFirestore dbf = FirebaseFirestore.getInstance();
        final CollectionReference recipeCollection = dbf.collection("Recipe");

        recipeCollection.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable
                    FirebaseFirestoreException error) {

                // Clear the old list
                recipeArrayList.clear();

                for(QueryDocumentSnapshot doc: queryDocumentSnapshots)
                {
                    // Log.d(TAG, String.valueOf(doc.getData().get("Province Name")));

                    try {

                        // fetch recipe info
                        // fetch rest of recipe info
                        String category = (String) doc.getData().get("Category");
                        String comments = (String) doc.getData().get("Comments");
                        String preptime = (String) doc.getData().get("Preparation Time");
                        String title = (String) doc.getData().get("Recipe Name");
                        String servings = (String) doc.getData().get("Servings");

                        int preptimeHours = Integer.parseInt(preptime);
                        int preptimeMins = 0;
                        int servingsString = Integer.parseInt(servings);

                        Recipe recipe = new Recipe(title, comments, servingsString, preptimeHours, preptimeMins, category,
                                ingredientList);

                        // fetch recipe image and store in bitmap
                        StorageReference mStorageReference;

                        // find the proper reference image
                        mStorageReference = FirebaseStorage.getInstance().getReference().child("Recipe_Image/" + recipe.getTitle());

                        try {
                            final File localFile = File.createTempFile("imageCache", "jpg"); // temp file to store image
                            mStorageReference.getFile(localFile)
                                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                            // successfully put stuff in file;
                                            System.out.println("Image received");
                                            Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                                            recipe.setBitmap(bitmap);
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            // did not put stuff in file
                                            System.out.println("No image detected!");
                                        }
                                    });

                        } catch (IOException e) {
                            System.out.println("Firebase failure!");
                            e.printStackTrace();
                        }

                        recipeArrayList.add(recipe); // Adding new recipe using Firebase data

                    } catch (Exception e) {
                        System.out.println("Error with firebase pull, incorrect formatting");
                    }
                }
                recipeAdapter.notifyDataSetChanged(); // Notifying the adapter to render any new data fetched from the cloud
            }
        });

        // notificationsViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        // create the instance of the ListView to set the recipe adapter
        ListView storage = root.findViewById(R.id.recipestorage);
        storage.setAdapter(recipeAdapter);

        // storage is the listview of our recipes
        // we can set it's on click method so when we click we associate recipes with it

        storage.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                DisplayRecipeInfo disp = new DisplayRecipeInfo(recipeAdapter.getItem(i));
                disp.show(getChildFragmentManager(), TAG);

            }
        });

        // set the spinner to sort things correctly
        sortSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedItem = sortSpinner.getItemAtPosition(i).toString();
                CompareRecipes compare = new CompareRecipes(selectedItem, asc);

                Collections.sort(recipeArrayList, compare.returnComparator());
                recipeAdapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}