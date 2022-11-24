package com.example.mealy.ui.home;

import android.graphics.Bitmap;

import com.example.mealy.ui.ingredientStorage.Ingredient;
import com.example.mealy.ui.recipes.Recipe;

import java.io.Serializable;
import java.util.List;

public class Meal implements Serializable {
    String title;
    Integer servings;
    String date; // the day that this meal is associated with

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
     * Get the servings for the meal
     * @return: servings of meal
     */
    public String getDate() {
        return date;
    }

    /**
     * Set the servings for the meal
     * @param: servings of meal
     */
    public void setDate(String date) {
        this.date = date;
    }

    /**
     * Constructor for the recipe class.
     *
     * @param: title The title of the recipe
     * @param: recipes The recipes involved in this meal plan
     * @param: ingredients The ingredients involved in this meal plan
     * @param: servings The servings of this recipe
     */
    public Meal(String title,
                  Integer servings,
                  String date,
                  List recipes,
                  List ingredients){
        this.title = title;
        this.servings = servings;
        this.date = date;
        this.mealRecipes = recipes;
        this.mealIngredients = ingredients;
    }

}
