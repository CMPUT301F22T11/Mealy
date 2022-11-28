package com.example.mealy.ui.home;

import android.graphics.Bitmap;

import com.example.mealy.ui.ingredientStorage.Ingredient;
import com.example.mealy.ui.recipes.Recipe;

import java.io.Serializable;
import java.util.List;

public class Meal implements Serializable {
    String title;
    Integer servings;
    String startDate; // the day that this meal plan starts with
    String endDate; // the day that this meal plan ends with (should be consecutive)

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
     * Get the servings for the meal
     * @return: servings of meal
     */
    public int getServings() {
        return servings;
    }

    /**
     * Set the servings for the meal
     * @param: servings of meal
     */
    public void setServings(int servings) {
        this.servings = servings;
    }

    /**
     * Get the servings for the meal in string format
     * @return: servings of meal as a string
     */
    public String getServingsString() {
        return "Serving size: " + servings.toString();
    }

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
                  Integer servings,
                  String startDate,
                  String endDate,
                  List recipes,
                  List ingredients){
        this.title = title;
        this.servings = servings;
        this.startDate = startDate;
        this.endDate = endDate;
        this.mealRecipes = recipes;
        this.mealIngredients = ingredients;
    }

}
