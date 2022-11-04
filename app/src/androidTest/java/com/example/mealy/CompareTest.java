
package com.example.mealy;

import org.junit.Test;
import static org.junit.Assert.*;

import com.example.mealy.comparators.Compare;

import java.util.ArrayList;
import java.util.Collections;

public class CompareTest {

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

    public ArrayList<Ingredient> makeIngredientList() {
        ArrayList<Ingredient> foodList = new ArrayList<>();
        foodList.add(ingred1);
        foodList.add(ingred2);
        foodList.add(ingred4);
        foodList.add(ingred3);
        return foodList;
    }

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

    @Test
    public void testCompareDesc() {
        // Ordering should be Apple -> Asparagus -> Turmeric -> Burger
        ArrayList<Ingredient> foodList = makeIngredientList();
        assertTrue(foodList.get(0) == ingred1
                && foodList.get(1) == ingred2
                && foodList.get(2) == ingred4
                && foodList.get(3) == ingred3);
        Compare compare1 = new Compare("Desc", 1);
        Collections.sort(foodList, compare1.returnComparator());
        // Ordering should now be Burger -> Asparagus -> Apple -> Turmeric
        assertTrue(foodList.get(0) == ingred3
                && foodList.get(1) == ingred2
                && foodList.get(2) == ingred1
                && foodList.get(3) == ingred4);
        Compare compare2 = new Compare("Desc", -1);
        Collections.sort(foodList, compare2.returnComparator());
        // Ordering should now be Turmeric -> Apple -> Asparagus -> Burger
        assertTrue(foodList.get(0) == ingred4
                && foodList.get(1) == ingred1
                && foodList.get(2) == ingred2
                && foodList.get(3) == ingred3);

    }

    @Test
    public void testCompareExp() {
        // Ordering should be Apple -> Asparagus -> Turmeric -> Burger
        ArrayList<Ingredient> foodList = makeIngredientList();
        assertTrue(foodList.get(0) == ingred1
                && foodList.get(1) == ingred2
                && foodList.get(2) == ingred4
                && foodList.get(3) == ingred3);
        Compare compare1 = new Compare("Exp", 1);
        Collections.sort(foodList, compare1.returnComparator());
        // Ordering should now be Asparagus -> Apple -> Burger -> Turmeric
        assertTrue(foodList.get(0) == ingred2
                && foodList.get(1) == ingred1
                && foodList.get(2) == ingred3
                && foodList.get(3) == ingred4);
        Compare compare2 = new Compare("Exp", -1);
        Collections.sort(foodList, compare2.returnComparator());
        // Ordering should now be Turmeric -> Burger -> Apple -> Asparagus
        assertTrue(foodList.get(0) == ingred4
                && foodList.get(1) == ingred3
                && foodList.get(2) == ingred1
                && foodList.get(3) == ingred2);

    }

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
/*
public class CityListTest {
    private CityList mockCityList() {
        CityList cityList = new CityList();
        cityList.add(mockCity());
        return cityList;
    }
    private City mockCity() {
        return new City("Edmonton", "Alberta");
    }

    @Test
    void testAdd() {
        CityList cityList = mockCityList();
        assertEquals(1, cityList.getCities().size());
        City city = new City("Regina", "Saskatchewan");
        cityList.add(city);
        assertEquals(2, cityList.getCities().size());
        assertTrue(cityList.getCities().contains(city));
    }

    @Test
    void testAddException() {
        CityList cityList = mockCityList();
        City city = new City("Yellowknife", "Northwest Territories");
        cityList.add(city);
        assertThrows( IllegalArgumentException.class, () -> {
            cityList.add(city); });
    }

    @Test
    void testGetCities() {
        CityList cityList = mockCityList();
        assertEquals(0,
                mockCity().compareTo(cityList.getCities().get(0)));
        City city = new City("Charlottetown", "Prince Edward Island");
        cityList.add(city);
        assertEquals(0, city.compareTo(cityList.getCities().get(0)));
        assertEquals(0,
                mockCity().compareTo(cityList.getCities().get(1)));
    }

    @Test
    void testHasCity() {
        CityList cityList = mockCityList();
        City city = new City("Charlottetown", "Prince Edward Island");
        assertFalse(cityList.hasCity(city));
        cityList.add(city);
        assertTrue(cityList.hasCity(city));
    }

    @Test
    void testDeleteCity() {
        CityList cityList = mockCityList();
        City city = new City("Charlottetown", "Prince Edward Island");
        cityList.add(city);
        cityList.delete(city);
        assertFalse(cityList.hasCity(city));
    }

    @Test
    void testDeleteException() {
        CityList cityList = mockCityList();
        City city = new City("Charlottetown", "Prince Edward Island");
        assertThrows( IllegalArgumentException.class, () -> {
            cityList.delete(city); });
    }

    @Test
    void testCountCities() {
        CityList cityList = mockCityList();
        assertEquals(0, cityList.countCities());
        City city = new City("Charlottetown", "Prince Edward Island");
        cityList.add(city);
        assertEquals(1, cityList.countCities());

    }

}
 */