package com.khanenka.restapiservlet.servlet;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonSyntaxException;
import com.khanenka.restapiservlet.exception.DatabaseConnectionException;
import com.khanenka.restapiservlet.model.productdto.OrderDetailDTO;
import com.khanenka.restapiservlet.repository.OrderDetailDao;
import com.khanenka.restapiservlet.repository.impl.OrderDetailDaoImpl;

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
 * Servlet для обработки запросов, связанных с деталями заказов.
 * Поддерживает операции получения, добавления, обновления и удаления деталей заказов.
 *
 * <p>Этот сервлет обрабатывает следующие HTTP-методы:</p>
 * <ul>
 *     <li><strong>GET</strong> - Получение всех деталей заказов.</li>
 *     <li><strong>POST</strong> - Добавление новой детали заказа.</li>
 *     <li><strong>PUT</strong> - Обновление существующей детали заказа.</li>
 *     <li><strong>DELETE</strong> - Удаление детали заказа.</li>
 * </ul>
 *
 * <p>Для сериализации и десериализации объектов используется библиотека Gson.</p>
 */
@WebServlet("/orderdetail")
public class OrderDetailServlet extends HttpServlet {
    /**
     * Объект доступа к данным для операций с деталями заказов.
     */
    static OrderDetailDao orderDetailDao = new OrderDetailDaoImpl();

    /**
     * Экземпляр Gson для работы с JSON.
     */
    final Gson gson = new Gson();

    /**
     * Обрабатывает HTTP GET запросы.
     *
     * <p>Возвращает список всех деталей заказов в формате JSON.</p>
     *
     * @param request  Объект HttpServletRequest, содержащий информацию о запросе.
     * @param response Объект HttpServletResponse, используемый для формирования ответа.
     * @throws ServletException В случае обработки ошибок сервлета.
     * @throws IOException      В случае ошибок ввода-вывода.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        List<OrderDetailDTO> orderDetailDTOList = null;
        try {
            // Устанавливаем кодировку для запроса и ответа
            request.setCharacterEncoding(CHARSET_UTF8);
            response.setCharacterEncoding(CHARSET_UTF8);
            // Получаем все детали заказов из базы данных
            orderDetailDTOList = orderDetailDao.getAllOrderDetails();
            // Конвертируем список деталей заказов в формат JSON
            JsonArray jsonArray = gson.toJsonTree(orderDetailDTOList).getAsJsonArray();
            // Устанавливаем тип контента и отправляем JSON в ответе
            response.setContentType("application/json; charset=UTF-8");
            response.getWriter().print(jsonArray.toString());
        } catch (RuntimeException | IOException | SQLException e) {
            // Логируем ошибки, если они произошли
            log("Failed to get order", e);
        }
    }

    /**
     * Обрабатывает HTTP POST запросы.
     *
     * <p>Добавляет новую деталь заказа, полученную из тела запроса в формате JSON.</p>
     *
     * @param request  Объект HttpServletRequest, содержащий информацию о запросе.
     * @param response Объект HttpServletResponse, используемый для формирования ответа.
     * @throws ServletException В случае обработки ошибок сервлета.
     * @throws IOException      В случае ошибок ввода-вывода.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            // Устанавливаем кодировку для запроса
            request.setCharacterEncoding(CHARSET_UTF8);
            response.setCharacterEncoding(CHARSET_UTF8);

            // Читаем тело запроса с входящими данными JSON
            BufferedReader reader = new BufferedReader(new InputStreamReader(request.getInputStream(), StandardCharsets.UTF_8));
            String json = reader.lines().collect(Collectors.joining());

            // Преобразуем JSON в объект OrderDetailDTO
            OrderDetailDTO orderDetailDTO = gson.fromJson(json, OrderDetailDTO.class);

            // Добавляем новую деталь заказа в базу данных
            orderDetailDao.addOrderDetail(orderDetailDTO);
        } catch (JsonSyntaxException e) {
            // Устанавливаем ответ 400 (Неверный запрос) в случае ошибки синтаксиса JSON
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        } catch (IOException e) {
            // Логируем ошибки, если они произошли
            e.printStackTrace();
        }
    }

    /**
     * Обрабатывает HTTP PUT запросы.
     *
     * <p>Обновляет существующую деталь заказа, полученную из тела запроса в формате JSON.</p>
     *
     * @param request  Объект HttpServletRequest, содержащий информацию о запросе.
     * @param response Объект HttpServletResponse, используемый для формирования ответа.
     * @throws ServletException В случае обработки ошибок сервлета.
     * @throws IOException      В случае ошибок ввода-вывода.
     */
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
            orderDetailDao.updateOrderDetail(orderDetailDTO);
        } catch (JsonSyntaxException e) {
            // Устанавливаем ответ 400 (Неверный запрос) в случае ошибки синтаксиса JSON
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        } catch (Exception e) {
            // Логируем ошибки, если они произошли
            e.printStackTrace();
        }
    }

    /**
     * Обрабатывает HTTP DELETE запросы.
     *
     * <p>Удаляет деталь заказа, полученную из тела запроса в формате JSON.</p>
     *
     * @param req  Объект HttpServletRequest, содержащий информацию о запросе.
     * @param resp Объект HttpServletResponse, используемый для формирования ответа.
     * @throws IOException В случае ошибок ввода-вывода.
     */
    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            // Устанавливаем кодировку для запроса
            req.setCharacterEncoding(CHARSET_UTF8);
            resp.setCharacterEncoding(CHARSET_UTF8);

            // Читаем тело запроса с входящими данными JSON
            BufferedReader reader = new BufferedReader(new InputStreamReader(req.getInputStream(), StandardCharsets.UTF_8));
            String json = reader.lines().collect(Collectors.joining());

            // Преобразуем JSON в объект OrderDetailDTO
            OrderDetailDTO orderDetailDTO = gson.fromJson(json, OrderDetailDTO.class);

            // Удаляем деталь заказа из базы данных
            orderDetailDao.deleteOrderDetail(orderDetailDTO);
        } catch (DatabaseConnectionException e) {
            // Логируем ошибки, связанные с подключением к базе данных
            e.printStackTrace();
        } catch (JsonSyntaxException e) {
            // Устанавливаем ответ 400 (Неверный запрос) в случае ошибки синтаксиса JSON
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        } catch (IOException e) {
            // Обрабатываем IOException отдельно и устанавливаем ответ 400 (Неверный запрос)
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            e.printStackTrace();
        }
    }
}


