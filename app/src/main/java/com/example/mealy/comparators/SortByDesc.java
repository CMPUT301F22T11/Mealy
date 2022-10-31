package com.example.mealy.comparators;

import com.example.mealy.Ingredient;

import java.util.Comparator;

public class SortByDesc implements Comparator<Ingredient> {
    private int asc;

    public SortByDesc(int asc) {
        super();
        this.asc = asc;
    }

    public int compare(Ingredient a, Ingredient b) {
        return a.getDescription().compareTo(b.getDescription()) * asc;
    }
}