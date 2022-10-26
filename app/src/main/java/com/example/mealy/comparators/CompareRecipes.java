package com.example.mealy.comparators;

import com.example.mealy.Recipe;

import java.util.Comparator;

public class CompareRecipes {
    private Comparator<Recipe> comparator;

    public CompareRecipes(String str) {
        if (str.equals("Title")) {
            this.comparator = new SortByTitle();
        } else if (str.equals("PrepTime")) {
            this.comparator = new SortByPrepTime();
        } else if (str.equals("Servings")) {
            this.comparator = new SortByServings();
        } else if (str.equals("Category")) {
            this.comparator = new SortByCategory();
        } else {
            // should never happen
            this.comparator = new SortByTitle();
        }
    }

    public Comparator<Recipe> returnComparator() {
        return this.comparator;
    }
}