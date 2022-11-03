package com.example.mealy.comparators.recipes;

import com.example.mealy.Recipe;

import java.util.Comparator;

public class SortByRecipeTitle implements Comparator<Recipe> {

    private int asc;

    public SortByRecipeTitle(int asc) {
        super();
        this.asc = asc;
    }

    public int compare(Recipe a, Recipe b) {
        return a.getTitle().compareTo(b.getTitle()) * asc;
    }
}