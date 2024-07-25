package com.khanenka.restapiservlet.repository.impl;

import com.khanenka.restapiservlet.model.CategoryType;
import com.khanenka.restapiservlet.model.OrderStatus;
import com.khanenka.restapiservlet.model.productdto.OrderDetailDTO;
import com.khanenka.restapiservlet.model.productdto.ProductCategoryDTOByNameAndType;
import com.khanenka.restapiservlet.model.productdto.ProductDTO;
import com.khanenka.restapiservlet.repository.OrderDetailDao;
import com.khanenka.restapiservlet.repository.ProductCategoryDao;
import com.khanenka.restapiservlet.repository.ProductDao;
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.*;

@Testcontainers
public class OrderDetailDaoImplTest {


    private Connection connection;
    private ProductDao productDao = new ProductDaoImpl();
    private ProductCategoryDao productCategoryDao = new ProductCategoryDaoImpl();
    private OrderDetailDao orderDetailDao = new OrderDetailDaoImpl();


    @Rule
    public PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:13");

    @Before
    public void createTableOrderDetailAndOrderDetailProductInContainer() throws SQLException {
        connection = DriverManager.getConnection(postgres.getJdbcUrl(), postgres.getUsername(), postgres.getPassword());
        orderDetailDao = new OrderDetailDaoImpl(connection);
        orderDetailDao = new OrderDetailDaoImpl(connection);
        orderDetailDao.createOrderDetailTable();
        orderDetailDao.createOrderDetailProductTable();
        productDao = new ProductDaoImpl(connection);
        productDao.createProductProductCategory();
        productDao.createProductCategoryTable();

        productCategoryDao = new ProductCategoryDaoImpl(connection);
        productDao.createTableProduct();
    }


    @Test
    public void createOrderDetailTable() {
        connection = mock(Connection.class);
        orderDetailDao = mock(OrderDetailDaoImpl.class);
        doNothing().when(orderDetailDao).createOrderDetailTable();

        // Вызов метода
        orderDetailDao.createOrderDetailTable();

        // Проверка
        verify(orderDetailDao, times(1)).createOrderDetailTable();

    }


    @Test
    public void addOrderDetail() throws Exception {
        orderDetailDao = new OrderDetailDaoImpl(DriverManager.getConnection(postgres.getJdbcUrl(), postgres.getUsername(), postgres.getPassword()));
        productCategoryDao = new ProductCategoryDaoImpl(DriverManager.getConnection(postgres.getJdbcUrl(), postgres.getUsername(), postgres.getPassword()));
        productDao = new ProductDaoImpl(DriverManager.getConnection(postgres.getJdbcUrl(), postgres.getUsername(), postgres.getPassword()));

        ProductCategoryDTOByNameAndType productCategoryDTO = new ProductCategoryDTOByNameAndType();
        productCategoryDTO.setNameProductCategory("Category 1");
        productCategoryDTO.setTypeProductCategory(CategoryType.DRINK);

        List<ProductCategoryDTOByNameAndType> productCategoryDTOS = new ArrayList<>();

        productCategoryDTOS.add(productCategoryDTO);
        productCategoryDao.addProductCategory(productCategoryDTO);

        OrderDetailDTO orderDetailDTO = new OrderDetailDTO();
        orderDetailDTO.setIdOrderDetail(1);
        orderDetailDTO.setOrderStatus(OrderStatus.DELIVERED);
        orderDetailDTO.setTotalAmauntOrderDetail(new BigDecimal("20.00"));
        ProductDTO productDTO = new ProductDTO(1, "test", new BigDecimal("20.00"));
        List<ProductDTO> productDTOList = new ArrayList<>();
        productDTOList.add(productDTO);
        orderDetailDTO.setProducts(productDTOList);
        orderDetailDao.addOrderDetail(orderDetailDTO);


        List<OrderDetailDTO> allOrderDetails = orderDetailDao.getAllOrderDetails();

        assertEquals(1, allOrderDetails.size());

        assertEquals(1, allOrderDetails.get(0).getIdOrderDetail());

        assertEquals(OrderStatus.DELIVERED, allOrderDetails.get(0).getOrderStatus());
        assertEquals(new BigDecimal("20.00"), allOrderDetails.get(0).getTotalAmauntOrderDetail());

    }


