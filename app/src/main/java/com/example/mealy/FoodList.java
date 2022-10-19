package com.example.mealy;



import java.util.ArrayList;

public class FoodList  {
    private ArrayList<Ingredient> ingredients;

    public void addFood(Ingredient ingredient) {
        ingredients.add(ingredient);
    }

    public void removeFood(Ingredient ingredient) {
        ingredients.remove(ingredient);
    }

    public Ingredient getFood(int index) {
        return ingredients.get(index);
    }

}
