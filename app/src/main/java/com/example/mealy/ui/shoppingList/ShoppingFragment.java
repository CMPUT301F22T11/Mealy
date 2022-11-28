package com.example.mealy.ui.shoppingList;


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

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.mealy.R;
import com.example.mealy.comparators.shoppingList.CompareShopping;
import com.example.mealy.databinding.ShoppingListDashboardBinding;

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
    private ImageButton addButton;

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

        // list that will store all our shopping ingredient objects
        ArrayList<ShoppingIngredient> shoppingArrayList = new ArrayList<>();

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

        // create the instance of the ListView to set the shopping list adapter
        ListView storage = root.findViewById(R.id.shoppingStorage);
        storage.setAdapter(shoppingAdapter);

        ArrayList<ShoppingIngredient> checkedItems = new ArrayList<ShoppingIngredient>();

        storage.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d("CLICKING", "CLIOKING");
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
                Log.d("TAG", "Checkboxes counted: " + checkedItems.size());
                for(int j=0; j<checkedItems.size(); j++){
                    Log.d("LIST", checkedItems.get(j).toString());
                }
                if (! checkedItems.isEmpty()) {
                    ShoppingListAdd displayAdd = new ShoppingListAdd(checkedItems);
                    displayAdd.show(getChildFragmentManager(), TAG);
                }

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


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}