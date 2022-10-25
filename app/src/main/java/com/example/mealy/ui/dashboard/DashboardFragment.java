package com.example.mealy.ui.dashboard;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
import com.example.mealy.comparators.Compare;
import com.example.mealy.databinding.FragmentDashboardBinding;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class DashboardFragment extends Fragment {

    private FragmentDashboardBinding binding;

    private Spinner spinner;
    private ListView ingredients;

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


        /*
        String selected = spinner.getSelectedItem().toString();
        Compare compare = new Compare(selected);

         */

        ArrayList<Ingredient> foodList = new ArrayList<>();
        foodList.add(new Ingredient("Meat Rat",
                "Yummy for my tummy",
                1, "Whole",
                "Baked",
                "Fridge",
                LocalDate.parse("2022-12-03"),
                R.drawable.meat_rat));

        foodList.add(new Ingredient("Meat Skull",
                "Going to hell",
                1, "Whole",
                "Fried",
                "Head",
                LocalDate.parse("2023-12-03"),
                R.drawable.meatskull));

        //ingredients.setAdapter(cityAdapter);

        // Now create the instance of the NumebrsViewAdapter and pass
        // the context and arrayList created above
        IngredientList ingredientList = new IngredientList(getActivity(), foodList);

        // create the instance of the ListView to set the numbersViewAdapter
        ListView storage = root.findViewById(R.id.storage);

        // set the numbersViewAdapter for ListView
        storage.setAdapter(ingredientList);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedItem = spinner.getItemAtPosition(i).toString();
                Compare compare = new Compare(selectedItem);

                Collections.sort(foodList, compare.returnComparator());
                ingredientList.notifyDataSetChanged();
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