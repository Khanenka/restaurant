package com.khanenka.restapiservlet.repository.impl;

import com.khanenka.restapiservlet.model.CategoryType;
import com.khanenka.restapiservlet.model.productdto.ProductCategoryDTOByNameAndType;
import com.khanenka.restapiservlet.model.productdto.ProductDTOByNameAndPrice;
import com.khanenka.restapiservlet.repository.ProductCategoryDao;
import com.khanenka.restapiservlet.repository.ProductDao;
import com.khanenka.restapiservlet.repository.implementation.ProductCategoryDAOImpl;
import com.khanenka.restapiservlet.repository.implementation.ProductDAOImpl;
import com.khanenka.restapiservlet.util.DBConnection;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

@Testcontainers
public class ProductDaoImplTest {


    private Connection connection= DBConnection.getConnection();
    private ProductDao productDao = new ProductDAOImpl(connection);
    private ProductCategoryDao productCategoryDao = new ProductCategoryDAOImpl(connection);


    @Rule
    public PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:13");

    @Before
    public void createTableProduct() throws SQLException {
        connection = DriverManager.getConnection(postgres.getJdbcUrl(), postgres.getUsername(), postgres.getPassword());
        productDao = new ProductDAOImpl(connection);
        productCategoryDao = new ProductCategoryDAOImpl(connection);
        productDao.createTableProduct();


    }

    @Before
    public void createProductCategoryTable() throws SQLException {
        connection = DriverManager.getConnection(postgres.getJdbcUrl(), postgres.getUsername(), postgres.getPassword());
        productDao = new ProductDAOImpl(connection);
        productDao.createProductCategoryTable();
    }

    @Before
    public void createProductProductCategory() throws SQLException {
        connection = DriverManager.getConnection(postgres.getJdbcUrl(), postgres.getUsername(), postgres.getPassword());
        productDao = new ProductDAOImpl(connection);
        productDao.createProductProductCategory();
    }


    @Test
    public void testAddProduct() throws SQLException {

        ProductDAOImpl productDAO = new ProductDAOImpl(DriverManager.getConnection(
                postgres.getJdbcUrl(), postgres.getUsername(), postgres.getPassword()));


        ProductCategoryDTOByNameAndType productCategoryDTO = new ProductCategoryDTOByNameAndType();
        productCategoryDTO.setNameProductCategory("Category 1");
        productCategoryDTO.setTypeProductCategory(CategoryType.DRINK);
        List<ProductCategoryDTOByNameAndType> productCategoryDTOS = new ArrayList<>();
        productCategoryDTOS.add(productCategoryDTO);

        ProductDTOByNameAndPrice product = new ProductDTOByNameAndPrice(
                "Test Product", BigDecimal.valueOf(10.00), productCategoryDTOS,"new category");

        productDAO.addProduct(product);

        List<ProductDTOByNameAndPrice> allProducts = productDAO.getAllProducts();

        assertEquals(1, allProducts.size());
        ProductDTOByNameAndPrice retrievedProduct = allProducts.get(0);
        assertEquals("Test Product", retrievedProduct.getNameProduct());
        BigDecimal bigDecimal = new BigDecimal("10.00");
        assertEquals(bigDecimal, retrievedProduct.getPriceProduct());
        assertEquals(CategoryType.DRINK, retrievedProduct.getProductCategoryDTOS().get(0).getTypeProductCategory());
        assertNull("new category",allProducts.get(0).getProductCategoryDTOS().get(0).getNewCategory());
//
    }

