package com.khanenka.restapiservlet.repository;

import com.khanenka.restapiservlet.model.productdto.OrderDetailDTO;

import java.sql.SQLException;
import java.util.List;

/**
 * Интерфейс для доступа к данным заказа и деталей заказа.
 * <p>
 * Этот интерфейс определяет методы для работы с таблицей деталей заказов,
 * включая создание таблицы, добавление, получение, обновление и удаление
 * записей деталей заказов.
 *
 * @author Khanenka
 */
public interface OrderDetailDao {
    /**
     * Создает таблицу для хранения деталей заказов.
     */
    void createOrderDetailTable();

    /**
     * Создает таблицу для хранения продуктов деталей заказов.
     */
    void createOrderDetailProductTable();

    /**
     * Добавляет новую деталь заказа в базу данных.
     *
     * @param orderDetail объект, содержащий информацию о детали заказа
     */
    void addOrderDetail(OrderDetailDTO orderDetail);

    /**
     * Получает список всех деталей заказов из базы данных.
     *
     * @return список объектов OrderDetailDTO, представляющих детали заказов
     * @throws SQLException если возникает ошибка при доступе к базе данных
     */
    List<OrderDetailDTO> getAllOrderDetails() throws SQLException;

    /**
     * Обновляет существующую деталь заказа в базе данных.
     *
     * @param orderDetail объект, содержащий обновленную информацию о детали заказа
     * @throws SQLException если возникает ошибка при доступе к базе данных
     */
    void updateOrderDetail(OrderDetailDTO orderDetail) throws SQLException;

    /**
     * Удаляет деталь заказа из базы данных.
     *
     * @param orderDetail объект, представляющий деталь заказа, которую нужно удалить
     */
    void deleteOrderDetail(OrderDetailDTO orderDetail);
}
