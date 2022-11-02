package com.example.mealy.comparators.recipes;

import com.example.mealy.Recipe;
import com.example.mealy.comparators.SortByCategory;

import java.util.Comparator;

/**
 * This class functions to compare recipes to each other for sorting purposes.
 */
public class CompareRecipes {
    private Comparator<Recipe> comparator;

    /**
     * Constructor: compare recipes by a specific field for sorting purposes.
     * The field this class is initialized with dictates the comparator for sorting.
     * @param: field The field which to compare recipes. Either
     * title, preptime, servings or category
     * @param: asc Whether to sort it in ascending order or not
     */
    public CompareRecipes(String field, int asc) {
        if (field.equals("Title")) {
            this.comparator = new SortByRecipeTitle(asc);
        } else if (field.equals("PrepTime")) {
            this.comparator = new SortByPrepTime(asc);
        } else if (field.equals("Servings")) {
            this.comparator = new SortByServings(asc);
        } else if (field.equals("Category")) {
            this.comparator = new SortByRecipeCategory(asc);
        } else {
            // should never happen
            this.comparator = new SortByRecipeTitle(asc);
        }
    }

    /**
     * Return the comparator of this particular class.
     * @return: compartor (either for title, preptime, servings or category)
     */
    public Comparator<Recipe> returnComparator() {
        return this.comparator;
    }
}