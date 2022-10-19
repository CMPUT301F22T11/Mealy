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
    LocalDate expiration;

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

    public LocalDate getExpiration() {
        return expiration;
    }

    public void setExpiration(LocalDate expiration) {
        this.expiration = expiration;
    }

    public Ingredient(String name, String description, int amount, String unit, String category, String location, LocalDate localDate){
        this.name = name;
        this.description = description;
        this.amount = amount;
        this.unit = unit;
        this.category = category;
        this.location = location;
        this.expiration = localDate;
    }

}
