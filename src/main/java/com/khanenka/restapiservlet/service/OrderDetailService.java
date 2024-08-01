package com.khanenka.restapiservlet.service;


import com.khanenka.restapiservlet.exceptions.DatabaseConnectionException;
import com.khanenka.restapiservlet.model.productdto.OrderDetailDTO;
import com.khanenka.restapiservlet.model.productdto.ProductDTO;
import com.khanenka.restapiservlet.repository.implementation.OrderDetailDAOImpl;
import com.khanenka.restapiservlet.repository.implementation.ProductDAOImpl;
import com.khanenka.restapiservlet.util.DBConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.util.List;


public class OrderDetailService {
    Connection connection= DBConnection.getConnection();
    OrderDetailDAOImpl orderDetailDAO;
    ProductDAOImpl productDAO;
    Logger logger = LoggerFactory.getLogger(OrderDetailService.class);

    public OrderDetailService() {
    }

    public OrderDetailService(OrderDetailDAOImpl orderDetailDAO, ProductDAOImpl productDAO) {
        this.orderDetailDAO = orderDetailDAO;
        this.productDAO = productDAO;
    }

    public void addOrderDetail(OrderDetailDTO orderDetail) {
                orderDetailDAO.addOrderDetail(orderDetail);
                orderDetailDAO.addOrderDetailProduct(orderDetail);
                for (ProductDTO product : orderDetail.getProducts()) {
                    productDAO.addProductById(product);
                }
    }

    public List<OrderDetailDTO> getOrderDetail() {
            return orderDetailDAO.getAllOrderDetails();
    }

    public void updateOrderDetail(OrderDetailDTO orderDetail) {
        try {
            orderDetailDAO.updateOrderDetail(orderDetail);
            orderDetailDAO.deleteOrderDetailProductById(orderDetail.getIdOrderDetail());
            orderDetailDAO.addOrderDetailProduct(orderDetail);
        } catch (IllegalArgumentException e) {
            throw new DatabaseConnectionException("Error updating order detail");
        }
    }

    public void deleteOrderDetail(OrderDetailDTO orderDetailDTO) {
        orderDetailDAO.deleteOrderDetail(orderDetailDTO);
        orderDetailDAO.deleteOrderDetailProductById(orderDetailDTO.getIdOrderDetail());
    }
}
