package com.example.mealy.ui.home;

import android.graphics.Bitmap;

import com.example.mealy.ui.ingredientStorage.Ingredient;
import com.example.mealy.ui.recipes.Recipe;

import java.io.Serializable;
import java.util.List;

public class Meal implements Serializable {
    String title;

    private List<Ingredient> mealIngredients;
    private List<Recipe> mealRecipes;

    /**
     * Get the title of the meal
     * @return: title of meal
     */
    public String getTitle() {
        return title;
    }

    /**
     * Set the title of the meal
     * @param: title of meal
     */
    public void setTitle(String name) {
        this.title = name;
    }

    /**
     * Get the ingredients list of this meal
     * @return: ingredients list of this meal
     */
    public List<Ingredient> getMealIngredients() { return mealIngredients; }

    /**
     * Add an ingredient to the ingredients list of this meal
     * @param: ingredients to add
     */
    public void addIngredient(Ingredient ingredient) { mealIngredients.add(ingredient); }

    /**
     * Remove an ingredient from the ingredients list of this recipe
     * @param: ingredient to remove
     */
    // public void removeIngredient(Ingredient ingredient) {recipeIngredients.remove(ingredient); }

    /**
     * Constructor for the recipe class.
     *
     * @param: title The title of the recipe
     * @param: recipes The recipes involved in this meal plan
     * @param: ingredients The ingredients involved in this meal plan
     */
    public Meal(String title,
                  List recipes,
                  List ingredients){
        this.title = title;
        this.mealRecipes = recipes;
        this.mealIngredients = ingredients;
    }

}
