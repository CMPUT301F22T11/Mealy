package com.example.mealy;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class RecipeClickFragment extends DialogFragment {

    TextView title;
    Button viewButton;
    Button editButton;
    Button deleteButton;

    DialogFragment recipeView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Bundle bundle = getArguments();
        Recipe selectedRecipe = (Recipe) bundle.getSerializable("Recipe");

        // set dialog to fragment layout defined in our xml file
        View currentView = inflater.inflate(R.layout.recipe_click_fragment, container, false);

        // change title of fragment
        title = currentView.findViewById(R.id.recipeClickNameText);
        title.setText(selectedRecipe.getTitle());

        // assign UI buttons here
        viewButton = currentView.findViewById(R.id.viewRecipeButton);
        editButton = currentView.findViewById(R.id.editRecipeButton);
        deleteButton = currentView.findViewById(R.id.deleteRecipeButton);

        viewButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // This should bring us to our view fragment
                recipeView = new RecipeViewFragment();
                recipeView.setArguments(bundle); // pass recipe info
                recipeView.show(getChildFragmentManager(), "test");
            }
        });

        editButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // This should bring us to our edit fragment
                System.out.println("Clicked edit button!");
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // This should just delete our entry from list
                System.out.println("Clicked delete button!");
            }
        });

        return currentView;
    }
}