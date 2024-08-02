package com.khanenka.restapiservlet.model;

import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class ProductCategoryTest {

    private ProductCategory productCategory;

    @Before
    public void setUp() {
        productCategory = new ProductCategory(1, "Drink", CategoryType.DRINK);
    }

    @Test
    public void testConstructorAndGetters() {
        assertEquals(1, productCategory.getIdProductCategory());
        assertEquals("Drink", productCategory.getNameProductCategory());
        assertEquals(CategoryType.DRINK, productCategory.getTypeProductCategory());
        assertNull(productCategory.getProducts());
    }

    @Test
    public void testSetters() {
        productCategory.setNameProductCategory("Home Appliances");
        productCategory.setTypeProductCategory(CategoryType.FISH);
        assertEquals("Home Appliances", productCategory.getNameProductCategory());
        assertEquals(CategoryType.FISH, productCategory.getTypeProductCategory());
    }

    @Test
    public void testToString() {
        String expectedString =
                "ProductCategory(" +
                        "idProductCategory=1, nameProductCategory=Drink, typeProductCategory=DRINK, products=null)";
        assertEquals(expectedString, productCategory.toString());
    }

    @Test
    public void testEqualsAndHashCode() {
        ProductCategory productCategory1 = new ProductCategory(
                1, "Drink", CategoryType.DRINK);
        ProductCategory productCategory2 = new ProductCategory(
                1, "Drink", CategoryType.DRINK);
        ProductCategory productCategory3 = new ProductCategory(
                2, "Fish", CategoryType.FISH);
        assertEquals(productCategory1, productCategory2);
        assertNotEquals(productCategory1, productCategory3);
        assertEquals(productCategory1.hashCode(), productCategory2.hashCode());
    }

    @Test
    public void testAddProducts() {
        List<Product> products = new ArrayList<>();
        products.add(new Product(1, "TV", new BigDecimal(500.0)));
        products.add(new Product(2, "Radio", new BigDecimal(100.0)));
        productCategory.setProducts(products);
        assertEquals(products, productCategory.getProducts());
        assertEquals(2, productCategory.getProducts().size());
    }
}
