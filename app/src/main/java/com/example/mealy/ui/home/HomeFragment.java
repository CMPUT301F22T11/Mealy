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
import android.widget.ImageButton;
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

import org.checkerframework.checker.units.qual.A;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
/**
 * The home tab fragment. This fragment is the homescreen of the app and displays
 * all of the user's current meal plans. In addition, the user can add and display
 * meal plans in this tab.
 */
public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    private ListView mealPlanListView; // list of mealplans

    final String TAG = "Logging";

    CollectionReference collectionReference;

    FirebaseFirestore db = FirebaseFirestore.getInstance();


    ImageButton Add_Meal_Button;

    ArrayList<Meal> listofMeals = new ArrayList<Meal>();
    ArrayList<Meal> mealAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        CalendarView calendarThis = root.findViewById(R.id.calendar);
        TextView date_viewThis = root.findViewById(R.id.date_view);

//        final TextView textView = binding.textHome;
//        homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        mealPlanListView = root.findViewById(R.id.mealplanlistview);
        // list that will store all our recipe objects
        ArrayList<Meal> mealArrayList = new ArrayList<Meal>();

        // Create the adapter and set it to the arraylist
        MealList mealAdapter = new MealList(getActivity(), mealArrayList);

        // create the instance of the ListView to set the meal adapter
        mealPlanListView.setAdapter(mealAdapter);



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


        Add_Meal_Button = root.findViewById(R.id.meal_button);
        //This is for testing, to set the button to your view, modify it in test view
        Add_Meal_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new MealPlanAdd().show(getParentFragmentManager(),"meal_plan");
            }
        });

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
                                String date_string
                                        = year + "-"
                                        + (month + 1) + "-" + dayOfMonth;

                                // set this date in TextView for Display
                                date_viewThis.setText("Planned Meals For: " + date_string);

                                collectionReference = db.collection("MealPlan");
                                collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
                                    /**
                                     * Retrieve entries of Ingredients and categories from the firebase, and notify the nameAdapter and categoryAdapter
                                     * that was created for each respective lists.
                                     *
                                     * @param queryDocumentSnapshots returns each document within the collection
                                     * @param error
                                     */
                                    @Override
                                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable
                                            FirebaseFirestoreException error) {
                                        mealArrayList.clear();

                                        // Pull every meal
                                        for(QueryDocumentSnapshot doc: queryDocumentSnapshots) {
                                            String startDate = (String) doc.getData().get("Start Date");
                                            String endDate = (String) doc.getData().get("End Date");

                                            String[] startDateComponents = startDate.split("-");
                                            String[] endDateComponents = endDate.split("-");
                                            String[] currentDateComponents = date_string.split("-");

                                            int startYear = Integer.parseInt(startDateComponents[0]);
                                            int startMonth = Integer.parseInt(startDateComponents[1]);
                                            int startDay = Integer.parseInt(startDateComponents[2]);

                                            int endYear = Integer.parseInt(endDateComponents[0]);
                                            int endMonth = Integer.parseInt(endDateComponents[1]);
                                            int endDay = Integer.parseInt(endDateComponents[2]);

                                            int thisYear = Integer.parseInt(currentDateComponents[0]);
                                            int thisMonth = Integer.parseInt(currentDateComponents[1]);
                                            int thisDay = Integer.parseInt(currentDateComponents[2]);

                                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                                            try {
                                                Date startDateObj = sdf.parse(startDate);
                                                Date endDateObj = sdf.parse(endDate);
                                                Date thisDateObj = sdf.parse(date_string);

                                                // PULL ONLY IF OUR SELECTED DATE IS WITHIN RANGE OF THIS DOCUMENT
                                                // check if date in between two years
                                                // if not, then check if date is in between
                                                // https://stackoverflow.com/questions/883060/how-can-i-determine-if-a-date-is-between-two-dates-in-java
                                                if (!(startDateObj.compareTo(thisDateObj) * thisDateObj.compareTo(endDateObj) >= 0)) {
                                                    continue;
                                                }

                                            } catch (ParseException e) {
                                                e.printStackTrace();
                                            }

                                            System.out.println("Meal plan detected for this day!");
                                            String mealPlan = (String) doc.getId();
                                            ArrayList<HashMap<String, String>> listIng = (ArrayList<HashMap<String, String>>) doc.getData().get("Ingredients");
                                            ArrayList<HashMap<String, String>> listRec = (ArrayList<HashMap<String, String>>) doc.getData().get("Recipes");

                                            ArrayList<Ingredient> listofIng = new ArrayList<Ingredient>();
                                            ArrayList<Recipe> listofRec = new ArrayList<Recipe>();

                                            for ( HashMap<String, String> x : listIng) {
                                                String amount = "", category = "", desc = "", exp = "", location = "", unit = "", unitCategory = "", ingName = "";
                                                for (HashMap.Entry<String, String> ntry : x.entrySet()) {
                                                    if (ntry.getKey().equals("amount")) {
                                                        amount = ntry.getValue();
                                                    }
                                                    else if (ntry.getKey().equals("category")) {
                                                        category = ntry.getValue();
                                                    }
                                                    else if (ntry.getKey().equals("description")) {
                                                        desc = ntry.getValue();
                                                    }
                                                    else if (ntry.getKey().equals("expiryDate")) {
                                                        exp = ntry.getValue();
                                                    }
                                                    else if (ntry.getKey().equals("location")) {
                                                        location = ntry.getValue();
                                                    }
                                                    else if (ntry.getKey().equals("unit")) {
                                                        unit = ntry.getValue();
                                                    }
                                                    else if (ntry.getKey().equals("unitCategory")) {
                                                        unitCategory = ntry.getValue();
                                                    }
                                                    else if (ntry.getKey().equals("name")) {
                                                        ingName = ntry.getValue();
                                                    }
                                                }
                                            Ingredient addIng = new Ingredient(ingName, desc, amount, unit, unitCategory, category, location, exp);
                                            listofIng.add(addIng);
                                                // do something with key and/or tab
                                            }
                                            for (  HashMap<String, String> x : listRec) {
                                                String comments = "", category = "", prepHour = "0", prepMin = "0", servings = "0", recName = "";
                                                for (HashMap.Entry<String, String> ntry : x.entrySet()) {
                                                    if (ntry.getKey().equals("comments")) {
                                                        comments = ntry.getValue();
                                                        System.out.println("comments for this recipe: " + comments);
                                                    }
                                                    else if (ntry.getKey().equals("category")) {
                                                        category = ntry.getValue();
                                                        System.out.println("category for this recipe: " + category);
                                                    }
                                                    else if (ntry.getKey().equals("preptimeHours")) {
                                                        prepHour = String.valueOf(ntry.getValue());
                                                    }
                                                    else if (ntry.getKey().equals("preptimeMins")) {
                                                        prepMin = String.valueOf(ntry.getValue());
                                                    }
                                                    else if (ntry.getKey().equals("servings")) {
                                                        servings = String.valueOf(ntry.getValue());
                                                    }
                                                    else if (ntry.getKey().equals("title")) {
                                                        recName = ntry.getValue();
                                                        System.out.println("title for this recipe: " + recName);
                                                    }

                                                }
                                            ArrayList<Ingredient> noIng = new ArrayList<>();
                                            Recipe addRec = new Recipe(recName, comments, Integer.parseInt(servings), Integer.parseInt(prepHour), Integer.parseInt(prepMin), category, noIng);
                                            listofRec.add(addRec);
                                                // do something with key and/or tab
                                            }

                                            System.out.println("Recipes added...");
                                            System.out.println(listofRec);

                                            System.out.println("Ingredients added...");
                                            System.out.println(listofIng);

                                            Meal addMeal = new Meal(mealPlan, startDate, endDate, listofRec, listofIng);
                                            mealArrayList.add(addMeal);

                                        }
                                        mealAdapter.notifyDataSetChanged();
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