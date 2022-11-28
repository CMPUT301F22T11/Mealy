package com.example.mealy.ui.ingredientStorage;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.mealy.functions.General;
import com.example.mealy.ui.recipes.RecipeIngredient;

/**
 * Ingredients
 */
public class Ingredient implements Parcelable {
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

    /**
     * Constructor for the class, given the parameter of a Parcel instance.
     * @param source The Parcel in which the recipe ingredient gets all its attributes.
     */
    public Ingredient(Parcel source) {
        this.name = source.readString();
        this.description = source.readString();
        this.amount = source.readString();
        this.unit = source.readString();
        this.unitCategory = source.readString();
        this.category = source.readString();
        this.location = source.readString();
        this.expiryDate = source.readString();
    }

    /**
     * Method used to create a bitmask return value. When we need to put a FileDescriptor object into Parcelable, we specify
     * the CONTENTS_FILE_DESCRIPTOR field as the return value of this method. Not used for the current implementation.
     * @return Returns a bitmask
     */
    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * Method that writes to the Parcel with the attributes of this instance.
     * @param parcel The Parcel in which the recipe ingredient should be written
     * @param i Flag that provides additional details in how the object should be written
     */
    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.name);
        parcel.writeString(this.description);
        parcel.writeString(this.amount);
        parcel.writeString(this.unit);
        parcel.writeString(this.category);
        parcel.writeString(this.unitCategory);
        parcel.writeString(this.location);
        parcel.writeString(this.expiryDate);
    }

    /**
     * Class that generates the instances of the RecipeIngredient class from a parcel.
     */
    public static final Creator<Ingredient> CREATOR = new Creator<Ingredient>() {
        /**
         * This method creates a new instance of the parcelable class, given from Parcelable.writeToParcel()
         * @param in Parcel to create class
         * @return Returns the created recipe ingredient class
         */
        @Override
        public Ingredient createFromParcel(Parcel in) {
            return new Ingredient(in);
        }

        /**
         * Creates an array of the Parcelable class.
         * @param size specify size of array
         * @return Returns the created array
         */
        @Override
        public Ingredient[] newArray(int size) {
            return new Ingredient[size];
        }
    };
}
