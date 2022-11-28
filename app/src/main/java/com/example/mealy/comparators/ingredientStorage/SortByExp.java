package com.example.mealy.comparators.ingredientStorage;

import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.example.mealy.functions.DateFunc;
import com.example.mealy.functions.Validate;
import com.example.mealy.ui.ingredientStorage.Ingredient;

import java.time.LocalDate;
import java.util.Comparator;

/**
 * Small class for sorting ingredients by expiration date.
 */
public class SortByExp implements Comparator<Ingredient> {
    private final int asc;
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
        LocalDate l1;
        LocalDate l2;

        if (Validate.validDate(a.getExpiryDate())) {
            l1 = LocalDate.parse(a.getExpiryDate());
        } else {
            l1 = LocalDate.parse("0000-01-01");
        }

        if (Validate.validDate(b.getExpiryDate())) {
            l2 = LocalDate.parse(b.getExpiryDate());
        } else {
            l2 = LocalDate.parse("0000-01-01");
        }
        //Log.wtf(l1.toString(), String.valueOf(l1.compareTo(l2)));

        return l1.compareTo(l2) * asc;
    }
}