    @Test
    public void getAllOrderDetails() throws Exception {
        // Arrange: Setup necessary dependencies and data
        orderDetailDao = new OrderDetailDaoImpl(DriverManager.getConnection(postgres.getJdbcUrl(), postgres.getUsername(), postgres.getPassword()));


        // Create and add a product category
        ProductCategoryDTOByNameAndType productCategoryDTO = new ProductCategoryDTOByNameAndType();
        productCategoryDTO.setNameProductCategory("Category 2");
        productCategoryDTO.setTypeProductCategory(CategoryType.DRINK);
        productCategoryDao.addProductCategory(productCategoryDTO);

        // Create some order details
        OrderDetailDTO orderDetailDTO1 = new OrderDetailDTO();
        orderDetailDTO1.setIdOrderDetail(1);
        orderDetailDTO1.setOrderStatus(OrderStatus.DELIVERED);
        orderDetailDTO1.setTotalAmauntOrderDetail(new BigDecimal("30.00"));
        ProductDTO productDTO1 = new ProductDTO(1, "test product 1", new BigDecimal("30.00"));
        List<ProductDTO> productDTOList1 = new ArrayList<>();
        productDTOList1.add(productDTO1);
        orderDetailDTO1.setProducts(productDTOList1);
        orderDetailDao.addOrderDetail(orderDetailDTO1);

        OrderDetailDTO orderDetailDTO2 = new OrderDetailDTO();
        orderDetailDTO2.setIdOrderDetail(2);
        orderDetailDTO2.setOrderStatus(OrderStatus.PENDING);
        orderDetailDTO2.setTotalAmauntOrderDetail(new BigDecimal("50.00"));
        ProductDTO productDTO2 = new ProductDTO(2, "test product 2", new BigDecimal("50.00"));
        List<ProductDTO> productDTOList2 = new ArrayList<>();
        productDTOList2.add(productDTO2);
        orderDetailDTO2.setProducts(productDTOList2);
        orderDetailDao.addOrderDetail(orderDetailDTO2);

        // Act: Call the method under test
        List<OrderDetailDTO> allOrderDetails = orderDetailDao.getAllOrderDetails();

        // Assert: Validate the results
        assertEquals(2, allOrderDetails.size());

        assertEquals(1, allOrderDetails.get(0).getIdOrderDetail());
        assertEquals(OrderStatus.DELIVERED, allOrderDetails.get(0).getOrderStatus());
        assertEquals(new BigDecimal("30.00"), allOrderDetails.get(0).getTotalAmauntOrderDetail());

        assertEquals(2, allOrderDetails.get(1).getIdOrderDetail());
        assertEquals(OrderStatus.PENDING, allOrderDetails.get(1).getOrderStatus());
        assertEquals(new BigDecimal("50.00"), allOrderDetails.get(1).getTotalAmauntOrderDetail());
    }

    @Test
    public void updateOrderDetail() throws Exception {
        // Arrange: Setup necessary dependencies and data
        orderDetailDao = new OrderDetailDaoImpl(DriverManager.getConnection(postgres.getJdbcUrl(), postgres.getUsername(), postgres.getPassword()));

        // Create and add an order detail to update
        OrderDetailDTO orderDetailDTO = new OrderDetailDTO();
        orderDetailDTO.setIdOrderDetail(1);
        orderDetailDTO.setOrderStatus(OrderStatus.DELIVERED);
        orderDetailDTO.setTotalAmauntOrderDetail(new BigDecimal("30.00"));
        ProductDTO productDTO = new ProductDTO(1, "test product 1", new BigDecimal("30.00"));
        List<ProductDTO> productDTOList = new ArrayList<>();
        productDTOList.add(productDTO);
        orderDetailDTO.setProducts(productDTOList);
        orderDetailDao.addOrderDetail(orderDetailDTO);

        // Act: Update the order detail
        orderDetailDTO.setIdOrderDetail(1);
        orderDetailDTO.setOrderStatus(OrderStatus.SHIPPED); // Change the status
        orderDetailDTO.setTotalAmauntOrderDetail(new BigDecimal("35.00")); // Change the amount
        orderDetailDao.updateOrderDetail(orderDetailDTO);

        List<OrderDetailDTO> allOrderDetails = orderDetailDao.getAllOrderDetails();
        long idOrderDetail = allOrderDetails.get(0).getIdOrderDetail();
        OrderStatus orderStatus = allOrderDetails.get(0).getOrderStatus();
        BigDecimal totalAmauntOrderDetail = allOrderDetails.get(0).getTotalAmauntOrderDetail();

        assertEquals(1, idOrderDetail);
        assertEquals(OrderStatus.SHIPPED, orderStatus);
        assertEquals(new BigDecimal("35.00"), totalAmauntOrderDetail);
    }

    @Test
    public void deleteOrderDetail() throws Exception {
        // Arrange: Setup necessary dependencies and data
        orderDetailDao = new OrderDetailDaoImpl(DriverManager.getConnection(postgres.getJdbcUrl(), postgres.getUsername(), postgres.getPassword()));

        // Create and add an order detail to delete
        OrderDetailDTO orderDetailDTO = new OrderDetailDTO();
        orderDetailDTO.setIdOrderDetail(2);
        orderDetailDTO.setOrderStatus(OrderStatus.PENDING); // Initial order status
        orderDetailDTO.setTotalAmauntOrderDetail(new BigDecimal("20.00"));
        ProductDTO productDTO = new ProductDTO(2, "test product 2", new BigDecimal("20.00"));
        List<ProductDTO> productDTOList = new ArrayList<>();
        productDTOList.add(productDTO);
        orderDetailDTO.setProducts(productDTOList);
        orderDetailDao.addOrderDetail(orderDetailDTO);

        // Act: Delete the order detail
        orderDetailDao.deleteOrderDetail(orderDetailDTO);

        // Assert: Verify the order detail has been deleted
        List<OrderDetailDTO> allOrderDetails = orderDetailDao.getAllOrderDetails();
        boolean exists = allOrderDetails.stream()
                .anyMatch(detail -> detail.getIdOrderDetail() == orderDetailDTO.getIdOrderDetail());

        assertFalse("Order detail should be deleted", exists);
    }
}