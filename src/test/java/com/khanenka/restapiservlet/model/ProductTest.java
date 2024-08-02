package com.khanenka.restapiservlet.model;

import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.*;

public class ProductTest {

    Product product = new Product(1L, "Test Product", BigDecimal.valueOf(99.99));


    @Test
    public void testProductCreation() {
        assertNotNull(product);
    }

    @Test
    public void testProductId() {
        assertEquals(1L, product.getIdProduct());
    }

    @Test
    public void testProductName() {
        assertEquals("Test Product", product.getNameProduct());
    }

    @Test
    public void testProductPrice() {
        assertEquals(BigDecimal.valueOf(99.99), product.getPriceProduct());
    }

    @Test
    public void testProductQuantity() {
        assertEquals(0, product.getQuantityProduct());
    }

    @Test
    public void testProductAvailability() {
        assertFalse(product.isAvailableProduct());
    }

}