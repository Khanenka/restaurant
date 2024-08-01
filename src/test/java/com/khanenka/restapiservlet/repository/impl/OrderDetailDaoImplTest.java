package com.khanenka.restapiservlet.repository.impl;

import com.khanenka.restapiservlet.model.CategoryType;
import com.khanenka.restapiservlet.model.OrderStatus;
import com.khanenka.restapiservlet.model.productdto.OrderDetailDTO;
import com.khanenka.restapiservlet.model.productdto.ProductCategoryDTOByNameAndType;
import com.khanenka.restapiservlet.model.productdto.ProductDTO;
import com.khanenka.restapiservlet.repository.OrderDetailDao;
import com.khanenka.restapiservlet.repository.ProductCategoryDao;
import com.khanenka.restapiservlet.repository.ProductDao;
import com.khanenka.restapiservlet.repository.implementation.OrderDetailDAOImpl;
import com.khanenka.restapiservlet.repository.implementation.ProductCategoryDAOImpl;
import com.khanenka.restapiservlet.repository.implementation.ProductDAOImpl;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@Testcontainers
public class OrderDetailDaoImplTest {


    private Connection connection;
    private ProductDao productDao = new ProductDAOImpl(connection);
    private ProductCategoryDao productCategoryDao = new ProductCategoryDAOImpl(connection);
    private OrderDetailDao orderDetailDao = new OrderDetailDAOImpl();


    @Rule
    public PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:13");

    @Before
    public void createTableOrderDetailAndOrderDetailProductInContainer() throws SQLException {
        connection = DriverManager.getConnection(postgres.getJdbcUrl(), postgres.getUsername(), postgres.getPassword());
        orderDetailDao = new OrderDetailDAOImpl(connection);
        orderDetailDao = new OrderDetailDAOImpl(connection);
        orderDetailDao.createOrderDetailTable();
        orderDetailDao.createOrderDetailProductTable();
        productDao = new ProductDAOImpl(connection);
        productDao.createProductProductCategory();
        productDao.createProductCategoryTable();
        productCategoryDao = new ProductCategoryDAOImpl(connection);
        productDao.createTableProduct();
    }

    @Test
    public void createOrderDetailTable() {
        connection = mock(Connection.class);
        orderDetailDao = mock(OrderDetailDAOImpl.class);
        doNothing().when(orderDetailDao).createOrderDetailTable();
        orderDetailDao.createOrderDetailTable();
        verify(orderDetailDao, times(1)).createOrderDetailTable();
    }


    @Test
    public void addOrderDetail() throws Exception {
        orderDetailDao = new OrderDetailDAOImpl(
                DriverManager.getConnection(postgres.getJdbcUrl(), postgres.getUsername(), postgres.getPassword()));
        productCategoryDao = new ProductCategoryDAOImpl(
                DriverManager.getConnection(postgres.getJdbcUrl(), postgres.getUsername(), postgres.getPassword()));
        productDao = new ProductDAOImpl(
                DriverManager.getConnection(postgres.getJdbcUrl(), postgres.getUsername(), postgres.getPassword()));
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
        orderDetailDao = new OrderDetailDAOImpl(DriverManager.getConnection(postgres.getJdbcUrl(), postgres.getUsername(), postgres.getPassword()));
        ProductCategoryDTOByNameAndType productCategoryDTO = new ProductCategoryDTOByNameAndType();
        productCategoryDTO.setNameProductCategory("Category 2");
        productCategoryDTO.setTypeProductCategory(CategoryType.DRINK);
        productCategoryDao.addProductCategory(productCategoryDTO);
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
        List<OrderDetailDTO> allOrderDetails = orderDetailDao.getAllOrderDetails();
        assertEquals(2, allOrderDetails.size());
        assertEquals(1, allOrderDetails.get(1).getIdOrderDetail());
        assertEquals(OrderStatus.DELIVERED, allOrderDetails.get(1).getOrderStatus());
        assertEquals(new BigDecimal("30.00"), allOrderDetails.get(1).getTotalAmauntOrderDetail());
        assertEquals(2, allOrderDetails.get(0).getIdOrderDetail());
        assertEquals(OrderStatus.PENDING, allOrderDetails.get(0).getOrderStatus());
        assertEquals(new BigDecimal("50.00"), allOrderDetails.get(0).getTotalAmauntOrderDetail());
    }

