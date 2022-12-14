package com.example.mealy.ui.shoppingList;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.mealy.R;
import com.example.mealy.comparators.shoppingList.CompareShopping;
import com.example.mealy.databinding.ShoppingListDashboardBinding;
import com.example.mealy.ui.home.Meal;
import com.example.mealy.ui.ingredientStorage.Ingredient;
import com.example.mealy.ui.recipes.Recipe;
import com.example.mealy.ui.recipes.RecipeIngredient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * This fragment represents the shopping list screen.
 * Here, we have a ListView for our ingredients needed for recipes and all its items,
 * as well as a few extra UI elements for sorting.
 */
public class ShoppingFragment extends Fragment {

    private ShoppingListDashboardBinding binding;
    private final ShoppingFragment fragment = this;
    private Spinner sortSpinner; // for selecting sorting category
    private ListView shoppingIngredientListView; // list of shopping ingredients
    private ImageButton flipButton; // for flipping order of the shopping list
    private ImageButton addButton;
    private ImageButton refreshButton;
    final String TAG = "Logging";

    int asc = 1; // for sort order

    /**
     * This is the onCreateView that creates the view for the fragment
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
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
        refreshButton = root.findViewById(R.id.refreshButton);

        // create sorting spinner with sort categories
        ArrayList<String> options = new ArrayList<>(Arrays.asList("Title", "Description", "Category", "Quantity"));
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getActivity(), R.layout.spinner_layout, options);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown);
        sortSpinner.setAdapter(adapter);

        // list that will store all our firebase objects
        ArrayList<ShoppingIngredient> shoppingArrayList = new ArrayList<>();
        ArrayList<Ingredient> ingredientList = new ArrayList<>();
        ArrayList<RecipeIngredient> recipeIngredientsList = new ArrayList<>();
        ArrayList<Meal> mealArrayList = new ArrayList<>();
        ArrayList<Recipe> recipeArrayList = new ArrayList<>();
        ArrayList<ShoppingIngredient> toRemove = new ArrayList<>();



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


        // create the instance of the ListView to set the shopping list adapter
        ListView storage = root.findViewById(R.id.shoppingStorage);
        storage.setAdapter(shoppingAdapter);



        // Getting all the ingredients from the FireBase
        FirebaseFirestore dbf = FirebaseFirestore.getInstance();
        final CollectionReference shoppingCollectionIngredient = dbf.collection("Ingredients");
        shoppingCollectionIngredient.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@androidx.annotation.Nullable QuerySnapshot queryDocumentSnapshots, @androidx.annotation.Nullable
                    FirebaseFirestoreException error) {
                // Clear the old list
                ingredientList.clear();
                for(QueryDocumentSnapshot doc: queryDocumentSnapshots)
                {
                    Log.d(TAG, (doc.getId()));
                    // get available categories
                    String name = doc.getId();
                    String category = (String) doc.getData().get("Category");
                    String desc = (String) doc.getData().get("Description");
                    String exp = (String) doc.getData().get("Expiry Date");
                    String location = (String) doc.getData().get("Location");
                    String amount = (String) doc.getData().get("Quantity");
                    String unitC = (String) doc.getData().get("Unit Category");
                    String unit = (String) doc.getData().get("Quantity Unit");

                    Ingredient ingred = new Ingredient(name, desc, amount, unit, unitC, category, location, exp);
                    ingredientList.add(ingred); // Adding Ingredients from FireStore
                }
                Log.d("shoppingIngredient", Integer.toString(ingredientList.size()));
            }
        });
        Log.d("shoppingIngredientAfter", Integer.toString(ingredientList.size()));

        // Getting all the recipes from firebase
        final CollectionReference recipeCollection = dbf.collection("Recipe");
        recipeCollection.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@androidx.annotation.Nullable QuerySnapshot queryDocumentSnapshots, @androidx.annotation.Nullable
                    FirebaseFirestoreException error) {

                // Clear the old list
                recipeArrayList.clear();

                for(QueryDocumentSnapshot doc: queryDocumentSnapshots)
                {
                    try {

                        // fetch recipe info
                        // fetch rest of recipe info
                        String category = (String) doc.getData().get("Category");
                        String comments = (String) doc.getData().get("Comments");
                        String preptime = (String) doc.getData().get("Preparation Time");
                        String preptimeM = (String) doc.getData().get("Preparation Time Min");
                        String title = (String) doc.getData().get("Recipe Name");
                        String servings = (String) doc.getData().get("Servings");

                        int preptimeHours = Integer.parseInt(preptime);
                        int preptimeMins = Integer.parseInt(preptimeM);
                        int servingsString = Integer.parseInt(servings);

                        Recipe recipe = new Recipe(title, comments, servingsString, preptimeHours, preptimeMins, category,
                                ingredientList);

                        recipeArrayList.add(recipe);
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
                        Log.d(TAG, ("Error with firebase pull"));
                    }

                }
                Log.d("shoppingRecipe", Integer.toString(recipeIngredientsList.size()));
            }
        });

        // Getting the meals from the firebase
        final CollectionReference mealCollection = dbf.collection("MealPlan");
        mealCollection.addSnapshotListener(new EventListener<QuerySnapshot>() {
            /**
             * Retrieve entries of Ingredients and categories from the firebase, and notify the nameAdapter and categoryAdapter
             * that was created for each respective lists.
             *
             * @param queryDocumentSnapshots returns each document within the collection
             * @param error
             */
            @Override
            public void onEvent(@androidx.annotation.Nullable QuerySnapshot queryDocumentSnapshots, @androidx.annotation.Nullable
                    FirebaseFirestoreException error) {
                mealArrayList.clear();

                // Pull every meal
                for(QueryDocumentSnapshot doc: queryDocumentSnapshots) {
                    long days = 1;
                    String startDate = (String) doc.getData().get("Start Date");
                    String endDate = (String) doc.getData().get("End Date");
                    String[] startDateComponents = startDate.split("-");
                    String[] endDateComponents = endDate.split("-");

                    int startYear = Integer.parseInt(startDateComponents[0]);
                    int startMonth = Integer.parseInt(startDateComponents[1]);
                    int startDay = Integer.parseInt(startDateComponents[2]);

                    int endYear = Integer.parseInt(endDateComponents[0]);
                    int endMonth = Integer.parseInt(endDateComponents[1]);
                    int endDay = Integer.parseInt(endDateComponents[2]);


                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    try {
                        Date startDateObj = sdf.parse(startDate);
                        Date endDateObj = sdf.parse(endDate);

                        long diff = endDateObj.getTime() - startDateObj.getTime();
                        days = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS) + 1;

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

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
                    Meal addMeal = new Meal(mealPlan, startDate, endDate, listofRec, listofIng);
                    System.out.println("Days: " + days);
                    for(int i = 0; i < days; i++){
                        mealArrayList.add(addMeal);
                    }

                }


                shoppingArrayList.clear();
                toRemove.clear();

                // Creating the lists for the
                List<Recipe> recipeMealList = new ArrayList<Recipe>();
                List<Ingredient> ingredientMealList = new ArrayList<Ingredient>();
                List<Ingredient> ingredientMealListTemp = new ArrayList<Ingredient>();
                // Going through each meal and adding the ingredients required to make each meal into the shopping list
                for (Meal x : mealArrayList){
                    ingredientMealListTemp.clear();
                    recipeMealList = x.getMealRecipes();
                    ingredientMealList = x.getMealIngredients();

                    // Going through the recipe and adding ingredients from the recipe into the ingredientMealList to later add to the shopping list
                    for (Recipe y : recipeMealList){
                        String recipeName = y.getTitle();
                        int userServings = y.getServings();
                        int recipeServings = 0;

                        for (Recipe temp : recipeArrayList){
                            if (temp.getTitle().equals(recipeName)){
                                recipeServings = temp.getServings();
                            }
                        }

                        double count = 1;
                        if(userServings/recipeServings > 1){
                            count = Math.ceil(userServings/recipeServings);
                        }
                        for (int i = 0; i < count; i++){
                            // Checking all the ingredients of the recipe and adding them to ingredientMealList
                            for (RecipeIngredient z : recipeIngredientsList){
                                String tempTitle[] = z.getTitle().split(",");

                                if(tempTitle[1].equals(recipeName)){
                                    Ingredient tempIngredient = new Ingredient(tempTitle[0], z.getDescription(), z.getAmount(), z.getUnit(), z.getUnitCategory(), z.getCategory(), "NULL", "NULL");
                                    ingredientMealListTemp.add(tempIngredient);
                                }
                            }
                        }
                    }


                    // Adding ingredients into the shopping ingredient list
                    for (Ingredient y : ingredientMealList){
                        ShoppingIngredient tempIngredient = new ShoppingIngredient(y.getName(), y.getDescription(), y.getAmount(), y.getUnit(), y.getCategory());
                        String tempName = tempIngredient.getName();

                        // If the selected ingredient already exists in the shopping ingredient list, then add more needed to the list
                        boolean shoppingIngredientExists = false;

                        for(ShoppingIngredient z : shoppingArrayList){
                            if (z.getName().equals(tempName)){
                                z.setQuantity(Double.toString(Double.valueOf(z.getQuantity()) + Double.valueOf(tempIngredient.getQuantity())));
                                shoppingIngredientExists = true;
                            }
                        }
                        // Otherwise, if the ingredient does not already exists in the shopping list, then add it
                        if(shoppingIngredientExists == false){
                            shoppingArrayList.add(tempIngredient);
                        }
                    }

                    // Adding ingredients into the shopping ingredient list
                    for (Ingredient y : ingredientMealListTemp){
                        ShoppingIngredient tempIngredient = new ShoppingIngredient(y.getName(), y.getDescription(), y.getAmount(), y.getUnit(), y.getCategory());
                        String tempName = tempIngredient.getName();

                        // If the selected ingredient already exists in the shopping ingredient list, then add more needed to the list
                        boolean shoppingIngredientExists = false;

                        for(ShoppingIngredient z : shoppingArrayList){
                            Log.d("TAGtempName", tempName);
                            Log.d("TAGzName", z.getName());
                            if (z.getName().equals(tempName)){
                                z.setQuantity(Double.toString(Double.valueOf(z.getQuantity()) + Double.valueOf(tempIngredient.getQuantity())));
                                shoppingIngredientExists = true;
                                Log.d("TAG", "True");
                            }
                        }
                        // Otherwise, if the ingredient does not already exists in the shopping list, then add it
                        if(shoppingIngredientExists == false){
                            Log.d("TAG", "False");
                            shoppingArrayList.add(tempIngredient);
                        }
                    }
                }

                if(shoppingArrayList.isEmpty()){
                    Toast toast=Toast.makeText(getContext(),"Add meals into the meal planner to update your shopping list!",Toast.LENGTH_LONG);
                    toast.setMargin(50,50);
                    toast.show();
                }

                // Removing items from shopping list if we already have enough ingredients in storage
                // In other words, the user needs to buy them if they are not in storage

                for (ShoppingIngredient x : shoppingArrayList){
                    String name = x.getName();
                    String amount = x.getQuantity();
                    double amountNeeded = Double.valueOf(amount);
                    Log.d("shoppingName", name);
                    Log.d("shoppingAmount", amount);

                    // For every ingredient in the the ingredient storage list, see if it matches the shopping list ingredient
                    // If it does, then check if it need to buy more, otherwise, remove from shopping list.
                    for (Ingredient y : ingredientList){
                        Log.d("shoppingIngredientName", y.getName());
                        if(y.getName().equals(name)){
                            String amountStorage = y.getAmount();
                            double amountHave = Double.valueOf(amountStorage);
                            Log.d("shoppingHave", amountStorage);
                            if(amountNeeded <= amountHave){
                                toRemove.add(x);
                            } else {
                                x.setQuantity(Double.toString(amountNeeded - amountHave));
                            }
                        }
                    }
                }
                // Removing items from the shopping list that we already own
                for(ShoppingIngredient i : toRemove){
                    shoppingArrayList.remove(i);
                }
                shoppingAdapter.notifyDataSetChanged();
            }
        });

        ArrayList<ShoppingIngredient> checkedItems = new ArrayList<ShoppingIngredient>();

        storage.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d("CLICKING", "CLICKING");
                CheckBox checkBox = view.findViewById(R.id.checkbox);
                checkBox.toggle();
                if(checkBox.isChecked()){
                    checkedItems.add(shoppingArrayList.get(i));
                } else {
                    checkedItems.remove(shoppingArrayList.get(i));
                }

            }
        });




        // add button on click
        // to see what items are checked https://stackoverflow.com/questions/4831918/how-to-get-all-checked-items-from-a-listview
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // List of checked items
                if (! checkedItems.isEmpty()) {
                    ShoppingListAddFragment displayAdd = new ShoppingListAddFragment(checkedItems, ingredientList);
                    displayAdd.show(getChildFragmentManager(), TAG);
                }
                shoppingAdapter.notifyDataSetChanged();
                getParentFragmentManager().beginTransaction().detach(fragment).commitNow();
                getParentFragmentManager().beginTransaction().attach(fragment).commitNow();
            }
        });

        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("CLICKINGBUTTON", "YES");
                shoppingAdapter.notifyDataSetChanged();
                getParentFragmentManager().beginTransaction().detach(fragment).commitNow();
                getParentFragmentManager().beginTransaction().attach(fragment).commitNow();
            }
        });

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


    /**
     * When the view is destroyed the following are run
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}