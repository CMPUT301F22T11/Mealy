package com.example.mealy.comparators;

import com.example.mealy.Recipe;

import java.util.Comparator;

public class SortByPrepTime implements Comparator<Recipe>{
    public int compare(Recipe a, Recipe b) {
        // return least hours, or if same least mins
        if (a.getPreptimeHours() == b.getPreptimeHours()) {
            return Integer.compare(a.getPreptimeMins(), b.getPreptimeMins());
        } else {
            return Integer.compare(a.getPreptimeHours(), b.getPreptimeHours());
        }
    }
}

