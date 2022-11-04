package com.example.mealy.comparators.recipes;

import com.example.mealy.ui.recipes.Recipe;

import java.util.Comparator;

/**
 * This class functions to compare recipes by title
 */
public class SortByRecipeTitle implements Comparator<Recipe> {

    private int asc;

    public SortByRecipeTitle(int asc) {
        super();
        this.asc = asc;
    }

    public int compare(Recipe a, Recipe b) {
        return a.getTitle().compareTo(b.getTitle()) * asc;
    }
}