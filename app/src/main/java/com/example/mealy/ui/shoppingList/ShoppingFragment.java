package com.example.mealy.ui.shoppingList;


import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.mealy.R;
import com.example.mealy.comparators.shoppingList.CompareShopping;
import com.example.mealy.databinding.ShoppingListDashboardBinding;
import com.example.mealy.ui.home.Meal;
import com.example.mealy.ui.ingredientStorage.Ingredient;
import com.example.mealy.ui.recipes.DisplayRecipeInfo;
import com.example.mealy.ui.recipes.RecipeIngredient;
import com.google.firebase.database.annotations.Nullable;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/**
 * This fragment represents the shopping list screen.
 * Here, we have a ListView for our ingredients needed for recipes and all its items,
 * as well as a few extra UI elements for sorting.
 */
public class ShoppingFragment extends Fragment {

    private ShoppingListDashboardBinding binding;

    private Spinner sortSpinner; // for selecting sorting category
    private ListView shoppingIngredientListView; // list of shopping ingredients
    private ImageButton flipButton; // for flipping order of the shopping list
    private Button addButton;

    final String TAG = "Logging";

    int asc = 1; // for sort order

    @RequiresApi(api = Build.VERSION_CODES.O)
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState)
    {

        ShoppingViewModel notificationsViewModel =
                new ViewModelProvider(this).get(ShoppingViewModel.class);

        binding = ShoppingListDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        asc = 1;
        // have some flip button to change sort order

        // assign UI elements
        shoppingIngredientListView = root.findViewById(R.id.shoppingStorage);
        flipButton = root.findViewById(R.id.flip_shopping_sort);
        sortSpinner = root.findViewById(R.id.shoppingSort);
        addButton = root.findViewById(R.id.addShoppingIngredient);

        // create sorting spinner with sort categories
        ArrayList<String> options = new ArrayList<>(Arrays.asList("Title", "Description", "Category"));
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getActivity(), R.layout.spinner_layout, options);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown);
        sortSpinner.setAdapter(adapter);

        // list that will store all our firebase objects
        ArrayList<ShoppingIngredient> shoppingArrayList = new ArrayList<>();
        ArrayList<Ingredient> ingredientList = new ArrayList<>();
        ArrayList<RecipeIngredient> recipeIngredientsList = new ArrayList<>();
        ArrayList<Meal> mealList = new ArrayList<>();


        // Create the adapter and set it to the arraylist
        ShoppingList shoppingAdapter = new ShoppingList(getActivity(), shoppingArrayList);

        // set flip button on click
        flipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                asc *= -1;

                int selection = sortSpinner.getSelectedItemPosition();
                String selectedItem = sortSpinner.getItemAtPosition(selection).toString();
                CompareShopping compare = new CompareShopping(selectedItem, asc);

                Collections.sort(shoppingArrayList, compare.returnComparator());
                shoppingAdapter.notifyDataSetChanged();

            }
        });

        // add button on click
        // to see what items are checked https://stackoverflow.com/questions/4831918/how-to-get-all-checked-items-from-a-listview
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShoppingListAdd disp = new ShoppingListAdd();
                disp.show(getChildFragmentManager(), TAG);
            }
        });

        // add sample ingredient for shopping list (change later once meal planner and receipe ingredients are functional)
        ShoppingIngredient sample = new ShoppingIngredient("Tomato",
                "Tomato is a red fruit",
                "10",
                "kg",
                "Vegetable");
        shoppingArrayList.add(sample);

        ShoppingIngredient sample2 = new ShoppingIngredient("Grape",
                "Grape is a green fruit",
                "5",
                "kg",
                "Fruit");
        shoppingArrayList.add(sample2);

        ShoppingIngredient sample3 = new ShoppingIngredient("Apple",
                "Apple is a red fruit",
                "7",
                "kg",
                "Fruit");
        shoppingArrayList.add(sample3);

        ShoppingIngredient sample4 = new ShoppingIngredient("B",
                "Apple is a red fruit",
                "7",
                "kg",
                "Drink");
        shoppingArrayList.add(sample4);

        ShoppingIngredient sample5 = new ShoppingIngredient("C",
                "This is a snack",
                "7",
                "kg",
                "Snack");
        shoppingArrayList.add(sample5);

        ShoppingIngredient sample6 = new ShoppingIngredient("D",
                "This is a drink",
                "7",
                "kg",
                "Drink");
        shoppingArrayList.add(sample6);

        ShoppingIngredient sample7 = new ShoppingIngredient("E",
                "Apple is a red fruit",
                "7",
                "kg",
                "Fruit");
        shoppingArrayList.add(sample7);

        // PULL FROM FIREBASE
        // https://www.youtube.com/watch?v=xzCsJF9WtPU
        // get shopping table
        FirebaseFirestore dbf = FirebaseFirestore.getInstance();
        //TODO: Fix this for firebase

        // Getting all the ingredients from the FireBase
        final CollectionReference shoppingCollectionIngredient = dbf.collection("Ingredients");
        shoppingCollectionIngredient.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable
                    FirebaseFirestoreException error) {

                // Clear the old list
                ingredientList.clear();

                for(QueryDocumentSnapshot doc: queryDocumentSnapshots)
                {
                    try {

                        Log.d(TAG, (doc.getId()));
                        // get available categories
                        String name = (String) doc.getId();
                        String category = (String) doc.getData().get("Category");
                        String desc = (String) doc.getData().get("Description");
                        String exp = (String) doc.getData().get("Expiry Date");
                        String location = (String) doc.getData().get("Location");
                        String amount = (String) doc.getData().get("Quantity");
                        String unitC = (String) doc.getData().get("Unit Category");
                        String unit = (String) doc.getData().get("Quantity Unit");

                        Ingredient ingred = new Ingredient(name, desc, amount, unit, unitC, category, location, exp);
                        ingredientList.add(ingred); // Adding Ingredients from FireStore
                    } catch (Exception e) {
                        System.out.println("Error with firebase pull, incorrect formatting");
                    }
                }
            }
        });

        // Getting all the recipe's ingredients from the FireBase
        final CollectionReference shoppingCollection = dbf.collection("RecipeIngredients");
        shoppingCollection.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable
                    FirebaseFirestoreException error) {

                // Clear the old list
                recipeIngredientsList.clear();

                for(QueryDocumentSnapshot doc: queryDocumentSnapshots)
                {
                    try {

                        Log.d(TAG, (doc.getId()));
                        // get available categories
                        String amount = (String) doc.getData().get("Amount");
                        String category = (String) doc.getData().get("Category");
                        String description = (String) doc.getData().get("Description");
                        String ID = (String) doc.getData().get("ID");
                        String name = (String) doc.getData().get("Name");
                        String recipeName = (String) doc.getData().get("Recipe Name");
                        String unit = (String) doc.getData().get("Unit");
                        String unitCategory = (String) doc.getData().get("Unit Category");

                        String title = name + "," + recipeName;
                        RecipeIngredient ingredientRec = new RecipeIngredient(title, description, amount, unit, category, unitCategory);
                        recipeIngredientsList.add(ingredientRec); // Adding Ingredients from FireStore
                    } catch (Exception e) {
                        System.out.println("Error with firebase pull, incorrect formatting");
                    }
                }
            }
        });

        // TODO: Getting all the meals from the Firebase
        /*
        final CollectionReference shoppingCollection = dbf.collection("RecipeIngredients");
        shoppingCollection.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable
                    FirebaseFirestoreException error) {

                // Clear the old list
                recipeIngredientsList.clear();

                for(QueryDocumentSnapshot doc: queryDocumentSnapshots)
                {
                    try {

                        Log.d(TAG, (doc.getId()));
                        // get available categories
                        String endDate = (String) doc.getData().get("End Date");
                        String startDate = (String) doc.getData().get("Start Date");
                        String servings = (String) doc.getData().get("Servings");
                        String title = (String) doc.getData().get("Title");
                        String recipes = (String) doc.getData().get("Recipes");
                        String ingredients = (String) doc.getData().get("Ingredients");

                        RecipeIngredient ingredientRec = new RecipeIngredient(title, description, amount, unit, category, unitCategory);
                        recipeIngredientsList.add(ingredientRec); // Adding Ingredients from FireStore
                    } catch (Exception e) {
                        System.out.println("Error with firebase pull, incorrect formatting");
                    }
                }
            }
        });*/

        // TODO: get the ingredients from meal and import them into shoppingArrayList
        for (Meal x : mealList){
            String recipes = x.getTitle(); // This is supposed to get the recipes

        }

        // Removing items from shopping list if we already have enough the ingredients in storage
        // Otherwise the user needs to buy them so they are in storage
        for (ShoppingIngredient x : shoppingArrayList){
            String name = x.getName();
            String amount = x.getQuantity();
            int amountNeeded = Integer.valueOf(amount);

            for (Ingredient y : ingredientList){
                if(y.getName() == name){
                    String amountStorage = y.getAmount();
                    int amountHave = Integer.valueOf(amountStorage);
                    if(amountNeeded <= amountHave){
                        shoppingArrayList.remove(x);
                    } else {
                        x.setQuantity(Integer.toString(amountHave - amountNeeded));
                    }
                }
            }
        }



        // notificationsViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        shoppingAdapter.notifyDataSetChanged();

        // create the instance of the ListView to set the shopping list adapter
        ListView storage = root.findViewById(R.id.shoppingStorage);
        storage.setAdapter(shoppingAdapter);

        // set the spinner to sort things correctly
        sortSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedItem = sortSpinner.getItemAtPosition(i).toString();
                CompareShopping compare = new CompareShopping(selectedItem, asc);

                Collections.sort(shoppingArrayList, compare.returnComparator());
                shoppingAdapter.notifyDataSetChanged();
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