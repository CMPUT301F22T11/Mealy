package com.example.mealy.comparators;

import com.example.mealy.Ingredient;

import java.util.Comparator;

/**
 * Small class for sorting ingredients by location.
 */
public class SortByLocation implements Comparator<Ingredient> {
    private int asc;
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
        return a.getLocation().compareTo(b.getLocation()) * asc;
    }
}
