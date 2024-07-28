package com.khanenka.restapiservlet.servlet;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonSyntaxException;
import com.khanenka.restapiservlet.model.productdto.OrderDetailDTO;
import com.khanenka.restapiservlet.repository.impl.OrderDetailDaoImpl;
import com.khanenka.restapiservlet.repository.implementation.OrderDetailDAOImpl;
import com.khanenka.restapiservlet.service.OrderDetailService;
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

import static com.khanenka.restapiservlet.servlet.ProductHomeServlet.CHARSET_UTF8;

@WebServlet("/orderdetails")
public class OrderDetailsServlet extends HttpServlet {
    Connection connection = DBConnection.getConnection();
    private OrderDetailDAOImpl orderDetailDAO = new OrderDetailDAOImpl(connection);
    private OrderDetailService orderDetailService=new OrderDetailService(orderDetailDAO);

    final Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {

        try {
            // Устанавливаем кодировку для запроса и ответа
            request.setCharacterEncoding(CHARSET_UTF8);
            response.setCharacterEncoding(CHARSET_UTF8);
            List<OrderDetailDTO> orderDetailDTOList = null;
            // Получаем все детали заказов из базы данных
            orderDetailDTOList = orderDetailService.getOrderDetail();
            // Конвертируем список деталей заказов в формат JSON
            JsonArray jsonArray = gson.toJsonTree(orderDetailDTOList).getAsJsonArray();
            System.out.println(jsonArray);
            // Устанавливаем тип контента и отправляем JSON в ответе
            response.setContentType("application/json; charset=UTF-8");
            response.getWriter().print(jsonArray.toString());
        } catch (RuntimeException | IOException e) {
            // Логируем ошибки, если они произошли
            log("Failed to get order", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            // Устанавливаем кодировку для запроса
            request.setCharacterEncoding(CHARSET_UTF8);
            response.setCharacterEncoding(CHARSET_UTF8);

            // Читаем тело запроса с входящими данными JSON
            BufferedReader reader = new BufferedReader(new InputStreamReader(request.getInputStream(), StandardCharsets.UTF_8));
            String json = reader.lines().collect(Collectors.joining());
            System.out.println(json);
            // Преобразуем JSON в объект OrderDetailDTO
            OrderDetailDTO orderDetailDTO = gson.fromJson(json, OrderDetailDTO.class);
            System.out.println(orderDetailDTO);
            // Добавляем новую деталь заказа в базу данных
            orderDetailService.addOrderDetail(orderDetailDTO);
        } catch (JsonSyntaxException e) {
            // Устанавливаем ответ 400 (Неверный запрос) в случае ошибки синтаксиса JSON
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        } catch (IOException e) {
            // Логируем ошибки, если они произошли
            e.printStackTrace();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            // Устанавливаем кодировку для запроса
            request.setCharacterEncoding(CHARSET_UTF8);
            response.setCharacterEncoding(CHARSET_UTF8);

            // Читаем тело запроса с входящими данными JSON
            BufferedReader reader = new BufferedReader(new InputStreamReader(request.getInputStream(), StandardCharsets.UTF_8));
            String json = reader.lines().collect(Collectors.joining());

            // Преобразуем JSON в объект OrderDetailDTO
            OrderDetailDTO orderDetailDTO = gson.fromJson(json, OrderDetailDTO.class);

            // Обновляем существующую деталь заказа в базе данных
            orderDetailDAO.updateOrderDetail(orderDetailDTO);
        } catch (JsonSyntaxException e) {
            // Устанавливаем ответ 400 (Неверный запрос) в случае ошибки синтаксиса JSON
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        } catch (Exception e) {
            // Логируем ошибки, если они произошли
            e.printStackTrace();
        }
    }

}
