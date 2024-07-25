package com.khanenka.restapiservlet.servlet;

import com.google.gson.Gson;
import com.khanenka.restapiservlet.exception.DatabaseConnectionException;
import com.khanenka.restapiservlet.model.CategoryType;
import com.khanenka.restapiservlet.model.productdto.ProductCategoryDTOByNameAndType;
import com.khanenka.restapiservlet.repository.ProductCategoryDao;
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
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class ProductCategoryServletTest {
    private ProductCategoryServlet servlet; // replace with actual servlet class name
    private ProductCategoryDao productCategoryDao; // replace with actual DAO class
    private Gson gson;
    private PrintWriter writer;

    @Before
    public void setUp() {
        productCategoryDao = mock(ProductCategoryDao.class);
        gson = new Gson();
        writer = mock(PrintWriter.class);
        servlet = new ProductCategoryServlet(); // replace with actual servlet class name
        servlet.productCategoryDao = productCategoryDao; // Inject mock DAO
    }

    @Test
    public void testDoPost_ValidCategory() throws Exception {
        String jsonInput = "{\"nameProductCategory\":\"Test\",\"typeProductCategory\":\"DRINK\"}";

        // Mock HTTP request and response
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        ServletInputStream inputStream = new MockServletInputStream(jsonInput);
        when(request.getInputStream()).thenReturn(inputStream);
        when(request.getCharacterEncoding()).thenReturn(StandardCharsets.UTF_8.name());

        // Call doPost
        servlet.doPost(request, response);

        // Verify that the DAO's addProductCategory method was called with the correct parameter
        ArgumentCaptor<ProductCategoryDTOByNameAndType> argumentCaptor = ArgumentCaptor.forClass(ProductCategoryDTOByNameAndType.class);
        verify(productCategoryDao).addProductCategory(argumentCaptor.capture());
        ProductCategoryDTOByNameAndType actualCategory = argumentCaptor.getValue();
        assertEquals("Test", actualCategory.getNameProductCategory());
    }
    @Test
    public void testDoPost_JsonSyntaxException() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        // Устанавливаем входные данные, которые будут вызывать JsonSyntaxException
        String invalidJson = "invalid json";
        MockServletInputStream mockServletInputStream=new MockServletInputStream(invalidJson);
        when(request.getInputStream()).thenReturn(mockServletInputStream);

        // Вызываем метод doPost
        servlet.doPost(request, response);

        // Проверяем, что установлен статус 400
        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        verify(productCategoryDao, never()).addProductCategory(any());
    }

    @Test
    public void testDoGet_Success() throws IOException, SQLException, ServletException {
        // Mock HTTP request and response
        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse resp = mock(HttpServletResponse.class);
        List<ProductCategoryDTOByNameAndType> categoryList = new ArrayList<>();
        ProductCategoryDTOByNameAndType productCategoryDTOByNameAndType = new ProductCategoryDTOByNameAndType("Test", CategoryType.DRINK, null);
        categoryList.add(productCategoryDTOByNameAndType); // Modify accordingly

        // Setup mock behavior
        when(productCategoryDao.getAllProductCategories()).thenReturn(categoryList);
        when(resp.getWriter()).thenReturn(writer);

        // Call doGet
        servlet.doGet(req, resp);

        // Verify that the response writer was called
        verify(writer).print(contains("Test"));
        verify(resp).setContentType("application/json");
    }

    @Test
    public void testDoPut_Success() throws IOException, ServletException, SQLException {
        // Prepare
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        String jsonInput = "{\"nameProductCategory\":\"Test\",\"typeProductCategory\":\"DRINK\",\"newCategory\":\"Soft Drinks\"}";

        MockServletInputStream mockServletInputStream = new MockServletInputStream(jsonInput);
        when(request.getInputStream()).thenReturn(mockServletInputStream);

        // Call doPut
        servlet.doPut(request, response);

        // Verify the updateProductCategory method was called
        ArgumentCaptor<ProductCategoryDTOByNameAndType> productCaptor = ArgumentCaptor.forClass(ProductCategoryDTOByNameAndType.class);
        verify(productCategoryDao).updateProductCategory(productCaptor.capture(), eq("Soft Drinks"));

        ProductCategoryDTOByNameAndType updatedCategory = productCaptor.getValue();
        assertEquals("Test", updatedCategory.getNameProductCategory());
        assertEquals(CategoryType.DRINK, updatedCategory.getTypeProductCategory());
    }
    @Test
    public void testDoPut_InvalidJsonSyntax() throws IOException, ServletException {
        String invalidJson = "{invalidJson}"; // Пример некорректного JSON
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        // Настроить мок для входящего потока
        MockServletInputStream mockServletInputStream=new MockServletInputStream(invalidJson);
        when(request.getInputStream()).thenReturn(mockServletInputStream);

        servlet.doPut(request, response);

        // Проверка, что статус ответа установлен в 400 Bad Request
        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }
    @Test
    public void testDoDelete_Success() throws Exception {
        // Prepare
        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse resp = mock(HttpServletResponse.class);
        String jsonInput = "{\"nameProductCategory\":\"Test\",\"typeProductCategory\":\"DRINK\"}";

        MockServletInputStream mockServletInputStream = new MockServletInputStream(jsonInput);
        when(req.getInputStream()).thenReturn(mockServletInputStream);

        // Call doDelete
        servlet.doDelete(req, resp);

        // Verify deleteProductCategory was called correctly
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
        String json = "{\"name\":\"category\",\"type\":\"type\"}"; // Ваш JSON
        MockServletInputStream mockServletInputStream=new MockServletInputStream(json);
        when(req.getInputStream()).thenReturn(mockServletInputStream);
        doThrow(new DatabaseConnectionException("Database connection error")).when(productCategoryDao).deleteProductCategory(any());

        // Act
        servlet.doDelete(req, resp);

        // Assert: No exceptions are thrown, just verify that the method was called
        verify(resp, never()).setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }

    @Test
    public void testDoDeleteJsonSyntaxException() throws Exception {
        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse resp = mock(HttpServletResponse.class);
        String invalidJson = "{invalid json";
        MockServletInputStream mockServletInputStream=new MockServletInputStream(invalidJson);
        when(req.getInputStream()).thenReturn(mockServletInputStream);

        // Act
        servlet.doDelete(req, resp);

        // Assert
        verify(resp).setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }


    // Custom MockServletInputStream class to simulate ServletInputStream
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