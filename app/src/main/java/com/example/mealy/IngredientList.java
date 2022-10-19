package com.example.mealy;



import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class IngredientList extends ArrayAdapter<Ingredient> {
    private ArrayList<Ingredient> ingredients;
    private Context context;

    public IngredientList(Context context, ArrayList<Ingredient> cities){
        super(context,0, cities);
        this.ingredients = ingredients;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        //return super.getView(position, convertView, parent);

        View view = convertView;

        // TO DO
        if(view == null){
            //view = LayoutInflater.from(context).inflate(R.layout.content, parent,false);
        }
        Ingredient ingredient = ingredients.get(position);
//
//        TextView cityName = view.findViewById(R.id.city_text);
//        TextView provinceName = view.findViewById(R.id.province_text);
//
//        cityName.setText(city.getCityName());
//        provinceName.setText(city.getProvinceName());

        return view;
    }

    public ArrayList<Ingredient> getCities() {
        return ingredients;
    }
}
