package com.khanenka.restapiservlet.servlet;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonSyntaxException;
import com.khanenka.restapiservlet.model.productdto.ProductCategoryDTOByNameAndType;
import com.khanenka.restapiservlet.repository.implementation.ProductCategoryDAOImpl;
import com.khanenka.restapiservlet.repository.implementation.ProductDAOImpl;
import com.khanenka.restapiservlet.service.ProductCategoryService;
import com.khanenka.restapiservlet.util.DBConnection;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.util.List;
import java.util.stream.Collectors;

import static com.khanenka.restapiservlet.servlet.ProductServlet.CHARSET_UTF8;


@WebServlet("/categories")
public class ProductsCategoryServlet extends HttpServlet {
    static Connection connection = DBConnection.getConnection();
    static ProductCategoryDAOImpl productCategoryDao = new ProductCategoryDAOImpl(connection);
    static ProductDAOImpl productDao = new ProductDAOImpl(connection);
    ProductCategoryService productCategoryService = new ProductCategoryService(productCategoryDao, productDao);
    static Gson gson = new Gson();

    public ProductsCategoryServlet() {
        super();
    }

    public ProductsCategoryServlet(ProductDAOImpl productDao, ProductCategoryDAOImpl productCategoryDao) {
        super();
        this.productCategoryService = new ProductCategoryService(productCategoryDao, productDao);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        response.setContentType("application/json; charset=UTF-8");
        try {
            request.setCharacterEncoding(CHARSET_UTF8);
            response.setCharacterEncoding(CHARSET_UTF8);
            List<ProductCategoryDTOByNameAndType> productCategoryDTOS = productCategoryService.getAllProductCategory();
            JsonArray jsonArray = gson.toJsonTree(productCategoryDTOS).getAsJsonArray();
            response.setContentType("application/json");
            response.getWriter().print(jsonArray.toString());
        } catch (IOException e) {
            log("Error get product category: " + e.getMessage());
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        try {
            request.setCharacterEncoding(CHARSET_UTF8);
            response.setCharacterEncoding(CHARSET_UTF8);
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    request.getInputStream(), StandardCharsets.UTF_8));
            String json = reader.lines().collect(Collectors.joining());
            ProductCategoryDTOByNameAndType productCategoryDTOByNameAndType = gson.fromJson(
                    json, ProductCategoryDTOByNameAndType.class);
            productCategoryService.addProductCategory(productCategoryDTOByNameAndType);
        } catch (Exception e) {
            // Логирование ошибки при добавлении продукта
            log("Error add product category: " + e.getMessage());
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) {
        try {
            request.setCharacterEncoding(CHARSET_UTF8);
            response.setCharacterEncoding(CHARSET_UTF8);
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    request.getInputStream(), StandardCharsets.UTF_8));
            String json = reader.lines().collect(Collectors.joining());
            ProductCategoryDTOByNameAndType product = gson.fromJson(
                    json, ProductCategoryDTOByNameAndType.class);
            productCategoryService.updateProductCategory(product, product.getNewCategory());
        } catch (JsonSyntaxException | IOException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) {
        try {
            request.setCharacterEncoding(CHARSET_UTF8);
            response.setCharacterEncoding(CHARSET_UTF8);
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(request.getInputStream(), StandardCharsets.UTF_8));
            String json = reader.lines().collect(Collectors.joining());
            ProductCategoryDTOByNameAndType productCategory =
                    gson.fromJson(json, ProductCategoryDTOByNameAndType.class);
            productCategoryService.deleteProductCategory(productCategory);
        } catch (JsonSyntaxException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        } catch (RuntimeException | IOException e) {
            e.printStackTrace();
        }
    }
}
