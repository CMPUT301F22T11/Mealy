package com.example.mealy.ui.ingredientStorage;

import com.example.mealy.functions.General;

/**
 * Ingredients
 */
public class Ingredient {
    String name;
    String description;
    String amount;
    String unit;
    String unitCategory;
    String category;
    String location;
    String expiryDate;

    public Ingredient(String name,
                      String description,
                      String amount,
                      String unit,
                      String unitCategory,
                      String category,
                      String location,
                      String expiryDate){
        this.name = name;
        this.description = description;
        this.amount = amount;
        this.unit = unit;
        this.unitCategory = unitCategory;
        this.category = category;
        this.location = location;
        this.expiryDate = expiryDate;

    }

    public String getName() {
        return General.blankIfVoid(name);
    }

    public String getDescription() {
        return General.blankIfVoid(description);
    }

    public String getAmount() {
        return General.blankIfVoid(amount);
    }

    public String getUnit() {
        return General.blankIfVoid(unit);
    }

    public String getCategory() {
        return General.blankIfVoid(category);
    }

    public String getLocation() {
        return General.blankIfVoid(location);
    }

    public String getExpiryDate() {
        return General.blankIfVoid(expiryDate);
    }

    public String getUnitCategory() {return General.blankIfVoid(unitCategory);}

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public void setUnitCategory(String unitCategory) {this.unitCategory = unitCategory;}
}
