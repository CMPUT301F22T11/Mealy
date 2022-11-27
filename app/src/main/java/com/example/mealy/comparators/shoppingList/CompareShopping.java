package com.example.mealy.comparators.shoppingList;

import com.example.mealy.ui.shoppingList.ShoppingIngredient;

import com.example.mealy.comparators.recipes.SortByPrepTime;
import com.example.mealy.comparators.recipes.SortByRecipeCategory;
import com.example.mealy.comparators.recipes.SortByRecipeTitle;
import com.example.mealy.comparators.recipes.SortByServings;
import com.example.mealy.ui.recipes.Recipe;

import java.util.Comparator;

/**
 * This class functions to compare recipes to each other for sorting purposes.
 */
public class CompareShopping {
    private Comparator<ShoppingIngredient> comparator;

    /**
     * Constructor: compare recipes by a specific field for sorting purposes.
     * The field this class is initialized with dictates the comparator for sorting.
     * @param: field The field which to compare recipes. Either
     * title, preptime, servings or category
     * @param: asc Whether to sort it in ascending order or not
     */
    public CompareShopping(String field, int asc) {
        if (field.equals("Title")) {
            this.comparator = new SortByTitle(asc);
        } else if (field.equals("Description")) {
            this.comparator = new SortByDescription(asc);
        } else if (field.equals("Quantity")) {
            this.comparator = new SortByAmount(asc);
        } else if (field.equals("Category")) {
            this.comparator = new SortByCategory(asc);
        } else {
            // should never happen
            this.comparator = new SortByTitle(asc);
        }
    }

    /**
     * Return the comparator of this particular class.
     * @return: compartor (either for title, preptime, servings or category)
     */
    public Comparator<ShoppingIngredient> returnComparator() {
        return this.comparator;
    }
}
