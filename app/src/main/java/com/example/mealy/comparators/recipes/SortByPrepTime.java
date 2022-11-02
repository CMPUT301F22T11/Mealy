package com.example.mealy.comparators.recipes;

import com.example.mealy.Recipe;

import java.util.Comparator;

public class SortByPrepTime implements Comparator<Recipe>{

    private int asc;

    public SortByPrepTime(int asc) {
        super();
        this.asc = asc;
    }

    public int compare(Recipe a, Recipe b) {
        // return least hours, or if same least mins
        if (a.getPreptimeHours() == b.getPreptimeHours()) {
            return Integer.compare(a.getPreptimeMins(), b.getPreptimeMins()) * asc;
        } else {
            return Integer.compare(a.getPreptimeHours(), b.getPreptimeHours()) * asc;
        }
    }
}

