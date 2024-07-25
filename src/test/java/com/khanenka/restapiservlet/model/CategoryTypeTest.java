package com.khanenka.restapiservlet.model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class CategoryTypeTest {

    @Test
    public void testEnumValues() {
        // Проверяем, что перечисление содержит определенные значения
        assertEquals(2, CategoryType.values().length);
        assertEquals(CategoryType.DRINK, CategoryType.valueOf("DRINK"));
        assertEquals(CategoryType.FISH, CategoryType.valueOf("FISH"));
    }

    @Test
    public void testEnumNotNull() {
        // Проверяем, что перечисление не равно null
        for (CategoryType type : CategoryType.values()) {
            assertNotNull(type);
        }
    }

    @Test
    public void testEnumName() {
        // Проверяем имена перечисления
        assertEquals("DRINK", CategoryType.DRINK.name());
        assertEquals("FISH", CategoryType.FISH.name());
    }

    @Test
    public void testEnumOrdinal() {
        // Проверяем порядковые номера перечисления
        assertEquals(0, CategoryType.DRINK.ordinal());
        assertEquals(1, CategoryType.FISH.ordinal());
    }
}