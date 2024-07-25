package com.khanenka.restapiservlet.model;

import org.junit.Test;

import static org.junit.Assert.*;

public class ProductCategoryTest {
    ProductCategory productCategory = new ProductCategory(1L, "Electronics", CategoryType.DRINK);

    @Test
    public void testProductCategoryCreation() {
        assertNotNull(productCategory);
    }

    @Test
    public void testProductCategoryId() {
        assertEquals(1L, productCategory.getIdProductCategory());
    }

    @Test
    public void testProductCategoryName() {
        assertEquals("Electronics", productCategory.getNameProductCategory());
    }

    @Test
    public void testProductCategoryType() {
        assertEquals(CategoryType.DRINK, productCategory.getTypeProductCategory());
    }

    @Test
    public void testProductCategoryProductsInitialization() {
        assertNull(productCategory.getProducts());
    }

}