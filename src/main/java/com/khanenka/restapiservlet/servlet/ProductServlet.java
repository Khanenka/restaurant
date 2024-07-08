package com.khanenka.restapiservlet.servlet;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.khanenka.restapiservlet.model.productDTO.ProductDTO;
import com.khanenka.restapiservlet.repository.ProductDao;
import com.khanenka.restapiservlet.repository.impl.ProductDaoImpl;

import javax.servlet.RequestDispatcher;
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

@WebServlet("/test")
public class ProductServlet extends HttpServlet {

    Connection conn;
    RequestDispatcher rd;
    static String sqlCreateProductTable = "CREATE TABLE IF NOT EXISTS Product (\n" +
            "    idProduct SERIAL PRIMARY KEY,\n" +
            "    nameProduct NOT NULL UNIQUE VARCHAR(100),\n" +
            "    priceProduct DECIMAL(10, 2),\n" +
            "    quantityProduct INT,\n" +
            "    availableProduct BOOLEAN\n" +
            ");";

    private ProductDao productDao;


    public ProductServlet() {
        this.productDao = new ProductDaoImpl();
    }


    // properties.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("C:/Users/leonid/IdeaProjects/RestApiServlet/src/main/resources/db.properties"));


    //    public void init(ServletConfig servletConfig) {
//        try {
//            super.init(servletConfig);
//            Connection conn = DBConnection.getConnection();
//            Statement stmt = conn.createStatement();
//            {
//                stmt.executeUpdate(sqlCreateProductTable);
//            }
//        } catch (ServletException | SQLException ex) {
//            throw new RuntimeException(ex);
//        }
//        DBConnection.closeConnection(conn);
//    }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        response.setCharacterEncoding("UTF-8");
        BufferedReader reader = new BufferedReader(new InputStreamReader(request.getInputStream(), StandardCharsets.UTF_8));
        String json = reader.lines().collect(Collectors.joining());


        Gson gson = new Gson();
        ProductDTO product = gson.fromJson(json, ProductDTO.class);
        System.out.println(product);


        try {
            productDao.addProduct(product);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        response.setCharacterEncoding("UTF-8");
        BufferedReader reader = new BufferedReader(new InputStreamReader(request.getInputStream(), StandardCharsets.UTF_8));
        String json = reader.lines().collect(Collectors.joining());

        System.out.println(json);
        Gson gson = new Gson();


        ProductDTO product = gson.fromJson(json, ProductDTO.class);

        System.out.println(product);
//        Product product1 = ProductUtil.convertToEntity(product);

        try {
            productDao.updateProduct(product);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

        resp.setCharacterEncoding("UTF-8");
        BufferedReader reader = new BufferedReader(new InputStreamReader(req.getInputStream(), StandardCharsets.UTF_8));
        String json = reader.lines().collect(Collectors.joining());

        System.out.println(json);
        Gson gson = new Gson();


        ProductDTO product = gson.fromJson(json, ProductDTO.class);

        System.out.println(product);
//        Product product1 = ProductUtil.convertToEntity(product);

        try {
            productDao.deleteProduct(product);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");


        List<ProductDTO> productList = null;
        try {
            productList = productDao.getAllProducts();
            System.out.println(productList);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        Gson gson = new Gson();
        JsonArray jsonArray = gson.toJsonTree(productList).getAsJsonArray();

        response.setContentType("application/json");
        response.getWriter().print(jsonArray.toString());
    }
}






