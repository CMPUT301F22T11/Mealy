package com.example.mealy.comparators.ingredientStorage;

import com.example.mealy.ui.ingredientStorage.Ingredient;

import java.util.Comparator;
import java.util.Locale;

/**
 * Small class for sorting ingredients by name.
 */
public class SortByName implements Comparator<Ingredient> {
    private final int asc;
    /**
     * Call super of Comparator, yet set the asc attribute to whatever the user has selected.
     * @param asc
     */
    public SortByName(int asc) {
        super();
        this.asc = asc;
    }
    /**
     * Compare two ingredients based on name. Flip the organization based on
     * user selection
     * @param a - Ingredient
     * @param b - Ingredient
     * @return int
     */
    public int compare(Ingredient a, Ingredient b) {
        return a.getName().toLowerCase().compareTo(b.getName().toLowerCase()) * asc;
    }
}
