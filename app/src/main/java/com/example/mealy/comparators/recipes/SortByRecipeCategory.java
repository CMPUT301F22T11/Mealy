package com.example.mealy.comparators.recipes;

import com.example.mealy.ui.recipes.Recipe;

import java.util.Comparator;

/**
 * This class functions to compare recipes by category
 */
public class SortByRecipeCategory implements Comparator<Recipe> {
    private final int asc;

    public SortByRecipeCategory(int asc) {
        super();
        this.asc = asc;
    }

    public int compare(Recipe a, Recipe b) {
        return a.getCategory().toLowerCase().compareTo(b.getCategory().toLowerCase()) * asc;
    }
}