package com.example.mealy;

import java.time.LocalDate;

/**
 * Recipes
 */
public class Recipe {
    String title;
    String comments;
    Integer servings;
    int preptime; // Change later to proper format
    String category;
    int imageID;

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
        return servings.toString();
    }

    public int getPreptime() { return preptime; }

    public void setPreptime(int preptime) {
        this.preptime = preptime;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getImageID() {
        return imageID;
    }

    public Recipe(String title,
                      String comments,
                      int servings,
                      int preptime,
                      String category,
                      int imageID) {
        this.title = title;
        this.comments = comments;
        this.servings = servings;
        this.preptime = preptime;
        this.category = category;
        this.imageID = imageID;
    }
}