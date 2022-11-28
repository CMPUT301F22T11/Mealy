package com.example.mealy.ui.shoppingList;

import com.example.mealy.functions.General;

/**
 * Ingredients
 */
public class ShoppingIngredient {
    String name;
    String description;
    String quantity;
    String unit;
    String category;
    String unitCategory;

    public ShoppingIngredient(String name,
                      String description,
                      String quantity,
                      String unit,
                      String category, String unitCategory){
        this.name = name;
        this.description = description;
        this.quantity = quantity;
        this.unit = unit;
        this.category = category;
        this.unitCategory = unitCategory;

    }

    public String getName() {
        return General.blankIfVoid(name);
    }

    public String getDescription() {
        return General.blankIfVoid(description);
    }

    public String getQuantity() {
        return General.blankIfVoid(quantity);
    }

    public String getUnit() {
        return General.blankIfVoid(unit);
    }

    public String getCategory() {
        return General.blankIfVoid(category);
    }

    public String getUnitCategory() {
        return General.blankIfVoid(unitCategory);
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setQuantity(String amount) {
        this.quantity = amount;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setUnitCategory(String unitCategory) {
        this.unitCategory = unitCategory;
    }
}
