package com.khanenka.restapiservlet.servlet;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.khanenka.restapiservlet.exception.DatabaseConnectionException;
import com.khanenka.restapiservlet.model.productdto.ProductDTOByNameAndPrice;
import com.khanenka.restapiservlet.model.productdto.ProductDTOByNameAndTypeAndNewProduct;
import com.khanenka.restapiservlet.repository.ProductDao;
import com.khanenka.restapiservlet.repository.impl.ProductDaoImpl;
import com.khanenka.restapiservlet.repository.implementation.OrderDetailDAOImpl;
import com.khanenka.restapiservlet.repository.implementation.ProductCategoryDAOImpl;
import com.khanenka.restapiservlet.repository.implementation.ProductDAOImpl;
import com.khanenka.restapiservlet.service.ProductService;
import com.khanenka.restapiservlet.util.DBConnection;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.util.List;
import java.util.stream.Collectors;

import static com.khanenka.restapiservlet.servlet.ProductHomeServlet.CHARSET_UTF8;

@WebServlet("/products")
public class ProductServlet extends HttpServlet {
    Connection connection = DBConnection.getConnection();
    private ProductService productService;
    private ProductDAOImpl productDao = new ProductDAOImpl(connection);
    private ProductCategoryDAOImpl productCategoryDao = new ProductCategoryDAOImpl(connection);
    private OrderDetailDAOImpl orderDAO = new OrderDetailDAOImpl(connection);

    Gson gson = new Gson();
    @Override
    public void init() throws ServletException {
        super.init();
        this.productService = new ProductService(productDao,productCategoryDao,orderDAO);

    }
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json; charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            List<ProductDTOByNameAndPrice> products = productService.getAllProducts();
            String jsonResponse = convertToJson(products);
            out.print(jsonResponse);
            response.setStatus(HttpServletResponse.SC_OK);
        } catch (DatabaseConnectionException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        try {
            request.setCharacterEncoding(CHARSET_UTF8);
            response.setCharacterEncoding(CHARSET_UTF8);
            // Чтение JSON из тела запроса
            BufferedReader reader = new BufferedReader
                    (new InputStreamReader(request.getInputStream(), StandardCharsets.UTF_8));
            String json = reader.lines().collect(Collectors.joining());
            ProductDTOByNameAndPrice product = gson.fromJson(json, ProductDTOByNameAndPrice.class);
            productService.addProduct(product);
            response.setStatus(HttpServletResponse.SC_CREATED);
        } catch (Exception e) {
            // Логирование ошибки при добавлении продукта
            log("Error add product: " + e.getMessage());
            // Установка статуса ответа на 500 (Internal Server Error)
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setContentType("application/json; charset=UTF-8");

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(request.getInputStream(), StandardCharsets.UTF_8))) {
            // Считывание и десериализация JSON
            String json = reader.lines().collect(Collectors.joining());
            ProductDTOByNameAndTypeAndNewProduct product = gson.fromJson(
                    json, ProductDTOByNameAndTypeAndNewProduct.class);

            // Создание DTO для обновленного продукта
            ProductDTOByNameAndPrice productDTOByNameAndPrice = getProductDTOByNameAndPriceNewCategory(product);

            // Обновление продукта через сервисный метод
            productService.updateProduct(productDTOByNameAndPrice, product.getNewProduct());
            response.setStatus(HttpServletResponse.SC_OK);
        } catch (DatabaseConnectionException e) {
            log("Database error: " + e.getMessage());
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write( e.getMessage());
        } catch (Exception e) {
            log("Error updating product: " + e.getMessage());
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write(e.getMessage());
        }
    }
    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            req.setCharacterEncoding(CHARSET_UTF8);
            resp.setCharacterEncoding(CHARSET_UTF8);
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(req.getInputStream(), StandardCharsets.UTF_8));
            String json = reader.lines().collect(Collectors.joining());
            ProductDTOByNameAndPrice product = gson.fromJson(json, ProductDTOByNameAndPrice.class);
            productService.deleteProduct(product);
            // Успешный ответ
            resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
        } catch (JsonSyntaxException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("Invalid JSON format.");
        } catch (DatabaseConnectionException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("Database error: " + e.getMessage());
        } catch (Exception e) {
            // Обработка других исключений
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("Unexpected error: " + e.getMessage());
        }
    }
    private ProductDTOByNameAndPrice getProductDTOByNameAndPriceNewCategory(
            ProductDTOByNameAndTypeAndNewProduct product){
        ProductDTOByNameAndPrice productDTOByNameAndPrice = new ProductDTOByNameAndPrice();
        productDTOByNameAndPrice.setNameProduct(product.getNameProduct());
        productDTOByNameAndPrice.setPriceProduct(product.getPriceProduct());
        productDTOByNameAndPrice.setProductCategoryDTOS(product.getProductCategoryDTOS());
        return productDTOByNameAndPrice;
    }
    private String convertToJson(List<ProductDTOByNameAndPrice> products) {
        return gson.toJson(products);
    }
}

