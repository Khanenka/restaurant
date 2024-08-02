package com.khanenka.restapiservlet.service;

import com.khanenka.restapiservlet.model.CategoryType;
import com.khanenka.restapiservlet.model.productdto.ProductCategoryDTOByNameAndType;
import com.khanenka.restapiservlet.model.productdto.ProductDTOByNameAndPrice;
import com.khanenka.restapiservlet.repository.implementation.ProductCategoryDAOImpl;
import com.khanenka.restapiservlet.repository.implementation.ProductDAOImpl;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

public class ProductCategoryServiceTest {
    private ProductCategoryService productCategoryService;
    private ProductCategoryDAOImpl productCategoryDAOMock;
    private ProductDAOImpl productDAOMock;

    @Before
    public void setup() {
        productCategoryDAOMock = mock(ProductCategoryDAOImpl.class);
        productDAOMock = mock(ProductDAOImpl.class);
        productCategoryService = new ProductCategoryService(productCategoryDAOMock, productDAOMock);
    }

    @Test
    public void testGetAllProductCategory() {
        ProductCategoryDTOByNameAndType productCategoryDTO = new ProductCategoryDTOByNameAndType();
        productCategoryDTO.setNameProductCategory("Category 1");
        productCategoryDTO.setTypeProductCategory(CategoryType.DRINK);
        List<ProductCategoryDTOByNameAndType> categories = Arrays.asList(
                new ProductCategoryDTOByNameAndType(
                        "Category 1", CategoryType.DRINK, null, "New"));
        when(productCategoryDAOMock.getAllProductCategories()).thenReturn(categories);
        List<ProductCategoryDTOByNameAndType> result = productCategoryService.getAllProductCategory();
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(productCategoryDAOMock, times(1)).getAllProductCategories();
    }

    @Test
    public void testAddProductCategory_WithProducts() throws SQLException {
        ProductCategoryDTOByNameAndType category = new ProductCategoryDTOByNameAndType(
                "Category 1", CategoryType.DRINK, null, "New");
        List<ProductCategoryDTOByNameAndType> categories = Arrays.asList(
                new ProductCategoryDTOByNameAndType(
                        "Category 1", CategoryType.DRINK, null, "New"));
        ProductDTOByNameAndPrice product = new ProductDTOByNameAndPrice("Test Product",
                BigDecimal.valueOf(10.00), categories, "New Category");
        category.setProductDTOS(Arrays.asList(product));
        productCategoryService.addProductCategory(category);
        verify(productCategoryDAOMock, times(1)).addProductCategory(category);
        verify(productDAOMock, times(1)).addProduct(product);
        verify(productCategoryDAOMock, times(1)).addProductJoinProductCategory(
                product.getNameProduct(), category.getNameProductCategory());
    }

    @Test
    public void testUpdateProductCategory() {
        ProductCategoryDTOByNameAndType category = new ProductCategoryDTOByNameAndType(
                "Category 1", CategoryType.DRINK, null, "New");
        String newCategoryName = "New Category";
        productCategoryService.updateProductCategory(category, newCategoryName);
        verify(productCategoryDAOMock, times(1)).updateProductCategory(category, newCategoryName);
        verify(productCategoryDAOMock, times(1)).deleteProductProductCategoryByName(
                category.getNameProductCategory());
        verify(productCategoryDAOMock, times(1)).addProductJoinProductCategory(
                category.getNameProductCategory(), newCategoryName);
    }

    @Test
    public void testDeleteProductCategory() {
        ProductCategoryDTOByNameAndType category = new ProductCategoryDTOByNameAndType(
                "Category 1", CategoryType.DRINK, null, "New");
        List<ProductCategoryDTOByNameAndType> categories = Arrays.asList(
                new ProductCategoryDTOByNameAndType(
                        "Category 1", CategoryType.DRINK, null, "New"));
        category.setProductDTOS(Arrays.asList(new ProductDTOByNameAndPrice("Test Product",
                BigDecimal.valueOf(10.00), categories, "New Category")));
        productCategoryService.deleteProductCategory(category);
        verify(productCategoryDAOMock, times(1)).deleteProductCategory(category);
        verify(productCategoryDAOMock, times(1)).deleteProductProductCategoryByName(
                category.getNameProductCategory());
        for (ProductDTOByNameAndPrice product : category.getProductDTOS()) {
            verify(productDAOMock, times(1)).deleteProduct(product);
        }
    }
}
