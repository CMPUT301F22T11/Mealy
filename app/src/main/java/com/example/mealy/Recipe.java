package com.example.mealy;

import java.io.Serializable;
import java.util.List;

/**
 * Recipes
 */
public class Recipe implements Serializable {
    String title;
    String comments;
    Integer servings;
    int preptimeHours;
    int preptimeMins;
    String category;
    int imageID;
    private List<Ingredient> recipeIngredients;

    public String getTitle() {
        return title;
    }

    public void setTitle(String name) {
        this.title = name;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getComments() { return this.comments; }

    public int getServings() {
        return servings;
    }

    public void setServings(int servings) {
        this.servings = servings;
    }

    public String getServingsString() {
        return "Serves " + servings.toString();
    }

    public int getPreptimeHours() { return preptimeHours; }

    public void setPreptimeHours(int preptimeHours) {this.preptimeHours = preptimeHours;}

    public int getPreptimeMins() { return preptimeMins; }

    public void setPreptimeMins(int preptimeMins) {this.preptimeMins = preptimeMins;}

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getImageID() {
        return imageID;
    }

    public List<Ingredient> getRecipeIngredients() { return recipeIngredients; }

    public void addIngredient(Ingredient ingredient) { recipeIngredients.add(ingredient); }

    public void removeIngredient(Ingredient ingredient) {recipeIngredients.remove(ingredient); }

    public Recipe(String title,
                      String comments,
                      int servings,
                      int preptimeHours,
                      int preptimeMins,
                      String category,
                      int imageID,
                      List ingredients){
        this.title = title;
        this.comments = comments;
        this.servings = servings;
        this.preptimeHours = preptimeHours;
        this.preptimeMins = preptimeMins;
        this.category = category;
        this.imageID = imageID;
        this.recipeIngredients = ingredients;
    }
}