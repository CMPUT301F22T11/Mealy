package com.example.mealy.ui.ingredientStorage;

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
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.mealy.R;
import com.example.mealy.comparators.Compare;
import com.example.mealy.databinding.FragmentDashboardBinding;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/**
 * The ingredients tab fragment. This allows the user to add, view, and edit ingredients from a
 * listview format, and sort them by defined categories.
 */
public class IngredientFragment extends Fragment {

    private FragmentDashboardBinding binding;

    final String TAG = "Logging";

    // sorting selection
    private Spinner spinner;
    // inverse sorting
    private ImageButton flip;
    private int asc;
    FirebaseFirestore db;

    private ImageButton Home_Add_Ingredient_Entry;

    private ListView storage;
    // listview adapter
    private IngredientList ingredientList;
    private ArrayList<Ingredient> foodList;

    @RequiresApi(api = Build.VERSION_CODES.O)
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        IngredientViewModel ingredientViewModel =
                new ViewModelProvider(this).get(IngredientViewModel.class);

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // initialize prompts on the screen
        spinner = root.findViewById(R.id.ingredientsort);
        flip = root.findViewById(R.id.flip);
        Home_Add_Ingredient_Entry = root.findViewById(R.id.Home_Add_Ingredient_Entry);

        // create the instance of the ListView
        storage = root.findViewById(R.id.storage);
        asc = 1;

        // initialize spinner with predefined categories
        ArrayList<String> options = new ArrayList<>(Arrays.asList("Name", "Description", "Expiration", "Location", "Category"));
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getActivity(), R.layout.spinner_layout, options);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown);
        spinner.setAdapter(adapter);

        db = FirebaseFirestore.getInstance();
        // get from the firebase the 'ingredients' collection
        final CollectionReference collectionReference = db.collection("Ingredients");

        foodList = new ArrayList<>();

        // Create an instance of the IngredientList, and pass
        // the context and arrayList created above
        ingredientList = new IngredientList(getActivity(), foodList);

        // set the Adapter for ListView
        storage.setAdapter(ingredientList);

        // when a listview item is clicked, show the ingredient information, along with
        // prompts to delete, edit or close.
        storage.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                DisplayIngredientInfo disp = new DisplayIngredientInfo((ingredientList.getItem(i)));
                disp.show(getChildFragmentManager(), TAG);
            }
        });

        // snapshot listener for updating from the firebase. this will pull data from
        // the database and update the listview accordingly.
        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable
                    FirebaseFirestoreException error) {
                // Clear the old list
                foodList.clear();
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
                    foodList.add(ingred); // Adding Ingredients from FireStore
                }
                ingredientList.notifyDataSetChanged(); // Notifying the adapter to render any new data fetched from the cloud
            }
        });


        // this is for changing the way the ingredientList items are organized.
        flip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // on click, change the organization to invert
                asc *= -1;

                // now get the spinner's selection once again
                int selection = spinner.getSelectedItemPosition();
                String selectedItem = spinner.getItemAtPosition(selection).toString();
                // insert into compare class to select the comparator
                Compare compare = new Compare(selectedItem, asc);

                // sort the ingredientList and notify that the dataset changed.
                Collections.sort(foodList, compare.returnComparator());
                ingredientList.notifyDataSetChanged();

            }
        });

        // this is for selecting the organization or sorting method
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                // get the user's sorting selection and insert into the Compare class
                String selectedItem = spinner.getItemAtPosition(i).toString();
                Compare compare = new Compare(selectedItem, asc);

                // resort the ingredientList using the selected comparator
                Collections.sort(foodList, compare.returnComparator());
                ingredientList.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        Home_Add_Ingredient_Entry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IngredientAdd addIngredient = new IngredientAdd();
                addIngredient.show(getChildFragmentManager(), TAG);
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