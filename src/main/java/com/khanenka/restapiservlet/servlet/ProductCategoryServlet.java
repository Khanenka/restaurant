//package com.khanenka.restapiservlet.servlet;
//
//import com.khanenka.restapiservlet.DBConnection;
//import com.khanenka.restapiservlet.model.productDTO.ProductCategoryDTO;
//import com.khanenka.restapiservlet.repository.ProductCategoryDao;
//import com.khanenka.restapiservlet.repository.impl.ProductCategoryDaoImpl;
//
//import javax.servlet.RequestDispatcher;
//import javax.servlet.ServletConfig;
//import javax.servlet.ServletException;
//import javax.servlet.annotation.WebServlet;
//import javax.servlet.http.HttpServlet;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import java.sql.Connection;
//import java.sql.SQLException;
//import java.sql.Statement;
//import java.util.List;
//
//@WebServlet("/category")
//public class ProductCategoryServlet extends HttpServlet {
//
//
//    private Connection connection;
//    private ProductCategoryDao productCategoryDao;
//
//    public ProductCategoryServlet() {
//        this.productCategoryDao = new ProductCategoryDaoImpl();
//    }
//
//    public void init(ServletConfig servletConfig) {
//        try {
//            super.init(servletConfig);
//            Connection conn = DBConnection.getConnection();
//            Statement stmt = conn.createStatement();
//            {
//                stmt.executeUpdate("CREATE TABLE IF NOT EXISTS ProductCategory ( id SERIAL PRIMARY KEY, name VARCHAR(255) NOT NULL UNIQUE, type VARCHAR(50) NOT NULL);");
//            }
//        } catch (ServletException | SQLException ex) {
//            throw new RuntimeException(ex);
//        }
//        DBConnection.closeConnection(connection);
//    }
//
//    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        this.doGet(request, response);
//    }
//    //
//
//
//    @Override
//    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//
//        String action = request.getServletPath();
//        switch (action) {
//
//            default:
//                getAllProductCategory(request,response);
//                break;
//        }
//    }
//
//
//     void getAllProductCategory(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//
//        List<ProductCategoryDTO> listProductCategory = productCategoryDao.getAllProductsCategory();
//        request.setCharacterEncoding("UTF-8");
//        request.setAttribute("listProductCategory", listProductCategory);
//
//        RequestDispatcher dispatcher = request.getRequestDispatcher("product-category-list.jsp");
//        dispatcher.forward(request, response);
//    }
//    void showNewFormProductCategory(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//
//        RequestDispatcher dispatcher = request.getRequestDispatcher("product-category-form.jsp");
//        dispatcher.forward(request, response);
//    }
//
//    void insertProductCategory(HttpServletRequest request, HttpServletResponse response) throws IOException {
//        request.setCharacterEncoding("UTF-8");
//        productCategoryDao.addProductCategory(request, response);
//        response.sendRedirect("product-category-list.jsp");
//    }
//
//}
