package com.example.mealy.functions;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Objects;
import java.util.regex.Pattern;

public class Validate {

    public static boolean NoEmptyValues(HashMap<String, String> data) {
        for (String name: data.keySet() ) {
            if (data.get(name) == null) {return false;}
            else {if (data.get(name).isEmpty()) {return false;}}
        }
        return true;
    }

    public static boolean IsEmpty(String string) {
        if (string == null) {return true;}
        else {return string.isEmpty();}
    }



    public static boolean ValidDate(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        if (IsEmpty(date)) {
            return false;
        }
        else if (date.length() != 10) {
            return false;
        }
        else if (!Pattern.matches("\\A\\$?\\d+\\.?\\d?\\d?$", date)){
                return false;
        }
        try {
            // stores the date if the format is right
            Date dateObject = sdf.parse(date);

            assert dateObject != null;
            // checks if the date truly exists (like feb 30 does not exist)
            return (sdf.format(dateObject)).equals(date);

        } catch (ParseException e) {
            return false;
        }

    }



}
