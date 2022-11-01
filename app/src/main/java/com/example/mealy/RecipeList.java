package com.example.mealy;
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
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.example.mealy.Recipe;
import com.example.mealy.ui.notifications.NotificationsFragment;

import java.util.ArrayList;

public class RecipeList extends ArrayAdapter<Recipe> {

    ConstraintLayout recipeEntryBox;
    DialogFragment recipeOptions;
    Context context;

    // invoke the suitable constructor of the ArrayAdapter class
    public RecipeList(@NonNull Context context, ArrayList<Recipe> arrayList) {

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
            currentItemView = LayoutInflater.from(getContext()).inflate(R.layout.recipe_list_view, parent, false);
        }

        // get the position of the view from the ArrayAdapter
        Recipe currentRecipe = getItem(position);

        // then according to the position of the view assign the desired image for the same
        ImageView recipeImage = currentItemView.findViewById(R.id.imageView);
        assert recipeImage != null;
        recipeImage.setImageResource(currentRecipe.getImageID());

        // then according to the position of the view assign the desired TextView 1 for the same
        TextView textView1 = currentItemView.findViewById(R.id.recipeNameDisplay);
        textView1.setText(currentRecipe.getTitle());

        // then according to the position of the view assign the desired TextView 2 for the same
        TextView textView2 = currentItemView.findViewById(R.id.servingDisplay);
        textView2.setText(currentRecipe.getServingsString());

        // set onClick functionality for each recipe box
        recipeEntryBox = currentItemView.findViewById(R.id.RecipeBoxConstraintLayout);

        // set each recipe box so that we can open up another dialog
        recipeEntryBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // get the recipe data of the box we selected
                System.out.println("Clicked the entry box");

                // launch dialog fragment from within list
                // https://stackoverflow.com/questions/18436524/launch-a-dialog-fragment-on-button-click-from-a-custom-base-adaptergetview-img
                FragmentActivity activity = (FragmentActivity)(context);
                FragmentManager fm = activity.getSupportFragmentManager();

                recipeOptions = new RecipeClickFragment(); // later maybe pass things like position of item in list
                recipeOptions.show(fm, "test");
            }
        });


        // then return the recyclable view
        return currentItemView;
    }
}