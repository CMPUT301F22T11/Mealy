package com.example.mealy;

import org.junit.Test;
import static org.junit.Assert.*;

import com.example.mealy.functions.General;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

public class GeneralTest {
    /**
     * Test blank if void method, check cases when the string
     * is null, empty or set.
     */
    @Test
    public void testBlankIfVoid() {
        assertEquals(General.blankIfVoid(""), "");
        assertEquals(General.blankIfVoid(null), "");
        assertEquals(General.blankIfVoid("Hello"), "Hello");
    }



}