    @Test
    public void updateOrderDetail() throws Exception {
        orderDetailDao = new OrderDetailDAOImpl(
                DriverManager.getConnection(postgres.getJdbcUrl(), postgres.getUsername(), postgres.getPassword()));
        OrderDetailDTO orderDetailDTO = new OrderDetailDTO();
        orderDetailDTO.setIdOrderDetail(1);
        orderDetailDTO.setOrderStatus(OrderStatus.DELIVERED);
        orderDetailDTO.setTotalAmauntOrderDetail(new BigDecimal("30.00"));
        ProductDTO productDTO = new ProductDTO(1, "test product 1", new BigDecimal("30.00"));
        List<ProductDTO> productDTOList = new ArrayList<>();
        productDTOList.add(productDTO);
        orderDetailDTO.setProducts(productDTOList);
        orderDetailDao.addOrderDetail(orderDetailDTO);
        orderDetailDTO.setIdOrderDetail(1);
        orderDetailDTO.setOrderStatus(OrderStatus.SHIPPED);
        orderDetailDTO.setTotalAmauntOrderDetail(new BigDecimal("35.00"));
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
        orderDetailDao = new OrderDetailDAOImpl(
                DriverManager.getConnection(postgres.getJdbcUrl(), postgres.getUsername(), postgres.getPassword()));
        OrderDetailDTO orderDetailDTO = new OrderDetailDTO();
        orderDetailDTO.setIdOrderDetail(2);
        orderDetailDTO.setOrderStatus(OrderStatus.PENDING); // Initial order status
        orderDetailDTO.setTotalAmauntOrderDetail(new BigDecimal("20.00"));
        ProductDTO productDTO = new ProductDTO(2, "test product 2", new BigDecimal("20.00"));
        List<ProductDTO> productDTOList = new ArrayList<>();
        productDTOList.add(productDTO);
        orderDetailDTO.setProducts(productDTOList);
        orderDetailDao.addOrderDetail(orderDetailDTO);
        orderDetailDao.deleteOrderDetail(orderDetailDTO);
        List<OrderDetailDTO> allOrderDetails = orderDetailDao.getAllOrderDetails();
        boolean exists = allOrderDetails.stream()
                .anyMatch(detail -> detail.getIdOrderDetail() == orderDetailDTO.getIdOrderDetail());
        assertFalse("Order detail should be deleted", exists);
    }

    @Test
    public void deleteOrderDetailProductByIdTest() throws SQLException {
        OrderDetailDTO orderDetailDTO = new OrderDetailDTO();
        long id=0;
        orderDetailDTO.setIdOrderDetail(2);
        orderDetailDTO.setOrderStatus(OrderStatus.PENDING); // Initial order status
        orderDetailDTO.setTotalAmauntOrderDetail(new BigDecimal("20.00"));
        ProductDTO productDTO = new ProductDTO(2, "test product 2", new BigDecimal("20.00"));
        List<ProductDTO> productDTOList = new ArrayList<>();
        productDTOList.add(productDTO);
        orderDetailDTO.setProducts(productDTOList);
        orderDetailDao.addOrderDetailProduct(orderDetailDTO);
        orderDetailDao.deleteOrderDetailProductById(2);
        PreparedStatement statement = connection.prepareStatement("select order_detail_id from order_detail_product");
        ResultSet resultSet=statement.executeQuery();
        while (resultSet.next()){
             id = resultSet.getLong("order_detail_id");
        }
        assertEquals(0,id);
    }
}
