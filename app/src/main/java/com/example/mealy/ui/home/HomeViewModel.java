package com.example.mealy.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

/**
 * This is the view model of the meal plan fragment which
 * displays the string in the meal plan tab.
 */
public class HomeViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public HomeViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Meal Plan");
    }

    public LiveData<String> getText() {
        return mText;
    }
}