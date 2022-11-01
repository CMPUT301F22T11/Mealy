package com.example.mealy;

import android.app.Dialog;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * This is just to test your fragment. It's not a proper fragment
 */

public class RecipeClickFragment extends DialogFragment {

    Button viewButton;
    Button editButton;
    Button deleteButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // set dialog to fragment layout defined in our xml file
        View currentView = inflater.inflate(R.layout.recipe_click_fragment, container, false);

        // assign UI buttons here
        viewButton = currentView.findViewById(R.id.viewRecipeButton);
        editButton = currentView.findViewById(R.id.editRecipeButton);
        deleteButton = currentView.findViewById(R.id.deleteRecipeButton);

        viewButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // This should bring us to our view fragment
                System.out.println("Clicked view button!");
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