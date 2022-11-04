package com.example.mealy.ui.recipes;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.mealy.R;

import java.util.ArrayList;

/**
 * This class represents the Recipe Ingredient list that gets created for a given recipe. This allows us to track the
 * total amount of ingredients for the recipe, and allows us to edit/delete it.
 */
public class RecipeIngredientList extends ArrayAdapter<RecipeIngredient> {

    private ArrayList<RecipeIngredient> ingredients;
    private Context context;

    /**
     * Constructor for the class. Parameters: context of the current activity, and an arraylist of Recipe Ingredients.
     */
    public RecipeIngredientList(Context context, ArrayList<RecipeIngredient> ingredients){
        super(context,0, ingredients);
        this.ingredients = ingredients;
        this.context = context;
    }


    /**
     * Method that constructs the view for the list. Constructs all the xml elements (TextViews, ImageButtons, etc.). Currently,
     * the delete function for the entries of the list is not working.
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//        return super.getView(position, convertView, parent);
        View view = convertView;

        if(view == null){
            view = LayoutInflater.from(getContext()).inflate(R.layout.r_i_entry, parent,false);
        }

        RecipeIngredient ingredient = getItem(position);

        TextView ingredientName = view.findViewById(R.id.r_i_name);
        TextView amountName = view.findViewById(R.id.r_i_amount);
        TextView unitName = view.findViewById(R.id.r_i_unit);
        ImageButton deleteIngredient = view.findViewById(R.id.r_i_delete);

        ingredientName.setText(ingredient.getTitle());
        amountName.setText(ingredient.getAmount());
        unitName.setText(ingredient.getUnit());


        deleteIngredient.setOnClickListener(new View.OnClickListener() {
            /**
             * This method is currently not working; when the user clicks on the delete button for the
             * specific entry, this method creates a dialog that gives the user a choice to continue with their action.
             * If they select yes, then the entry would be deleted from the list. However, this function has not
             * been implemented yet.
             */
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(getContext())
                        .setTitle("Delete Ingredient")
                        .setMessage("Are you sure you want to delete this ingredient?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                ingredients.remove(position);

                            }
                        })
                        .setNegativeButton(android.R.string.no, null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        });

        return view;

    }
}
