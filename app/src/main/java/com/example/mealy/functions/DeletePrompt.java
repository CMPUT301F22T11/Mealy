package com.example.mealy.functions;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

/**
 *  Prompts the user to delete an item, if they accept, it deletes it from Firestore.
 */
public class DeletePrompt extends DialogFragment {

    private final String collectionName;
    private final String document;

    /**
     * Takes in parameters to be able to delete the item from firestore
     * @param collectionName The name of the collection (e.g. Ingredients)
     * @param document The name of the item in the collection (e.g. Apple)
     */
    public DeletePrompt(String collectionName, String document){
        this.collectionName = collectionName;
        this.document = document;

    }

    /**
     * Prompts the user to delete the item or to cancel. If they want to delete it, it will delete it from Firestore
     * @param savedInstanceState idk tbh
     * @return Builder
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        //view = LayoutInflater.from(getActivity()).inflate(R.layout.display_ingredient_info, null);

        // Creates a dialog builder thing that lets you display information and click buttons and stuff
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        return builder
                .setTitle("Confirm Deletion?")
                .setNeutralButton("Delete", new DialogInterface.OnClickListener() { // deletes the food item
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // deletes item from Firestore
                        Firestore.deleteFromFirestore(collectionName, document);
                    }
                })
                .setPositiveButton("Cancel", null).create();
    }



}
