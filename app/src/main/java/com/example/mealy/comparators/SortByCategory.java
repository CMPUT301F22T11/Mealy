package com.example.mealy.comparators;

import com.example.mealy.Recipe;

import java.util.Comparator;

public class SortByCategory implements Comparator<Recipe> {
    public int compare(Recipe a, Recipe b) {
        return a.getCategory().compareTo(b.getCategory());
    }
}