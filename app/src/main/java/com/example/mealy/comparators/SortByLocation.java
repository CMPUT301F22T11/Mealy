package com.example.mealy.comparators;

import com.example.mealy.Ingredient;

import java.util.Comparator;

public class SortByLocation implements Comparator<Ingredient> {
    private int asc;

    public SortByLocation(int asc) {
        super();
        this.asc = asc;
    }

    public int compare(Ingredient a, Ingredient b) {
        return a.getLocation().compareTo(b.getLocation()) * asc;
    }
}
