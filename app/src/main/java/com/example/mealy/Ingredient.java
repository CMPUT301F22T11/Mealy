package com.example.mealy;

import java.time.LocalDate;

/**
 * Ingredients
 */
public class Ingredient {
    String name;
    String description;
    int amount;
    String unit;
    String category;
    String location;
    String expiryDate;
    //LocalDate expiration;
    //int imageID;

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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    /*
    public LocalDate getExpiration() {
        return expiration;
    }

    public void setExpiration(LocalDate expiration) {
        this.expiration = expiration;
    }

    public String getExpirationInText() {
        return expiration.toString();
    }

    public int getImageID() {
        return imageID;
    }
     */

    public Ingredient(String name,
                      String description,
                      int amount,
                      String unit,
                      String category,
                      String location,
                      String expiryDate){
        this.name = name;
        this.description = description;
        this.amount = amount;
        this.unit = unit;
        this.category = category;
        this.location = location;
        this.expiryDate = expiryDate;
        //this.expiration = localDate;
        //this.imageID = imageID;
    }

}
