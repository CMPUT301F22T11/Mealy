package com.example.mealy.comparators;

import com.example.mealy.Ingredient;

import java.util.Comparator;

public class SortByCategory implements Comparator<Ingredient> {
    public int compare(Ingredient a, Ingredient b) {
        return a.getCategory().compareTo(b.getCategory());
    }
}
