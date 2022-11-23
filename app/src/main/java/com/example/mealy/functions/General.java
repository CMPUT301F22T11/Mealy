package com.example.mealy.functions;

import android.content.Context;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class General {
    public static int dpToPx(int dp, Context context) {
        float density = context.getResources().getDisplayMetrics().density;
        return Math.round((float) dp * density);
    }

    /**
     * If the string is empty or void, returns an empty string.
     * Else it returns the original string
     * @param string String to check if its void
     * @return empty string or original string
     */
    public static String blankIfVoid(String string) {
        return Validate.isEmpty(string) ? "" : string;
    }

    public static Map<String, Object> listToMap(ArrayList strings){
        Map<String, Object> results = new HashMap<String, Object>();
        for (int i = 0; i < strings.size(); i++) {
            results.put(String.valueOf(i),strings.get(i).toString());
        }
        return results;
    }

    public static ArrayList<String> mapToArrayList(Map<String, Object> map) {
        return new ArrayList<>(Arrays.asList(map.values().toArray(new String[0])));
    }

}
