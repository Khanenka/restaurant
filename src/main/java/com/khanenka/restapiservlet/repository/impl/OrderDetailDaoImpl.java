package com.khanenka.restapiservlet.repository.impl;

import com.khanenka.restapiservlet.DBConnection;
import com.khanenka.restapiservlet.exception.DatabaseConnectionException;
import com.khanenka.restapiservlet.model.OrderStatus;
import com.khanenka.restapiservlet.model.productDTO.OrderDetailDTO;
import com.khanenka.restapiservlet.model.productDTO.ProductDTO;
import com.khanenka.restapiservlet.repository.OrderDetailDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OrderDetailDaoImpl implements OrderDetailDao {
    static final String QUERY_INSERT_ORDER_DETAIL = "INSERT INTO order_detail (id,order_status, total_amount) VALUES (?,?, ?)";
    static final String QUERY_SELECT_ALL_ORDER_DETAIL = "SELECT * FROM order_detail";
    static final String UPDATE_ORDER_DETAIL_SQL = "UPDATE order_detail set order_status = ?, total_amount = ? WHERE id = ?";
    static final String QUERY_DELETE_ORDER_DETAIL = "DELETE FROM order_detail WHERE id = ?";
    static final String QUERY_SELECT_PRODUCTS_BY_ORDER_DETAIL_ID = "SELECT p.* FROM product p JOIN order_detail_product odp ON p.idproduct= odp.product_id WHERE odp.order_detail_id = ?";
    static final String QUERY_INSERT_ORDER_DETAIL_PRODUCT = "INSERT INTO order_detail_product (order_detail_id, product_id) VALUES (?, ?)";
    static final String QUERY_JOIN_ORDER_DETAIL_PRODUCT_BY_ID = "SELECT odp.* FROM order_detail_product odp WHERE odp.order_detail_id = ?";
    static final String QUERY_DELETE_ORDER_DETAIL_PRODUCT_BY_ORDER_DETAIL_ID = "DELETE FROM order_detail_product WHERE order_detail_id = ?";


    static final String ORDER_STATUS = "order_status";
    static final String TOTAL_AMOUNT = "total_amount";

    Connection connection = DBConnection.getConnection();
    Logger logger = LoggerFactory.getLogger(OrderDetailDaoImpl.class);

    @Override
    public void addOrderDetail(OrderDetailDTO orderDetail) throws Exception {
        connection = DBConnection.getConnection();
        connection.setAutoCommit(false);

        try (PreparedStatement orderDetailStatement = connection.prepareStatement(QUERY_INSERT_ORDER_DETAIL)) {
            orderDetailStatement.setLong(1,orderDetail.getIdOrderDetail());
            orderDetailStatement.setString(2, orderDetail.getOrderStatus().name());
            orderDetailStatement.setBigDecimal(3, orderDetail.getTotalAmauntOrderDetail());
            orderDetailStatement.executeUpdate();

            try (PreparedStatement productOrderDetailStatement = connection.prepareStatement(QUERY_INSERT_ORDER_DETAIL_PRODUCT)) {
                for (ProductDTO product : orderDetail.getProducts()) {
                    productOrderDetailStatement.setLong(1, orderDetail.getIdOrderDetail());
                    productOrderDetailStatement.setLong(2, product.getIdProduct());
                    productOrderDetailStatement.addBatch();
                }

                productOrderDetailStatement.executeBatch();
                connection.commit();
            }
        } catch (SQLException e) {
            throw new DatabaseConnectionException("Failed to connect to the database");
        }
    }

    public List<OrderDetailDTO> getAllOrderDetails() throws SQLException {
        List<OrderDetailDTO> orderDetailList = new ArrayList<>();
        connection = DBConnection.getConnection();

        ResultSet resultSet;
        try (PreparedStatement statement = connection.prepareStatement(QUERY_SELECT_ALL_ORDER_DETAIL)) {
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                OrderDetailDTO orderDetail = new OrderDetailDTO();
                orderDetail.setIdOrderDetail(resultSet.getLong("id"));
                orderDetail.setOrderStatus(OrderStatus.valueOf(resultSet.getString(ORDER_STATUS)));
                orderDetail.setTotalAmauntOrderDetail(resultSet.getBigDecimal(TOTAL_AMOUNT));

                // Get products
                ResultSet productResultSet;
                try (PreparedStatement productStatement = connection.prepareStatement(QUERY_SELECT_PRODUCTS_BY_ORDER_DETAIL_ID)) {
                    productStatement.setLong(1, orderDetail.getIdOrderDetail());
                    productResultSet = productStatement.executeQuery();

                    List<ProductDTO> products = new ArrayList<>();
                    while (productResultSet.next()) {
                        ProductDTO product = new ProductDTO();
                        product.setIdProduct(productResultSet.getLong("idproduct"));
                        product.setNameProduct(productResultSet.getString("nameProduct"));
                        product.setPriceProduct(productResultSet.getBigDecimal("priceProduct"));
                        products.add(product);
                    }

                    orderDetail.setProducts(products);
                }
                orderDetailList.add(orderDetail);
            }
        }
        return orderDetailList;
    }

    @Override
    public void updateOrderDetail(OrderDetailDTO orderDetail) throws SQLException {
        connection = DBConnection.getConnection();
        connection.setAutoCommit(false);

        try (PreparedStatement updateStatement = connection.prepareStatement(UPDATE_ORDER_DETAIL_SQL)) {
            updateStatement.setString(1, orderDetail.getOrderStatus().name());
            updateStatement.setBigDecimal(2, orderDetail.getTotalAmauntOrderDetail());
            updateStatement.setLong(3, orderDetail.getIdOrderDetail());
            int rowsUpdated = updateStatement.executeUpdate();

            if (rowsUpdated > 0) {
                try (PreparedStatement deleteProductsStatement = connection.prepareStatement(QUERY_DELETE_ORDER_DETAIL_PRODUCT_BY_ORDER_DETAIL_ID)) {
                    deleteProductsStatement.setLong(1, orderDetail.getIdOrderDetail());
                    deleteProductsStatement.executeUpdate();

                    try (PreparedStatement insertProductsStatement = connection.prepareStatement(QUERY_INSERT_ORDER_DETAIL_PRODUCT)) {
                        for (ProductDTO product : orderDetail.getProducts()) {
                            insertProductsStatement.setLong(1, orderDetail.getIdOrderDetail());
                            insertProductsStatement.setLong(2, product.getIdProduct());
                            insertProductsStatement.addBatch();
                        }
                        insertProductsStatement.executeBatch();
                        connection.commit();
                    }
                }
            } else {
                throw new SQLException("Failed to update order detail");
            }
        }
    }

    @Override
    public void deleteOrderDetail(OrderDetailDTO orderDetail) throws Exception {
        connection = DBConnection.getConnection();
        connection.setAutoCommit(false);

        PreparedStatement deleteStatement = connection.prepareStatement(QUERY_DELETE_ORDER_DETAIL);
        deleteStatement.setLong(1, orderDetail.getIdOrderDetail());
        deleteStatement.executeUpdate();

        PreparedStatement deleteOrderDetailProductStatement = connection.prepareStatement(QUERY_DELETE_ORDER_DETAIL_PRODUCT_BY_ORDER_DETAIL_ID);
        deleteOrderDetailProductStatement.setLong(1, orderDetail.getIdOrderDetail());
        deleteOrderDetailProductStatement.executeUpdate();

        connection.commit();
    }
}