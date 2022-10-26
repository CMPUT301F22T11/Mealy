package com.example.mealy.comparators;

import com.example.mealy.Recipe;

import java.util.Comparator;

public class SortByPrepTime implements Comparator<Recipe>{
    public int compare(Recipe a, Recipe b) {
        return Integer.compare(a.getPreptime(), b.getPreptime());
    }
}

