package com.khanenka.restapiservlet.servlet;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.khanenka.restapiservlet.model.productDTO.OrderDetailDTO;
import com.khanenka.restapiservlet.model.productDTO.ProductCategoryDTO;
import com.khanenka.restapiservlet.model.productDTO.ProductDTO;
import com.khanenka.restapiservlet.repository.OrderDetailDao;
import com.khanenka.restapiservlet.repository.ProductCategoryDao;
import com.khanenka.restapiservlet.repository.ProductDao;
import com.khanenka.restapiservlet.repository.impl.OrderDetailDaoImpl;
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
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet("/orderdetail")
public class OrderDetailServlet extends HttpServlet {

    private OrderDetailDao orderDetailDao;


    public OrderDetailServlet() {
        this.orderDetailDao = new OrderDetailDaoImpl();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");


        List<OrderDetailDTO> orderDetailDTOList = null;
        try {
            orderDetailDTOList = orderDetailDao.getAllOrderDetails();
            System.out.println(orderDetailDTOList);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        Gson gson = new Gson();
        JsonArray jsonArray = gson.toJsonTree(orderDetailDTOList).getAsJsonArray();

        response.setContentType("application/json");
        response.getWriter().print(jsonArray.toString());
    }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        response.setCharacterEncoding("UTF-8");
        BufferedReader reader = new BufferedReader(new InputStreamReader(request.getInputStream(), StandardCharsets.UTF_8));
        String json = reader.lines().collect(Collectors.joining());


        Gson gson = new Gson();
        OrderDetailDTO orderDetailDTO = gson.fromJson(json, OrderDetailDTO.class);
        System.out.println(orderDetailDTO);


        try {
            orderDetailDao.addOrderDetail(orderDetailDTO);
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


        OrderDetailDTO orderDetailDTO = gson.fromJson(json, OrderDetailDTO.class);

        System.out.println(orderDetailDTO);
//        Product product1 = ProductUtil.convertToEntity(product);

        try {
            orderDetailDao.updateOrderDetail(orderDetailDTO);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }



}
