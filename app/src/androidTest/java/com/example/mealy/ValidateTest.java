package com.example.mealy;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.example.mealy.functions.Validate;

import org.junit.Test;

public class ValidateTest {
    String emptyStr;

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

    @Test
    public void validDateTest() {
        assertFalse(Validate.isEmpty(emptyStr));
        String date = "";
        assertFalse(Validate.validDate(date));
        date = "some random text";
        assertFalse(Validate.validDate(date));
        date = "1234567890";
        assertFalse(Validate.validDate(date));
        date = "!@#$%^&*()";
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
    }



}
