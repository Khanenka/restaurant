package com.khanenka.restapiservlet.service;

import com.khanenka.restapiservlet.exceptions.DatabaseConnectionException;
import com.khanenka.restapiservlet.model.productdto.OrderDetailDTO;
import com.khanenka.restapiservlet.model.productdto.ProductDTO;
import com.khanenka.restapiservlet.repository.implementation.OrderDetailDAOImpl;
import com.khanenka.restapiservlet.repository.implementation.ProductDAOImpl;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class OrderDetailServiceTest {
    private OrderDetailDAOImpl orderDetailDAO;
    private ProductDAOImpl productDAO;
    private OrderDetailService orderDetailService;

    @Before
    public void setUp() {
        orderDetailDAO = mock(OrderDetailDAOImpl.class);
        productDAO = mock(ProductDAOImpl.class);
        orderDetailService = new OrderDetailService(orderDetailDAO, productDAO);
    }

    @Test
    public void testAddOrderDetail() {
        OrderDetailDTO orderDetail = new OrderDetailDTO();
        orderDetail.setProducts(Arrays.asList(new ProductDTO(), new ProductDTO()));
        orderDetailService.addOrderDetail(orderDetail);
        verify(orderDetailDAO).addOrderDetail(orderDetail);
        verify(orderDetailDAO).addOrderDetailProduct(orderDetail);
        verify(productDAO, times(2)).addProductById(any(ProductDTO.class));
    }

    @Test
    public void testGetOrderDetail() {
        // given
        List<OrderDetailDTO> orderDetails = Arrays.asList(new OrderDetailDTO(), new OrderDetailDTO());
        when(orderDetailDAO.getAllOrderDetails()).thenReturn(orderDetails);
        List<OrderDetailDTO> result = orderDetailService.getOrderDetail();
        verify(orderDetailDAO).getAllOrderDetails();
        assertEquals(orderDetails, result);
    }

    @Test
    public void testUpdateOrderDetail() {
        OrderDetailDTO orderDetail = new OrderDetailDTO();
        orderDetail.setIdOrderDetail(1);
        orderDetailService.updateOrderDetail(orderDetail);
        verify(orderDetailDAO).updateOrderDetail(orderDetail);
        verify(orderDetailDAO).deleteOrderDetailProductById(orderDetail.getIdOrderDetail());
        verify(orderDetailDAO).addOrderDetailProduct(orderDetail);
    }

    @Test
    public void testUpdateOrderDetailThrowsException() {
        OrderDetailDTO orderDetail = new OrderDetailDTO();
        orderDetail.setIdOrderDetail(1);
        doThrow(IllegalArgumentException.class).when(orderDetailDAO).updateOrderDetail(any());
        assertThrows(DatabaseConnectionException.class, () -> orderDetailService.updateOrderDetail(orderDetail));
    }

    @Test
    public void testDeleteOrderDetail() {
        OrderDetailDTO orderDetailDTO = new OrderDetailDTO();
        orderDetailDTO.setIdOrderDetail(1);
        orderDetailService.deleteOrderDetail(orderDetailDTO);
        verify(orderDetailDAO).deleteOrderDetail(orderDetailDTO);
        verify(orderDetailDAO).deleteOrderDetailProductById(orderDetailDTO.getIdOrderDetail());
    }
}
