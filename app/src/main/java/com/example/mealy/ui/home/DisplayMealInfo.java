package com.example.mealy.ui.home;

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
import com.example.mealy.ui.ingredientStorage.Ingredient;
import com.example.mealy.ui.recipes.DisplayRecipeInfo;
import com.example.mealy.ui.recipes.Recipe;
import com.example.mealy.ui.recipes.RecipeEntry;

import java.util.List;

public class DisplayMealInfo extends DialogFragment {

    private final Meal meal;

    private final String mealName;
    private final String servingsString;
    private final String startDate;
    private final String endDate;
    private final Integer servings;

    private final List<Recipe> mealRecipes;
    private final List<Ingredient> mealIngredients;

    TextView view_date;
    TextView view_servings;

    View view;
    private final DisplayMealInfo fragment = this;

    public DisplayMealInfo(Meal meal) {
        this.meal = meal;
        this.mealName = meal.getTitle();
        this.startDate = meal.getStartDate();
        this.endDate = meal.getEndDate();
        this.servingsString = meal.getServingsString();
        this.servings = meal.getServings();
        this.mealRecipes = meal.getMealRecipes();
        this.mealIngredients = meal.getMealIngredients();

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        view = LayoutInflater.from(getActivity()).inflate(R.layout.display_meal_info, null);

        // Creates a dialog builder thing that lets you display information and click buttons and stuff
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        return builder
                .setView(view)
                .setTitle(mealName)
                .setNegativeButton("Close", null) // closes the dialog
                .setNeutralButton("Delete", new DialogInterface.OnClickListener() { // deletes the food item
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        new DeletePrompt("Meal", mealName).show(getParentFragmentManager(),"delete_prompt");
                    }
                }).create();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        // set date
        view_date = view.findViewById(R.id.mealDate);
        view_date.setText(startDate + " to " + endDate);

        // set servings
        view_servings = view.findViewById(R.id.mealServings);
        view_servings.setText(servings.toString());

        return view;
    }

}
