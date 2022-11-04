package com.example.mealy;

import static org.junit.Assert.assertEquals;

import com.example.mealy.functions.DateFunc;

import org.junit.Test;

public class DateFuncTest {

    /**
     * tests the conversion of int to date
     */
    @Test
    public void makeIntDateTest() {
        assertEquals(DateFunc.makeIntDate(1,2,3), "0003-02-01");
        assertEquals(DateFunc.makeIntDate(11,22,3333), "3333-22-11");
        assertEquals(DateFunc.makeIntDate(111,222,33333), "33333-222-111");
    }

    /**
     * tests the conversion of int to string
     */
    @Test
    public void makeIntStringTest() {
        assertEquals(DateFunc.makeIntString(2,2,3), "FEB 02 0003");
        assertEquals(DateFunc.makeIntString(69,13,3333), "JAN 69 3333");
    }

    String emptyString;

    /**
     * tests the conversion of date to string
     */
    @Test
    public void makeDateStringTest() {
        assertEquals(DateFunc.makeDateString("2222-11-12"), "NOV 12 2222");
        assertEquals(DateFunc.makeDateString("2222-13-12"), "JAN 12 2222");
        assertEquals(DateFunc.makeDateString(emptyString), "");
    }

    /**
     * tests the conversion of string to date
     */
    @Test
    public void makeStringDate() {
        assertEquals(DateFunc.makeStringDate("NOV 02 2222"), "2222-11-02");
        assertEquals(DateFunc.makeStringDate("JAN 12 2222"), "2222-01-12");
    }





}
