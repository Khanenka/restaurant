package com.khanenka.restapiservlet.servlet;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.khanenka.restapiservlet.model.productdto.ProductDTOByNameAndPrice;
import com.khanenka.restapiservlet.repository.ProductCategoryDao;
import com.khanenka.restapiservlet.repository.ProductDao;
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


@WebServlet("/products")
public class ProductServlet extends HttpServlet {
    static Connection connection = DBConnection.getConnection();
    ProductService productService;
    static ProductDAOImpl productDao = new ProductDAOImpl(connection);
    static ProductCategoryDAOImpl productCategoryDao = new ProductCategoryDAOImpl(connection);
    static OrderDetailDAOImpl orderDAO = new OrderDetailDAOImpl(connection);
    static final String CHARSET_UTF8 = "UTF-8";
    static Gson gson = new Gson();

    public ProductServlet() {
        super();
    }

    public ProductServlet(ProductDao productDao, ProductCategoryDao productCategoryDao,
                          OrderDetailDAOImpl orderDAO) {
        super();
        this.productService = new ProductService(productDao, productCategoryDao, orderDAO);
    }

    @Override
    public void init() throws ServletException {
        super.init();
        productService = new ProductService(productDao, productCategoryDao, orderDAO);
        productDao.createTableProduct();
        productDao.createProductProductCategory();
        productDao.createProductCategoryTable();
        orderDAO.createOrderDetailTable();
        orderDAO.createOrderDetailProductTable();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        response.setContentType("application/json; charset=UTF-8");
        PrintWriter out = null;
        try {

            out = response.getWriter();
            List<ProductDTOByNameAndPrice> products = productService.getAllProducts();
            String jsonResponse = convertToJson(products);
            out.print(jsonResponse);
            response.setStatus(HttpServletResponse.SC_OK);
        } catch (IOException e) {
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
    protected void doPut(HttpServletRequest request, HttpServletResponse response) {
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setContentType("application/json; charset=UTF-8");
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(request.getInputStream(), StandardCharsets.UTF_8))) {
            String json = reader.lines().collect(Collectors.joining());
            ProductDTOByNameAndPrice product = gson.fromJson(
                    json, ProductDTOByNameAndPrice.class);
            productService.updateProduct(product, product.getNewProduct());
            response.setStatus(HttpServletResponse.SC_OK);
        } catch (JsonSyntaxException e) {
            log("Error updating product: " + e.getMessage());
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        } catch (IOException ioException) {
            // Handle the IOException when getting the input stream
            log("Error retrieving input stream: " + ioException.getMessage());
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
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
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    private String convertToJson(List<ProductDTOByNameAndPrice> products) {
        return gson.toJson(products);
    }
}

