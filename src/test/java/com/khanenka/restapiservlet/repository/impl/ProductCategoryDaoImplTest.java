package com.khanenka.restapiservlet.repository.impl;

import com.khanenka.restapiservlet.exceptions.DatabaseConnectionException;
import com.khanenka.restapiservlet.model.CategoryType;
import com.khanenka.restapiservlet.model.productdto.ProductCategoryDTOByNameAndType;
import com.khanenka.restapiservlet.model.productdto.ProductDTOByNameAndPrice;
import com.khanenka.restapiservlet.repository.ProductDao;
import com.khanenka.restapiservlet.repository.implementation.ProductCategoryDAOImpl;
import com.khanenka.restapiservlet.repository.implementation.ProductDAOImpl;
import com.khanenka.restapiservlet.util.DBConnection;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Testcontainers
public class ProductCategoryDaoImplTest {
    private Connection connection = DBConnection.getConnection();
    private ProductDao productDao = new ProductDAOImpl(connection);
    private ProductCategoryDAOImpl productCategoryDao = new ProductCategoryDAOImpl(connection);

    @Mock
    private PreparedStatement preparedStatement;

    @Before
    public void createTableProduct() {
        productDao = new ProductDAOImpl(connection);
        productCategoryDao = new ProductCategoryDAOImpl(connection);
        productDao.createTableProduct();
    }

    @Before
    public void createProductCategoryTable() {
        productDao = new ProductDAOImpl(connection);
        productDao.createProductCategoryTable();
    }

    @Before
    public void createProductProductCategory() {
        productDao = new ProductDAOImpl(connection);
        productDao.createProductProductCategory();
    }

    @Test
    public void testProductCategoryDaoImpl() {
        productCategoryDao = new ProductCategoryDAOImpl(connection);
        assertNotNull(productCategoryDao);
    }

    @Test
    public void addProductCategory() throws SQLException {
        ProductCategoryDTOByNameAndType productCategoryDTO = new ProductCategoryDTOByNameAndType();
        productCategoryDTO.setNameProductCategory("Category 1");
        productCategoryDTO.setTypeProductCategory(CategoryType.DRINK);
        List<ProductCategoryDTOByNameAndType> productCategoryDTOS = new ArrayList<>();
        productCategoryDTOS.add(productCategoryDTO);
        ProductDTOByNameAndPrice product = new ProductDTOByNameAndPrice("Test Product",
                BigDecimal.valueOf(10.00), productCategoryDTOS, "New category");
        productDao.addProduct(product);
        List<ProductCategoryDTOByNameAndType> allProductCategories = productCategoryDao.getAllProductCategories();
        assertNotNull(product);
        assertEquals(1, allProductCategories.size());
        ProductCategoryDTOByNameAndType retrievedProductCategory = allProductCategories.get(0);
        assertEquals("Category 1", retrievedProductCategory.getNameProductCategory());
        assertEquals(CategoryType.DRINK, retrievedProductCategory.getTypeProductCategory());
        List<ProductDTOByNameAndPrice> productsInCategory = retrievedProductCategory.getProductDTOS();
        assertNotNull(productsInCategory);
        assertEquals(1, productsInCategory.size());
        ProductDTOByNameAndPrice retrievedProduct = productsInCategory.get(0);
        assertEquals("Test Product", retrievedProduct.getNameProduct());
        assertEquals(BigDecimal.valueOf(10.00), product.getPriceProduct());
    }

    @Test
    public void getAllProductCategories() throws SQLException {
        ProductCategoryDTOByNameAndType productCategoryDTO = new ProductCategoryDTOByNameAndType();
        productCategoryDTO.setNameProductCategory("Category 1");
        productCategoryDTO.setTypeProductCategory(CategoryType.DRINK);
        List<ProductCategoryDTOByNameAndType> productCategoryDTOS = new ArrayList<>();
        productCategoryDTOS.add(productCategoryDTO);
        ProductDTOByNameAndPrice product = new ProductDTOByNameAndPrice("Test Product",
                BigDecimal.valueOf(10.00), productCategoryDTOS, "New Category");
        ProductDTOByNameAndPrice product1 = new ProductDTOByNameAndPrice("Test Product",
                BigDecimal.valueOf(10.00), productCategoryDTOS, "New Category");
        productDao.addProduct(product);
        productDao.addProduct(product1);
        List<ProductCategoryDTOByNameAndType> allProductCategories = productCategoryDao.getAllProductCategories();
        assertEquals(2, allProductCategories.size());
        assertEquals("Category 1", allProductCategories.get(0).getNameProductCategory());
        assertEquals(CategoryType.DRINK, allProductCategories.get(0).getTypeProductCategory());
        assertEquals(1, allProductCategories.get(0).getProductDTOS().size());
        assertEquals(new BigDecimal("10.00"), allProductCategories.get(0).getProductDTOS().
                get(0).getPriceProduct());
    }

