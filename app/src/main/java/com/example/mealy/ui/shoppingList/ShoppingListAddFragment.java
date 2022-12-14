package com.example.mealy.ui.shoppingList;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.DialogFragment;

import com.example.mealy.R;
import com.example.mealy.databinding.ShoppingListAddBinding;
import com.example.mealy.functions.Firestore;
import com.example.mealy.ui.ingredientStorage.Ingredient;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * This is the class that the user can confirm their additions to the
 * ingredient storage from the shopping cart
 */
public class ShoppingListAddFragment extends DialogFragment {

    private ShoppingListAddBinding binding;
    private final ShoppingListAddFragment fragment = this;
    private ListView ShoppingListview;
    private Button addButton;
    ArrayList<ShoppingIngredient> items;
    ArrayList<ShoppingIngredient> shoppingAddList;
    ArrayList<ShoppingIngredient> ingredientsToBeAdded = new ArrayList<ShoppingIngredient>();
    ArrayList<Ingredient> itemList = new ArrayList<>();
    View view;
    boolean flagItem = false;
    // variables that are passed through

    public ShoppingListAddFragment(ArrayList<ShoppingIngredient> items, ArrayList<Ingredient> itemList) {
        this.items = items;
        this.itemList = itemList;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    /**
     * This is the override for the oncreate
     * @param savedInstanceState
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState)
    {
        binding = ShoppingListAddBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        view = inflater.inflate(R.layout.shopping_list_add, container, false);

        // Initialize button
        addButton = view.findViewById(R.id.ShoppingCartAddSelectedAddButton);
        // Initialize list
        ShoppingListview = view.findViewById(R.id.shoppingCartAddSelectedList);

        // remove duplicates
        for(int i=0; i<items.size(); i++){
            for(int j=0; j<items.size(); j++){
                if(i!=j && (items.get(i) == items.get(j))){
                    items.remove(j);
                }
            }
        }
        // Create the adapter and set it to the arraylist
        ShoppingListAdd shoppingAdapter = new ShoppingListAdd(getActivity(), items);

        shoppingAdapter.notifyDataSetChanged();

        // create the instance of the ListView to set the shopping list adapter
        ListView storage = view.findViewById(R.id.shoppingCartAddSelectedList);
        storage.setAdapter(shoppingAdapter);

        // add button
        addButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                final String collection = "Ingredients";
                for(int i=0; i<items.size(); i++){
                    flagItem = false;
                    for(int j=0; j<itemList.size(); j++){
                        if(items.get(i).getName().equals(itemList.get(j).getName())){
                            String ingredientName = items.get(i).getName();
                            HashMap<String, String> data = GetDataExisting(items.get(i), j);
                            Firestore.storeToFirestore(collection, ingredientName, data);
                            flagItem = true;
                        }
                    }
                    if(flagItem == false) {
                        String ingredientName = items.get(i).getName();
                        HashMap<String, String> data = GetData(items.get(i));
                        Firestore.storeToFirestore(collection, ingredientName, data);

                    }
                }
                shoppingAdapter.notifyDataSetChanged();
                getParentFragmentManager().beginTransaction().remove(fragment).commit();
            }
        });

        return view;
    }


    /**
     * Takes all inputted data (except for ingredient name) and stores it into a HashMap
     * Hashmap:
     * Key            Value
     * Category       categoryName
     * Quantity       ingredientQuantity
     * Quantity Unit  unit
     * Expiry Date    expiryDate
     * Description    description
     *
     * @return HashMap of the data inputted (except for ingredient name)
     */
    private HashMap<String, String> GetData(ShoppingIngredient Item) {

        HashMap<String, String> data = new HashMap<>();
        String categoryName = Item.getCategory();
        String location = null;
        String ingredientQuantity = Item.getQuantity();
        String unit = Item.getUnit();
        String expiryDate = null;
        String unitCategory = null;
        String description = Item.getDescription();

        data.put("Category", categoryName);
        data.put("Quantity", ingredientQuantity);
        data.put("Quantity Unit", unit);
        data.put("Unit Category", unitCategory);
        data.put("Expiry Date", expiryDate);
        data.put("Description", description);
        data.put("Location", location);

        return data;
    }

    /**
     * Takes all inputted data (except for ingredient name) and stores it into a HashMap
     * (This is for an existing entry)
     * Hashmap:
     * Key            Value
     * Category       categoryName
     * Quantity       ingredientQuantity
     * Quantity Unit  unit
     * Expiry Date    expiryDate
     * Description    description
     *
     * @return HashMap of the data inputted (except for ingredient name)
     */
    private HashMap<String, String> GetDataExisting(ShoppingIngredient Item, int index) {

        HashMap<String, String> data = new HashMap<>();
        String categoryName = Item.getCategory();
        String location = itemList.get(index).getLocation();
        float existingQuant = Float.parseFloat(itemList.get(index).getAmount());
        float newQuant = Float.parseFloat(Item.getQuantity());
        String ingredientQuantity = String.valueOf(existingQuant+newQuant);
        String unit = Item.getUnit();
        String expiryDate = itemList.get(index).getExpiryDate();
        String unitCategory = itemList.get(index).getCategory();
        String description = Item.getDescription();

        data.put("Category", categoryName);
        data.put("Quantity", ingredientQuantity);
        data.put("Quantity Unit", unit);
        data.put("Unit Category", unitCategory);
        data.put("Expiry Date", expiryDate);
        data.put("Description", description);
        data.put("Location", location);

        return data;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding=null;
    }
}
