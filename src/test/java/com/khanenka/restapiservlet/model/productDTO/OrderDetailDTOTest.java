package com.khanenka.restapiservlet.model.productDTO;

import com.khanenka.restapiservlet.model.OrderStatus;
import com.khanenka.restapiservlet.model.productdto.OrderDetailDTO;
import com.khanenka.restapiservlet.model.productdto.ProductDTO;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class OrderDetailDTOTest {
    @Test
    public void testConstructorAndGetters() {
        // Arrange
        long idOrderDetail = 1L;
        OrderStatus orderStatus = OrderStatus.DELIVERED; // Assume OrderStatus is an enum
        BigDecimal totalAmountOrderDetail = new BigDecimal("199.99");

        ProductDTO productDto1 = new ProductDTO(1L, "Product 1", new BigDecimal("99.99"));
        ProductDTO productDto2 = new ProductDTO(2L, "Product 2", new BigDecimal("100.00"));
        List<ProductDTO> productDTOS = Arrays.asList(productDto1, productDto2);

        // Act
        OrderDetailDTO orderDetailDTO = new OrderDetailDTO(idOrderDetail, orderStatus, totalAmountOrderDetail, productDTOS);

        // Assert
        assertEquals(orderDetailDTO.getIdOrderDetail(), idOrderDetail);
        assertEquals(orderDetailDTO.getOrderStatus(), orderStatus);
        assertEquals(orderDetailDTO.getTotalAmauntOrderDetail(), totalAmountOrderDetail);
        assertEquals(orderDetailDTO.getProducts(), productDTOS);
    }

    @Test
    public void testSetters() {
        // Arrange
        OrderDetailDTO orderDetailDTO = new OrderDetailDTO();

        // Act
        long newId = 2L;
        orderDetailDTO.setIdOrderDetail(newId);
        orderDetailDTO.setOrderStatus(OrderStatus.DELIVERED);
        orderDetailDTO.setTotalAmauntOrderDetail(new BigDecimal("299.99"));

        ProductDTO newProductDto = new ProductDTO(3L, "Product 3", new BigDecimal("299.99"));
        List<ProductDTO> newProductDTOS = Arrays.asList(newProductDto);
        orderDetailDTO.setProducts(newProductDTOS);

        // Assert
        assertEquals(orderDetailDTO.getIdOrderDetail(), newId);
        assertEquals(OrderStatus.DELIVERED,orderDetailDTO.getOrderStatus() );
        assertEquals(orderDetailDTO.getTotalAmauntOrderDetail(), new BigDecimal("299.99"));
        assertEquals(orderDetailDTO.getProducts(), newProductDTOS);
    }

    @Test
    public void testToString() {
        // Arrange
        long idOrderDetail = 1L;
        OrderDetailDTO orderDetailDTO = new OrderDetailDTO(idOrderDetail, OrderStatus.PENDING, new BigDecimal("199.99"), null);

        // Act
        String result = orderDetailDTO.toString();

        String expected = "OrderDetailDTO(idOrderDetail=" + orderDetailDTO.getIdOrderDetail() + ", orderStatus=" + orderDetailDTO.getOrderStatus() + ", totalAmauntOrderDetail=" + orderDetailDTO.getTotalAmauntOrderDetail() + ", products=" + orderDetailDTO.getProducts() + ")";

        // Assert
        assertEquals(result, expected);
    }
}
