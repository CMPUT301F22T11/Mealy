package com.example.mealy.comparators.ingredientStorage;

import com.example.mealy.ui.ingredientStorage.Ingredient;

import java.util.Comparator;

/**
 * Small class for sorting ingredients by location.
 */
public class SortByLocation implements Comparator<Ingredient> {
    private final int asc;
    /**
     * Call super of Comparator, yet set the asc attribute to whatever the user has selected.
     * @param asc
     */
    public SortByLocation(int asc) {
        super();
        this.asc = asc;
    }
    /**
     * Compare two ingredients based on location. Flip the organization based on
     * user selection
     * @param a - Ingredient
     * @param b - Ingredient
     * @return int
     */
    public int compare(Ingredient a, Ingredient b) {
        return a.getLocation().toLowerCase().compareTo(b.getLocation().toLowerCase()) * asc;
    }
}
