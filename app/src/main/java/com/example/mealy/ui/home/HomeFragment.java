package com.example.mealy.ui.home;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.CalendarView;
import android.widget.AdapterView;
import android.widget.ListView;

import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.CalendarView;

import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.mealy.R;
import com.example.mealy.databinding.FragmentHomeBinding;
import com.example.mealy.ui.ingredientStorage.Ingredient;
import com.example.mealy.ui.ingredientStorage.IngredientAdd;
import com.example.mealy.ui.recipes.DisplayRecipeInfo;
import com.example.mealy.ui.recipes.Recipe;
import com.example.mealy.ui.recipes.RecipeList;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;



public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    private ListView mealPlanListView; // list of mealplans

    final String TAG = "Logging";

    CollectionReference collectionReference;

    FirebaseFirestore db = FirebaseFirestore.getInstance();


    Button Add_Meal_Button;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        CalendarView calendarThis = (CalendarView) root.findViewById(R.id.calendar);
        TextView date_viewThis = (TextView) root.findViewById(R.id.date_view);

        //final TextView textView = binding.textHome;
        //homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

//        collectionReference = db.collection("Ingredients");
//        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
//            /**
//             * Retrieve entries of Ingredients and categories from the firebase, and notify the nameAdapter and categoryAdapter
//             * that was created for each respective lists.
//             *
//             * @param queryDocumentSnapshots returns each document within the collection
//             * @param error
//             */
//            @Override
//            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable
//                    FirebaseFirestoreException error) {
//                for(QueryDocumentSnapshot doc: queryDocumentSnapshots) {
//                    String ingredient = (String) doc.getId();
//                    String category = (String) doc.getData().get("Category");
//                    String desc = (String) doc.getData().get("Description");
//                    String amount = (String) doc.getData().get("Quantity");
//                    String unitCategory = (String) doc.getData().get("Unit Category");
//                    String unit = (String) doc.getData().get("Quantity Unit");
//                    String location = (String) doc.getData().get("Location");
//                    String exp = (String) doc.getData().get("Expiry Date");
//                    IngredientRecipeList.add(ingredient);
//                    IRMap.put(ingredient, "I");
//                    Ingredient newIn = new Ingredient(ingredient, desc, amount, unit, unitCategory, category, location, exp);
//                    listIngredient.add(newIn);
//                }
//                IRAdapter.notifyDataSetChanged();
//            }
//        });

//
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
        //
        mealPlanListView = root.findViewById(R.id.mealplanlistview);

        // list that will store all our recipe objects
        ArrayList<Meal> mealArrayList = new ArrayList<>();

        // Create the adapter and set it to the arraylist
        MealList mealAdapter = new MealList(getActivity(), mealArrayList);

        // create the instance of the ListView to set the meal adapter
        mealPlanListView.setAdapter(mealAdapter);

        Add_Meal_Button = root.findViewById(R.id.meal_button);
        //This is for testing, to set the button to your view, modify it in test view
        Add_Meal_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new MealPlanAdd().show(getParentFragmentManager(),"meal_plan");
            }
        });

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

        Meal sample = new Meal("Lunch", 3, "2022-11-11", "2022-11-13", sampleRecipes, sampleIngredients);

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
                                        = year + "-"
                                        + (month + 1) + "-" + dayOfMonth;

                                // set this date in TextView for Display
                                date_viewThis.setText("Planned Meals For: " + Date);

                                // pull only meal plans that have this corresponding date
                                // PULL FROM FIREBASE

                                // get recipe table
                                FirebaseFirestore dbf = FirebaseFirestore.getInstance();
                                final CollectionReference mealCollection = dbf.collection("Meals");

                                mealCollection.addSnapshotListener(new EventListener<QuerySnapshot>() {
                                    @Override
                                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable
                                            FirebaseFirestoreException error) {

                                        // Clear the old list
                                        mealArrayList.clear();

                                        for(QueryDocumentSnapshot doc: queryDocumentSnapshots)
                                        {
                                            try {

                                                // fetch meal info
                                                // fetch rest of recipe info
                                                String mealStartDate = (String) doc.getData().get("Start Date");
                                                String mealEndDate = (String) doc.getData().get("End Date");

                                                String[] startDateComponents = mealStartDate.split("-");
                                                String[] endDateComponents = mealEndDate.split("-");
                                                String[] currentDateComponents = Date.split("-");

                                                int startYear = Integer.parseInt(startDateComponents[0]);
                                                int startMonth = Integer.parseInt(startDateComponents[1]);
                                                int startDay = Integer.parseInt(startDateComponents[2]);

                                                int endYear = Integer.parseInt(endDateComponents[0]);
                                                int endMonth = Integer.parseInt(endDateComponents[1]);
                                                int endDay = Integer.parseInt(endDateComponents[2]);

                                                int thisYear = Integer.parseInt(currentDateComponents[0]);
                                                int thisMonth = Integer.parseInt(currentDateComponents[1]);
                                                int thisDay = Integer.parseInt(currentDateComponents[2]);

                                                // ONLY FETCH IF DATE IN BETWEEN THIS PARTICULAR RANGE OF DATES
                                                if (!((thisYear >= startYear && thisYear <= endYear) &&
                                                        (thisMonth >= startMonth && thisMonth <= endMonth) &&
                                                        thisDay >= startDay && thisDay <= endDay)) {
                                                    continue;
                                                }

                                                System.out.println("Meal plan detected for this day!");

                                                String title = (String) doc.getData().get("Title");
                                                System.out.println("Got title");
                                                String servingsString = (String) doc.getData().get("Servings");
                                                System.out.println("Got servings");
                                                int servings = Integer.parseInt(servingsString);

                                                Meal meal = new Meal(title, servings, mealStartDate, mealEndDate, sampleRecipes, sampleIngredients);

                                                mealArrayList.add(meal); // Adding new recipe using Firebase data

                                            } catch (Exception e) {
                                                System.out.println("Error with firebase pull, incorrect formatting");
                                            }
                                        }
                                        mealAdapter.notifyDataSetChanged(); // Notifying the adapter to render any new data fetched from the cloud
                                    }
                                });



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