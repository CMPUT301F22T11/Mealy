package com.example.mealy.ui.shoppingList;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

/**
 * This is the shopping view model class for the shopping list fragment
 */
public class ShoppingViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    /**
     * This is the constructor
     */
    public ShoppingViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is the shopping list fragment");
    }

    /**
     * This gets the text of the data
     * @return returns the text
     */
    public LiveData<String> getText() {
        return mText;
    }
}
