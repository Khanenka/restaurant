package com.khanenka.restapiservlet.servlet;


import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonSyntaxException;
import com.khanenka.restapiservlet.exception.DatabaseConnectionException;
import com.khanenka.restapiservlet.model.productdto.ProductCategoryDTOByNameAndType;
import com.khanenka.restapiservlet.model.productdto.ProductCategoryDTOByNameAndTypeAndNewCategory;
import com.khanenka.restapiservlet.repository.ProductCategoryDao;
import com.khanenka.restapiservlet.repository.impl.ProductCategoryDaoImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

import static com.khanenka.restapiservlet.servlet.ProductHomeServlet.CHARSET_UTF8;

/**
 * Сервлет для обработки операций с категориями продуктов.
 * <p>
 * Этот сервлет поддерживает операции создания, получения, обновления и удаления категорий продуктов.
 * Он использует DAO-объект для взаимодействия с базой данных.
 * </p>
 * Поддерживаемые HTTP методы:
 * <ul>
 *     <li>POST - для добавления новой категории продукта</li>
 *     <li>GET - для получения всех категорий продуктов</li>
 *     <li>PUT - для обновления существующей категории продукта</li>
 *     <li>DELETE - для удаления категории продукта</li>
 * </ul>
 *
 * @author Khanenka
 * @version 1.0
 */
@WebServlet("/category")
public class ProductCategoryServlet extends HttpServlet {

    // Инициализация DAO для работы с категориями продуктов
    static ProductCategoryDao productCategoryDao = new ProductCategoryDaoImpl();
    /**
     * объект для работы с json
     */
    final Gson gson = new Gson();

    /**
     * Обрабатывает POST запрос для добавления новой категории продукта.
     *
     * @param request  объект HttpServletRequest, который содержит данные запроса.
     * @param response объект HttpServletResponse, который используется для отправки ответа на запрос.
     * @throws ServletException если возникает ошибка в процессе обработки запроса.
     * @throws IOException      если возникает ошибка ввода-вывода.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        try {
            request.setCharacterEncoding(CHARSET_UTF8); // Установка utf-8

            response.setCharacterEncoding(CHARSET_UTF8); // Установка кодировки ответа
            BufferedReader reader = new BufferedReader(new InputStreamReader(request.getInputStream(), StandardCharsets.UTF_8)); // Чтение входящего потока
            String json = reader.lines().collect(Collectors.joining()); // Собираем все строки в одну

            // Десериализация JSON в объект DTO
            ProductCategoryDTOByNameAndType productCategoryDTOByNameAndType = gson.fromJson(json, ProductCategoryDTOByNameAndType.class);

            // Добавление категории продукта в базу данных
            productCategoryDao.addProductCategory(productCategoryDTOByNameAndType);
        } catch (JsonSyntaxException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST); // 400 Bad Request
        } catch (IOException e) {
            e.printStackTrace(); // Обработка исключения ввода-вывода
        }

    }

    /**
     * Обрабатывает GET запрос для получения всех категорий продуктов.
     *
     * @param request  объект HttpServletRequest, который содержит данные запроса.
     * @param response объект HttpServletResponse, который используется для отправки ответа на запрос.
     * @throws ServletException если возникает ошибка в процессе обработки запроса.
     * @throws IOException      если возникает ошибка ввода-вывода.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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

    /**
     * Обрабатывает PUT запрос для обновления существующей категории продукта.
     *
     * @param request  объект HttpServletRequest, который содержит данные запроса.
     * @param response объект HttpServletResponse, который используется для отправки ответа на запрос.
     * @throws ServletException если возникает ошибка в процессе обработки запроса.
     * @throws IOException      если возникает ошибка ввода-вывода.
     */
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
            ProductCategoryDTOByNameAndType productCategoryDTOByNameAndType = new ProductCategoryDTOByNameAndType();
            productCategoryDTOByNameAndType.setNameProductCategory(product.getNameProductCategory());
            productCategoryDTOByNameAndType.setTypeProductCategory(product.getTypeProductCategory());
            productCategoryDTOByNameAndType.setProductDTOS(product.getProductDTOS());

            // Обновление категории продукта в базе данных
            productCategoryDao.updateProductCategory(productCategoryDTOByNameAndType, product.getNewCategory());
        } catch (JsonSyntaxException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST); // 400 Bad Request
        } catch (Exception e) {
            e.printStackTrace(); // Обработка других исключений
        }

    }

    /**
     * Обрабатывает DELETE запрос для удаления категории продукта.
     *
     * @param req  объект HttpServletRequest, который содержит данные запроса.
     * @param resp объект HttpServletResponse, который используется для отправки ответа на запрос.
     * @throws ServletException если возникает ошибка в процессе обработки запроса.
     * @throws IOException      если возникает ошибка ввода-вывода.
     */
    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        try {
            req.setCharacterEncoding(CHARSET_UTF8); // Установка utf-8

            resp.setCharacterEncoding(CHARSET_UTF8); // Установка кодировки ответа
            BufferedReader reader = new BufferedReader(new InputStreamReader(req.getInputStream(), StandardCharsets.UTF_8)); // Чтение входящего потока
            String json = reader.lines().collect(Collectors.joining()); // Собираем все строки в одну

            // Десериализация JSON в объект DTO для удаления
            ProductCategoryDTOByNameAndType productCategory = gson.fromJson(json, ProductCategoryDTOByNameAndType.class);

            // Удаление категории продукта из базы данных
            productCategoryDao.deleteProductCategory(productCategory);
        } catch (DatabaseConnectionException e) {
            e.printStackTrace(); // Обработка исключения связи с базой данных
        } catch (JsonSyntaxException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST); // 400 Bad Request
        } catch (IOException e) {
            // Обработка IOException отдельно
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST); // 400 Bad Request
            e.printStackTrace();
        }
    }
}
