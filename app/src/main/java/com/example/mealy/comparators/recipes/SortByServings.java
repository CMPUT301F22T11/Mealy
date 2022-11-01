package com.example.mealy.comparators.recipes;

import com.example.mealy.Recipe;

import java.util.Comparator;

public class SortByServings implements Comparator<Recipe> {
    public int compare(Recipe a, Recipe b) {
        return Integer.compare(a.getServings(), b.getServings());
    }
}
