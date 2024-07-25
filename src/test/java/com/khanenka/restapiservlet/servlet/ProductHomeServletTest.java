package com.khanenka.restapiservlet.servlet;

import com.google.gson.Gson;
import com.khanenka.restapiservlet.model.productdto.ProductCategoryDTOByNameAndType;
import com.khanenka.restapiservlet.model.productdto.ProductDTOByNameAndPrice;
import com.khanenka.restapiservlet.model.productdto.ProductDTOByNameAndTypeAndNewProduct;
import com.khanenka.restapiservlet.repository.OrderDetailDao;
import com.khanenka.restapiservlet.repository.ProductDao;
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
import java.util.ArrayList;
import java.util.List;

import static com.khanenka.restapiservlet.servlet.ProductHomeServlet.CHARSET_UTF8;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

public class ProductHomeServletTest {

    private ProductHomeServlet servlet; // replace with actual servlet class name
    private ProductDao productDao; // replace with actual DAO class
    private OrderDetailDao orderDetailDao;
    private Gson gson;


    @Before
    public void setUp() {
        productDao = mock(ProductDao.class);
        orderDetailDao = mock(OrderDetailDao.class);

        gson = new Gson();
        servlet = new ProductHomeServlet(); // replace with actual servlet class name
        servlet.productDao = productDao; // Inject mock DAO
        servlet.orderDetailDao = orderDetailDao;
    }

    @Test
    public void testConstructor() {
        assertNotNull(servlet.productDao);
        assertNotNull(servlet.productCategoryDao);
        assertNotNull(servlet.orderDetailDao);
    }

    @Test
    public void testInit() {

        servlet.init();

        // Проверяем, что методы создания таблиц были вызваны
        verify(productDao).createTableProduct();
        verify(productDao).createProductProductCategory();
        verify(productDao).createProductCategoryTable();
        verify(orderDetailDao).createOrderDetailTable();
        verify(orderDetailDao).createOrderDetailProductTable();
    }

    @Test
    public void testDoPost_ValidProduct() throws Exception {

        String jsonInput = "{\"nameProduct\":\"Test Product\",\"priceProduct\":\"100.00\",\"productCategoryDTOS\":[{\n" +
                "    \"nameProductCategory\":\"Безалкогольные\",\n" +
                "    \"typeProductCategory\":\"DRINK\"\n" +
                "}\n" +
                "]}";
        //mock
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        ServletInputStream inputStream = new MockServletInputStream(jsonInput);
        when(request.getInputStream()).thenReturn(inputStream);
        when(request.getCharacterEncoding()).thenReturn(StandardCharsets.UTF_8.name());

        //вызовем doPost
        servlet.doPost(request, response);

        ArgumentCaptor<ProductDTOByNameAndPrice> argumentCaptor = ArgumentCaptor.forClass(ProductDTOByNameAndPrice.class);
        verify(productDao).addProduct(argumentCaptor.capture());
        ProductDTOByNameAndPrice actualProduct = argumentCaptor.getValue();
        verify(productDao).addProduct(actualProduct);


    }


    @Test
    public void testDoPut_Success() throws IOException, ServletException {
        // Подготовка
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

        // Заготовим объект для response
        PrintWriter printWriter = mock(PrintWriter.class);
        when(response.getWriter()).thenReturn(printWriter);

        // Действие
        servlet.doPut(request, response);

        // Проверка
        ArgumentCaptor<ProductDTOByNameAndPrice> productCaptor = ArgumentCaptor.forClass(ProductDTOByNameAndPrice.class);
        verify(productDao, times(1)).updateProduct(productCaptor.capture(), eq("Test Update"));

        ProductDTOByNameAndPrice updatedProduct = productCaptor.getValue();
        assertEquals(inputProduct.getNameProduct(), updatedProduct.getNameProduct());
        assertEquals(inputProduct.getPriceProduct(), updatedProduct.getPriceProduct());
    }

    @Test
    public void testDoDelete_Success() throws IOException {
        // Подготовка
        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse resp = mock(HttpServletResponse.class);
        String jsonInput = "{\"nameProduct\":\"Test Product\",\"priceProduct\":\"100.00\",\"productCategoryDTOS\":[{\n" +
                "    \"nameProductCategory\":\"Безалкогольные\",\n" +
                "    \"typeProductCategory\":\"DRINK\"\n" +
                "}\n" +
                "]}";
        MockServletInputStream mockServletInputStream = new MockServletInputStream(jsonInput);
        when(req.getInputStream()).thenReturn(mockServletInputStream);

        // Вызов
        servlet.doDelete(req, resp);

        // Проверка
        ArgumentCaptor<ProductDTOByNameAndPrice> productCaptor = ArgumentCaptor.forClass(ProductDTOByNameAndPrice.class);
        verify(productDao, times(1)).deleteProduct(productCaptor.capture());
        ProductDTOByNameAndPrice deletedProduct = productCaptor.getValue();

        // Проверьте, что объект соответствует ожиданиям
        assertEquals("Test Product", deletedProduct.getNameProduct());
        assertEquals(new BigDecimal("100.00"), deletedProduct.getPriceProduct());
        verify(resp, never()).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        verify(resp, never()).setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }

    @Test
    public void testDoGet_SuccessScenario() throws IOException {
        // Подготовка
        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse resp = mock(HttpServletResponse.class);
        PrintWriter printWriter = mock(PrintWriter.class);
        List<ProductDTOByNameAndPrice> productList = new ArrayList<>();
        productList.add(new ProductDTOByNameAndPrice("Test Product", new BigDecimal("10.00"), null)); // Добавьте нужные поля

        // Настройка mock-объектов
        when(productDao.getAllProducts()).thenReturn(productList);
        when(req.getCharacterEncoding()).thenReturn(CHARSET_UTF8);
        when(resp.getWriter()).thenReturn(printWriter); // Предоставляем writer для HttpServletResponse
        when(resp.getCharacterEncoding()).thenReturn(CHARSET_UTF8);

        // Вызов
        servlet.doGet(req, resp);
        // Проверка
        verify(printWriter).print(contains("Test Product")); // Проверяем, что запись в writer включает название продукта
        verify(resp).setContentType("application/json; charset=UTF-8"); // Проверяем, что заголовок установлен
    }

    // Custom MockServletInputStream class to simulate ServletInputStream
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
