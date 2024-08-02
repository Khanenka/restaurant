package com.khanenka.restapiservlet.service;

import com.khanenka.restapiservlet.model.CategoryType;
import com.khanenka.restapiservlet.model.productdto.ProductCategoryDTOByNameAndType;
import com.khanenka.restapiservlet.model.productdto.ProductDTOByNameAndPrice;
import com.khanenka.restapiservlet.repository.OrderDetailDao;
import com.khanenka.restapiservlet.repository.ProductCategoryDao;
import com.khanenka.restapiservlet.repository.ProductDao;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class ProductServiceTest {
    private ProductDao productDAO;
    private ProductCategoryDao productCategoryDAO;
    private OrderDetailDao orderDetailDAO;
    private ProductService productService;

    @Before
    public void setUp() {
        productDAO = mock(ProductDao.class);
        productCategoryDAO = mock(ProductCategoryDao.class);
        orderDetailDAO = mock(OrderDetailDao.class);
        productService = new ProductService(productDAO, productCategoryDAO, orderDetailDAO);
    }

    @Test
    public void testAddProductSuccess() throws SQLException {
        List<ProductCategoryDTOByNameAndType> categories = Arrays.asList(
                new ProductCategoryDTOByNameAndType(
                        "Category 1", CategoryType.DRINK, null, "New"));
        ProductDTOByNameAndPrice product = new ProductDTOByNameAndPrice("Test Product",
                BigDecimal.valueOf(10.00), categories, "New Category");
        productService.addProduct(product);
        verify(productDAO).addProduct(product);
    }

    @Test
    public void testAddProductThrowsExceptionForNullName() {
        List<ProductCategoryDTOByNameAndType> categories = Arrays.asList(
                new ProductCategoryDTOByNameAndType(
                        "Category 1", CategoryType.DRINK, null, "New"));
        ProductDTOByNameAndPrice product = new ProductDTOByNameAndPrice(null,
                BigDecimal.valueOf(10.00), categories, "New Category");

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            productService.addProduct(product);
        });
        assertEquals("Product name cannot be null or empty", exception.getMessage());
    }

    @Test
    public void testDeleteProductSuccess() throws SQLException {
        List<ProductCategoryDTOByNameAndType> categories = Arrays.asList(
                new ProductCategoryDTOByNameAndType(
                        "Category 1", CategoryType.DRINK, null, "New"));
        ProductDTOByNameAndPrice product = new ProductDTOByNameAndPrice("Test Product",
                BigDecimal.valueOf(10.00), categories, "New Category");
        product.setProductCategoryDTOS(new ArrayList<>());
        productService.deleteProduct(product);
        verify(productDAO).deleteProduct(product);
        verify(productDAO).deleteProductCategories(product.getNameProduct());
    }

    @Test
    public void testUpdateProduct() {
        List<ProductCategoryDTOByNameAndType> categories = Arrays.asList(
                new ProductCategoryDTOByNameAndType(
                        "Category 1", CategoryType.DRINK, null, "New"));
        ProductDTOByNameAndPrice product = new ProductDTOByNameAndPrice("Test Product",
                BigDecimal.valueOf(10.00), categories, "New Category");
        String updatedNameProduct = "New";
        productService.updateProduct(product, updatedNameProduct);
        verify(productDAO).updateProductProductCategory(product);
        verify(productDAO).updateProduct(product, updatedNameProduct);
    }

    @Test
    public void testValidateProductThrowsExceptionForEmptyPrice() {
        List<ProductCategoryDTOByNameAndType> categories = Arrays.asList(
                new ProductCategoryDTOByNameAndType(
                        "Category 1", CategoryType.DRINK, null, "New"));
        ProductDTOByNameAndPrice product = new ProductDTOByNameAndPrice("Test Product",
                BigDecimal.ZERO, categories, "New Category");

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            productService.addProduct(product);
        });
        assertEquals("Price should be greater than zero", exception.getMessage());
    }

    @Test
    public void testGetAllProducts() {
        List<ProductDTOByNameAndPrice> productList = new ArrayList<>();
        when(productDAO.getAllProducts()).thenReturn(productList);
        List<ProductDTOByNameAndPrice> result = productService.getAllProducts();
        assertEquals(productList, result);
        verify(productDAO).getAllProducts();
    }
}
