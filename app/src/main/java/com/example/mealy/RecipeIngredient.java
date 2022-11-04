package com.example.mealy;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
/**
 * This is the specific object class for the Recipe Ingredient itself.
 * Contains getters and setters for the various attributes it has. This class
 * implements the Parcelable interface, to allow passing of this object as data
 * between fragments.
 */
public class RecipeIngredient implements Parcelable {
    private String title;
    private String description;
    private String amount;
    private String unit;
    private String category;


        // Getters
    /**
     * Get the title for this Recipe Ingredient.
     */
    public String getTitle() {
        return this.title;
    }

    /**
     * Get the description for this Recipe Ingredient.
     */
    public String getDescription() {
        return this.description;
    }

    /**
     * Get the amount for this Recipe Ingredient.
     */
    public String getAmount() {
        return this.amount;
    }

    /**
     * Get the unit for this Recipe Ingredient.
     */
    public String getUnit() {
        return this.unit;
    }

    /**
     * Get the category for this Recipe Ingredient.
     */
    public String getCategory() {
        return this.category;
    }

    // Setters
    /**
     * Set the title for this Recipe Ingredient.
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Set the description for this Recipe Ingredient.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Set the amount for this Recipe Ingredient.
     */
    public void setAmount(String amount) {
        this.amount = amount;
    }

    /**
     * Set the unit for this Recipe Ingredient.
     */
    public void setUnit(String unit) {
        this.unit = unit;
    }

    /**
     * Set the category for this Recipe Ingredient.
     */
    public void setCategory(String category) {
        this.category = category;
    }

    // Constructor
    /**
     * Constructor for the class.
     */
    public RecipeIngredient(String title, String description, String amount, String unit, String category) {
        this.title = title;
        this.description = description;
        this.amount = amount;
        this.unit = unit;
        this.category = category;
    }

    /**
     * Constructor for the class, given the parameter of a Parcel instance.
     */
    public RecipeIngredient(Parcel source) {
        this.title = source.readString();
        this.description = source.readString();
        this.amount = source.readString();
        this.unit = source.readString();
        this.category = source.readString();
    }

    /**
     * Method used to create a bitmask return value. When we need to put a FileDescriptor object into Parcelable, we specify
     * the CONTENTS_FILE_DESCRIPTOR field as the return value of this method. Not used for the current implementation.
     */
    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * Method that writes to the Parcel with the attributes of this instance.
     */
    @Override
    public void writeToParcel(Parcel parcel, int i) {
    parcel.writeString(this.title);
    parcel.writeString(this.description);
    parcel.writeString(this.amount);
    parcel.writeString(this.unit);
    parcel.writeString(this.category);

    }

    /**
     * Class that generates the instances of the RecipeIngredient class from a parcel.
     */
    public static final Creator<RecipeIngredient> CREATOR = new Creator<RecipeIngredient>() {
        /**
         * This method creates a new instance of the parcelable class, given from Parcelable.writeToParcel()
         */
        @Override
        public RecipeIngredient createFromParcel(Parcel in) {
            return new RecipeIngredient(in);
        }

        /**
         * Creates an array of the Parcelable class.
         */
        @Override
        public RecipeIngredient[] newArray(int size) {
            return new RecipeIngredient[size];
        }
    };


}
