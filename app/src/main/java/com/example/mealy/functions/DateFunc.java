package com.example.mealy.functions;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

/**
 * This class has a bunch of useful functions related to dates
 * "Int" indicates an integer, usually in this format (int day, int month, int year)
 * "Date" indicates a date in string form ("yyyy-MM-dd") (e.g. 2023-01-02)
 * "String" indicates the date as text ("Month day Year") (e.g. JAN 2 2023)
 * "Obj" indicates the Date object
 *
 * Current usage: User input of date is in "String" format. However the database is in
 * "Date" format. This allows for the easy conversion between the two formats.
 */

public class DateFunc {

    static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    /**
     * Gets the current date
     * @return Returns the current date in "Date" format ("yyyy-MM-dd")
     */
    public static String getTodaysDate() {
        Date date = new Date();
        return sdf.format(date);
    }

    /**
     * input a month as an int and returns it as a string (JAN FEB MAR...)
     * @param month month as an int
     * @return month as a string
     */
    public static String getMonthFormat(int month)
    {
        if(month == 1)
            return "JAN";
        if(month == 2)
            return "FEB";
        if(month == 3)
            return "MAR";
        if(month == 4)
            return "APR";
        if(month == 5)
            return "MAY";
        if(month == 6)
            return "JUN";
        if(month == 7)
            return "JUL";
        if(month == 8)
            return "AUG";
        if(month == 9)
            return "SEP";
        if(month == 10)
            return "OCT";
        if(month == 11)
            return "NOV";
        if(month == 12)
            return "DEC";

        //default should never happen
        return "JAN";
    }

    /**
     * input a month as an string("JAN", "FEB", "MAR"...) and returns it as an int
     * @param month month as a String ("JAN", "FEB", "MAR"...)
     * @return month as an int
     */
    public static int monthToInt(String month)
    {
        if(Objects.equals(month, "JAN"))
            return 1;
        if(Objects.equals(month, "FEB"))
            return 2;
        if(Objects.equals(month, "MAR"))
            return 3;
        if(Objects.equals(month, "APR"))
            return 4;
        if(Objects.equals(month, "MAY"))
            return 5;
        if(Objects.equals(month, "JUN"))
            return 6;
        if(Objects.equals(month, "JUL"))
            return 7;
        if(Objects.equals(month, "AUG"))
            return 8;
        if(Objects.equals(month, "SEP"))
            return 9;
        if(Objects.equals(month, "OCT"))
            return 10;
        if(Objects.equals(month, "NOV"))
            return 11;
        if(Objects.equals(month, "DEC"))
            return 12;

        //default should never happen
        return 1;
    }

    /**
     * Takes in day, month, year as an int. Returns the date in "Date" format ("yyyy-MM-dd")
     * @param day day of the month as int
     * @param month the month as int
     * @param year the year as int
     * @return Date in "Date" format ("yyyy-MM-dd")
     */
    public static String makeIntDate(int day, int month, int year)
    {
        // if the day or month is below 10, it registers as (1,2,...) instead of (01, 02,...)
        // so we need to format the integer into that.
        String newMonth = String.valueOf(month);
        String newDay = String.valueOf(day);
        String newYear = String.valueOf(year);

        DecimalFormat formatter = new DecimalFormat("00");
        newMonth = formatter.format(month);

        formatter = new DecimalFormat("00");
        newDay = formatter.format(day);

        formatter = new DecimalFormat("0000");
        newYear = formatter.format(year);

        return newYear + "-" + newMonth + "-" + newDay;
    }

    /**
     *  Takes in day, month, year as an int. Returns the date in "String" format ("Month day Year")
     * @param day day of the month as int
     * @param month the month as int
     * @param year the year as int
     * @return the date in "String" format ("Month day Year") (e.g. JAN 2 2023)
     */
    public static String makeIntString(int day, int month, int year)
    {
        // if the day or month is below 10, it registers as (1,2,...) instead of (01, 02,...)
        // so we need to format the integer into that.
        String newMonth = getMonthFormat(month);
        String newDay;
        String newYear;

        DecimalFormat formatter = new DecimalFormat("00");
        newDay = formatter.format(day);

        formatter = new DecimalFormat("0000");
        newYear = formatter.format(year);

        return  newMonth + " " + newDay + " " + newYear;
    }

    /**(NOT TESTED)
     * Takes in the date in "Date" format and converts it to "String" format
     * @param date date in "Date" format ("yyyy-MM-dd")
     * @return date in "String" format ("Month day Year") (e.g. JAN 2 2023)
     */
    public static String makeDateString(String date)
    {   if (Validate.isEmpty(date)) {
            return "";
        } else {
            String[] spliced = date.split("-");
            spliced[1] = getMonthFormat(Integer.parseInt(spliced[1]));

            return spliced[1] + " " + spliced[2] + " " + spliced[0];
        }
    }

    /**
     * Takes the date in "String" Format and converts it to "Date" Format
     * @param str Date in "String" Format ("Month day Year") (e.g. JAN 2 2023)
     * @return Date in "Date" format ("yyyy-MM-dd")
     */
    public static String makeStringDate(String str) {
        String[] spliced = str.split(" ");
        int month = monthToInt(spliced[0]);

        String newMonth;
        DecimalFormat formatter = new DecimalFormat("00");

        newMonth = formatter.format(month);
        spliced[0] = newMonth;

        return spliced[2] + "-" + spliced[0] + "-" + spliced[1];
    }

    /**
     * Takes in date in "Obj" format and converts it to "Date" format
     * @param date Date object
     * @return Date in "Date" format ("yyyy-MM-dd")
     */
    public static String makeObjDate(Date date) {
        return sdf.format(date);
    }

    /**
     * Takes in date in "Obj" format and converts it to "Date" format
     * @param date Date in "Date" format ("yyyy-MM-dd")
     * @return Date Object
     */
    public static Date makeDateObj(String date) throws ParseException {
        return sdf.parse(date);
    }

}


