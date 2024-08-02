package com.khanenka.restapiservlet.servlet;

import com.google.gson.Gson;
import com.khanenka.restapiservlet.model.productdto.ProductCategoryDTOByNameAndType;
import com.khanenka.restapiservlet.model.productdto.ProductDTOByNameAndPrice;
import com.khanenka.restapiservlet.model.productdto.ProductDTOByNameAndTypeAndNewProduct;
import com.khanenka.restapiservlet.repository.implementation.OrderDetailDAOImpl;
import com.khanenka.restapiservlet.repository.implementation.ProductCategoryDAOImpl;
import com.khanenka.restapiservlet.repository.implementation.ProductDAOImpl;
import com.khanenka.restapiservlet.service.ProductService;
import com.khanenka.restapiservlet.util.DBConnection;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

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
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import static com.khanenka.restapiservlet.servlet.ProductServlet.CHARSET_UTF8;
import static com.khanenka.restapiservlet.servlet.ProductServlet.orderDAO;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

public class ProductServletTest {
    Connection connection = DBConnection.getConnection();
    private ProductDAOImpl productDao;
    private ProductCategoryDAOImpl productCategoryDao;
    private OrderDetailDAOImpl orderDetailDao;
    private Gson gson;
    private ProductServlet servlet;
    ProductService productService = new ProductService(productDao, productCategoryDao, orderDetailDao);

    @Before
    public void setUp() {
        productDao = mock(ProductDAOImpl.class);
        productCategoryDao = mock(ProductCategoryDAOImpl.class);
        orderDetailDao = mock(OrderDetailDAOImpl.class);
        gson = new Gson();
        servlet = new ProductServlet(productDao, productCategoryDao, orderDetailDao);
    }

    @Test
    public void testProductServletConstructor() {
        assertNotNull(servlet);
    }

    @Test
    public void testConstructor() {
        assertNotNull(servlet.productDao);
        assertNotNull(servlet.productCategoryDao);
        assertNotNull(orderDAO);
        assertNotNull(servlet.productService);
        assertNotNull(ProductServlet.productDao);
        assertNotNull(ProductServlet.productCategoryDao);
        assertNotNull(ProductServlet.orderDAO);
    }

    @Test
    public void testDoPost_ValidProduct() throws Exception {
        String jsonInput = "{\"nameProduct\":\"Test Product\",\"priceProduct\":\"100.00\"," +
                "\"productCategoryDTOS\":[{\n" +
                "    \"nameProductCategory\":\"Безалкогольные\",\n" +
                "    \"typeProductCategory\":\"DRINK\"\n" +
                "}\n" +
                "]}";
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        ServletInputStream inputStream = new MockServletInputStream(jsonInput);
        when(request.getInputStream()).thenReturn(inputStream);
        when(request.getCharacterEncoding()).thenReturn(StandardCharsets.UTF_8.name());
        servlet.doPost(request, response);
        ArgumentCaptor<ProductDTOByNameAndPrice> argumentCaptor =
                ArgumentCaptor.forClass(ProductDTOByNameAndPrice.class);
        verify(productDao).addProduct(argumentCaptor.capture());
        ProductDTOByNameAndPrice actualProduct = argumentCaptor.getValue();
        verify(productDao).addProduct(actualProduct);
    }

    @Test
    public void testDoPost_InvalidJSON() throws Exception {
        String invalidJsonInput = "{\"nameProduct\":\"Test Product\",\"priceProduct\":\"100.00\"," +
                "\"productCategoryDTOS\":[";
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        ServletInputStream inputStream = new MockServletInputStream(invalidJsonInput);
        when(request.getInputStream()).thenReturn(inputStream);
        when(request.getCharacterEncoding()).thenReturn(StandardCharsets.UTF_8.name());
        verify(productDao, never()).addProduct(any());
    }


