package com.example.mealy.ui.notifications;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.mealy.Ingredient;
import com.example.mealy.R;
import com.example.mealy.Recipe;
import com.example.mealy.RecipeList;
import com.example.mealy.comparators.CompareRecipes;
import com.example.mealy.databinding.FragmentNotificationsBinding;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

// This is where I will temporarily put my RecipeList view
public class NotificationsFragment extends Fragment {

    private FragmentNotificationsBinding binding;

    private Spinner sortSpinner; // for selecting sorting category
    private ListView recipeListView; // for selecting list of recipes

    @RequiresApi(api = Build.VERSION_CODES.O)
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        NotificationsViewModel notificationsViewModel =
                new ViewModelProvider(this).get(NotificationsViewModel.class);

        binding = FragmentNotificationsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

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
                LocalDate.parse("2025-12-13"),
                R.drawable.rathair);

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

        // notificationsViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        // Create the adapter and set it to the arraylist
        RecipeList recipeList = new RecipeList(getActivity(), recipeArrayList);

        // create the instance of the ListView to set the numbersViewAdapter
        ListView storage = root.findViewById(R.id.recipestorage);

        // set the numbersViewAdapter for ListView
        storage.setAdapter(recipeList);

        // set the spinner to sort things correctly
        sortSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedItem = sortSpinner.getItemAtPosition(i).toString();
                CompareRecipes compare = new CompareRecipes(selectedItem);

                Collections.sort(recipeArrayList, compare.returnComparator());
                recipeList.notifyDataSetChanged();
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