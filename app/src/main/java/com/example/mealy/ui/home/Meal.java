package com.example.mealy.ui.home;

import android.graphics.Bitmap;

import com.example.mealy.ui.ingredientStorage.Ingredient;
import com.example.mealy.ui.recipes.Recipe;

import java.io.Serializable;
import java.util.List;

/**
 * A class representing a meal.
 * This contains all necessary attributes used to describe a meal.
 *
 * Recipes are serializable and can be passed between fragments.
 */
public class Meal implements Serializable {
    String title;
    String startDate; // the day that this meal plan starts with
    String endDate; // the day that this meal plan ends with (should be consecutive)

    private final List<Ingredient> mealIngredients;
    private final List<Recipe> mealRecipes;

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
     * Get the recipes list of this meal
     * @return: recipes list of this meal
     */
    public List<Recipe> getMealRecipes() { return mealRecipes; }

    /**
     * Add an recipe to the recipes list of this meal
     * @param: recipe to add
     */
    public void addRecipe(Recipe recipe) { mealRecipes.add(recipe); }

    /**
     * Get the start date for the meal
     * @return: start date of meal (yyyy-mm-dd)
     */
    public String getStartDate() {
        return startDate;
    }

    /**
     * Set the start date for the meal
     * @param: start date of meal (yyyy-mm-dd)
     */
    public void setStartDate(String date) {
        this.startDate = date;
    }

    /**
     * Get the end date for the meal
     * @return: end date of meal (yyyy-mm-dd)
     */
    public String getEndDate() {
        return endDate;
    }

    /**
     * Set the end date for the meal
     * @param: end date of meal (yyyy-mm-dd)
     */
    public void setEndDate(String date) {
        this.endDate = date;
    }

    /**
     * Constructor for the recipe class.
     *
     * @param: title The title of the recipe
     * @param: recipes The recipes involved in this meal plan
     * @param: ingredients The ingredients involved in this meal plan
     * @param: servings The servings of this recipe
     * @param: date The date of this meal plan in format yyyy-mm-dd
     */
    public Meal(String title,
                  String startDate,
                  String endDate,
                  List recipes,
                  List ingredients){
        this.title = title;
        this.startDate = startDate;
        this.endDate = endDate;
        this.mealRecipes = recipes;
        this.mealIngredients = ingredients;
    }

}
