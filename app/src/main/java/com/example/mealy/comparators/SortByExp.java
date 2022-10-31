package com.example.mealy.comparators;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.mealy.Ingredient;

import java.util.Comparator;

public class SortByExp implements Comparator<Ingredient> {
    private int asc;

    public SortByExp(int asc) {
        super();
        this.asc = asc;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public int compare(Ingredient a, Ingredient b) {
        return a.getExpiration().compareTo(b.getExpiration()) * asc;
    }
}
