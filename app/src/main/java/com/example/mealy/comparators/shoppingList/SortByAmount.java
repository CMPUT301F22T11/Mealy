package com.example.mealy.comparators.shoppingList;

import com.example.mealy.ui.shoppingList.ShoppingIngredient;

import java.util.Comparator;

public class SortByAmount implements Comparator<ShoppingIngredient> {
    private int asc;
    /**
     * Call super of Comparator, yet set the asc attribute to whatever the user has selected.
     * @param asc
     */
    public SortByAmount(int asc) {
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
    public int compare(ShoppingIngredient a, ShoppingIngredient b) {
        return a.getQuantity().compareTo(b.getQuantity()) * asc;
    }
}
