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

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * This class represents the Recipe Ingredient list that gets created for a given recipe. This allows us to track the
 * total amount of ingredients for the recipe, and allows us to edit/delete it.
 */
public class RecipeIngredientList extends ArrayAdapter<RecipeIngredient> {

    private ArrayList<RecipeIngredient> ingredients;
    private Context context;

    /**
     * Constructor for the class.
     * @param context context of the current activity
     * @param ingredients The arraylist of Recipe Ingredients.
     */
    public RecipeIngredientList(Context context, ArrayList<RecipeIngredient> ingredients){
        super(context,0, ingredients);
        this.ingredients = ingredients;
        this.context = context;
    }


    /**
     * Method that constructs the view for the list. Constructs all the xml elements (TextViews, ImageButtons, etc.). Currently,
     * the delete function for the entries of the list is not working.
     *
     * @param convertView THe old view to reuse
     * @param parent The parent that this view will eventually be attached to
     * @param position Get the current position of the item in the list within the adapter
     * @return The view is created
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


        ingredientName.setText(ingredient.getTitle());
        amountName.setText(ingredient.getAmount());
        unitName.setText(ingredient.getUnit());

        return view;

    }
}
