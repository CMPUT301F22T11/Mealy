package com.example.mealy.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.CalendarView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.mealy.R;
import com.example.mealy.databinding.FragmentHomeBinding;
import com.example.mealy.ui.ingredientStorage.Ingredient;
import com.example.mealy.ui.recipes.DisplayRecipeInfo;
import com.example.mealy.ui.recipes.Recipe;
import com.example.mealy.ui.recipes.RecipeList;

import java.util.ArrayList;
import java.util.List;



public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    private ListView mealPlanListView; // list of mealplans

    final String TAG = "Logging";

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        CalendarView calendarThis = (CalendarView) root.findViewById(R.id.calendar);
        TextView date_viewThis = (TextView) root.findViewById(R.id.date_view);

        final TextView textView = binding.textHome;
        homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        //
        mealPlanListView = root.findViewById(R.id.mealplanlistview);

        // list that will store all our recipe objects
        ArrayList<Meal> mealArrayList = new ArrayList<>();

        // Create the adapter and set it to the arraylist
        MealList mealAdapter = new MealList(getActivity(), mealArrayList);

        // create the instance of the ListView to set the meal adapter
        mealPlanListView.setAdapter(mealAdapter);

        // set some sample meal plans
        Ingredient apple = new Ingredient("Apple",
                "Red",
                "0",
                "lb",
                "Weight",
                "Raw Food",
                "Pantry",
                "2022-12-05");

        // make ingredient list
        List sampleIngredients = new ArrayList<Ingredient>();
        sampleIngredients.add(apple);

        List<Recipe> sampleRecipes = new ArrayList();

        Recipe applePie = new Recipe("Apple pie",
                "Delicious apple pie made from real apple",
                4, 1, 30,
                "Baked", sampleIngredients);

        Recipe friedApple = new Recipe("Fried apple",
                "Apples that are fried in a vat of oil",
                2, 0, 15,
                "Fried", sampleIngredients);

        sampleRecipes.add(applePie);
        sampleRecipes.add(friedApple);

        Meal sample = new Meal("Lunch", 3, sampleRecipes, sampleIngredients);

        // add the meals to the list and connect it to the adapter
        mealArrayList.add(sample);
        mealAdapter.notifyDataSetChanged();

        // set it so that clicking on a meal plan displays the info
        mealPlanListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                DisplayMealInfo disp = new DisplayMealInfo(mealAdapter.getItem(i));
                disp.show(getChildFragmentManager(), TAG);

            }
        });

        // Add Listener in calendar
        calendarThis
                .setOnDateChangeListener(
                        new CalendarView
                                .OnDateChangeListener() {
                            @Override

                            // In this Listener have one method
                            // and in this method we will
                            // get the value of DAYS, MONTH, YEARS
                            public void onSelectedDayChange(
                                    @NonNull CalendarView view,
                                    int year,
                                    int month,
                                    int dayOfMonth)
                            {

                                // Store the value of date with
                                // format in String type Variable
                                // Add 1 in month because month
                                // index is start with 0
                                String Date
                                        = dayOfMonth + "-"
                                        + (month + 1) + "-" + year;

                                // set this date in TextView for Display
                                date_viewThis.setText(Date);
                            }
                        });
//        float scalingFactor = 0.8f; // scale down to half the size
//        calendarThis.setScaleX(scalingFactor);
//        calendarThis.setScaleY(scalingFactor);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}