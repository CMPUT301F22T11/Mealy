package com.example.mealy.comparators;

import com.example.mealy.Ingredient;

import java.util.Comparator;

public class Compare {
    private Comparator<Ingredient> comparator;

    public Compare(String str, int asc) {
        if (str.equals("Name")) {
            this.comparator = new SortByName(asc);
        } else if (str.equals("Desc")) {
            this.comparator = new SortByDesc(asc);
        } else if (str.equals("Exp")) {
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

    public Comparator<Ingredient> returnComparator() {
        return this.comparator;
    }
}
