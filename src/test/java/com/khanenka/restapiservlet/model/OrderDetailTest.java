package com.khanenka.restapiservlet.model;

import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

public class OrderDetailTest {
    private OrderDetail orderDetail = orderDetail = new OrderDetail(1L, OrderStatus.DELIVERED, BigDecimal.valueOf(100), null);
    private OrderStatus orderStatus;
    private List<Product> products;

    @BeforeEach
    public void setUp() {
        orderStatus = mock(OrderStatus.class);
        products = new ArrayList<>();
        orderDetail = new OrderDetail(1L, OrderStatus.DELIVERED, BigDecimal.valueOf(100), products);
    }

    @Test
    public void testGetIdOrderDetail() {
        assertEquals(1L, orderDetail.getIdOrderDetail());
    }

    @Test
    public void testGetOrderStatus() {
        assertEquals(OrderStatus.DELIVERED, orderDetail.getOrderStatus());
    }

    @Test
    public void testGetTotalAmauntOrderDetail() {
        assertEquals(BigDecimal.valueOf(100), orderDetail.getTotalAmauntOrderDetail());
    }

    @Test
    public void testGetProducts() {
        assertEquals(products, orderDetail.getProducts());
    }

    @Test
    public void testSetIdOrderDetail() {
        orderDetail.setIdOrderDetail(2L);
        assertEquals(2L, orderDetail.getIdOrderDetail());
    }

    @Test
    public void testSetOrderStatus() {
        OrderStatus newOrderStatus = mock(OrderStatus.class);
        orderDetail.setOrderStatus(newOrderStatus);
        assertEquals(newOrderStatus, orderDetail.getOrderStatus());
    }

    @Test
    public void testSetTotalAmauntOrderDetail() {
        orderDetail.setTotalAmauntOrderDetail(BigDecimal.valueOf(150));
        assertEquals(BigDecimal.valueOf(150), orderDetail.getTotalAmauntOrderDetail());
    }

    @Test
    public void testSetProducts() {
        List<Product> newProducts = new ArrayList<>();
        orderDetail.setProducts(newProducts);
        assertEquals(newProducts, orderDetail.getProducts());
    }

    @Test
    public void testToString() {
        String expectedString = "OrderDetail(idOrderDetail=1, orderStatus=" + OrderStatus.DELIVERED +
                ", totalAmauntOrderDetail=100, products=" + products + ")";
        assertEquals(expectedString, orderDetail.toString());
    }

    @Test
    public void testEqualsAndHashCode() {
        OrderDetail orderDetail2 = new OrderDetail(1L, OrderStatus.DELIVERED, BigDecimal.valueOf(100), products);
        OrderDetail orderDetail3 = new OrderDetail(2L, OrderStatus.DELIVERED, BigDecimal.valueOf(200), products);

        assertEquals(orderDetail, orderDetail2);
        assertNotEquals(orderDetail, orderDetail3);
        assertEquals(orderDetail.hashCode(), orderDetail2.hashCode());
        assertNotEquals(orderDetail.hashCode(), orderDetail3.hashCode());
    }

    @Test
    public void testNoArgsConstructor() {
        OrderDetail newOrderDetail = new OrderDetail();
        assertNull(newOrderDetail.getOrderStatus());
        assertNull(newOrderDetail.getProducts());
        assertEquals(0L, newOrderDetail.getIdOrderDetail());
        assertNull(newOrderDetail.getTotalAmauntOrderDetail());
    }
}
