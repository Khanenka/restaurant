package com.khanenka.restapiservlet.repository;

import com.khanenka.restapiservlet.model.productDTO.OrderDetailDTO;

import java.sql.SQLException;
import java.util.List;

public interface OrderDetailDao {
    void addOrderDetail(OrderDetailDTO orderDetail) throws Exception;
    List<OrderDetailDTO> getAllOrderDetails() throws SQLException;
    void updateOrderDetail(OrderDetailDTO orderDetail) throws SQLException;
    void deleteOrderDetail(OrderDetailDTO orderDetail) throws Exception;
}
