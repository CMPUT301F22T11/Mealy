package com.example.mealy.ui.shoppingList;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.mealy.R;
import com.example.mealy.databinding.ShoppingListAddBinding;
import com.example.mealy.ui.recipes.RecipeViewModel;

public class ShoppingListAdd extends DialogFragment {

    //private ShoppingListAddBinding binding;
    private final ShoppingListAdd fragment = this;

    View view;


    public ShoppingListAdd() {
        // Constructor: TODO
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

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
