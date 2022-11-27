package com.example.mealy.ui.shoppingList;


import android.os.Build;
import android.os.Bundle;
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
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.mealy.comparators.shoppingList.CompareShopping;
import com.example.mealy.databinding.FragmentNotificationsBinding;
import com.example.mealy.R;
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
import java.util.List;

/**
 * This fragment represents the shopping list screen.
 * Here, we have a ListView for our ingredients needed for recipes and all its items,
 * as well as a few extra UI elements for sorting.
 */
public class ShoppingFragment extends Fragment {

    private FragmentNotificationsBinding binding;

    private Spinner sortSpinner; // for selecting sorting category
    private ListView shoppingIngredientListView; // list of shopping ingredients
    private ImageButton flipButton; // for flipping order of the shopping list

    final String TAG = "Logging";

    int asc = 1; // for sort order

    @RequiresApi(api = Build.VERSION_CODES.O)
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState)
    {

        ShoppingViewModel notificationsViewModel =
                new ViewModelProvider(this).get(ShoppingViewModel.class);

        binding = FragmentNotificationsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        asc = 1;
        // have some flip button to change sort order

        // assign UI elements
        shoppingIngredientListView = root.findViewById(R.id.shoppingStorage);
        flipButton = root.findViewById(R.id.flip_shopping_sort);
        sortSpinner = root.findViewById(R.id.shoppingSort);

        // create sorting spinner with sort categories
        ArrayList<String> options = new ArrayList<>(Arrays.asList("Title", "Description", "Category", "Quantity"));
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
        ShoppingIngredient sample = new ShoppingIngredient("tomato",
                "Tomato is a red fruit",
                "10",
                "kg",
                "Vegetable");
        shoppingArrayList.add(sample);

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