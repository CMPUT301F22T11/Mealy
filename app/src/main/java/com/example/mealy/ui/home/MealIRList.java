package com.example.mealy.ui.home;

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
import com.example.mealy.ui.ingredientStorage.Ingredient;
import com.example.mealy.ui.recipes.Recipe;
import com.example.mealy.ui.recipes.RecipeIngredient;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * This class represents the Recipe Ingredient list that gets created for a given recipe. This allows us to track the
 * total amount of ingredients for the recipe, and allows us to edit/delete it.
 */
public class MealIRList extends ArrayAdapter<Object> {

    private ArrayList<Object> objects;
    private Context context;

    public MealIRList(Context context, ArrayList<Object> objects){
        super(context,0, objects);
        this.objects = objects;
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
            view = LayoutInflater.from(getContext()).inflate(R.layout.ir_entry, parent,false);
        }

        Object thisOb = getItem(position);

        if (thisOb instanceof Ingredient) {

            TextView ingredientName = view.findViewById(R.id.ir_name);
            TextView servingName = view.findViewById(R.id.ir_servings);
            Ingredient thisIngredient = (Ingredient) thisOb;
            String oldName = thisIngredient.getName();
            String servingSize = oldName.substring(oldName.length() - 1);

            thisIngredient.setName(oldName.substring(0,oldName.length()-1));

            ingredientName.setText(thisIngredient.getName());
            servingName.setText(servingSize);


        }
        else if (thisOb instanceof Object) {
            TextView recipeName = view.findViewById(R.id.ir_name);
            TextView recipeServing = view.findViewById(R.id.ir_servings);
            recipeName.setText(((Recipe) thisOb).getTitle());
            recipeServing.setText(Integer.toString(((Recipe) thisOb).getServings()));
        }

        return view;

    }
}