    @Test
    public void testDoPut_Success() throws IOException, ServletException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        ProductDTOByNameAndTypeAndNewProduct inputProduct = new ProductDTOByNameAndTypeAndNewProduct();
        inputProduct.setNameProduct("Test Product");
        inputProduct.setPriceProduct(new BigDecimal("100.0"));
        inputProduct.setNewProduct("Test Update");
        ProductCategoryDTOByNameAndType category = new ProductCategoryDTOByNameAndType();
        inputProduct.setProductCategoryDTOS(List.of(category));
        String json = gson.toJson(inputProduct);
        MockServletInputStream mockServletInputStream = new MockServletInputStream(json);
        when(request.getInputStream()).thenReturn(mockServletInputStream);
        PrintWriter printWriter = mock(PrintWriter.class);
        when(response.getWriter()).thenReturn(printWriter);
        servlet.doPut(request, response);
        ArgumentCaptor<ProductDTOByNameAndPrice> productCaptor =
                ArgumentCaptor.forClass(ProductDTOByNameAndPrice.class);
        verify(productDao,
                times(1)).updateProduct(productCaptor.capture(), eq("Test Update"));
        ProductDTOByNameAndPrice updatedProduct = productCaptor.getValue();
        assertEquals(inputProduct.getNameProduct(), updatedProduct.getNameProduct());
        assertEquals(inputProduct.getPriceProduct(), updatedProduct.getPriceProduct());
    }

    @Test
    public void testDoDelete_Success() throws IOException {
        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse resp = mock(HttpServletResponse.class);
        String jsonInput = "{\"nameProduct\":\"Test Product\",\"priceProduct\":\"100.00\"," +
                "\"productCategoryDTOS\":[{\n" +
                "    \"nameProductCategory\":\"Безалкогольные\",\n" +
                "    \"typeProductCategory\":\"DRINK\"\n" +
                "}\n" +
                "]}";
        MockServletInputStream mockServletInputStream = new MockServletInputStream(jsonInput);
        when(req.getInputStream()).thenReturn(mockServletInputStream);
        servlet.doDelete(req, resp);
        ArgumentCaptor<ProductDTOByNameAndPrice> productCaptor = ArgumentCaptor.forClass(ProductDTOByNameAndPrice.class);
        verify(productDao, times(1)).deleteProduct(productCaptor.capture());
        ProductDTOByNameAndPrice deletedProduct = productCaptor.getValue();
        assertEquals("Test Product", deletedProduct.getNameProduct());
        assertEquals(new BigDecimal("100.00"), deletedProduct.getPriceProduct());
        verify(resp, never()).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        verify(resp, never()).setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }

    @Test
    public void testDoGet_SuccessScenario() throws IOException {
        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse resp = mock(HttpServletResponse.class);
        PrintWriter printWriter = mock(PrintWriter.class);
        List<ProductDTOByNameAndPrice> productList = new ArrayList<>();
        productList.add(new ProductDTOByNameAndPrice(
                "Test Product", new BigDecimal("10.00"),
                null, "New Product"));
        when(productDao.getAllProducts()).thenReturn(productList);
        when(req.getCharacterEncoding()).thenReturn(CHARSET_UTF8);
        when(resp.getWriter()).thenReturn(printWriter);
        when(resp.getCharacterEncoding()).thenReturn(CHARSET_UTF8);
        servlet.doGet(req, resp);
        verify(printWriter).print(contains("Test Product"));
        verify(resp).setContentType("application/json; charset=UTF-8");
    }

    @Test
    public void testDoPost_ExceptionScenario() throws IOException {
        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse resp = mock(HttpServletResponse.class);
        PrintWriter printWriter = mock(PrintWriter.class);
        when(req.getCharacterEncoding()).thenReturn(CHARSET_UTF8);
        when(resp.getWriter()).thenThrow(new IOException("Test IOException"));
        servlet.doPost(req, resp);
        verify(resp).setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }

    @Test
    public void testDoGet_ExceptionScenario() throws IOException {
        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse resp = mock(HttpServletResponse.class);
        PrintWriter printWriter = mock(PrintWriter.class);
        when(req.getCharacterEncoding()).thenReturn(CHARSET_UTF8);
        when(resp.getWriter()).thenThrow(new IOException("Test IOException"));
        servlet.doGet(req, resp);
        verify(resp).setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        verify(resp).setContentType("application/json; charset=UTF-8");
    }

    @Test
    public void testDoDelete_ServerError() throws Exception {
        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse resp = mock(HttpServletResponse.class);
        servlet.doDelete(req, resp);
        verify(resp).setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }

    @Test
    public void shouldReturnBadRequestWhenJsonIsInvalid() throws IOException {
        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse resp = mock(HttpServletResponse.class);
        String jsonInput = "{\"nameProduct\":\"Test Product\",\"priceProduct\":\"100.00\"," +
                "\"productCategoryDTOS\":{\n" +
                "    \"nameProductCategory\":\"Безалкогольные\",\n" +
                "    \"typeProductCategory\":\"DRINK\"\n" +
                "}\n" +
                "}";
        MockServletInputStream mockServletInputStream = new MockServletInputStream(jsonInput);
        when(req.getInputStream()).thenReturn(mockServletInputStream);
        servlet.doDelete(req, resp);
        verify(resp).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        verify(productDao, never()).deleteProduct(any());
    }

    static class MockServletInputStream extends ServletInputStream {
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
