package com.khanenka.restapiservlet.servlet;

import com.google.gson.Gson;
import com.khanenka.restapiservlet.model.OrderStatus;
import com.khanenka.restapiservlet.model.productdto.OrderDetailDTO;
import com.khanenka.restapiservlet.model.productdto.ProductDTO;
import com.khanenka.restapiservlet.repository.implementation.OrderDetailDAOImpl;
import com.khanenka.restapiservlet.repository.implementation.ProductDAOImpl;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

public class OrderDetailServletTest {
    private OrderDetailsServlet orderDetailServlet;
    private OrderDetailDAOImpl orderDetailDaoMock;
    private ProductDAOImpl productMock;
    private HttpServletRequest requestMock;
    private HttpServletResponse responseMock;
    private PrintWriter writer;

    @Before
    public void setUp() {
        orderDetailDaoMock = Mockito.mock(OrderDetailDAOImpl.class);
        productMock = Mockito.mock(ProductDAOImpl.class);
        writer = mock(PrintWriter.class);
        orderDetailServlet = new OrderDetailsServlet(orderDetailDaoMock, productMock);
        requestMock = Mockito.mock(HttpServletRequest.class);
        responseMock = Mockito.mock(HttpServletResponse.class);
    }

    @Test
    public void testOrderDetailDaoInitialization() {
        assertNotNull("OrderDetailDao should be initialized", orderDetailServlet.orderDetailService);
    }

    @Test
    public void testDoGet_Success() throws Exception {
        OrderDetailDTO orderDetailDTO = new OrderDetailDTO();
        orderDetailDTO.setIdOrderDetail(1);
        orderDetailDTO.setOrderStatus(OrderStatus.PENDING);
        orderDetailDTO.setTotalAmauntOrderDetail(new BigDecimal("30.00"));
        orderDetailDTO.setProducts(Arrays.asList(
                new ProductDTO(2, "Коктейль Зажигалка", new BigDecimal("10.00"))));
        List<OrderDetailDTO> orderDetailDTOList = Arrays.asList(orderDetailDTO);
        when(orderDetailDaoMock.getAllOrderDetails()).thenReturn(orderDetailDTOList);
        PrintWriter writerMock = mock(PrintWriter.class);
        when(responseMock.getWriter()).thenReturn(writerMock);
        orderDetailServlet.doGet(requestMock, responseMock);
        verify(responseMock).setContentType("application/json; charset=UTF-8");
        verify(writerMock).print(any(String.class));
        ArgumentCaptor<String> jsonCaptor = ArgumentCaptor.forClass(String.class);
        verify(writerMock).print(jsonCaptor.capture());
        String jsonResponse = jsonCaptor.getValue();
        ObjectMapper objectMapper = new ObjectMapper();
        List<OrderDetailDTO> actualOrderDetails = objectMapper.readValue(
                jsonResponse, objectMapper.getTypeFactory().constructCollectionType(List.class, OrderDetailDTO.class));
        assertNotNull(actualOrderDetails);
        assertEquals(1, actualOrderDetails.size());
        OrderDetailDTO actualOrderDetail = actualOrderDetails.get(0);
        assertEquals(1, actualOrderDetail.getIdOrderDetail());
        assertEquals(OrderStatus.PENDING, actualOrderDetail.getOrderStatus());
        assertEquals(new BigDecimal("30.00"), actualOrderDetail.getTotalAmauntOrderDetail());
        List<ProductDTO> actualProducts = actualOrderDetail.getProducts();
        assertNotNull(actualProducts);
        assertEquals(1, actualProducts.size());
        ProductDTO actualProduct = actualProducts.get(0);
        assertEquals(2, actualProduct.getIdProduct());
        assertEquals("Коктейль Зажигалка", actualProduct.getNameProduct());
        assertEquals(new BigDecimal("10.00"), actualProduct.getPriceProduct());
    }

    @Test
    public void testDoPost_Success() throws Exception {
        // Mocking a valid OrderDetailDTO
        OrderDetailDTO orderDetailDTO = new OrderDetailDTO();
        orderDetailDTO.setIdOrderDetail(1);
        orderDetailDTO.setOrderStatus(OrderStatus.PENDING);
        orderDetailDTO.setTotalAmauntOrderDetail(new BigDecimal("30.00"));
        orderDetailDTO.setProducts(Arrays.asList(
                new ProductDTO(2, "Коктейль Зажигалка", new BigDecimal("10.00"))));
        Gson gson = new Gson();
        String jsonOrderDetail = gson.toJson(orderDetailDTO);
        MockServletInputStream mockServletInputStream = new MockServletInputStream(jsonOrderDetail);
        when(requestMock.getInputStream()).thenReturn(mockServletInputStream);
        doNothing().when(orderDetailDaoMock).addOrderDetail(any(OrderDetailDTO.class));
        orderDetailServlet.doPost(requestMock, responseMock);
        verify(orderDetailDaoMock).addOrderDetail(argThat(argument -> {
            assertNotNull(argument);
            assertEquals(1, argument.getIdOrderDetail());
            assertEquals(OrderStatus.PENDING, argument.getOrderStatus());
            assertEquals(new BigDecimal("30.00"), argument.getTotalAmauntOrderDetail());
            List<ProductDTO> products = argument.getProducts();
            assertNotNull(products);
            assertEquals(1, products.size());
            ProductDTO product = products.get(0);
            assertEquals(2, product.getIdProduct());
            assertEquals("Коктейль Зажигалка", product.getNameProduct());
            assertEquals(new BigDecimal("10.00"), product.getPriceProduct());
            return true;
        }));
        verify(responseMock).setCharacterEncoding("UTF-8");
    }

