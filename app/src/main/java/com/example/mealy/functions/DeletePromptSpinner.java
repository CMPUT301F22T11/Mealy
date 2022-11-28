package com.example.mealy.functions;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class DeletePromptSpinner extends DeletePrompt{

    String collectionName;
    String document;
    ArrayList<String> data;
    int index;

    public DeletePromptSpinner(String collectionName, String document, ArrayList<String> data, int index){
        super(collectionName, document);
        this.collectionName = collectionName;
        this.document = document;
        this.data = data;
        this.index = index;

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        //view = LayoutInflater.from(getActivity()).inflate(R.layout.display_ingredient_info, null);

        // Creates a dialog builder thing that lets you display information and click buttons and stuff
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        return builder
                .setTitle("Confirm Deletion?")
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() { // deletes the food item
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        data.remove(index);
                        Firestore.storeToFirestore(collectionName, document, General.listToMap(data));
                    }
                })
                .setNeutralButton("Cancel", null).create();
    }
}

