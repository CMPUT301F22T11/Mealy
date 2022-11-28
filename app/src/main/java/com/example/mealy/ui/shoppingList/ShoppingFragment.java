package com.example.mealy.ui.shoppingList;


import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

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
        shoppingIngredientListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
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

        // List of checked items
        ArrayList<Integer> checkedItems = new ArrayList<Integer>();



        // get all checked items
        SparseBooleanArray checked = shoppingIngredientListView.getCheckedItemPositions();

        // add button on click
        // to see what items are checked https://stackoverflow.com/questions/4831918/how-to-get-all-checked-items-from-a-listview
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // from https://stackoverflow.com/questions/19027843/android-get-text-of-all-checked-checkboxes-in-listview
                CheckBox cb;
                for (int x = 0; x<shoppingIngredientListView.getChildCount();x++){
                    cb = (CheckBox) shoppingIngredientListView.getChildAt(x).findViewById(R.id.checkbox);
                    if(cb.isChecked()){
                        checkedItems.add(x);
                        Log.d("TAG", "Index " + x + " is counted");
                    }
                }
                Log.d("TAG", "Checkboxes counted: " + checkedItems.size());
                if (! checkedItems.isEmpty()) {
                    ShoppingListAdd disp = new ShoppingListAdd();
                    disp.show(getChildFragmentManager(), TAG);
                }
                checkedItems.clear();
                /*
                if (checked != null) {
                    Log.d("TAG", String.valueOf("Checked Size:" + checked.size()));
                    for (int i = 0; i < checked.size(); i++) {
                        if (checked.valueAt(i)) {
                            checkedItems.add(i);
                        }
                    }

                    for (int i = 0; i < checkedItems.size(); i++) {
                        Log.d("TAG", "Checked Item: " + checkedItems.get(i).toString());
                    }


                } else {
                    Log.i(TAG, "Failed");
                }
                 */
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

        final CollectionReference shoppingCollection = dbf.collection("Recipe");
        /*
        shoppingCollection.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override

            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable
                    FirebaseFirestoreException error) {

                // Clear the old list
                shoppingArrayList.clear();

                /*for(QueryDocumentSnapshot doc: queryDocumentSnapshots)
                {
                    try {

                        // fetch ingredient info
                        // fetch rest of ingredient info
                        String category = (String) doc.getData().get("Category");
                        String description = (String) doc.getData().get("Description");
                        String quantity = (String) doc.getData().get("Amount");
                        String name = (String) doc.getData().get("Name");
                        String unit = (String) doc.getData().get("Unit");


                        ShoppingIngredient temp = new ShoppingIngredient(name, description, quantity, unit, category);

                        // fetch ingredient image and store in bitmap
                        StorageReference mStorageReference;

                        // TODO: find the proper reference image
                        //mStorageReference = FirebaseStorage.getInstance().getReference().child("Recipe_Image/" + temp.getTitle());

                        try {
                            final File localFile = File.createTempFile("imageCache", "jpg"); // temp file to store image
                            mStorageReference.getFile(localFile)
                                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                            // successfully put stuff in file;
                                            System.out.println("Image received");
                                            Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                                            recipe.setBitmap(bitmap);
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            // did not put stuff in file
                                            System.out.println("No image detected!");
                                        }
                                    });

                        } catch (IOException e) {
                            System.out.println("Firebase failure!");
                            e.printStackTrace();
                        }

                        shoppingArrayList.add(temp); // Adding new ingredient using Firebase data

                    } catch (Exception e) {
                        System.out.println("Error with firebase pull, incorrect formatting");
                    }
                }
                shoppingAdapter.notifyDataSetChanged(); // Notifying the adapter to render any new data fetched from the cloud
            }
        });*/

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