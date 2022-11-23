package com.example.mealy.ui.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.DialogFragment;

import com.example.mealy.R;

import java.util.ArrayList;

/**
 * A class representing a meal list adapter.
 * This allows us to modify and track recipes in our list.
 */
public class MealList extends ArrayAdapter<Meal> {

    ConstraintLayout recipeEntryBox;
    DialogFragment recipeOptions;
    Context context;

    // invoke the suitable constructor of the ArrayAdapter class
    public MealList(@NonNull Context context, ArrayList<Meal> arrayList) {

        // pass the context and arrayList for the super
        // constructor of the ArrayAdapter class
        super(context, 0, arrayList);
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        // convertView which is recyclable view
        View currentItemView = convertView;

        // of the recyclable view is null then inflate the custom layout for the same
        if (currentItemView == null) {
            currentItemView = LayoutInflater.from(getContext()).inflate(R.layout.mealplan_list_view, parent, false);
        }

        // get the position of the view from the ArrayAdapter
        Meal currentMeal = getItem(position);

        // then according to the position of the view assign the desired TextView 1 for the same
        TextView textView1 = currentItemView.findViewById(R.id.recipeNameDisplay);
        textView1.setText(currentMeal.getTitle());

        // then according to the position of the view assign the desired TextView 2 for the same
        TextView textView2 = currentItemView.findViewById(R.id.servingDisplay);
        textView2.setText(currentMeal.getServingsString());

        // then return the recyclable view
        return currentItemView;
    }
}
