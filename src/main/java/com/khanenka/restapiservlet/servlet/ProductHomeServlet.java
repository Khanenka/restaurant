package com.khanenka.restapiservlet.servlet;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonSyntaxException;
import com.khanenka.restapiservlet.model.productdto.ProductDTOByNameAndPrice;
import com.khanenka.restapiservlet.model.productdto.ProductDTOByNameAndTypeAndNewProduct;
import com.khanenka.restapiservlet.repository.OrderDetailDao;
import com.khanenka.restapiservlet.repository.ProductCategoryDao;
import com.khanenka.restapiservlet.repository.ProductDao;
import com.khanenka.restapiservlet.repository.impl.OrderDetailDaoImpl;
import com.khanenka.restapiservlet.repository.impl.ProductCategoryDaoImpl;
import com.khanenka.restapiservlet.repository.impl.ProductDaoImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Сервлет для обработки запросов, связанных с продуктами .
 * Поддерживает операции создания, обновления, удаления и получения продуктов.
 *
 * <p>Данный сервлет использует DAO (Data Access Object) для работы с данными
 * продуктов и категорий продуктов. Все данные обрабатываются в формате JSON.</p>
 *
 * <p>Доступные методы:</p>
 * <ul>
 *     <li>{@link #doPost(HttpServletRequest, HttpServletResponse)} - добавляет новый продукт.</li>
 *     <li>{@link #doPut(HttpServletRequest, HttpServletResponse)} - обновляет существующий продукт.</li>
 *     <li>{@link #doDelete(HttpServletRequest, HttpServletResponse)} - удаляет продукт.</li>
 *     <li>{@link #doGet(HttpServletRequest, HttpServletResponse)} - получает список всех продуктов.</li>
 * </ul>
 *
 * <p>Кодировка для всех операций установлена на UTF-8.</p>
 */
@WebServlet("/product")
public class ProductHomeServlet extends HttpServlet {
    // Статические экземпляры DAO для работы с продуктами и заказами
    static ProductDao productDao = new ProductDaoImpl();
    /**
     * экземпляр DAO для работы с категорями продуктов
     */
    final ProductCategoryDao productCategoryDao;
    static OrderDetailDao orderDetailDao = new OrderDetailDaoImpl();
    static final String CHARSET_UTF8 = "UTF-8"; // Кодировка для работы с строками
    /**
     * объект для работы с json
     */
    final Gson gson = new Gson();


    /**
     * Конструктор сервлета, инициализирующий DAO для категорий продуктов
     */
    // Конструктор сервлета, инициализирующий DAO для категорий продуктов
    public ProductHomeServlet() {
        this.productCategoryDao = new ProductCategoryDaoImpl();
    }

    /**
     * Инициализация сервлета.
     * Создает необходимые таблицы в базе данных при старте сервлета.
     */
    @Override
    public void init() {
        productDao.createTableProduct(); // Создает таблицу с продуктами
        productDao.createProductProductCategory(); // Создает связь между продуктами и категориями
        productDao.createProductCategoryTable(); // Создает таблицу для категорий продуктов
        orderDetailDao.createOrderDetailTable(); // Создает таблицу деталей заказов
        orderDetailDao.createOrderDetailProductTable(); // Создает связь между деталями заказов и продуктами
    }

    /**
     * Обрабатывает POST-запрос для добавления нового продукта.
     * Получает данные о продукте в формате JSON и сохраняет их в базе данных.
     *
     * @param request  Объект запроса, содержащий данные о продукте.
     * @param response Объект ответа, используемый для отправки ответа клиенту.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        try {
            // Установка кодировки для входящих и исходящих данных
            request.setCharacterEncoding(CHARSET_UTF8);
            response.setCharacterEncoding(CHARSET_UTF8);
            // Чтение JSON из тела запроса
            BufferedReader reader = new BufferedReader(new InputStreamReader(request.getInputStream(), StandardCharsets.UTF_8));
            String json = reader.lines().collect(Collectors.joining());
            // Десериализация JSON в объект продукта
            ProductDTOByNameAndPrice product = gson.fromJson(json, ProductDTOByNameAndPrice.class);
            // Добавление продукта в базу данных
            productDao.addProduct(product);
        } catch (Exception e) {
            // Логирование ошибки при добавлении продукта
            log("Error add product" + e.getMessage());
        }
    }

    /**
     * Обрабатывает PUT-запрос для обновления существующего продукта.
     * Получает обновленные данные о продукте в формате JSON и сохраняет их в базе данных.
     *
     * @param request  Объект запроса, содержащий новые данные о продукте.
     * @param response Объект ответа, используемый для отправки ответа клиенту.
     * @throws ServletException если возникла ошибка в процессе обработки.
     * @throws IOException      если произошла ошибка ввода-вывода.
     */
    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            // Установка кодировки для входящих данных
            request.setCharacterEncoding(StandardCharsets.UTF_8.name());
            // Установка кодировки для ответа
            response.setCharacterEncoding(StandardCharsets.UTF_8.name());
            response.setContentType("application/json; charset=UTF-8");

            // Считывание JSON из запроса
            BufferedReader reader = new BufferedReader(new InputStreamReader(request.getInputStream(), StandardCharsets.UTF_8));
            String json = reader.lines().collect(Collectors.joining());
            // Десериализация JSON
            ProductDTOByNameAndTypeAndNewProduct product = gson.fromJson(json, ProductDTOByNameAndTypeAndNewProduct.class);

            // Создание DTO для обновленного продукта
            ProductDTOByNameAndPrice productDTOByNameAndPrice = new ProductDTOByNameAndPrice();
            productDTOByNameAndPrice.setNameProduct(product.getNameProduct());
            productDTOByNameAndPrice.setPriceProduct(product.getPriceProduct());
            productDTOByNameAndPrice.setProductCategoryDTOS(product.getProductCategoryDTOS());

            // Обновление в базе данных
            productDao.updateProduct(productDTOByNameAndPrice, product.getNewProduct());
        } catch (Exception e) {
            // Логирование ошибки при обновлении продукта
            log("Error update product: " + e.getMessage());
        }
    }

    /**
     * Обрабатывает DELETE-запрос для удаления продукта.
     * Получает данные о продукте в формате JSON и удаляет его из базы данных.
     *
     * @param req  Объект запроса для удаления продукта.
     * @param resp Объект ответа, используемый для отправки ответа клиенту.
     * @throws IOException если произошла ошибка ввода-вывода.
     */
    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            req.setCharacterEncoding(CHARSET_UTF8); // Установка utf-8
            resp.setCharacterEncoding(CHARSET_UTF8);
            BufferedReader reader = new BufferedReader(new InputStreamReader(req.getInputStream(), StandardCharsets.UTF_8));
            String json = reader.lines().collect(Collectors.joining());
            // Десериализация JSON
            ProductDTOByNameAndPrice product = gson.fromJson(json, ProductDTOByNameAndPrice.class);
            // Удаление продукта из базы данных
            productDao.deleteProduct(product);
        } catch (JsonSyntaxException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST); // Ошибка 400 Bad Request
        } catch (Exception e) {
            // Обработка других исключений
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR); // Ошибка 500 Internal Server Error
        }
    }

    /**
     * Обрабатывает GET-запрос для получения списка всех продуктов.
     * Возвращает список продуктов в формате JSON.
     *
     * @param request  Объект запроса.
     * @param response Объект ответа, используемый для отправки списка продуктов клиенту.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        List<ProductDTOByNameAndPrice> productList = null; // Список продуктов
        try {
            request.setCharacterEncoding(CHARSET_UTF8); // Установка utf-8
            response.setCharacterEncoding(CHARSET_UTF8);
            productList = productDao.getAllProducts(); // Получение всех продуктов
            JsonArray jsonArray = gson.toJsonTree(productList).getAsJsonArray(); // Преобразование списка в JSON

            // Устанавливаем кодировку ответа
            response.setContentType("application/json; charset=UTF-8");
            response.getWriter().print(jsonArray.toString()); // Отправка списка продуктов клиенту
        } catch (RuntimeException | IOException e) {
            // Логирование ошибки при получении продуктов
            log("Failed to create product product category", e);
        }
    }
}
