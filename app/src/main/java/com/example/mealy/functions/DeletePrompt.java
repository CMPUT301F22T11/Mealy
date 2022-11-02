package com.example.mealy.functions;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.mealy.R;

public class DeletePrompt extends DialogFragment {

    private final String collectionName;
    private final String document;
    //View view;

    public DeletePrompt(String collectionName, String document){

        this.collectionName = collectionName;
        this.document = document;

    }

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
                        Firestore.DeleteFromFirestore(collectionName, document);
                    }
                })
                .setPositiveButton("Cancel", null).create();
    }



}
