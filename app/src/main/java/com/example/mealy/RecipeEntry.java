package com.example.mealy;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.mealy.functions.Firestore;

import java.util.HashMap;

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
        View view = inflater.inflate(R.layout.recipe_entry, container, false);

        // Initialize interfaces
        InitializeCategorySpinner();
        InitializeEditText();

        return view;
    }

    private void InitializeCategorySpinner() {
        CategorySpinner = view.findViewById(R.id.Recipe_Entry_CategoryDropdown);
        RecipeCategories = new String[]{"Select A Category", "Breakfast", "Lunch", "Dinner", "Other"};
        categoryAdapter = new ArrayAdapter<CharSequence>(getContext(), android.R.layout.simple_spinner_dropdown_item, RecipeCategories);
        CategorySpinner.setAdapter(categoryAdapter);
    }

    private void InitializeSaveButton() {
        Save = (Button) view.findViewById(R.id.Recipe_Entry_saveButton);

        Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // add data to firestore
            }
        });
    }

    private void InitializeEditText() {
        RecipeName = view.findViewById(R.id.Recipe_Entry_RecipeName);
        PrepTime = view.findViewById(R.id.Recipe_Entry_prepTime);
        Servings = view.findViewById(R.id.Recipe_Entry_Servings);
        Comments = view.findViewById(R.id.Recipe_Entry_Comments);
        AddPhoto = view.findViewById(R.id.Recipe_Entry_AddImage);
        AddIngredient = view.findViewById(R.id.Recipe_Entry_addIngredientToRecipe);
    }

}


