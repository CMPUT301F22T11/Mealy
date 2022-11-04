package com.example.mealy.comparators.recipes;

import com.example.mealy.Recipe;

import java.util.Comparator;

public class SortByServings implements Comparator<Recipe> {

    private int asc;

    public SortByServings(int asc) {
        super();
        this.asc = asc;
    }

    public int compare(Recipe a, Recipe b) {
        return Integer.compare(a.getServings(), b.getServings()) * asc;
    }
}
