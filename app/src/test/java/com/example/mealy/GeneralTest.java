package com.example.mealy;

import org.junit.Test;
import static org.junit.Assert.*;

import com.example.mealy.functions.General;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
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

    /**
     * Test the list to map function by comparing the map's content to
     * the original list.
     */
    @Test
    public void testListToMap() {
        ArrayList list = new ArrayList<>();
        list.add("Hello");
        list.add("World");
        Map map = General.listToMap(list);
        assertEquals(map.get("0"), "Hello");
        assertEquals(map.get("1"), "World");
    }

    /**
     * Test the list to map function by comparing the map's content to
     * the original list.
     */
    @Test
    public void testMapToList() {
        Map map = new HashMap<String, Object>();
        map.put("0", "Hello");
        map.put("1", "World");
        ArrayList<String> list = General.mapToArrayList(map);
        assertEquals(list.get(0), "Hello");
        assertEquals(list.get(1), "World");
    }

}
