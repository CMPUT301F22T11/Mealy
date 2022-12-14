package com.example.mealy.ui.shoppingList;
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
public class ShoppingListAdd extends ArrayAdapter<ShoppingIngredient> {

    /**
     * invoke the suitable constructor of the ArrayAdapter class
     * @param context of the application
     * @param arrayList arraylist of shopping ingredients
     */
    public ShoppingListAdd(@NonNull Context context, ArrayList<ShoppingIngredient> arrayList) {

        // pass the context and arrayList for the super
        // constructor of the ArrayAdapter class
        super(context, 0, arrayList);
    }

    /**
     * gets the view of the fragment
     * @param position of the selected item
     * @param convertView
     * @param parent
     * @return
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        // convertView which is recyclable view
        View currentItemView = convertView;

        // of the recyclable view is null then inflate the custom layout for the same
        if (currentItemView == null) {
            currentItemView = LayoutInflater.from(getContext()).inflate(R.layout.shopping_list_add_view, parent, false);
        }

        // get the position of the view from the ArrayAdapter
        ShoppingIngredient currentIngredient = getItem(position);

        // then according to the position of the view assign the desired image for the same
        // currently disabled, image adding may be considered for ingredients at a later date.
        ImageView ingredientImage = currentItemView.findViewById(R.id.shoppingListAddImageView);
        assert ingredientImage != null;

        // then according to the position of the view assign the desired TextView 1 for the same
        TextView textView1 = currentItemView.findViewById(R.id.shoppingListAddIngredientName);
        textView1.setText(currentIngredient.getName());

        // then according to the position of the view assign the desired TextView 2 for the same
        TextView textView2 = currentItemView.findViewById(R.id.ShoppingListAddCategory);
        textView2.setText(currentIngredient.getCategory());

        // then according to the position of the view assign the desired TextView 3 for the same
        TextView textView3 = currentItemView.findViewById(R.id.ShoppingListAddQuantity);
        textView3.setText(currentIngredient.getQuantity() + " " + currentIngredient.getUnit());

        // then according to the position of the view assign the desired TextView 3 for the same
        TextView textView4 = currentItemView.findViewById(R.id.shoppingListAddDescription);
        textView4.setText(currentIngredient.getDescription());

        // then return the recyclable view
        return currentItemView;
    }
}
