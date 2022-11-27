package com.example.mealy.functions;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.mealy.R;

public class GetValue extends DialogFragment {
    String title;
    View view;
    String str;
    EditText text;


    public GetValue(String title) {
        this.title = title;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        view = LayoutInflater.from(getActivity()).inflate(R.layout.get_value, null);
        text = view.findViewById(R.id.string);



        // Creates a dialog builder thing that lets you display information and click buttons and stuff
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        return builder
                .setView(view)
                .setTitle(title)
                .setNeutralButton("Button 1", new DialogInterface.OnClickListener() { // deletes the food item
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        
                    }
                })
                .setPositiveButton("Button 2", new DialogInterface.OnClickListener() { // lets you edit the food item
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).create();
    }
}
