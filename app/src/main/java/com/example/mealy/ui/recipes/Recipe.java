package com.example.mealy.ui.recipes;

import android.graphics.Bitmap;

import com.example.mealy.ui.ingredientStorage.Ingredient;

import java.io.Serializable;
import java.util.List;

/**
 * A class representing a recipe.
 * This contains all necessary attributes used to describe a recipe.
 * Each item in the recipe list is a recipe.
 *
 * Recipes are serializable and can be passed between fragments.
 */
public class Recipe implements Serializable {
    String title;
    String comments;
    Integer servings;
    int preptimeHours;
    int preptimeMins;
    String category;

    // int imageID;

    Bitmap bitmap; // image of recipe
    boolean hasImage = false; // is this recipe associated with an image

    private List<Ingredient> recipeIngredients;

    /**
     * Set the bitmap of the recipe associated with this image
     * @param: bitmap of recipe
     */
    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
        this.hasImage = true;
    }

    /**
     * Get the bitmap of the recipe associated with this image, if it exists
     * @return: bitmap of recipe
     */
    public Bitmap getBitmap() {
        if (hasImage) {
            return this.bitmap;
        } else {
            return null;
        }
    }

    /**
     * Check if this recipe is associated with an image
     * @return: true if has image, false otherwise
     */
    public boolean hasImage() {
        return this.hasImage;
    }

    /**
     * Get the title of the recipe
     * @return: title of recipe
     */
    public String getTitle() {
        return title;
    }

    /**
     * Set the title of the recipe
     * @param: title of recipe
     */
    public void setTitle(String name) {
        this.title = name;
    }

    /**
     * Set the comments for the recipe
     * @param: comments of recipe
     */
    public void setComments(String comments) {
        this.comments = comments;
    }

    /**
     * Get the comments for the recipe
     * @return: comments of recipe
     */
    public String getComments() { return this.comments; }

    /**
     * Get the servings for the recipe
     * @return: servings of recipe
     */
    public int getServings() {
        return servings;
    }

    /**
     * Set the servings for the recipe
     * @param: servings of recipe
     */
    public void setServings(int servings) {
        this.servings = servings;
    }

    /**
     * Get the servings for the recipe in string format
     * @return: servings of recipe as a string
     */
    public String getServingsString() {
        return "Serves " + servings.toString();
    }

    /**
     * Get the hours of the prep time for the recipe
     * @return: prep time hours of this recipe
     */
    public int getPreptimeHours() { return preptimeHours; }

    /**
     * Set the hours of the prep time for the recipe
     * @param: prep time hours of this recipe
     */
    public void setPreptimeHours(int preptimeHours) {this.preptimeHours = preptimeHours;}

    /**
     * Get the minutes of the prep time for the recipe
     * @return: prep time minutes of this recipe
     */
    public int getPreptimeMins() { return preptimeMins; }

    /**
     * Set the minutes of the prep time for the recipe
     * @param: prep time minutes of this recipe
     */
    public void setPreptimeMins(int preptimeMins) {this.preptimeMins = preptimeMins;}

    /**
     * Get the category of this recipe
     * @return: category of this recipe
     */
    public String getCategory() {
        return category;
    }

    /**
     * Set the category of this recipe
     * @param: category of this recipe
     */
    public void setCategory(String category) {
        this.category = category;
    }

    /**
     * Get the ingredients list of this recipe
     * @return: ingredients list of this recipe
     */
    public List<Ingredient> getRecipeIngredients() { return recipeIngredients; }

    /**
     * Add an ingredient to the ingredients list of this recipe
     * @param: ingredients to add
     */
    public void addIngredient(Ingredient ingredient) { recipeIngredients.add(ingredient); }

    /**
     * Remove an ingredient from the ingredients list of this recipe
     * @param: ingredient to remove
     */
    public void removeIngredient(Ingredient ingredient) {recipeIngredients.remove(ingredient); }

    /**
     * Constructor for the recipe class.
     *
     * @param: title The title of the recipe
     * @param: comments The description/comments of the recipe
     * @param: servings The amount of servings for this recipe
     * @param: preptimeHours The amount of hours it takes to prep the recipe
     * @param: preptimeMins The amount of minutes it takes to prep the recipe
     * @param: category The category that this recipe belongs to
     * @param: ingredients A list of ingredients that are in this recipe
     */
    public Recipe(String title,
                  String comments,
                  int servings,
                  int preptimeHours,
                  int preptimeMins,
                  String category,
                  // int imageID,
                  List ingredients){
        this.title = title;
        this.comments = comments;
        this.servings = servings;
        this.preptimeHours = preptimeHours;
        this.preptimeMins = preptimeMins;
        this.category = category;
        this.recipeIngredients = ingredients;
    }
}