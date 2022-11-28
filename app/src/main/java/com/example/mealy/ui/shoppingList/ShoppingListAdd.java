package com.example.mealy.ui.shoppingList;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
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

import java.util.ArrayList;

public class ShoppingListAdd extends DialogFragment {

    //private ShoppingListAddBinding binding;
    private final ShoppingListAdd fragment = this;
    private ListView ShoppingListview;
    private Button addButton;
    ArrayList<Integer> indices;
    ArrayList<ShoppingIngredient> shoppingAddList;
    ArrayList<ShoppingIngredient> ingredientsToBeAdded = new ArrayList<ShoppingIngredient>();

    View view;

    // variables that are passed through

    public ShoppingListAdd(ArrayList<Integer> indices, ArrayList<ShoppingIngredient> shoppingAddListList) {
        this.indices = indices;
        this.shoppingAddList = shoppingAddListList;
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

        view = inflater.inflate(R.layout.shopping_list_add, container, false);

        // Initialize button
        addButton = view.findViewById(R.id.ShoppingCartAddSelectedAddButton);
        // Initialize list
        ShoppingListview = view.findViewById(R.id.shoppingCartAddSelectedList);
        for(int j=0; j<indices.size(); j++){
            Log.d("index", String.valueOf(indices.get(j)));
            Log.d("cart", String.valueOf(shoppingAddList.get(indices.get(j))));
            ingredientsToBeAdded.add(shoppingAddList.get(indices.get(j)));
        }
        // Create the adapter and set it to the arraylist
        ShoppingList shoppingAdapter = new ShoppingList(getActivity(), ingredientsToBeAdded);

        shoppingAdapter.notifyDataSetChanged();

        // create the instance of the ListView to set the shopping list adapter
        ListView storage = view.findViewById(R.id.shoppingCartAddSelectedList);
        storage.setAdapter(shoppingAdapter);


        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
