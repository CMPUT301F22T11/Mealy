
package com.example.mealy;

import org.junit.Test;
import static org.junit.Assert.*;

import com.example.mealy.comparators.recipes.CompareRecipes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RecipeComparatorTest {

    public ArrayList<Recipe> makeRecipeList() {
        ArrayList<Recipe> recipeArrayList = new ArrayList<>();

        // add sample ingredient for recipe list (change later)
        Ingredient rat_hair = new Ingredient("rat hair",
                "hair from a rat",
                "100",
                "strands",
                "protein",
                "rodent",
                "pantry",
                "2025-12-13");

        List ingredientList = new ArrayList<Ingredient>();
        ingredientList.add(rat_hair);

        // add sample items
        recipeArrayList.add(new Recipe("Meat Rat",
                "Yummy for my tummy",
                5, 3, 10,
                "Grilled",
                R.drawable.meat_rat,
                ingredientList));

        recipeArrayList.add(new Recipe("Meat Skull",
                "Going to hell",
                3, 4, 30,
                "Fried",
                R.drawable.meatskull,
                ingredientList));

        recipeArrayList.add(new Recipe("Rat Hair",
                "Going to hell",
                3, 4, 30,
                "Fried",
                R.drawable.rathair,
                ingredientList));

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