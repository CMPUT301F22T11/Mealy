package com.example.mealy.ui.dashboard;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.mealy.DisplayIngredientInfo;
import com.example.mealy.Ingredient;
import com.example.mealy.IngredientList;
import com.example.mealy.R;
import com.example.mealy.comparators.Compare;
import com.example.mealy.databinding.FragmentDashboardBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class DashboardFragment extends Fragment {

    private FragmentDashboardBinding binding;

    final String TAG = "Logging";

    private Spinner spinner;
    private Button flip;
    private int asc;
    FirebaseFirestore db;

    private ListView storage;
    private IngredientList ingredientList;
    private ArrayList<Ingredient> foodList;

    @RequiresApi(api = Build.VERSION_CODES.O)
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        DashboardViewModel dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        spinner = root.findViewById(R.id.ingredientsort);
        flip = root.findViewById(R.id.flip);
        // create the instance of the ListView
        storage = root.findViewById(R.id.storage);
        asc = 1;

        ArrayList<String> options = new ArrayList<>(Arrays.asList("Name", "Desc", "Exp", "Location", "Category"));
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item, options);
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinner.setAdapter(adapter);

        db = FirebaseFirestore.getInstance();
        final CollectionReference collectionReference = db.collection("Ingredients");

        foodList = new ArrayList<>();

        //ingredients.setAdapter(cityAdapter);

        // Now create the instance of the NumbersViewAdapter and pass
        // the context and arrayList created above
        ingredientList = new IngredientList(getActivity(), foodList);

        // set the numbersViewAdapter for ListView
        storage.setAdapter(ingredientList);

        storage.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                DisplayIngredientInfo disp = new DisplayIngredientInfo((ingredientList.getItem(i)));
                disp.show(getChildFragmentManager(), TAG);
            }
        });


        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable
                    FirebaseFirestoreException error) {
                // Clear the old list
                foodList.clear();
                for(QueryDocumentSnapshot doc: queryDocumentSnapshots)
                {
                    Log.d(TAG, (doc.getId()));
                    String name = (String) doc.getId();
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





        flip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                asc *= -1;

                int selection = spinner.getSelectedItemPosition();
                String selectedItem = spinner.getItemAtPosition(selection).toString();
                Compare compare = new Compare(selectedItem, asc);

                Collections.sort(foodList, compare.returnComparator());
                ingredientList.notifyDataSetChanged();

            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedItem = spinner.getItemAtPosition(i).toString();
                Compare compare = new Compare(selectedItem, asc);

                Collections.sort(foodList, compare.returnComparator());
                ingredientList.notifyDataSetChanged();
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