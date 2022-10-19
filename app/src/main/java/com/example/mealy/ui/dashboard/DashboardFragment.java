package com.example.mealy.ui.dashboard;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.mealy.Ingredient;
import com.example.mealy.IngredientList;
import com.example.mealy.R;
import com.example.mealy.databinding.FragmentDashboardBinding;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;

public class DashboardFragment extends Fragment {

    private FragmentDashboardBinding binding;

    private Spinner spinner;
    private LinearLayout ingredients;

    @RequiresApi(api = Build.VERSION_CODES.O)
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        DashboardViewModel dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        spinner = root.findViewById(R.id.ingredientsort);
        ingredients = root.findViewById(R.id.storage);

        ArrayList<String> options = new ArrayList<>(Arrays.asList("Name", "Desc", "Exp", "Location", "Category"));
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item, options);
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinner.setAdapter(adapter);

        ArrayList<Ingredient> foodList = new ArrayList<>();
        foodList.add(new Ingredient("Tomato",
                "Red and big",
                1, "Whole",
                "Stew",
                "Fridge",
                LocalDate.parse("2022-12-03")));

        //ingredients.setAdapter(cityAdapter);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}