package com.example.mealy.functions;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.mealy.R;

import java.util.ArrayList;
import java.util.List;

public class DeletableSpinnerArrayAdapter extends ArrayAdapter<String> {
    Context context;
    ArrayList<String> data;

    public DeletableSpinnerArrayAdapter(@NonNull Context context, @NonNull ArrayList<String> data) {
        super(context, 0, data);
        this.context = context;
        this.data = data;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.spinner_dropdown_deletable, parent, false);;

        TextView Name =  view.findViewById(R.id.spnItemName);
        TextView Delete = view.findViewById(R.id.spnItemDel);
        Name.setText(data.get(position));

        Delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO
            }
        });

        return view;




    }
}
