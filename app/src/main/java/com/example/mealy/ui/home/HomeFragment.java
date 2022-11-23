package com.example.mealy.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.mealy.R;
import com.example.mealy.databinding.FragmentHomeBinding;
import com.example.mealy.ui.recipes.Recipe;
import com.example.mealy.ui.recipes.RecipeList;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    private ListView mealPlanListView; // list of mealplans

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textHome;
        homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        //
        mealPlanListView = root.findViewById(R.id.mealplanlistview);

        // list that will store all our recipe objects
        ArrayList<Recipe> recipeArrayList = new ArrayList<>();

        // Create the adapter and set it to the arraylist
        RecipeList recipeAdapter = new RecipeList(getActivity(), recipeArrayList);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}