package com.example.mealy.functions;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.example.mealy.R;

import java.util.ArrayList;
import java.util.List;

public class DeletableSpinnerArrayAdapter extends ArrayAdapter<String> {
    Context context;
    ArrayList<String> data;
    String collection;
    String document;

    public DeletableSpinnerArrayAdapter(@NonNull Context context, @NonNull ArrayList<String> data, String collection, String document) {
        super(context, R.layout.spinner_layout, data);
        this.context = context;
        this.data = data;
        this.collection = collection;
        this.document = document;
    }

    public DeletableSpinnerArrayAdapter(@NonNull Context context, @NonNull ArrayList<String> data, String document) {
        super(context, R.layout.spinner_layout, data);
        this.context = context;
        this.data = data;
        collection = "Spinner";
        this.document = document;
    }





    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.spinner_dropdown_deletable, parent, false);

        TextView Name =  view.findViewById(R.id.spnItemName);
        TextView Delete = view.findViewById(R.id.spnItemDel);
        Name.setText(data.get(position));

        if (position == 0) {
            TextView tv = new TextView(getContext());
            tv.setVisibility(View.GONE);
            tv.setHeight(0);
            view = tv;
        }

        if (position == 1) {
            view.findViewById(R.id.spnItemDel).setVisibility(View.GONE);
        }

        Delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DeletePromptSpinner(collection, document, data, position).show(((FragmentActivity)view.getContext()).getSupportFragmentManager(),"delete_prompt");

            }
        });

        return view;

    }


}
