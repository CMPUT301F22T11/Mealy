package com.example.mealy;

import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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
        recipeImage.setImageResource(selectedRecipe.getImageID());

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