package com.example.mealy;

import android.app.Dialog;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import java.util.ArrayList;

/**
 * This is just to test your fragment. It's not a proper fragment
 */

public class TestFragment extends DialogFragment {
    int layout;

    public TestFragment(int layout) {
        // Required empty public constructor
        this.layout = layout;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = getLayoutInflater().inflate(R.layout.food_entry, null);

        return inflater.inflate(layout, container, false);
    }
}