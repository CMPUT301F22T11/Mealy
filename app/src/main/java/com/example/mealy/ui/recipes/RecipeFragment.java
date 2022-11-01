package com.example.mealy.ui.recipes;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.mealy.Ingredient;
import com.example.mealy.R;
import com.example.mealy.Recipe;
import com.example.mealy.RecipeClickFragment;
import com.example.mealy.RecipeList;
import com.example.mealy.comparators.recipes.CompareRecipes;
import com.example.mealy.databinding.FragmentNotificationsBinding;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

// This is where I will temporarily put my RecipeList view
public class RecipeFragment extends Fragment {

    private FragmentNotificationsBinding binding;

    private Spinner sortSpinner; // for selecting sorting category
    private ListView recipeListView; // list of recipes
    private ConstraintLayout recipeEntryBox; // each individual recipe contained in a box

    DialogFragment recipeOptions;

    int recipeIndex;
    int asc; // for sort order

    @RequiresApi(api = Build.VERSION_CODES.O)
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState)
    {

        RecipeViewModel notificationsViewModel =
                new ViewModelProvider(this).get(RecipeViewModel.class);

        binding = FragmentNotificationsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        asc = 1;
        // have some flip button to change sort order

        // assign UI elements
        sortSpinner = root.findViewById(R.id.recipesort);
        recipeListView = root.findViewById(R.id.recipestorage);

        // create sorting spinner with sort categories
        ArrayList<String> options = new ArrayList<>(Arrays.asList("Title", "Prep Time", "Servings", "Category"));
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item, options);
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        sortSpinner.setAdapter(adapter);

        // add sample ingredient for recipe list
        Ingredient rat_hair = new Ingredient("rat hair",
                "hair from a rat",
                100,
                "strands",
                "protein",
                "pantry",
                "2025-12-13");

        List ingredientList = new ArrayList();
        ingredientList.add(rat_hair);

        // add sample items
        ArrayList<Recipe> recipeArrayList = new ArrayList<>();
        recipeArrayList.add(new Recipe("Meat Rat",
                "Yummy for my tummy",
                5, 3, 10,
                "Grilled",
                R.drawable.meat_rat,
                ingredientList));

        recipeArrayList.add(new Recipe("Meat Skull",
                "Going to hell",
                3, 4, 30,
                "Fried",
                R.drawable.meatskull,
                ingredientList));

        recipeArrayList.add(new Recipe("Rat Hair",
                "Going to hell",
                3, 4, 30,
                "Fried",
                R.drawable.rathair,
                ingredientList));


        // notificationsViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        // Create the adapter and set it to the arraylist
        RecipeList recipeAdapter = new RecipeList(getActivity(), recipeArrayList);

        // create the instance of the ListView to set the recipe adapter
        ListView storage = root.findViewById(R.id.recipestorage);
        storage.setAdapter(recipeAdapter);



        // storage is the listview of our recipes
        // we can set it's on click method so when we click we associate recipes with it

        storage.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                // get recipe that user clicked on
                recipeIndex = i;
                Recipe selectedRecipe = recipeArrayList.get(i);

                Bundle bundle = new Bundle();
                bundle.putSerializable("Recipe", selectedRecipe);

                // launch dialog fragment from within list
                // https://stackoverflow.com/questions/18436524/launch-a-dialog-fragment-on-button-click-from-a-custom-base-adaptergetview-img

                recipeOptions = new RecipeClickFragment();
                recipeOptions.setArguments(bundle); // pass recipe info
                recipeOptions.show(getChildFragmentManager(), "test");
            }
        });



        // set the spinner to sort things correctly
        sortSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedItem = sortSpinner.getItemAtPosition(i).toString();
                CompareRecipes compare = new CompareRecipes(selectedItem, asc);

                Collections.sort(recipeArrayList, compare.returnComparator());
                recipeAdapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        return root;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}