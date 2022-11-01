package com.example.mealy;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.mealy.functions.DateFunc;
import com.example.mealy.functions.DeletePrompt;

public class DisplayIngredientInfo extends DialogFragment {

    private final String name;
    private final String description;
    private final String amount;
    private final String unit;
    private final String category;
    private final String location;
    private final String expiryDate;
    private final Ingredient ingredient;

    View view;
    private DisplayIngredientInfo fragment = this;

    public DisplayIngredientInfo(Ingredient ingredient) {
        this.ingredient = ingredient;
        name = ingredient.getName();
        description = ingredient.getDescription();
        amount = ingredient.getAmount();
        unit = ingredient.getUnit();
        category = ingredient.getCategory();
        location = ingredient.getLocation();
        expiryDate = DateFunc.MakeDateString(ingredient.getExpiryDate());
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        view = LayoutInflater.from(getActivity()).inflate(R.layout.display_ingredient_info, null);

        // Creates a dialog builder thing that lets you display information and click buttons and stuff
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        return builder
                .setView(view)
                .setTitle(name)
                .setNegativeButton("Close", null) // closes the dialog
                .setNeutralButton("Delete", new DialogInterface.OnClickListener() { // deletes the food item
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        new DeletePrompt("Ingredients", name).show(getParentFragmentManager(),"delete_prompt");
                    }
                })
                .setPositiveButton("Edit", new DialogInterface.OnClickListener() { // lets you edit the food item
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        new FoodEntry(ingredient).show(getParentFragmentManager(),"food_entry");
                    }
                }).create();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        TextView description1 = view.findViewById(R.id.description);
        TextView amount1 = view.findViewById(R.id.quantityNumber);
        TextView unit1 = view.findViewById(R.id.quantityUnits);
        TextView category1 = view.findViewById(R.id.category);
        TextView location1 = view.findViewById(R.id.location);
        TextView expiryDate1 = view.findViewById(R.id.expiryDate);

        description1.setText(description);
        amount1.setText(amount);
        unit1.setText(unit);
        category1.setText(category);
        location1.setText(location);
        expiryDate1.setText(expiryDate);

        return view;
    }
}
