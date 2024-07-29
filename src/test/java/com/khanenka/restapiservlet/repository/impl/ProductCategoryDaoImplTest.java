//package com.khanenka.restapiservlet.repository.impl;
//
//import com.khanenka.restapiservlet.model.CategoryType;
//import com.khanenka.restapiservlet.model.productdto.ProductCategoryDTOByNameAndType;
//import com.khanenka.restapiservlet.model.productdto.ProductDTOByNameAndPrice;
//import com.khanenka.restapiservlet.repository.ProductCategoryDao;
//import com.khanenka.restapiservlet.repository.ProductDao;
//import org.junit.Before;
//import org.junit.Rule;
//import org.junit.Test;
//import org.mockito.Mock;
//import org.testcontainers.containers.PostgreSQLContainer;
//import org.testcontainers.junit.jupiter.Testcontainers;
//
//import java.math.BigDecimal;
//import java.sql.*;
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.List;
//
//import static junit.framework.TestCase.assertEquals;
//import static org.junit.Assert.assertNotNull;
//import static org.junit.jupiter.api.Assertions.assertTrue;
//
//@Testcontainers
//public class ProductCategoryDaoImplTest {
//
//    private Connection connection;
//    private ProductDao productDao = new ProductDaoImpl();
//    private ProductCategoryDao productCategoryDao = new ProductCategoryDaoImpl();
//    @Mock
//    private ResultSet resultSet;
//    @Mock
//    private PreparedStatement preparedStatement;
//
//    @Rule
//    public PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:13");
//
//    @Before
//    public void createTableProduct() throws SQLException {
//        connection = DriverManager.getConnection(postgres.getJdbcUrl(), postgres.getUsername(), postgres.getPassword());
//        productDao = new ProductDaoImpl(connection);
//        productCategoryDao = new ProductCategoryDaoImpl(connection);
//        productDao.createTableProduct();
//
//
//    }
//
//    @Before
//    public void createProductCategoryTable() throws SQLException {
//        connection = DriverManager.getConnection(postgres.getJdbcUrl(), postgres.getUsername(), postgres.getPassword());
//        productDao = new ProductDaoImpl(connection);
//        productDao.createProductCategoryTable();
//    }
//
//    @Before
//    public void createProductProductCategory() throws SQLException {
//        connection = DriverManager.getConnection(postgres.getJdbcUrl(), postgres.getUsername(), postgres.getPassword());
//        productDao = new ProductDaoImpl(connection);
//        productDao.createProductProductCategory();
//    }
//
//    @Test
//    public void testProductCategoryDaoImpl() {
//        // Тест для конструктора по умолчанию
//        productCategoryDao = new ProductCategoryDaoImpl();
//        assertNotNull(productCategoryDao); // Проверка, что объект не равен null
//    }
//
//    @Test
//    public void testProductCategoryDaoImplConnection() {
//        // Тест для конструктора с подключением
//        productCategoryDao = new ProductCategoryDaoImpl(connection);
//        assertNotNull(productCategoryDao); // Проверка, что объект не равен null
//    }
//
//    @Test
//    public void addProductCategory() throws SQLException {
//
//        ProductDaoImpl productDAO = new ProductDaoImpl(DriverManager.getConnection(postgres.getJdbcUrl(), postgres.getUsername(), postgres.getPassword()));
//
//        // Create Product Category DTO
//        ProductCategoryDTOByNameAndType productCategoryDTO = new ProductCategoryDTOByNameAndType();
//        productCategoryDTO.setNameProductCategory("Category test AddProductCategory");
//        productCategoryDTO.setTypeProductCategory(CategoryType.DRINK);
//        List<ProductCategoryDTOByNameAndType> productCategoryDTOS = new ArrayList<>();
//        productCategoryDTOS.add(productCategoryDTO);
//
//        // Create Product DTO
//        ProductDTOByNameAndPrice product = new ProductDTOByNameAndPrice("Test Product", BigDecimal.valueOf(10.00), productCategoryDTOS);
//
//        // Add Product to Database
//        productDAO.addProduct(product);
//
//        // Retrieve all Product Categories from the Database
//        List<ProductCategoryDTOByNameAndType> allProductCategories = productCategoryDao.getAllProductCategories();
//
//        // Assertions
//        assertNotNull(product);
//        assertEquals(1, allProductCategories.size());
//
//        ProductCategoryDTOByNameAndType retrievedProductCategory = allProductCategories.get(0);
//        assertEquals("Category test AddProductCategory", retrievedProductCategory.getNameProductCategory());
//        assertEquals(CategoryType.DRINK, retrievedProductCategory.getTypeProductCategory());
//
//        // Check the product added to the category
//        List<ProductDTOByNameAndPrice> productsInCategory = retrievedProductCategory.getProductDTOS();
//        assertNotNull(productsInCategory);
//        assertEquals(1, productsInCategory.size());
//
//        // Check product details
//        ProductDTOByNameAndPrice retrievedProduct = productsInCategory.get(0);
//        assertEquals("Test Product", retrievedProduct.getNameProduct());
//        assertEquals(BigDecimal.valueOf(10.00), product.getPriceProduct());
//    }
//
//    @Test(expected = IllegalArgumentException.class)
//    public void testAddProductCategory_NullProduct() {
//        ProductCategoryDTOByNameAndType categoryWithNullProduct = new ProductCategoryDTOByNameAndType();
//        categoryWithNullProduct.setProductDTOS(Collections.singletonList(null));
//
//        productCategoryDao.addProductCategory(categoryWithNullProduct);
//    }
//
//
//    @Test
//    public void getAllProductCategories() throws SQLException {
//        ProductDaoImpl productDAO = new ProductDaoImpl(DriverManager.getConnection(postgres.getJdbcUrl(), postgres.getUsername(), postgres.getPassword()));
//        ProductCategoryDTOByNameAndType productCategoryDTO = new ProductCategoryDTOByNameAndType();
//        productCategoryDTO.setNameProductCategory("Category 1");
//        productCategoryDTO.setTypeProductCategory(CategoryType.DRINK);
//        List<ProductCategoryDTOByNameAndType> productCategoryDTOS = new ArrayList<>();
//        productCategoryDTOS.add(productCategoryDTO);
//        ProductDTOByNameAndPrice product = new ProductDTOByNameAndPrice("Test Product", BigDecimal.valueOf(10.00), productCategoryDTOS);
//        ProductDTOByNameAndPrice product1 = new ProductDTOByNameAndPrice("Test Product", BigDecimal.valueOf(10.00), productCategoryDTOS);
//        productDAO.addProduct(product);
//        productDao.addProduct(product1);
//
//
//        List<ProductCategoryDTOByNameAndType> allProductCategories = productCategoryDao.getAllProductCategories();
//
//        assertEquals(1, allProductCategories.size());
//        assertEquals("Category 1", allProductCategories.get(0).getNameProductCategory());
//        assertEquals(CategoryType.DRINK, allProductCategories.get(0).getTypeProductCategory());
//        assertEquals(1, allProductCategories.get(0).getProductDTOS().size());
//        assertEquals(new BigDecimal("10.00"), allProductCategories.get(0).getProductDTOS().get(0).getPriceProduct());
//
//    }
//
//    @Test
//    public void testGetAllProductCategoriesHandlesEmptyResultSet() throws SQLException {
//
//
//        List<ProductCategoryDTOByNameAndType> categories = productCategoryDao.getAllProductCategories();
//        assertTrue(categories.isEmpty(), "The categories list should be empty if no categories are found");
//    }
//
//    @Test
//    public void updateProductCategory() throws Exception {
//        ProductDaoImpl productDAO = new ProductDaoImpl(DriverManager.getConnection(postgres.getJdbcUrl(), postgres.getUsername(), postgres.getPassword()));
//
//
//        ProductCategoryDTOByNameAndType productCategoryDTO = new ProductCategoryDTOByNameAndType();
//        productCategoryDTO.setNameProductCategory("Category test AddProductCategory");
//        productCategoryDTO.setTypeProductCategory(CategoryType.DRINK);
//        List<ProductCategoryDTOByNameAndType> productCategoryDTOS = new ArrayList<>();
//        productCategoryDTOS.add(productCategoryDTO);
//
//        ProductDTOByNameAndPrice product = new ProductDTOByNameAndPrice("Test Product", BigDecimal.valueOf(10.00), productCategoryDTOS);
//
//        productDAO.addProduct(product);
//
//        List<ProductCategoryDTOByNameAndType> allProductCategories = productCategoryDao.getAllProductCategories();
//
//
//        assertNotNull(productCategoryDTO);
//        assertEquals(allProductCategories.get(0).getNameProductCategory(), "Category test AddProductCategory");
//        assertEquals(CategoryType.DRINK, productCategoryDTO.getTypeProductCategory());
//
//
//    }
//
//    @Test
//    public void deleteProductCategory() throws Exception {
//        ProductDaoImpl productDAO = new ProductDaoImpl(DriverManager.getConnection(postgres.getJdbcUrl(), postgres.getUsername(), postgres.getPassword()));
//        ProductCategoryDTOByNameAndType productCategoryDTO = new ProductCategoryDTOByNameAndType();
//        productCategoryDTO.setNameProductCategory("Category 1");
//        productCategoryDTO.setTypeProductCategory(CategoryType.DRINK);
//        List<ProductCategoryDTOByNameAndType> productCategoryDTOS = new ArrayList<>();
//        productCategoryDTOS.add(productCategoryDTO);
//        ProductDTOByNameAndPrice product = new ProductDTOByNameAndPrice("Test Product", BigDecimal.valueOf(10.00), productCategoryDTOS);
//        ProductDTOByNameAndPrice product1 = new ProductDTOByNameAndPrice("Test Product", BigDecimal.valueOf(10.00), productCategoryDTOS);
//        productDAO.addProduct(product);
//        productDao.addProduct(product1);
//
//        productCategoryDao.deleteProductCategory(productCategoryDTO);
//        List<ProductCategoryDTOByNameAndType> allProductCategories = productCategoryDao.getAllProductCategories();
//
//
//        assertEquals(0, allProductCategories.size());
//
//
//        // Verify that the associated products have been deleted (if this is the expected behavior)
//        List<ProductDTOByNameAndPrice> allProducts = productDAO.getAllProducts(); // Assuming this method exists
//        assertTrue(allProducts.stream().noneMatch(p -> p.getProductCategoryDTOS().contains(productCategoryDTO)),
//                "Products should not contain the deleted product category.");
//
//
//    }
//}