package com.example.mealy;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.mealy.functions.DeletePrompt;

public class DisplayRecipeInfo extends DialogFragment {

    private final String RecipeName;
    private final String PrepTime;
    private final String Servings;
    private final String Category;
    private final String Comments;
    private final int recipeImage;
    private final Recipe recipe;

    TextView view_title;
    ImageView view_recipeImage;
    TextView view_category;
    TextView view_preptime;
    TextView view_servings;
    TextView view_comments;

    View view;
    private DisplayRecipeInfo fragment = this;

    public DisplayRecipeInfo(Recipe recipe) {
        this.recipe = recipe;
        RecipeName = recipe.getTitle();
        PrepTime = "Prep Time: " + recipe.getPreptimeHours() + " hrs : " + recipe.getPreptimeMins() + " mins";
        Servings = recipe.getServingsString();
        Category = recipe.getCategory();
        Comments = recipe.getComments();
        recipeImage = recipe.getImageID();

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
                }).create();
//                .setPositiveButton("Edit", new DialogInterface.OnClickListener() { // lets you edit the food item
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        new RecipeEntry(recipe).show(getParentFragmentManager(),"food_entry");
//                    }
//                }).create();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        // display image
        view_recipeImage = view.findViewById(R.id.recipeViewImage);
        view_recipeImage.setImageResource(recipeImage);

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

        return view;
    }
}
