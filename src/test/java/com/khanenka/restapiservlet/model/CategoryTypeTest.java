package com.khanenka.restapiservlet.model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class CategoryTypeTest {

    @Test
    public void testEnumValues() {
        assertEquals(2, CategoryType.values().length);
        assertEquals(CategoryType.DRINK, CategoryType.valueOf("DRINK"));
        assertEquals(CategoryType.FISH, CategoryType.valueOf("FISH"));
    }

    @Test
    public void testEnumNotNull() {
        for (CategoryType type : CategoryType.values()) {
            assertNotNull(type);
        }
    }

    @Test
    public void testEnumName() {
        assertEquals("DRINK", CategoryType.DRINK.name());
        assertEquals("FISH", CategoryType.FISH.name());
    }

    @Test
    public void testEnumOrdinal() {
        assertEquals(0, CategoryType.DRINK.ordinal());
        assertEquals(1, CategoryType.FISH.ordinal());
    }
}