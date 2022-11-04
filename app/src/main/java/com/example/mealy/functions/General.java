package com.example.mealy.functions;

import android.content.Context;

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

}
