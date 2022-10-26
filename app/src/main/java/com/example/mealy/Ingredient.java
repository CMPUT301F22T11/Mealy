package com.example.mealy;

/**
 * Ingredients
 */
public class Ingredient {
    String name;
    String description;
    int amount;
    String unit;
    String category;
    String expiryDate;

    public Ingredient(String name, String description, int amount, String unit, String category, String expiryDate){
        this.name = name;
        this.description = description;
        this.amount = amount;
        this.unit = unit;
        this.category = category;
        this.expiryDate = expiryDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }
}
