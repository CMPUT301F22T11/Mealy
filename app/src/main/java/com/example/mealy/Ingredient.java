package com.example.mealy;

import java.time.LocalDate;

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
    //LocalDate expiration;
    //int imageID;

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
        //this.expiration = localDate;
        //this.imageID = imageID;

    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getAmount() {
        return amount;
    }

    public String getUnit() {
        return unit;
    }

    public String getCategory() {
        return category;
    }

    public String getLocation() {
        return location;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public String getUnitCategory() {return unitCategory;};

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

    public void setUnitCategory(String unitCategory) {this.unitCategory = unitCategory;};
}