    @Test
    public void testGetAllProducts() throws SQLException {


        ProductDAOImpl productDAO = new ProductDAOImpl(
                DriverManager.getConnection(postgres.getJdbcUrl(), postgres.getUsername(), postgres.getPassword()));
        ProductCategoryDTOByNameAndType productCategoryDTO = new ProductCategoryDTOByNameAndType();
        productCategoryDTO.setNameProductCategory("Category 1");
        productCategoryDTO.setTypeProductCategory(CategoryType.DRINK);
        List<ProductCategoryDTOByNameAndType> productCategoryDTOS = new ArrayList<>();
        productCategoryDTOS.add(productCategoryDTO);
        productCategoryDTO.setNameProductCategory("Category 2");
        productCategoryDTO.setTypeProductCategory(CategoryType.DRINK);
        productCategoryDTOS.add(productCategoryDTO);
        ProductDTOByNameAndPrice product = new ProductDTOByNameAndPrice(
                "Test Product", BigDecimal.valueOf(10.00), productCategoryDTOS,"new category");
        ProductDTOByNameAndPrice product1 = new ProductDTOByNameAndPrice(
                "Test Product", BigDecimal.valueOf(10.00), productCategoryDTOS,"new category");

        productDAO.addProduct(product);
        productDao.addProduct(product1);

        List<ProductDTOByNameAndPrice> allProducts = productDAO.getAllProducts();

        assertEquals(2, allProducts.size());
        assertEquals("Test Product", allProducts.get(0).getNameProduct());
        assertEquals(new BigDecimal("10.00"), allProducts.get(0).getPriceProduct());
        assertEquals(1, allProducts.get(0).getProductCategoryDTOS().size());
        assertEquals(CategoryType.DRINK, allProducts.get(0).getProductCategoryDTOS().get(0).getTypeProductCategory());
        assertNull("new category",allProducts.get(0).getProductCategoryDTOS().get(0).getNewCategory());
    }

    @Test
    public void testUpdateProduct() throws Exception {

        productDao = new ProductDAOImpl(connection);
        ProductDAOImpl productDAO = new ProductDAOImpl(DriverManager.getConnection(
                postgres.getJdbcUrl(), postgres.getUsername(), postgres.getPassword()));
        ProductCategoryDTOByNameAndType productCategoryDTO = new ProductCategoryDTOByNameAndType();
        productCategoryDTO.setNameProductCategory("Category 1");
        productCategoryDTO.setTypeProductCategory(CategoryType.DRINK);
        List<ProductCategoryDTOByNameAndType> productCategoryDTOS = new ArrayList<>();
        productCategoryDTOS.add(productCategoryDTO);
        productCategoryDTO.setNameProductCategory("Category 2");
        productCategoryDTO.setTypeProductCategory(CategoryType.DRINK);
        productCategoryDTOS.add(productCategoryDTO);
        ProductDTOByNameAndPrice product = new ProductDTOByNameAndPrice(
                "Test Product", new BigDecimal("10.00"), productCategoryDTOS,"new category");

        productDAO.addProduct(product);


        ProductCategoryDTOByNameAndType productCategory = new ProductCategoryDTOByNameAndType();
        productCategory.setNameProductCategory("Category 1");
        productCategory.setTypeProductCategory(CategoryType.DRINK);


        productDao.updateProduct(product, "Test Update Product");
        List<ProductDTOByNameAndPrice> allProducts = productDao.getAllProducts();

        assertNotNull(product);
        assertEquals(allProducts.get(0).getNameProduct(), "Test Update Product");
        assertEquals(new BigDecimal("10.00"), product.getPriceProduct());
        assertEquals("new category",product.getNewProduct());


    }

    @Test
    public void deleteProduct() throws SQLException {
        productDao = new ProductDAOImpl(connection);
        ProductDAOImpl productDAO = new ProductDAOImpl(DriverManager.getConnection(
                postgres.getJdbcUrl(), postgres.getUsername(), postgres.getPassword()));
        ProductCategoryDTOByNameAndType productCategoryDTO = new ProductCategoryDTOByNameAndType();
        productCategoryDTO.setNameProductCategory("Category 1");
        productCategoryDTO.setTypeProductCategory(CategoryType.DRINK);
        List<ProductCategoryDTOByNameAndType> productCategoryDTOS = new ArrayList<>();
        productCategoryDTOS.add(productCategoryDTO);
        productCategoryDTO.setNameProductCategory("Category 2");
        productCategoryDTO.setTypeProductCategory(CategoryType.DRINK);
        productCategoryDTOS.add(productCategoryDTO);
        ProductDTOByNameAndPrice product = new ProductDTOByNameAndPrice(
                "Test Product", new BigDecimal("10.00"), productCategoryDTOS,"new category");

        productDAO.addProduct(product);

        productDao.deleteProduct(product);

        List<ProductDTOByNameAndPrice> allProducts = productDao.getAllProducts();

        assertEquals(0, allProducts.size());
    }
}





