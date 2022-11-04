package com.example.mealy.comparators;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.mealy.ui.dashboard.Ingredient;

import java.time.LocalDate;
import java.util.Comparator;

/**
 * Small class for sorting ingredients by expiration date.
 */
public class SortByExp implements Comparator<Ingredient> {
    private int asc;
    /**
     * Call super of Comparator, yet set the asc attribute to whatever the user has selected.
     * @param asc
     */
    public SortByExp(int asc) {
        super();
        this.asc = asc;
    }
    /**
     * Compare two ingredients based on expiration date. Flip the organization based on
     * user selection. Parse the date from the stored string for accurate comparison.
     * @param a - Ingredient
     * @param b - Ingredient
     * @return int
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public int compare(Ingredient a, Ingredient b) {
        LocalDate l1 = LocalDate.parse(a.getExpiryDate());
        LocalDate l2 = LocalDate.parse((b.getExpiryDate()));
        return l1.compareTo(l2) * asc;
    }
}
