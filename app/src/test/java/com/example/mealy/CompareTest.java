
package com.example.mealy;

import org.junit.Test;
import static org.junit.Assert.*;

import com.example.mealy.comparators.ingredientStorage.Compare;
import com.example.mealy.ui.ingredientStorage.Ingredient;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Test case for the Comparator class. This also may represent the Ingredient storage, as it uses
 * an ArrayList as well.
 */
public class CompareTest {
    // Create a variety of ingredients. Their relative ordering is placed in the tests themselves.
    Ingredient ingred1 = new Ingredient(
            "Apple",
            "Red",
            "1",
            "lb",
            "Weight",
            "Raw Food",
            "Pantry",
            "2022-12-05");
    Ingredient ingred2 = new Ingredient(
            "Asparagus",
            "Green and prickly",
            "0.5",
            "lb",
            "Weight",
            "Raw Food",
            "Fridge",
            "2022-12-02");
    Ingredient ingred3 = new Ingredient(
            "Burger",
            "80% lean 20% fat",
            "5",
            "lb",
            "Weight",
            "Meat",
            "Freezer",
            "2023-12-25");
    Ingredient ingred4 = new Ingredient(
            "Turmeric",
            "Spice for Curry",
            "0.5",
            "lb",
            "Weight",
            "Spice",
            "Pantry",
            "2030-04-20");

    /**
     * Create an ArrayList representing the ingredientList in Storage. Add ingredients
     * out of order.
     * @return ArrayList<Ingredient>
     */
    public ArrayList<Ingredient> makeIngredientList() {
        ArrayList<Ingredient> foodList = new ArrayList<>();
        foodList.add(ingred1);
        foodList.add(ingred2);
        foodList.add(ingred4);
        foodList.add(ingred3);
        return foodList;
    }

    /**
     * Compare the categories of the 4 different ingredients. Firstly it will
     * sort in ascending order, then it will verify that the descending order
     * is correct.
     */
    @Test
    public void testCompareCategory() {
        // Ordering should be Apple -> Asparagus -> Turmeric -> Burger
        ArrayList<Ingredient> foodList = makeIngredientList();
        assertTrue(foodList.get(0) == ingred1
                && foodList.get(1) == ingred2
                && foodList.get(2) == ingred4
                && foodList.get(3) == ingred3);
        Compare compare1 = new Compare("Category", 1);
        Collections.sort(foodList, compare1.returnComparator());
        // Ordering should now be Burger -> Apple -> Asparagus -> Turmeric
        // (Ties are then broken by alphabetical again)
        assertTrue(foodList.get(0) == ingred3
                && foodList.get(1) == ingred1
                && foodList.get(2) == ingred2
                && foodList.get(3) == ingred4);
        Compare compare2 = new Compare("Category", -1);
        Collections.sort(foodList, compare2.returnComparator());
        // Ordering should now be Turmeric -> Apple -> Asparagus -> Burger
        // (Ties are then broken by alphabetical again)
        assertTrue(foodList.get(0) == ingred4
                && foodList.get(1) == ingred1
                && foodList.get(2) == ingred2
                && foodList.get(3) == ingred3);
    }

    /**
     * Compare the descriptions of the 4 different ingredients. Firstly it will
     * sort in ascending order, then it will verify that the descending order
     * is correct.
     */
    @Test
    public void testCompareDesc() {
        // Ordering should be Apple -> Asparagus -> Turmeric -> Burger
        ArrayList<Ingredient> foodList = makeIngredientList();
        assertTrue(foodList.get(0) == ingred1
                && foodList.get(1) == ingred2
                && foodList.get(2) == ingred4
                && foodList.get(3) == ingred3);
        Compare compare1 = new Compare("Description", 1);
        Collections.sort(foodList, compare1.returnComparator());
        // Ordering should now be Burger -> Asparagus -> Apple -> Turmeric
        assertTrue(foodList.get(0) == ingred3
                && foodList.get(1) == ingred2
                && foodList.get(2) == ingred1
                && foodList.get(3) == ingred4);
        Compare compare2 = new Compare("Description", -1);
        Collections.sort(foodList, compare2.returnComparator());
        // Ordering should now be Turmeric -> Apple -> Asparagus -> Burger
        assertTrue(foodList.get(0) == ingred4
                && foodList.get(1) == ingred1
                && foodList.get(2) == ingred2
                && foodList.get(3) == ingred3);

    }

