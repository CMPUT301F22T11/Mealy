package com.example.mealy.functions;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Objects;
import java.util.regex.Pattern;

public class Validate {

    /**
     * Checks if a string is empty or null
     * @param string Any string
     * @return True if the string is empty or null
     */
    public static boolean isEmpty(String string) {
        if (string == null) {return true;}
        else {return string.trim().isEmpty();}
    }

    /**
     * Checks if a string input of a date is valid and can be converted to a Date object
     * Must follow "yyyy-MM-dd" format. Also checks if the date is real
     * @param date String of date following "yyyy-MM-dd" format
     * @return Returns true if the date is valid
     */
    public static boolean validDate(String date) {
        // Setting the date format to yyyy-MM-dd
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        // If the string is empty, it's not valid
        if (isEmpty(date)) {
            return false;
        }
        // If the date is less than 10 characters long, it's not valid
        else if (date.length() != 10) {
            return false;
        }
        // d = an integer from 0-9. Checks if the string follows the format: dddd-dd-dd
        // If it's not met, the date is not valid
        else if (!Pattern.matches("\\d\\d\\d\\d-\\d\\d-\\d\\d", date)){
                return false;
        }
        // Checks if the date is a real date. This is done by converting the string to a Date object
        // If it fails, it's not valid. If it passes, it checks if the date is a real date
        try {
            // stores the date if the format is right
            Date dateObject = sdf.parse(date);

            // Java told me to put this even tho I already checked for it
            assert dateObject != null;

            // checks if the date truly exists (like Feb 30 does not exist)
            // Date Object crash course: If we put 2022-02-29 (Feb 29 2022), Date will
            // Convert it to 2022-03-01 (March 01 2022) without throwing any errors.
            // So it's just checking if we convert the string to a Date object then to a string,
            // does it keep the same value. If it doesn't, the date is not a real date
            return (sdf.format(dateObject)).equals(date);

        } catch (ParseException e) {
            // if the date cannot convert to a Date object, it's not a valid date
            return false;
        }
    }

    /**
     * Checks if the date passed ("yyyy-MM-dd")
     * @param date the date you want to check (Must be in "yyyy-MM-dd" format)
     * @return Returns true if the date has already passed
     * @throws Exception Date format is not valid. Use "yyyy-MM-dd"
     */
    public static boolean datePassed(String date) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        if (validDate(date)) {
            Date today = new Date(); // gets today's date
            Date givenDate = sdf.parse(date); // converts string date to a date object

            assert givenDate != null;
            return givenDate.compareTo(today) <= 0; // checks if the date passed
        } else {
            throw new Exception("Invalid Date");
        }
    }

    /**
     * Checks if the date passed
     * @param date the date you want to check
     * @return Returns true if the date has already passed
     */
    public static boolean datePassed(Date date) {
        return date.compareTo(new Date()) <= 0;
    }



}
