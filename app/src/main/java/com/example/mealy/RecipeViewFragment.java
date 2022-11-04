package com.example.mealy;

import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * This fragment represents the recipe view screen.
 * This simply shows all the information of the clicked recipe
 * and occurs after clicking 'view' from the RecipeClickFragment
 */
public class RecipeViewFragment extends DialogFragment {

    TextView title;
    ImageView recipeImage;
    TextView category;
    TextView preptime;
    TextView servings;
    TextView comments;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Bundle bundle = getArguments();
        Recipe selectedRecipe = (Recipe) bundle.getSerializable("Recipe");

        // set dialog to fragment layout defined in our xml file
        View currentView = inflater.inflate(R.layout.recipe_view_fragment, container, false);

        // change title of fragment
        title = currentView.findViewById(R.id.recipeViewTitle);
        title.setText(selectedRecipe.getTitle());

        // display image
        recipeImage = currentView.findViewById(R.id.recipeViewImage);
        recipeImage.setImageBitmap(selectedRecipe.getBitmap());

        // set category
        category = currentView.findViewById(R.id.recipeViewCategory);
        category.setText("Category: " + selectedRecipe.getCategory());

        // set preptime
        preptime = currentView.findViewById(R.id.recipeViewPrepTime);
        String preptimeString = "Prep Time: " + selectedRecipe.getPreptimeHours() + " hrs : " + selectedRecipe.getPreptimeMins() + " mins";
        preptime.setText(preptimeString);

        // set servings
        servings = currentView.findViewById(R.id.recipeViewServes);
        servings.setText(selectedRecipe.getServingsString());

        // set comments
        comments = currentView.findViewById(R.id.recipeViewComments);
        comments.setText(selectedRecipe.getComments());

        return currentView;
    }
}