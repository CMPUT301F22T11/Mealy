package com.example.mealy.comparators;

import com.example.mealy.Ingredient;

import java.util.Comparator;

public class Compare {
    private Comparator<Ingredient> comparator;

    public Compare(String str) {
        if (str.equals("Name")) {
            this.comparator = new SortByName();
        } else if (str.equals("Desc")) {
            this.comparator = new SortByDesc();
        } else if (str.equals("Exp")) {
            this.comparator = new SortByExp();
        } else if (str.equals("Location")) {
            this.comparator = new SortByLocation();
        } else if (str.equals("Category")) {
            this.comparator = new SortByCategory();
        } else {
            // should never happen
            this.comparator = new SortByName();
        }
    }

    public Comparator<Ingredient> returnComparator() {
        return this.comparator;
    }
}
