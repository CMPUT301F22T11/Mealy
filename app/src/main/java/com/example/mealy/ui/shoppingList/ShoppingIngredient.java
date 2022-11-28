package com.example.mealy.ui.shoppingList;

import com.example.mealy.functions.General;

/**
 * Ingredients for the shopping tab
 */
public class ShoppingIngredient {
    String name;
    String description;
    String quantity;
    String unit;
    String category;

    /**
     * Constructor
     * @param name of the specific ingredient
     * @param description of the specific ingredient
     * @param quantity of the specific ingredient
     * @param unit of the specific ingredient
     * @param category of the specific ingredient
     */
    public ShoppingIngredient(String name,
                      String description,
                      String quantity,
                      String unit,
                      String category){
        this.name = name;
        this.description = description;
        this.quantity = quantity;
        this.unit = unit;
        this.category = category;

    }

    /**
     * Gets the name of the item
     * @return the name of the item
     */
    public String getName() {
        return General.blankIfVoid(name);
    }

    /**
     * Gets the description of the item
     * @return the description of the item
     */
    public String getDescription() {
        return General.blankIfVoid(description);
    }

    /**
     * Gets the quantity of the item
     * @return the quantity of the item
     */
    public String getQuantity() {
        return General.blankIfVoid(quantity);
    }

    /**
     * Gets the unit of the item
     * @return the unit of the item
     */
    public String getUnit() {
        return General.blankIfVoid(unit);
    }

    /**
     * Gets the category of the item
     * @return the category of the item
     */
    public String getCategory() {
        return General.blankIfVoid(category);
    }

    /**
     * sets the name of the item
     * @param name of the item
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * sets the description of the item
     * @param description of the item
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * sets the quantity of the item
     * @param amount of the item
     */
    public void setQuantity(String amount) {
        this.quantity = amount;
    }

    /**
     * sets the unit of the item
     * @param unit of the item
     */
    public void setUnit(String unit) {
        this.unit = unit;
    }

    /**
     * sets the category of the item
     * @param category of the item
     */
    public void setCategory(String category) {
        this.category = category;
    }

}
