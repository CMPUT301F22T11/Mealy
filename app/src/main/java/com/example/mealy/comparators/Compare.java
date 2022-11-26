package com.example.mealy.comparators;

import com.example.mealy.ui.ingredientStorage.Ingredient;

import java.util.Comparator;

/**
 * Helper class which allows for easy selection of the comparators for ListView
 */
public class Compare {
    private Comparator<Ingredient> comparator;

    /**
     * This creates a Compare object, which essentially encapsulates the selection for
     * sorting.
     * @param str - what the user selected
     * @param asc - what way they would like it organized (ascending or descending)
     */
    public Compare(String str, int asc) {
        if (str.equals("Name")) {
            this.comparator = new SortByName(asc);
        } else if (str.equals("Description")) {
            this.comparator = new SortByDesc(asc);
        } else if (str.equals("Expiration")) {
            this.comparator = new SortByExp(asc);
        } else if (str.equals("Location")) {
            this.comparator = new SortByLocation(asc);
        } else if (str.equals("Category")) {
            this.comparator = new SortByCategory(asc);
        } else {
            // should never happen
            this.comparator = new SortByName(asc);
        }
    }

    /**
     * Return the selected comparator.
     * @return Comparator<Ingredient>
     */
    public Comparator<Ingredient> returnComparator() {
        return this.comparator;
    }
}
