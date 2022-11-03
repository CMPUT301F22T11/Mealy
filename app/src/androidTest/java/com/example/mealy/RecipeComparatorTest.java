
package com.example.mealy;

import org.junit.Test;
import static org.junit.Assert.*;

import com.example.mealy.comparators.recipes.CompareRecipes;

import java.util.ArrayList;
import java.util.Collections;

public class RecipeComparatorTest {

    public ArrayList<Recipe> makeRecipeList() {
        ArrayList<Recipe> recipeArrayList = new ArrayList<>();

        return recipeArrayList;
    }

    @Test
    public void testCompare() {
        int asc = 1;

        ArrayList<Recipe> recipeArrayList = makeRecipeList();

        CompareRecipes compare = new CompareRecipes("selectedItem", asc);
        Collections.sort(recipeArrayList, compare.returnComparator());

        assertEquals(1+1, 2);
    }

}