package com.khanenka.restapiservlet.servlet;

import com.google.gson.Gson;
import com.khanenka.restapiservlet.exceptions.DatabaseConnectionException;
import com.khanenka.restapiservlet.model.CategoryType;
import com.khanenka.restapiservlet.model.productdto.ProductCategoryDTOByNameAndType;
import com.khanenka.restapiservlet.repository.implementation.ProductCategoryDAOImpl;
import com.khanenka.restapiservlet.repository.implementation.ProductDAOImpl;
import com.khanenka.restapiservlet.service.ProductCategoryService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class ProductCategoryServletTest {
    private ProductsCategoryServlet servlet;
    private ProductCategoryDAOImpl productCategoryDao;
    private ProductDAOImpl productDao;
    private ProductCategoryService productCategoryService;
    private Gson gson;
    private PrintWriter writer;

    @Before
    public void setUp() {
        productCategoryDao = mock(ProductCategoryDAOImpl.class);
        productDao = mock(ProductDAOImpl.class);
        gson = new Gson();
        writer = mock(PrintWriter.class);
        servlet = new ProductsCategoryServlet(productDao,productCategoryDao);
    }

    @Test
    public void testDoPost_ValidCategory() throws Exception {
        String jsonInput = "{\"nameProductCategory\":\"Test\",\"typeProductCategory\":\"DRINK\"}";
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        ServletInputStream inputStream = new MockServletInputStream(jsonInput);
        when(request.getInputStream()).thenReturn(inputStream);
        when(request.getCharacterEncoding()).thenReturn(StandardCharsets.UTF_8.name());
        servlet.doPost(request, response);
        ArgumentCaptor<ProductCategoryDTOByNameAndType> argumentCaptor =
                ArgumentCaptor.forClass(ProductCategoryDTOByNameAndType.class);
        verify(productCategoryDao).addProductCategory(argumentCaptor.capture());
        ProductCategoryDTOByNameAndType actualCategory = argumentCaptor.getValue();
        assertEquals("Test", actualCategory.getNameProductCategory());
    }

    @Test
    public void testDoGet_Success() throws IOException {
        // Mock HTTP request and response
        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse resp = mock(HttpServletResponse.class);
        List<ProductCategoryDTOByNameAndType> categoryList = new ArrayList<>();
        ProductCategoryDTOByNameAndType productCategoryDTOByNameAndType = new ProductCategoryDTOByNameAndType(
                "Test", CategoryType.DRINK, null, "new category");
        categoryList.add(productCategoryDTOByNameAndType); // Modify accordingly

        // Setup mock behavior
        when(productCategoryDao.getAllProductCategories()).thenReturn(categoryList);
        when(resp.getWriter()).thenReturn(writer);
        // Call doGet
        servlet.doGet(req, resp);
        verify(writer).print(contains("Test"));
        verify(resp).setContentType("application/json");
    }

    @Test
    public void testDoPut_Success() throws IOException, SQLException {
        // Prepare
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        String jsonInput =
                "{\"nameProductCategory\":\"Test\",\"typeProductCategory\":\"DRINK\",\"newCategory\":\"Soft Drinks\"}";
        MockServletInputStream mockServletInputStream = new MockServletInputStream(jsonInput);
        when(request.getInputStream()).thenReturn(mockServletInputStream);
        servlet.doPut(request, response);
        ArgumentCaptor<ProductCategoryDTOByNameAndType> productCaptor = ArgumentCaptor
                .forClass(ProductCategoryDTOByNameAndType.class);
        verify(productCategoryDao).updateProductCategory(productCaptor.capture(), eq("Soft Drinks"));
        ProductCategoryDTOByNameAndType updatedCategory = productCaptor.getValue();
        assertEquals("Test", updatedCategory.getNameProductCategory());
        assertEquals(CategoryType.DRINK, updatedCategory.getTypeProductCategory());
    }

    @Test
    public void testDoPut_InvalidJsonSyntax() throws IOException {
        String invalidJson = "{invalidJson}";
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        // Настроить мок для входящего потока
        MockServletInputStream mockServletInputStream = new MockServletInputStream(invalidJson);
        when(request.getInputStream()).thenReturn(mockServletInputStream);
        servlet.doPut(request, response);
        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }

    @Test
    public void testDoDelete_Success() throws Exception {
        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse resp = mock(HttpServletResponse.class);
        String jsonInput = "{\"nameProductCategory\":\"Test\",\"typeProductCategory\":\"DRINK\"}";
        MockServletInputStream mockServletInputStream = new MockServletInputStream(jsonInput);
        when(req.getInputStream()).thenReturn(mockServletInputStream);
        servlet.doDelete(req, resp);
        ArgumentCaptor<ProductCategoryDTOByNameAndType> categoryCaptor = ArgumentCaptor.forClass(ProductCategoryDTOByNameAndType.class);
        verify(productCategoryDao).deleteProductCategory(categoryCaptor.capture());
        ProductCategoryDTOByNameAndType deletedCategory = categoryCaptor.getValue();
        assertEquals("Test", deletedCategory.getNameProductCategory());
        assertEquals(CategoryType.DRINK, deletedCategory.getTypeProductCategory());
    }

    @Test
    public void testDoDeleteDatabaseConnectionException() throws Exception {
        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse resp = mock(HttpServletResponse.class);
        String json = "{\"name\":\"category\",\"type\":\"type\"}";
        MockServletInputStream mockServletInputStream = new MockServletInputStream(json);
        when(req.getInputStream()).thenReturn(mockServletInputStream);
        doThrow(new DatabaseConnectionException("Database connection error"))
                .when(productCategoryDao).deleteProductCategory(any());
        servlet.doDelete(req, resp);
        verify(resp, never()).setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }

    @Test
    public void testDoDeleteJsonSyntaxException() throws Exception {
        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse resp = mock(HttpServletResponse.class);
        String invalidJson = "{invalid json";
        MockServletInputStream mockServletInputStream = new MockServletInputStream(invalidJson);
        when(req.getInputStream()).thenReturn(mockServletInputStream);
        servlet.doDelete(req, resp);
        verify(resp).setStatus(HttpServletResponse.SC_BAD_REQUEST);
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