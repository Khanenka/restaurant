package com.khanenka.restapiservlet.servlet;

import com.google.gson.Gson;
import com.khanenka.restapiservlet.model.OrderStatus;
import com.khanenka.restapiservlet.model.productdto.OrderDetailDTO;
import com.khanenka.restapiservlet.model.productdto.ProductDTO;
import com.khanenka.restapiservlet.repository.OrderDetailDao;
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
    private OrderDetailServlet orderDetailServlet;
    private OrderDetailDao orderDetailDaoMock;
    private HttpServletRequest requestMock;
    private HttpServletResponse responseMock;
    private PrintWriter writer;

    @Before
    public void setUp() {
        orderDetailDaoMock = Mockito.mock(OrderDetailDao.class);
        writer = mock(PrintWriter.class);
        orderDetailServlet = new OrderDetailServlet() {
            {
                orderDetailDao = orderDetailDaoMock;
            }
        };
        requestMock = Mockito.mock(HttpServletRequest.class);
        responseMock = Mockito.mock(HttpServletResponse.class);
    }

    @Test
    public void testOrderDetailDaoInitialization() {
        // Проверяем, что объект orderDetailDao был инициализирован
        assertNotNull("OrderDetailDao should be initialized", orderDetailServlet.orderDetailDao);
    }

    @Test
    public void testDoGet_Success() throws Exception {
        // Mocking the order details
        OrderDetailDTO orderDetailDTO = new OrderDetailDTO();
        orderDetailDTO.setIdOrderDetail(1);
        orderDetailDTO.setOrderStatus(OrderStatus.PENDING);
        orderDetailDTO.setTotalAmauntOrderDetail(new BigDecimal("30.00"));
        orderDetailDTO.setProducts(Arrays.asList(new ProductDTO(2, "Коктейль Зажигалка", new BigDecimal("10.00"))));

        List<OrderDetailDTO> orderDetailDTOList = Arrays.asList(orderDetailDTO);

        // Setting up the mocks
        when(orderDetailDaoMock.getAllOrderDetails()).thenReturn(orderDetailDTOList);

        PrintWriter writerMock = mock(PrintWriter.class);
        when(responseMock.getWriter()).thenReturn(writerMock);

        // Act
        orderDetailServlet.doGet(requestMock, responseMock);

        // Assert
        verify(responseMock).setContentType("application/json");
        verify(writerMock).print(any(String.class));

        // Capture the actual JSON output
        ArgumentCaptor<String> jsonCaptor = ArgumentCaptor.forClass(String.class);
        verify(writerMock).print(jsonCaptor.capture());
        String jsonResponse = jsonCaptor.getValue();

        // Use Jackson ObjectMapper to convert JSON string back to object
        ObjectMapper objectMapper = new ObjectMapper();
        List<OrderDetailDTO> actualOrderDetails = objectMapper.readValue(jsonResponse, objectMapper.getTypeFactory().constructCollectionType(List.class, OrderDetailDTO.class));

        // Assert the structure and contents of the response
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
    public void testDoGetSQLException() throws Exception {
        // Настройка
        when(orderDetailDaoMock.getAllOrderDetails()).thenThrow(new SQLException("Database error"));

        // Выполнение
        orderDetailServlet.doGet(requestMock, responseMock);

        // Проверка
        verify(writer, never()).print(anyString());
    }


    @Test
    public void testDoPost_Success() throws Exception {
        // Mocking a valid OrderDetailDTO
        OrderDetailDTO orderDetailDTO = new OrderDetailDTO();
        orderDetailDTO.setIdOrderDetail(1);
        orderDetailDTO.setOrderStatus(OrderStatus.PENDING);
        orderDetailDTO.setTotalAmauntOrderDetail(new BigDecimal("30.00"));
        orderDetailDTO.setProducts(Arrays.asList(new ProductDTO(2, "Коктейль Зажигалка", new BigDecimal("10.00"))));

        // Convert OrderDetailDTO to JSON using Gson
        Gson gson = new Gson();
        String jsonOrderDetail = gson.toJson(orderDetailDTO);

        // Setting up the mocks
        MockServletInputStream mockServletInputStream = new MockServletInputStream(jsonOrderDetail);
        when(requestMock.getInputStream()).thenReturn(mockServletInputStream);
        doNothing().when(orderDetailDaoMock).addOrderDetail(any(OrderDetailDTO.class));

        // Act
        orderDetailServlet.doPost(requestMock, responseMock);

        // Assert
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

            return true; // Accept any argument that passes the assertions above
        }));

        // Check that the response character encoding was set correctly
        verify(responseMock).setCharacterEncoding("UTF-8");
    }

    @Test
    public void testDoPost_JsonSyntaxException() throws IOException, ServletException {
        // Настройка мока для запроса, чтобы вызвать синтаксическую ошибку JSON
        String invalidJson = "{invalid json}";
        InputStream inputStream = new java.io.ByteArrayInputStream(invalidJson.getBytes(StandardCharsets.UTF_8));
        MockServletInputStream mockServletInputStream=new MockServletInputStream(inputStream.toString());
        when(requestMock.getInputStream()).thenReturn(mockServletInputStream);
        when(requestMock.getCharacterEncoding()).thenReturn(StandardCharsets.UTF_8.name());
        when(responseMock.getCharacterEncoding()).thenReturn(StandardCharsets.UTF_8.name());

        // Вызов метода doPost
        orderDetailServlet.doPost(requestMock, responseMock);

        // Проверка, что в случае ошибки ответ установлен на 400 (Неверный запрос)
        verify(responseMock).setStatus(HttpServletResponse.SC_BAD_REQUEST);

    }

    @Test
    public void testDoPut_Success() throws Exception {
        // Mocking a valid OrderDetailDTO
        OrderDetailDTO orderDetailDTO = new OrderDetailDTO();
        orderDetailDTO.setIdOrderDetail(1);
        orderDetailDTO.setOrderStatus(OrderStatus.SHIPPED);
        orderDetailDTO.setTotalAmauntOrderDetail(new BigDecimal("50.00"));
        orderDetailDTO.setProducts(Arrays.asList(new ProductDTO(2, "Коктейль Зажигалка", new BigDecimal("10.00"))));

        // Convert OrderDetailDTO to JSON using Gson
        Gson gson = new Gson();
        String jsonOrderDetail = gson.toJson(orderDetailDTO);

        // Setting up the mocks
        MockServletInputStream mockServletInputStream = new MockServletInputStream(jsonOrderDetail);
        when(requestMock.getInputStream()).thenReturn(mockServletInputStream);
        doNothing().when(orderDetailDaoMock).updateOrderDetail(any(OrderDetailDTO.class));

        // Act
        orderDetailServlet.doPut(requestMock, responseMock);

        // Assert
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

            return true; // Accept any argument that passes the assertions above
        }));

        // Check that the response character encoding was set correctly
        verify(responseMock).setCharacterEncoding("UTF-8");
    }


    @Test
    public void testDoPut_Exception() throws IOException, ServletException, SQLException {
        String json = "{\"id\":1,\"name\":\"Test Order\"}";
        MockServletInputStream mockServletInputStream=new MockServletInputStream(json);
        when(requestMock.getInputStream()).thenReturn(mockServletInputStream);
        doThrow(new RuntimeException("Test Exception")).when(orderDetailDaoMock).updateOrderDetail(any());

        // Вызов doPut
        orderDetailServlet.doPut(requestMock, responseMock);

        // Проверка, что никаких статус-кодов не установлено
        verify(responseMock, never()).setStatus(anyInt());
    }

    @Test
    public void testDoDelete_Success() throws Exception {
        // Mocking a valid OrderDetailDTO
        OrderDetailDTO orderDetailDTO = new OrderDetailDTO();
        orderDetailDTO.setIdOrderDetail(1);
        orderDetailDTO.setOrderStatus(OrderStatus.PENDING);
        orderDetailDTO.setTotalAmauntOrderDetail(new BigDecimal("30.00"));
        orderDetailDTO.setProducts(Arrays.asList(new ProductDTO(2, "Коктейль Зажигалка", new BigDecimal("10.00"))));

        // Convert OrderDetailDTO to JSON using Gson
        Gson gson = new Gson();
        String jsonOrderDetail = gson.toJson(orderDetailDTO);

        // Setting up the mocks
        MockServletInputStream mockServletInputStream = new MockServletInputStream(jsonOrderDetail);
        when(requestMock.getInputStream()).thenReturn(mockServletInputStream);
        doNothing().when(orderDetailDaoMock).addOrderDetail(any(OrderDetailDTO.class));

        // Act
        orderDetailServlet.doPost(requestMock, responseMock);
        String json = "{\"idOrderDetail\":1}"; // Пример JSON-данных
        MockServletInputStream mockServletInputStream1=new MockServletInputStream(json);
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