    @Test
    public void testGetAllProductCategoriesHandlesEmptyResultSet() {
        List<ProductCategoryDTOByNameAndType> categories = productCategoryDao.getAllProductCategories();
        System.out.println(categories.isEmpty());
        System.out.println(categories);
        assertFalse(categories.isEmpty());
    }

    @Test
    public void updateProductCategory() throws Exception {
        ProductCategoryDTOByNameAndType productCategoryDTO = new ProductCategoryDTOByNameAndType();
        productCategoryDTO.setNameProductCategory("Category 1");
        productCategoryDTO.setTypeProductCategory(CategoryType.DRINK);
        List<ProductCategoryDTOByNameAndType> productCategoryDTOS = new ArrayList<>();
        productCategoryDTOS.add(productCategoryDTO);
        ProductDTOByNameAndPrice product = new ProductDTOByNameAndPrice("Test Product",
                BigDecimal.valueOf(10.00), productCategoryDTOS, "New Category");
        productDao.addProduct(product);
        List<ProductCategoryDTOByNameAndType> allProductCategories = productCategoryDao.getAllProductCategories();
        assertNotNull(productCategoryDTO);
        assertEquals(allProductCategories.get(0).getNameProductCategory(), "Category 1");
        assertEquals(CategoryType.DRINK, productCategoryDTO.getTypeProductCategory());
    }

    @Test
    public void deleteProductCategory() throws Exception {
        ProductCategoryDTOByNameAndType productCategoryDTO = new ProductCategoryDTOByNameAndType();
        productCategoryDTO.setNameProductCategory("Category 1");
        productCategoryDTO.setTypeProductCategory(CategoryType.DRINK);
        List<ProductCategoryDTOByNameAndType> productCategoryDTOS = new ArrayList<>();
        productCategoryDTOS.add(productCategoryDTO);
        ProductDTOByNameAndPrice product = new ProductDTOByNameAndPrice("Test Product",
                BigDecimal.valueOf(10.00), productCategoryDTOS, "New Category");
        ProductDTOByNameAndPrice product1 = new ProductDTOByNameAndPrice("Test Product",
                BigDecimal.valueOf(10.00), productCategoryDTOS, "New Category");
        productDao.addProduct(product);
        productDao.addProduct(product1);
        productCategoryDao.deleteProductCategory(productCategoryDTO);
        List<ProductCategoryDTOByNameAndType> allProductCategories = productCategoryDao.getAllProductCategories();
        assertEquals(0, allProductCategories.size());
        List<ProductDTOByNameAndPrice> allProducts = productDao.getAllProducts();
        assertTrue(allProducts.stream().noneMatch(p -> p.getProductCategoryDTOS().contains(productCategoryDTO)),
                "Products should not contain the deleted product category.");
    }

    @Test
    public void deleteProductProductCategoryByName() {
        productCategoryDao.addProductJoinProductCategory(
                "Test product name", "Test category name");
        ResultSet productResult;
        String name = null;
        try {
            PreparedStatement productCategoryStatement =
                    connection.prepareStatement("SELECT name FROM product_productcategory");
            productResult = productCategoryStatement.executeQuery();
            while (productResult.next()) {
                name = productResult.getString("name");

            }
            assertEquals("Test category name", name);
        } catch (SQLException e) {
            throw new DatabaseConnectionException("error in joinProductProductCategoryByName");
        }
    }

    @Test
    public void testUpdateNonExistentProductCategory() {
        ProductCategoryDTOByNameAndType nonExistentCategory = new ProductCategoryDTOByNameAndType();
        nonExistentCategory.setNameProductCategory("Non-Existent Category");
        nonExistentCategory.setTypeProductCategory(CategoryType.DRINK);

        Exception exception = assertThrows(DatabaseConnectionException.class, () -> {
            productCategoryDao.updateProductCategory(nonExistentCategory, "New Category Name");
        });
        assertEquals("Failed to update product category", exception.getMessage());
    }

    @Test
    public void testDeleteNonExistentProductCategory() {
        ProductCategoryDTOByNameAndType nonExistentCategory = new ProductCategoryDTOByNameAndType();
        nonExistentCategory.setNameProductCategory("Non-Existent Category");
        nonExistentCategory.setTypeProductCategory(CategoryType.DRINK);
        assertDoesNotThrow(() -> productCategoryDao.deleteProductCategory(nonExistentCategory));
    }

    @Test
    public void testAddProductJoinProductCategorySuccess() {
        productCategoryDao.addProductJoinProductCategory(
                "Test product name", "Test category name");
        ResultSet productResult;
        String name = null;
        String productName = null;
        try {
            PreparedStatement productCategoryStatement =
                    connection.prepareStatement("SELECT * FROM product_productcategory;");
            productResult = productCategoryStatement.executeQuery();
            while (productResult.next()) {
                name = productResult.getString("name");
                productName = productResult.getString("nameproduct");
            }
            System.out.println(name + productName);
            assertEquals("Test category name", name);
            assertEquals("Test product name", productName);
        } catch (SQLException e) {
            throw new DatabaseConnectionException("error in joinProductProductCategoryByName");
        }
    }
}
