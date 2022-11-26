package com.example.mealy.ui.recipes;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.mealy.R;
import com.example.mealy.functions.DeletePrompt;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class DisplayRecipeInfo extends DialogFragment {

    private final String RecipeName;
    private final String PrepTime;
    private final String Servings;
    private final String Category;
    private final String Comments;
    private final Recipe recipe;

    private boolean hasImage; // does this selected recipe have an image associated?
    private Bitmap bitmap; // bitmap of recipe if exists

    TextView view_title;
    ImageView view_recipeImage;
    TextView view_category;
    TextView view_preptime;
    TextView view_servings;
    TextView view_comments;
    TextView view_ingredients;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    CollectionReference collectionReference;

    String ingList = "ingredients:";

    View view;
    private DisplayRecipeInfo fragment = this;

    public DisplayRecipeInfo(Recipe recipe) {
        this.recipe = recipe;
        RecipeName = recipe.getTitle();
        PrepTime = "Prep Time: " + recipe.getPreptimeHours() + " hrs : " + recipe.getPreptimeMins() + " mins";
        Log.d("preptimeh:", Integer.toString(recipe.getPreptimeHours()));
        Log.d("preptimem:", Integer.toString(recipe.getPreptimeMins()));
        Servings = recipe.getServingsString();
        Category = recipe.getCategory();
        Comments = recipe.getComments();

        this.hasImage = recipe.hasImage();

        // get the recipe image if it exists, otherwise do a placeholder
        if (recipe.hasImage()) {
            this.bitmap = recipe.getBitmap();
        }

    }


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        view = LayoutInflater.from(getActivity()).inflate(R.layout.display_recipe_info, null);

        // Creates a dialog builder thing that lets you display information and click buttons and stuff
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        return builder
                .setView(view)
                .setTitle(RecipeName)
                .setNegativeButton("Close", null) // closes the dialog
                .setNeutralButton("Delete", new DialogInterface.OnClickListener() { // deletes the food item
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        new DeletePrompt("Recipe", RecipeName).show(getParentFragmentManager(),"delete_prompt");
                    }
                })
                .setPositiveButton("Edit", new DialogInterface.OnClickListener() { // lets you edit the food item
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        new RecipeEntry(recipe).show(getParentFragmentManager(),"food_entry");
                    }
                }).create();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        // display image if exists
        view_recipeImage = view.findViewById(R.id.recipeViewImage);

        if (this.hasImage) {
            view_recipeImage.setImageBitmap(bitmap);
        } else {
            // placeholder image
            view_recipeImage.setImageResource(R.drawable.placeholder);
        }

        // set category
        view_category = view.findViewById(R.id.recipeViewCategory);
        view_category.setText("Category: " + Category);

        // set preptime
        view_preptime = view.findViewById(R.id.recipeViewPrepTime);
        view_preptime.setText("Preparation Time: " + PrepTime);

        // set servings
        view_servings = view.findViewById(R.id.recipeViewServes);
        view_servings.setText(Servings + " people.");

        // set comments
        view_comments = view.findViewById(R.id.recipeViewComments);
        view_comments.setText("Comments: " + Comments);

        view_ingredients = view.findViewById(R.id.IngredientsText);
        InitializeGetAll();

        return view;
    }

    private void InitializeGetAll() {
        collectionReference = db.collection("RecipeIngredients");
        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {

            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable
                    FirebaseFirestoreException error) {

                for(QueryDocumentSnapshot doc: queryDocumentSnapshots) {
                    String rec = (String) doc.getData().get("Recipe Name");

                    if (rec.equals(recipe.getTitle())) {
                        String ingredient = (String) doc.getId();
                        ingList += "\n";
                        ingList += ingredient;
                        view_ingredients.setText(ingList);
                    }

                }

            }
        });
    }
}
