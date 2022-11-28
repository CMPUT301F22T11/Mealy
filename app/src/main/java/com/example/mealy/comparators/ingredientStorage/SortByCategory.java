package com.example.mealy.comparators.ingredientStorage;

import com.example.mealy.ui.ingredientStorage.Ingredient;

import java.util.Comparator;

/**
 * Small class for sorting ingredients by categories.
 */
public class SortByCategory implements Comparator<Ingredient> {
    private final int asc;

    /**
     * Call super of Comparator, yet set the asc attribute to whatever the user has selected.
     * @param asc
     */
    public SortByCategory(int asc) {
        super();
        this.asc = asc;
    }

    /**
     * Compare two ingredients based on category. Flip the organization based on
     * user selection
     * @param a - Ingredient
     * @param b - Ingredient
     * @return int
     */
    public int compare(Ingredient a, Ingredient b) {
        return a.getCategory().toLowerCase().compareTo(b.getCategory().toLowerCase()) * asc;
    }
}