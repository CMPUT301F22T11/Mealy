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
import com.example.mealy.functions.DateFunc;
import com.example.mealy.functions.DeletePrompt;
import com.example.mealy.ui.ingredientStorage.Ingredient;
import com.example.mealy.ui.recipes.DisplayRecipeInfo;
import com.example.mealy.ui.recipes.Recipe;
import com.example.mealy.ui.recipes.RecipeEntry;

import java.util.List;

/**
 * A fragment that takes in an meal object and displays its info
 */
public class DisplayMealInfo extends DialogFragment {

    private final Meal meal;

    private  String mealName;
    private  String servingsString;
    private  String startDate;
    private  String endDate;
    private  Integer servings;

    private List<Recipe> mealRecipes;
    private  List<Ingredient> mealIngredients;

    TextView view_date;
    TextView view_servings;
    TextView recipeDisplay;
    TextView ingredientDisplay;
    TextView meal_title;

    View view;
    private  DisplayMealInfo fragment = this;

    /**
     * Takes in the meal info and stores it into the class
     * @param meal The ingredient that you want to display the info of
     */
    public DisplayMealInfo(Meal meal) {
        this.meal = meal;
        this.mealName = meal.getTitle();
        this.startDate = meal.getStartDate();
        this.endDate = meal.getEndDate();
        this.mealRecipes = meal.getMealRecipes();
        this.mealIngredients = meal.getMealIngredients();
    }

    /**
     * Displays the meal name and gives the option to delete the meal plan or close
     * the dialog box.
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        view = LayoutInflater.from(getActivity()).inflate(R.layout.display_meal_info, null);

        // Creates a dialog builder thing that lets you display information and click buttons and stuff
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.MyDialogTheme);
        return builder
                .setView(view)
                .setNegativeButton("Close", null) // closes the dialog
                .setNeutralButton("Delete", new DialogInterface.OnClickListener() { // deletes the food item
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        new DeletePrompt("MealPlan", mealName).show(getParentFragmentManager(),"delete_prompt");
                    }
                }).create();
    }

    /**
     * Displays all the info of meal. This includes the title, date range,
     * the ingredients list and the recipe list
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        // set date
        view_date = view.findViewById(R.id.mealDate);
        view_date.setText(DateFunc.makeDateString(startDate) + " to " + DateFunc.makeDateString(endDate));
        meal_title = view.findViewById(R.id.meal_title);
        meal_title.setText(mealName);
        recipeDisplay = view.findViewById(R.id.mealRecipeListDisplay);
        ingredientDisplay = view.findViewById(R.id.mealIngredientListDisplay);
        String recipes = "";
        String ingredients = "";

        for (Recipe x : mealRecipes) {

            System.out.println("Iterating over a recipe in this meal");
            System.out.println("Recipe title: " + x.getTitle());
            recipes += (x.getTitle() + ", Servings: " + x.getServings());
        }

        for (Ingredient x : mealIngredients) {
            ingredients += (x.getName());
        }

        recipeDisplay.setText(recipes);
        ingredientDisplay.setText(ingredients);


        // set servings
//        view_servings = view.findViewById(R.id.mealServings);
//        view_servings.setText(servings.toString());

        return view;
    }

}