    @Test
    public void testDoPost_JsonSyntaxException() throws IOException, ServletException {
        String invalidJson = "{invalid json}";
        InputStream inputStream = new java.io.ByteArrayInputStream(invalidJson.getBytes(StandardCharsets.UTF_8));
        MockServletInputStream mockServletInputStream = new MockServletInputStream(inputStream.toString());
        when(requestMock.getInputStream()).thenReturn(mockServletInputStream);
        when(requestMock.getCharacterEncoding()).thenReturn(StandardCharsets.UTF_8.name());
        when(responseMock.getCharacterEncoding()).thenReturn(StandardCharsets.UTF_8.name());
        orderDetailServlet.doPost(requestMock, responseMock);
        verify(responseMock).setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }

    @Test
    public void testDoPut_Success() throws Exception {
        OrderDetailDTO orderDetailDTO = new OrderDetailDTO();
        orderDetailDTO.setIdOrderDetail(1);
        orderDetailDTO.setOrderStatus(OrderStatus.SHIPPED);
        orderDetailDTO.setTotalAmauntOrderDetail(new BigDecimal("50.00"));
        orderDetailDTO.setProducts(Arrays.asList(
                new ProductDTO(2, "Коктейль Зажигалка", new BigDecimal("10.00"))));
        Gson gson = new Gson();
        String jsonOrderDetail = gson.toJson(orderDetailDTO);
        MockServletInputStream mockServletInputStream = new MockServletInputStream(jsonOrderDetail);
        when(requestMock.getInputStream()).thenReturn(mockServletInputStream);
        doNothing().when(orderDetailDaoMock).updateOrderDetail(any(OrderDetailDTO.class));
        orderDetailServlet.doPut(requestMock, responseMock);
        verify(orderDetailDaoMock).updateOrderDetail(argThat(argument -> {
            assertNotNull(argument);
            assertEquals(1, argument.getIdOrderDetail());
            assertEquals(OrderStatus.SHIPPED, argument.getOrderStatus());
            assertEquals(new BigDecimal("50.00"), argument.getTotalAmauntOrderDetail());
            List<ProductDTO> products = argument.getProducts();
            assertNotNull(products);
            assertEquals(1, products.size());
            ProductDTO product = products.get(0);
            assertEquals(2, product.getIdProduct());
            assertEquals("Коктейль Зажигалка", product.getNameProduct());
            assertEquals(new BigDecimal("10.00"), product.getPriceProduct());
            return true;
        }));
        verify(responseMock).setCharacterEncoding("UTF-8");
    }

    @Test
    public void testDoPut_Exception() throws IOException, ServletException, SQLException {
        String json = "{\"id\":1,\"name\":\"Test Order\"}";
        MockServletInputStream mockServletInputStream = new MockServletInputStream(json);
        when(requestMock.getInputStream()).thenReturn(mockServletInputStream);
        doThrow(new RuntimeException("Test Exception")).when(orderDetailDaoMock).updateOrderDetail(any());
        orderDetailServlet.doPut(requestMock, responseMock);
        verify(responseMock, never()).setStatus(anyInt());
    }

    @Test
    public void testDoDelete_Success() throws Exception {
        OrderDetailDTO orderDetailDTO = new OrderDetailDTO();
        orderDetailDTO.setIdOrderDetail(1);
        orderDetailDTO.setOrderStatus(OrderStatus.PENDING);
        orderDetailDTO.setTotalAmauntOrderDetail(new BigDecimal("30.00"));
        orderDetailDTO.setProducts(Arrays.asList(
                new ProductDTO(2, "Коктейль Зажигалка", new BigDecimal("10.00"))));
        Gson gson = new Gson();
        String jsonOrderDetail = gson.toJson(orderDetailDTO);
        MockServletInputStream mockServletInputStream = new MockServletInputStream(jsonOrderDetail);
        when(requestMock.getInputStream()).thenReturn(mockServletInputStream);
        doNothing().when(orderDetailDaoMock).addOrderDetail(any(OrderDetailDTO.class));
        orderDetailServlet.doPost(requestMock, responseMock);
        String json = "{\"idOrderDetail\":1}"; // Пример JSON-данных
        MockServletInputStream mockServletInputStream1 = new MockServletInputStream(json);
        when(requestMock.getInputStream()).thenReturn(mockServletInputStream1);
        orderDetailServlet.doDelete(requestMock, responseMock);
        ArgumentCaptor<OrderDetailDTO> orderDetailCaptor = ArgumentCaptor.forClass(OrderDetailDTO.class);
        verify(orderDetailDaoMock).deleteOrderDetail(orderDetailCaptor.capture());
        assertEquals(1, orderDetailCaptor.getValue().getIdOrderDetail());
        verify(responseMock, never()).setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }

    private static class MockServletInputStream extends ServletInputStream {
        private final InputStream inputStream;

        public MockServletInputStream(String input) {
            this.inputStream = new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8));
        }

        @Override
        public int read() throws IOException {
            return inputStream.read();
        }
    }
}
