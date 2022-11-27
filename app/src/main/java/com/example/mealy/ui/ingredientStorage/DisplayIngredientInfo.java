package com.example.mealy.ui.ingredientStorage;

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

import com.example.mealy.R;
import com.example.mealy.functions.DateFunc;
import com.example.mealy.functions.DeletePrompt;

/**
 * A fragment that takes in an ingredient object and displays its info
 */
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

    /**
     * Takes in the ingredient info and stores it into the class
     * @param ingredient The ingredient that you want to display the info of
     */
    public DisplayIngredientInfo(Ingredient ingredient) {
        this.ingredient = ingredient;
        name = ingredient.getName();
        description = ingredient.getDescription();
        amount = ingredient.getAmount();
        unit = " " + ingredient.getUnit(); // adding a space so it's not right next to the amount
        category = ingredient.getCategory();
        location = ingredient.getLocation();
        expiryDate = DateFunc.makeDateString(ingredient.getExpiryDate());
    }

    /**
     * Displays the ingredient name and gives the option to edit, close or delete the ingredient
     * @param savedInstanceState idk tbh
     * @return Builder for the Dialog
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        view = LayoutInflater.from(getActivity()).inflate(R.layout.display_ingredient_info, null);

        // Creates a dialog builder thing that lets you display information and click buttons and stuff
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.MyDialogTheme);
        return builder
                .setView(view)
                //.setTitle(name)
                .setNegativeButton("Close", null) // closes the dialog
                .setNeutralButton("Delete", new DialogInterface.OnClickListener() { // deletes the food item
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // Opens up a prompt to delete the ingredient from Firestore
                        new DeletePrompt("Ingredients", name).show(getParentFragmentManager(),"delete_prompt");
                    }
                })
                .setPositiveButton("Edit", new DialogInterface.OnClickListener() { // lets you edit the food item
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // opens up FoodEntry to modify the ingredient info
                        new IngredientAdd(ingredient).show(getParentFragmentManager(),"food_entry");
                    }
                }).create();
    }

    /**
     * Displays all the info of the the ingredient(except for the title, that is displayed by the Dialog)
     * @param inflater inflater of view
     * @param container container for view
     * @param savedInstanceState idk tbh
     * @return returns view
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        // gets the textview IDs
        TextView title1 = view.findViewById(R.id.ingredient_title);
        TextView description1 = view.findViewById(R.id.description);
        TextView amount1 = view.findViewById(R.id.quantityNumber);
        TextView unit1 = view.findViewById(R.id.quantityUnits);
        TextView category1 = view.findViewById(R.id.category);
        TextView location1 = view.findViewById(R.id.location);
        TextView expiryDate1 = view.findViewById(R.id.expiryDate);

        // Displays the info for the ingredient in each textview
        title1.setText(name);
        description1.setText(description);
        amount1.setText(amount);
        unit1.setText(unit);
        category1.setText(category);
        location1.setText(location);
        expiryDate1.setText(expiryDate);

        return view;
    }
}
