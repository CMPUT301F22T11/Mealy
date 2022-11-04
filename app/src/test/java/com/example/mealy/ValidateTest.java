package com.example.mealy;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;



import com.example.mealy.functions.Validate;

import org.junit.Test;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

/**
 * Checks if all the Validate functions work normally
 */
public class ValidateTest {
    String emptyStr;

    /**
     * checks if is empty actually finds empty strings (spaces or void)
     */
    @Test
    public void isEmptyTest() {
        assertTrue(Validate.isEmpty(emptyStr));
        String str = "";
        assertTrue(Validate.isEmpty(str));
        str = "                   ";
        assertTrue(Validate.isEmpty(str));
        str = "breh";
        assertFalse(Validate.isEmpty(str));
        str = "        breh         ";
        assertFalse(Validate.isEmpty(str));
    }

    /**
     * tests if the validDate function works as expected
     */
    @Test
    public void validDateTest() {
        assertFalse(Validate.validDate(emptyStr));
        String date = "";
        assertFalse(Validate.validDate(date));
        date = "some random text";
        assertFalse(Validate.validDate(date));
        date = "1234567890";
        assertFalse(Validate.validDate(date));
        date = "!@#$%^&*()";
        assertFalse(Validate.validDate(date));
        date = "2022-6-12";
        assertFalse(Validate.validDate(date));
        date = "1234-56-78";
        assertFalse(Validate.validDate(date));
        date = "12-12-2022";
        assertFalse(Validate.validDate(date));
        date = "0000-00-00";
        assertFalse(Validate.validDate(date));
        date = "2001-13-09";
        assertFalse(Validate.validDate(date));
        date = "2001-09-11";
        assertTrue(Validate.validDate(date));
        date = "9999-09-11";
        assertTrue(Validate.validDate(date));
    }

    /**
     * checks if the date passed function works as expected on both strings
     * and date objects
     * @throws Exception invalid date, shouldn't happen
     */
    @Test
    public void datePassed() throws Exception {
        assertThrows(Exception.class,() -> {Validate.datePassed("test");});
        assertFalse(Validate.datePassed("9999-12-12"));
        assertTrue(Validate.datePassed("2001-09-11"));

        Date date = new Date();
        Instant today = date.toInstant();

        date = Date.from(today.plus(1, ChronoUnit.DAYS));
        assertFalse(Validate.datePassed(date));
        date = Date.from(today.plus(-1, ChronoUnit.DAYS));
        assertTrue(Validate.datePassed(date));
    }



}
