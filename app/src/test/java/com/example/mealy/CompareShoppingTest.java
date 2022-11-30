package com.example.mealy;

import org.junit.Test;
import static org.junit.Assert.*;

import com.example.mealy.comparators.ingredientStorage.Compare;
import com.example.mealy.comparators.shoppingList.CompareShopping;
import com.example.mealy.ui.ingredientStorage.Ingredient;
import com.example.mealy.ui.shoppingList.ShoppingIngredient;

import java.util.ArrayList;
import java.util.Collections;

public class CompareShoppingTest {
    // Create a variety of ingredients. Their relative ordering is placed in the tests themselves.
    ShoppingIngredient ingred1 = new ShoppingIngredient(
            "Apple",
            "Red",
            "1",
            "lb",
            "Raw Food");
    ShoppingIngredient ingred2 = new ShoppingIngredient(
            "Asparagus",
            "Green and prickly",
            "0.5",
            "lb",
            "Raw Food");
    ShoppingIngredient ingred3 = new ShoppingIngredient(
            "Burger",
            "80% lean 20% fat",
            "5",
            "lb",
            "Meat");
    ShoppingIngredient ingred4 = new ShoppingIngredient(
            "Turmeric",
            "Spice for Curry",
            "3",
            "lb",
            "Spice");

    /**
     * Create an ArrayList representing the ingredientList in Storage. Add ingredients
     * out of order.
     * @return ArrayList<Ingredient>
     */
    public ArrayList<ShoppingIngredient> makeIngredientList() {
        ArrayList<ShoppingIngredient> foodList = new ArrayList<>();
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
        ArrayList<ShoppingIngredient> foodList = makeIngredientList();
        assertTrue(foodList.get(0) == ingred1
                && foodList.get(1) == ingred2
                && foodList.get(2) == ingred4
                && foodList.get(3) == ingred3);
        CompareShopping compare1 = new CompareShopping("Category", 1);
        Collections.sort(foodList, compare1.returnComparator());
        // Ordering should now be Burger -> Apple -> Asparagus -> Turmeric
        // (Ties are then broken by alphabetical again)
        assertTrue(foodList.get(0) == ingred3
                && foodList.get(1) == ingred1
                && foodList.get(2) == ingred2
                && foodList.get(3) == ingred4);
        CompareShopping compare2 = new CompareShopping("Category", -1);
        Collections.sort(foodList, compare2.returnComparator());
        // Ordering should now be Turmeric -> Apple -> Asparagus -> Burger
        // (Ties are then broken by alphabetical again)
        assertTrue(foodList.get(0) == ingred4
                && foodList.get(1) == ingred1
                && foodList.get(2) == ingred2
                && foodList.get(3) == ingred3);
    }

    /**
     * Compare the titles of the 4 different ingredients. Firstly it will
     * sort in ascending order, then it will verify that the descending order
     * is correct.
     */
    @Test
    public void testCompareTitle() {
        // Ordering should be Apple -> Asparagus -> Turmeric -> Burger
        ArrayList<ShoppingIngredient> foodList = makeIngredientList();
        assertTrue(foodList.get(0) == ingred1
                && foodList.get(1) == ingred2
                && foodList.get(2) == ingred4
                && foodList.get(3) == ingred3);
        CompareShopping compare1 = new CompareShopping("Title", 1);
        Collections.sort(foodList, compare1.returnComparator());
        // Ordering should now be  Apple -> Asparagus -> Burger -> Turmeric
        // (Ties are then broken by alphabetical again)
        assertTrue(foodList.get(0) == ingred1
                && foodList.get(1) == ingred2
                && foodList.get(2) == ingred3
                && foodList.get(3) == ingred4);
        CompareShopping compare2 = new CompareShopping("Title", -1);
        Collections.sort(foodList, compare2.returnComparator());
        // Ordering should now be Turmeric -> Burger -> Apple -> Asparagus
        // (Ties are then broken by alphabetical again)
        assertTrue(foodList.get(0) == ingred4
                && foodList.get(1) == ingred3
                && foodList.get(2) == ingred2
                && foodList.get(3) == ingred1);
    }

    /**
     * Compare the quantities of the 4 different ingredients. Firstly it will
     * sort in ascending order, then it will verify that the descending order
     * is correct.
     */
    @Test
    public void testCompareQuantity() {
        // Ordering should be Apple -> Asparagus -> Turmeric -> Burger
        ArrayList<ShoppingIngredient> foodList = makeIngredientList();
        assertTrue(foodList.get(0) == ingred1
                && foodList.get(1) == ingred2
                && foodList.get(2) == ingred4
                && foodList.get(3) == ingred3);
        CompareShopping compare1 = new CompareShopping("Quantity", 1);
        Collections.sort(foodList, compare1.returnComparator());
        // Ordering should now be Burger -> Turmeric -> Apple -> Asparagus
        // (Ties are then broken by alphabetical again)
        assertTrue(foodList.get(0) == ingred3
                && foodList.get(1) == ingred4
                && foodList.get(2) == ingred1
                && foodList.get(3) == ingred2);
        CompareShopping compare2 = new CompareShopping("Quantity", -1);
        Collections.sort(foodList, compare2.returnComparator());
        // Ordering should now be  Asparagus -> Apple -> Turmeric -> Burger
        // (Ties are then broken by alphabetical again)
        assertTrue(foodList.get(0) == ingred2
                && foodList.get(1) == ingred1
                && foodList.get(2) == ingred4
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
        ArrayList<ShoppingIngredient> foodList = makeIngredientList();
        assertTrue(foodList.get(0) == ingred1
                && foodList.get(1) == ingred2
                && foodList.get(2) == ingred4
                && foodList.get(3) == ingred3);
        CompareShopping compare1 = new CompareShopping("Description", 1);
        Collections.sort(foodList, compare1.returnComparator());
        // Ordering should now be Burger -> Asparagus -> Apple -> Turmeric
        assertTrue(foodList.get(0) == ingred3
                && foodList.get(1) == ingred2
                && foodList.get(2) == ingred1
                && foodList.get(3) == ingred4);
        CompareShopping compare2 = new CompareShopping("Description", -1);
        Collections.sort(foodList, compare2.returnComparator());
        // Ordering should now be Turmeric -> Apple -> Asparagus -> Burger
        assertTrue(foodList.get(0) == ingred4
                && foodList.get(1) == ingred1
                && foodList.get(2) == ingred2
                && foodList.get(3) == ingred3);

    }



}