    /**
     * Compare the expiration of the 4 different ingredients. Firstly it will
     * sort in ascending order, then it will verify that the descending order
     * is correct.
     */
    @Test
    public void testCompareExp() {
        // Ordering should be Apple -> Asparagus -> Turmeric -> Burger
        ArrayList<Ingredient> foodList = makeIngredientList();
        assertTrue(foodList.get(0) == ingred1
                && foodList.get(1) == ingred2
                && foodList.get(2) == ingred4
                && foodList.get(3) == ingred3);
        Compare compare1 = new Compare("Expiration", 1);
        Collections.sort(foodList, compare1.returnComparator());
        // Ordering should now be Asparagus -> Apple -> Burger -> Turmeric
        assertTrue(foodList.get(0) == ingred2
                && foodList.get(1) == ingred1
                && foodList.get(2) == ingred3
                && foodList.get(3) == ingred4);
        Compare compare2 = new Compare("Expiration", -1);
        Collections.sort(foodList, compare2.returnComparator());
        // Ordering should now be Turmeric -> Burger -> Apple -> Asparagus
        assertTrue(foodList.get(0) == ingred4
                && foodList.get(1) == ingred3
                && foodList.get(2) == ingred1
                && foodList.get(3) == ingred2);

    }

    /**
     * Compare the location of the 4 different ingredients. Firstly it will
     * sort in ascending order, then it will verify that the descending order
     * is correct.
     */
    @Test
    public void testCompareLocation() {
        // Ordering should be Apple -> Asparagus -> Turmeric -> Burger
        ArrayList<Ingredient> foodList = makeIngredientList();
        assertTrue(foodList.get(0) == ingred1
                && foodList.get(1) == ingred2
                && foodList.get(2) == ingred4
                && foodList.get(3) == ingred3);
        Compare compare1 = new Compare("Location", 1);
        Collections.sort(foodList, compare1.returnComparator());
        // Ordering should now be Burger -> Asparagus -> Apple -> Turmeric
        // (Ties are then broken by alphabetical again)
        assertTrue(foodList.get(0) == ingred3
                && foodList.get(1) == ingred2
                && foodList.get(2) == ingred1
                && foodList.get(3) == ingred4);
        Compare compare2 = new Compare("Location", -1);
        Collections.sort(foodList, compare2.returnComparator());
        // Ordering should now be Apple -> Turmeric -> Asparagus -> Burger
        // (Ties are then broken by alphabetical again)
        assertTrue(foodList.get(0) == ingred1
                && foodList.get(1) == ingred4
                && foodList.get(2) == ingred2
                && foodList.get(3) == ingred3);

    }

    /**
     * Compare the name of the 4 different ingredients. Firstly it will
     * sort in ascending order, then it will verify that the descending order
     * is correct.
     */
    @Test
    public void testCompareName() {
        // Ordering should be Apple -> Asparagus -> Turmeric -> Burger
        ArrayList<Ingredient> foodList = makeIngredientList();
        assertTrue(foodList.get(0) == ingred1
                && foodList.get(1) == ingred2
                && foodList.get(2) == ingred4
                && foodList.get(3) == ingred3);
        Compare compare1 = new Compare("Name", 1);
        Collections.sort(foodList, compare1.returnComparator());
        // Ordering should now be Apple -> Asparagus -> Burger -> Turmeric
        assertTrue(foodList.get(0) == ingred1
                && foodList.get(1) == ingred2
                && foodList.get(2) == ingred3
                && foodList.get(3) == ingred4);
        Compare compare2 = new Compare("Name", -1);
        Collections.sort(foodList, compare2.returnComparator());
        // Ordering should now be Turmeric -> Burger -> Asparagus -> Apple
        assertTrue(foodList.get(0) == ingred4
                && foodList.get(1) == ingred3
                && foodList.get(2) == ingred2
                && foodList.get(3) == ingred1);
    }


}