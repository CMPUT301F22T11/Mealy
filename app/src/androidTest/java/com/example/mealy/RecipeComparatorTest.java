
package com.example.mealy;

import org.junit.Test;
import static org.junit.Assert.*;

import com.example.mealy.comparators.recipes.CompareRecipes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RecipeComparatorTest {

    public List<Ingredient> makeIngredientList() {
        // add sample ingredient for recipe list constructor (not being compared)
        Ingredient apple = new Ingredient("Apple",
                "Red",
                "0",
                "lb",
                "Weight",
                "Raw Food",
                "Pantry",
                "2022-12-05");

        // make ingredient list
        List ingredientList = new ArrayList<Ingredient>();
        ingredientList.add(apple);

        return ingredientList;
    }

    // make some recipes
    Recipe applePie = new Recipe("Apple pie",
            "Delicious apple pie made from real apple",
            4, 1, 30,
            "Baked", makeIngredientList());

    Recipe friedApple = new Recipe("Fried apple",
            "Apples that are fried in a vat of oil",
            2, 0, 15,
            "Fried", makeIngredientList());

    Recipe candyApple = new Recipe("Candy apple",
            "Whole apples covered in a sugar coating",
            1, 0, 45,
            "Dessert", makeIngredientList());


    // make recipe list
    public ArrayList<Recipe> makeRecipeList() {
        ArrayList<Recipe> recipeArrayList = new ArrayList<>();

        recipeArrayList.add(applePie);
        recipeArrayList.add(friedApple);
        recipeArrayList.add(candyApple);

        return recipeArrayList;
    }

    /**
     * Test for comparing title sort functionality
     */
    @Test
    public void testCompareTitle() {
        ArrayList<Recipe> recipeArrayList = makeRecipeList();

        // sorting should be alphabetical: apple pie -> candy apple -> fried apple
        CompareRecipes compare = new CompareRecipes("Title", 1);
        Collections.sort(recipeArrayList, compare.returnComparator());

        assertTrue(recipeArrayList.get(0) == applePie
                && recipeArrayList.get(1) == candyApple
                && recipeArrayList.get(2) == friedApple);

        // sort in reverse alphabetical: fried apple -> candy apple -> apple pie
        CompareRecipes compareReverse = new CompareRecipes("Title", -1);
        Collections.sort(recipeArrayList, compareReverse.returnComparator());

        assertTrue(recipeArrayList.get(0) == friedApple
                && recipeArrayList.get(1) == candyApple
                && recipeArrayList.get(2) == applePie);
    }

    /**
     * Test for comparing prep time functionality
     */
    @Test
    public void testComparePrepTime() {
        ArrayList<Recipe> recipeArrayList = makeRecipeList();

        // sorting should be from least to most prep time: fried apple -> candy apple -> apple pie
        CompareRecipes compare = new CompareRecipes("Prep Time", 1);
        Collections.sort(recipeArrayList, compare.returnComparator());

        assertTrue(recipeArrayList.get(0) == friedApple
                && recipeArrayList.get(1) == candyApple
                && recipeArrayList.get(2) == applePie);

        // sorting should be from most to least prep time: apple pie -> candy apple -> fried apple
        CompareRecipes compareReverse = new CompareRecipes("Prep Time", -1);
        Collections.sort(recipeArrayList, compareReverse.returnComparator());

        assertTrue(recipeArrayList.get(0) == applePie
                && recipeArrayList.get(1) == candyApple
                && recipeArrayList.get(2) == friedApple);

    }

    /**
     * Test for comparing category functionality
     */
    @Test
    public void testCompareCategory() {
        ArrayList<Recipe> recipeArrayList = makeRecipeList();

        // sorting should be from alphabetical category: apple pie -> candy apple -> fried apple
        CompareRecipes compare = new CompareRecipes("Category", 1);
        Collections.sort(recipeArrayList, compare.returnComparator());

        assertTrue(recipeArrayList.get(0) == applePie
                && recipeArrayList.get(1) == candyApple
                && recipeArrayList.get(2) == friedApple);

        // sorting should be from reverse alphabetical category: fried apple -> candy apple -> apple pie
        CompareRecipes compareReverse = new CompareRecipes("Category", -1);
        Collections.sort(recipeArrayList, compareReverse.returnComparator());

        assertTrue(recipeArrayList.get(0) == friedApple
                && recipeArrayList.get(1) == candyApple
                && recipeArrayList.get(2) == applePie);
    }

    /**
     * Test for comparing servings functionality
     */
    @Test
    public void testServings() {
        ArrayList<Recipe> recipeArrayList = makeRecipeList();

        // sorting should be from least to most servings: candy apple -> fried apple -> apple pie
        CompareRecipes compare = new CompareRecipes("Servings", 1);
        Collections.sort(recipeArrayList, compare.returnComparator());

        assertTrue(recipeArrayList.get(0) == candyApple
                && recipeArrayList.get(1) == friedApple
                && recipeArrayList.get(2) == applePie);

        // sorting should be from most to least: apple pie -> fried apple -> candy apple
        CompareRecipes compareReverse = new CompareRecipes("Servings", -1);
        Collections.sort(recipeArrayList, compareReverse.returnComparator());

        assertTrue(recipeArrayList.get(0) == applePie
                && recipeArrayList.get(1) == friedApple
                && recipeArrayList.get(2) == candyApple);
    }

}