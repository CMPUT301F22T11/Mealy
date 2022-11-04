package com.example.mealy.ui.dashboard;
// taken from https://www.geeksforgeeks.org/custom-arrayadapter-with-listview-in-android/

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.mealy.R;

import java.util.ArrayList;

/**
 * This class extends from ArrayAdapter, and is used to insert the ingredient into the ingredientList
 */
public class IngredientList extends ArrayAdapter<Ingredient> {

    // invoke the suitable constructor of the ArrayAdapter class
    public IngredientList(@NonNull Context context, ArrayList<Ingredient> arrayList) {

        // pass the context and arrayList for the super
        // constructor of the ArrayAdapter class
        super(context, 0, arrayList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        // convertView which is recyclable view
        View currentItemView = convertView;

        // of the recyclable view is null then inflate the custom layout for the same
        if (currentItemView == null) {
            currentItemView = LayoutInflater.from(getContext()).inflate(R.layout.custom_list_view, parent, false);
        }

        // get the position of the view from the ArrayAdapter
        Ingredient currentIngredient = getItem(position);

        // then according to the position of the view assign the desired image for the same
        // currently disabled, image adding may be considered for ingredients at a later date.
        ImageView ingredientImage = currentItemView.findViewById(R.id.imageView);
        assert ingredientImage != null;
        //ingredientImage.setImageResource(R.drawable.meat_rat);

        // then according to the position of the view assign the desired TextView 1 for the same
        TextView textView1 = currentItemView.findViewById(R.id.textView1);
        textView1.setText(currentIngredient.getName());

        // then according to the position of the view assign the desired TextView 2 for the same
        TextView textView2 = currentItemView.findViewById(R.id.textView2);
        textView2.setText(currentIngredient.getExpiryDate());

        // then return the recyclable view
        return currentItemView;
    }
}
