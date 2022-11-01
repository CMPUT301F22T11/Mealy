package com.example.mealy.comparators.recipes;

import com.example.mealy.Recipe;

import java.util.Comparator;

public class SortByTitle implements Comparator<Recipe> {
    public int compare(Recipe a, Recipe b) {
        return a.getTitle().compareTo(b.getTitle());
    }
}