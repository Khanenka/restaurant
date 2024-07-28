package com.khanenka.restapiservlet.service;

import com.khanenka.restapiservlet.exception.DatabaseConnectionException;
import com.khanenka.restapiservlet.model.productdto.OrderDetailDTO;
import com.khanenka.restapiservlet.model.productdto.ProductDTO;
import com.khanenka.restapiservlet.repository.implementation.OrderDetailDAOImpl;
import org.slf4j.LoggerFactory;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import static com.khanenka.restapiservlet.repository.impl.OrderDetailDaoImpl.QUERY_DELETE_ORDER_DETAIL_PRODUCT_BY_ORDER_DETAIL_ID;
import static com.khanenka.restapiservlet.repository.impl.OrderDetailDaoImpl.QUERY_INSERT_ORDER_DETAIL_PRODUCT;

public class OrderDetailService {
    static OrderDetailDAOImpl orderDetailDAO;
    org.slf4j.Logger logger = LoggerFactory.getLogger(OrderDetailService.class);

    public OrderDetailService(OrderDetailDAOImpl orderDetailDAO) {
        this.orderDetailDAO = orderDetailDAO;
    }

    public void addOrderDetail(OrderDetailDTO orderDetail) throws SQLException {
        if (orderDetail.getProducts() != null && !orderDetail.getProducts().isEmpty()) {
            // Подготовка SQL-запроса для вставки продуктов в детали заказа
            orderDetailDAO.addOrderDetail(orderDetail);
            orderDetailDAO.addOrderDetailProduct(orderDetail);
            for (ProductDTO product : orderDetail.getProducts()) {
                orderDetailDAO.addProduct(product);
            }
        } else {
            logger.info("Product null");
        }
    }

    public List<OrderDetailDTO> getOrderDetail() {
        try {
            return orderDetailDAO.getAllOrderDetails();
        } catch (SQLException e) {
            throw new DatabaseConnectionException("Error getting all order details");
        }
    }

    public void updateOrderDetail(OrderDetailDTO orderDetail) {
        try {


        orderDetailDAO.updateOrderDetail(orderDetail);
        orderDetailDAO.deleteOrderDetailProductById(orderDetail.getIdOrderDetail());
        orderDetailDAO.addOrderDetailProduct(orderDetail);
        }catch (IllegalArgumentException e){
            throw new DatabaseConnectionException("Error updating order detail");
        }
//        int rowsUpdated = updateStatement.executeUpdate();
//        if (rowsUpdated > 0) {


//        } else {
//            // Если обновление не удалось, выбрасываем исключение
//            throw new SQLException("Failed to update order detail");
//        }
    }
}
