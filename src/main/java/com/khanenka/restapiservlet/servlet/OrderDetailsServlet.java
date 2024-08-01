package com.khanenka.restapiservlet.servlet;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonSyntaxException;
import com.khanenka.restapiservlet.model.productdto.OrderDetailDTO;
import com.khanenka.restapiservlet.repository.implementation.OrderDetailDAOImpl;
import com.khanenka.restapiservlet.repository.implementation.ProductDAOImpl;
import com.khanenka.restapiservlet.service.OrderDetailService;
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


@WebServlet("/orderdetails")
public class OrderDetailsServlet extends HttpServlet {
    static Connection connection = DBConnection.getConnection();
    static ProductDAOImpl productDAO = new ProductDAOImpl(connection);
    static OrderDetailDAOImpl orderDetailDAO = new OrderDetailDAOImpl(connection);
    OrderDetailService orderDetailService = new OrderDetailService(orderDetailDAO, productDAO);
    static Gson gson = new Gson();

    public OrderDetailsServlet() {
        super();
    }

    public OrderDetailsServlet(OrderDetailDAOImpl orderDetailDAO, ProductDAOImpl productDAO) {
        super();
        this.orderDetailService = new OrderDetailService(orderDetailDAO, productDAO);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        response.setContentType("application/json; charset=UTF-8");
        try {
            request.setCharacterEncoding(CHARSET_UTF8);
            response.setCharacterEncoding(CHARSET_UTF8);
            List<OrderDetailDTO> orderDetailDTOList = orderDetailService.getOrderDetail();
            JsonArray jsonArray = gson.toJsonTree(orderDetailDTOList).getAsJsonArray();
            response.getWriter().print(jsonArray.toString());
        } catch (RuntimeException | IOException e) {
            log("Failed to get order", e);
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
            OrderDetailDTO orderDetailDTO = gson.fromJson(json, OrderDetailDTO.class);
            orderDetailService.addOrderDetail(orderDetailDTO);
        } catch (JsonSyntaxException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        } catch (IOException e) {
            e.printStackTrace();
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
            OrderDetailDTO orderDetailDTO = gson.fromJson(json, OrderDetailDTO.class);
            orderDetailService.updateOrderDetail(orderDetailDTO);
        } catch (JsonSyntaxException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) {
        try {
            req.setCharacterEncoding(CHARSET_UTF8);
            resp.setCharacterEncoding(CHARSET_UTF8);
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(req.getInputStream(), StandardCharsets.UTF_8));
            String json = reader.lines().collect(Collectors.joining());
            OrderDetailDTO orderDetailDTO = gson.fromJson(json, OrderDetailDTO.class);
            orderDetailService.deleteOrderDetail(orderDetailDTO);
        } catch (JsonSyntaxException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        } catch (RuntimeException e) {
            e.printStackTrace();
        } catch (IOException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            e.printStackTrace();
        }
    }
}
