package com.example.mealy.comparators;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.mealy.Ingredient;

import java.time.LocalDate;
import java.util.Comparator;

public class SortByExp implements Comparator<Ingredient> {
    private int asc;

    public SortByExp(int asc) {
        super();
        this.asc = asc;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public int compare(Ingredient a, Ingredient b) {
        LocalDate l1 = LocalDate.parse(a.getExpiryDate());
        LocalDate l2 = LocalDate.parse((b.getExpiryDate()));
        return l1.compareTo(l2) * asc;
    }
}
