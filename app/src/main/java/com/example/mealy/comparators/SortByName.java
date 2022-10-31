package com.example.mealy.comparators;

import com.example.mealy.Ingredient;

import java.util.Comparator;

public class SortByName implements Comparator<Ingredient> {
    private int asc;

    public SortByName(int asc) {
        super();
        this.asc = asc;
    }

    public int compare(Ingredient a, Ingredient b) {
        return a.getName().compareTo(b.getName()) * asc;
    }
}
