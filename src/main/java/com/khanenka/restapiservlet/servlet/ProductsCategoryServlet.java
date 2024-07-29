package com.khanenka.restapiservlet.servlet;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonSyntaxException;
import com.khanenka.restapiservlet.exception.DatabaseConnectionException;
import com.khanenka.restapiservlet.model.productdto.ProductCategoryDTOByNameAndType;
import com.khanenka.restapiservlet.model.productdto.ProductCategoryDTOByNameAndTypeAndNewCategory;
import com.khanenka.restapiservlet.repository.implementation.ProductCategoryDAOImpl;
import com.khanenka.restapiservlet.repository.implementation.ProductDAOImpl;
import com.khanenka.restapiservlet.service.ProductCategoryService;
import com.khanenka.restapiservlet.util.DBConnection;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

import static com.khanenka.restapiservlet.servlet.ProductServlet.CHARSET_UTF8;


@WebServlet("/categories")
public class ProductsCategoryServlet extends HttpServlet {
    Connection connection = DBConnection.getConnection();

    private ProductCategoryDAOImpl productCategoryDao = new ProductCategoryDAOImpl(connection);
    private ProductDAOImpl productDao = new ProductDAOImpl(connection);
    private ProductCategoryService productCategoryService=new ProductCategoryService(productCategoryDao,productDao);

    Gson gson = new Gson();



    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        try {
            request.setCharacterEncoding(CHARSET_UTF8); // Установка utf-8
            response.setCharacterEncoding(CHARSET_UTF8); // Установка utf-8

            List<ProductCategoryDTOByNameAndType> productCategoryDTOS = null; // Список категорий продукта

            // Получение всех категорий продуктов из базы данных
            productCategoryDTOS = productCategoryDao.getAllProductCategories();

            // Конвертация списка в JSON массив
            JsonArray jsonArray = gson.toJsonTree(productCategoryDTOS).getAsJsonArray();

            response.setContentType("application/json"); // Установка типа содержимого ответа
            response.getWriter().print(jsonArray.toString()); // Отправка JSON массива в ответе
        } catch (SQLException | IOException e) {
            e.printStackTrace(); // Обработка исключений
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        try {
            request.setCharacterEncoding(CHARSET_UTF8); // Установка utf-8

            response.setCharacterEncoding(CHARSET_UTF8); // Установка кодировки ответа
            BufferedReader reader = new BufferedReader(new InputStreamReader(request.getInputStream(), StandardCharsets.UTF_8)); // Чтение входящего потока
            String json = reader.lines().collect(Collectors.joining()); // Собираем все строки в одну
            System.out.println(json);
            // Десериализация JSON в объект DTO
            ProductCategoryDTOByNameAndType productCategoryDTOByNameAndType = gson.fromJson(json, ProductCategoryDTOByNameAndType.class);
            System.out.println(productCategoryDTOByNameAndType);
            // Добавление категории продукта в базу данных
            productCategoryService.addProductCategory(productCategoryDTOByNameAndType);
        } catch (JsonSyntaxException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST); // 400 Bad Request
        } catch (IOException e) {
            e.printStackTrace(); // Обработка исключения ввода-вывода
        }

    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            request.setCharacterEncoding(CHARSET_UTF8); // Установка utf-8

            response.setCharacterEncoding(CHARSET_UTF8); // Установка кодировки ответа
            BufferedReader reader = new BufferedReader(new InputStreamReader(request.getInputStream(), StandardCharsets.UTF_8)); // Чтение входящего потока
            String json = reader.lines().collect(Collectors.joining()); // Собираем все строки в одну

            // Десериализация JSON в объект DTO для обновления
            ProductCategoryDTOByNameAndTypeAndNewCategory product = gson.fromJson(json, ProductCategoryDTOByNameAndTypeAndNewCategory.class);

            // Создание объекта для обновления
            ProductCategoryDTOByNameAndType productCategoryDTOByNameAndType = convertProductCategoryToNewProductCategory(product);

            // Обновление категории продукта в базе данных
            productCategoryService.updateProductCategory(productCategoryDTOByNameAndType, product.getNewCategory());
        } catch (JsonSyntaxException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST); // 400 Bad Request
        } catch (Exception e) {
            e.printStackTrace(); // Обработка других исключений
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response){
        try {
            request.setCharacterEncoding(CHARSET_UTF8); // Установка utf-8

            response.setCharacterEncoding(CHARSET_UTF8); // Установка кодировки ответа
            BufferedReader reader = new BufferedReader(new InputStreamReader(request.getInputStream(), StandardCharsets.UTF_8)); // Чтение входящего потока
            String json = reader.lines().collect(Collectors.joining()); // Собираем все строки в одну

            // Десериализация JSON в объект DTO для удаления
            ProductCategoryDTOByNameAndType productCategory = gson.fromJson(json, ProductCategoryDTOByNameAndType.class);

            productCategoryService.deleteProductCategory(productCategory);
        } catch (DatabaseConnectionException e) {
            e.printStackTrace(); // Обработка исключения связи с базой данных
        } catch (JsonSyntaxException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST); // 400 Bad Request
        } catch (IOException e) {
            // Обработка IOException отдельно
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST); // 400 Bad Request
            e.printStackTrace();
        }
    }
    private ProductCategoryDTOByNameAndType convertProductCategoryToNewProductCategory(ProductCategoryDTOByNameAndTypeAndNewCategory newCategory) {
        ProductCategoryDTOByNameAndType productCategoryDTOByNameAndType = new ProductCategoryDTOByNameAndType();
        productCategoryDTOByNameAndType.setNameProductCategory(newCategory.getNameProductCategory());
        productCategoryDTOByNameAndType.setTypeProductCategory(newCategory.getTypeProductCategory());
        productCategoryDTOByNameAndType.setProductDTOS(newCategory.getProductDTOS());
        return productCategoryDTOByNameAndType;
    }
    private String convertToJson(List<ProductCategoryDTOByNameAndType> productCategory) {
        return gson.toJson(productCategory);
    }

}
