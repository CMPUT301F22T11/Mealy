package com.example.mealy.comparators.recipes;

import com.example.mealy.Recipe;

import java.util.Comparator;

/**
 * This class functions to compare recipes by category
 */
public class SortByRecipeCategory implements Comparator<Recipe> {
    private int asc;

    public SortByRecipeCategory(int asc) {
        super();
        this.asc = asc;
    }

    public int compare(Recipe a, Recipe b) {
        return a.getCategory().compareTo(b.getCategory()) * asc;
    }
}