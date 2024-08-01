package com.khanenka.restapiservlet.repository.implementation;


import com.khanenka.restapiservlet.exceptions.DatabaseConnectionException;
import com.khanenka.restapiservlet.model.OrderStatus;
import com.khanenka.restapiservlet.model.productdto.OrderDetailDTO;
import com.khanenka.restapiservlet.model.productdto.ProductDTO;
import com.khanenka.restapiservlet.repository.OrderDetailDao;
import com.khanenka.restapiservlet.util.DBConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static com.khanenka.restapiservlet.util.QueryInDB.*;


public class OrderDetailDAOImpl implements OrderDetailDao {

    private Connection connection = DBConnection.getConnection();
    Logger logger = LoggerFactory.getLogger(OrderDetailDAOImpl.class);

    public OrderDetailDAOImpl() {
    }

    public OrderDetailDAOImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void createOrderDetailTable() {
        try
                (Statement statement = connection.createStatement()) {
            statement.executeUpdate(QUERY_CREATE_TABLE_ORDER_DETAIL);
        } catch (SQLException e) {
            throw new DatabaseConnectionException("Error creating order detail table: " + e);
        }
    }

    @Override
    public void createOrderDetailProductTable() {
        try
                (Statement statement = connection.createStatement()) {
            statement.executeUpdate(QUERY_CREATE_TABLE_ORDER_DETAIL_PRODUCT);
            logger.info("Order Detail With Product table created successfully");
        } catch (SQLException e) {
            logger.info("Error creating order detail with product table: ");
            throw new DatabaseConnectionException("Error creating order detail with product table: " + e);
        }
    }

    @Override
    public void addOrderDetail(OrderDetailDTO orderDetail) {
        try {
            connection.setAutoCommit(false);
            try (PreparedStatement orderDetailStatement = connection.prepareStatement(QUERY_INSERT_ORDER_DETAIL)) {
                orderDetailStatement.setLong(1, orderDetail.getIdOrderDetail());
                orderDetailStatement.setString(2, String.valueOf(orderDetail.getOrderStatus()));
                orderDetailStatement.setBigDecimal(3, orderDetail.getTotalAmauntOrderDetail());
                orderDetailStatement.executeUpdate();
                connection.commit();
            }
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                throw new DatabaseConnectionException("error in add order detail");
            }
            throw new DatabaseConnectionException("Failed to add order");
        }
    }

    @Override
    public void addOrderDetailProduct(OrderDetailDTO orderDetail) {
        try {
            connection.setAutoCommit(false);
            try (PreparedStatement productOrderDetailStatement = connection.prepareStatement(
                    QUERY_INSERT_ORDER_DETAIL_PRODUCT)) {
                // Проходимся по списку продуктов в деталях заказа
                for (ProductDTO product : orderDetail.getProducts()) {
                    productOrderDetailStatement.setLong(1, orderDetail.getIdOrderDetail());
                    productOrderDetailStatement.setLong(2, product.getIdProduct());
                    productOrderDetailStatement.addBatch();
                }
                productOrderDetailStatement.executeBatch();
                connection.commit();
            }
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                throw new DatabaseConnectionException("Error add order detail");
            }
            throw new DatabaseConnectionException("Error add order detail");
        }
    }



    @Override
    public List<OrderDetailDTO> getAllOrderDetails() {
        List<OrderDetailDTO> orderDetailList = new ArrayList<>();
        try
                (PreparedStatement statement = connection.prepareStatement(QUERY_SELECT_ALL_ORDER_DETAIL)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                OrderDetailDTO orderDetail = new OrderDetailDTO();
                orderDetail.setIdOrderDetail(resultSet.getLong("order_detail_id"));
                orderDetail.setOrderStatus(OrderStatus.valueOf(resultSet.getString(ORDER_STATUS)));
                orderDetail.setTotalAmauntOrderDetail(resultSet.getBigDecimal(TOTAL_AMOUNT));
                List<ProductDTO> products = joinProductOrderDetailById(orderDetail.getIdOrderDetail());
                orderDetail.setProducts(products);
                orderDetailList.add(orderDetail);
            }

            return orderDetailList;
        } catch (SQLException e) {
            throw new DatabaseConnectionException("error in get order detail");
        }
    }
@Override
    public List<ProductDTO> joinProductOrderDetailById(Long idOrderDetail) {
        ResultSet productResultSet;
        List<ProductDTO> products = new ArrayList<>();
        try
                (PreparedStatement productStatement =
                         connection.prepareStatement(QUERY_SELECT_PRODUCTS_BY_ORDER_DETAIL_ID)) {
            productStatement.setLong(1, idOrderDetail);
            productResultSet = productStatement.executeQuery();

            while (productResultSet.next()) {
                ProductDTO product = new ProductDTO();
                product.setIdProduct(productResultSet.getLong("idproduct"));
                product.setNameProduct(productResultSet.getString("nameproduct"));
                product.setPriceProduct(productResultSet.getBigDecimal("priceproduct"));
                products.add(product);
            }
        } catch (SQLException e) {
            throw new DatabaseConnectionException("Error in joining order details with product");
        }
        return products;
    }

    @Override
    public void updateOrderDetail(OrderDetailDTO orderDetail) {
        try {
            connection.setAutoCommit(false);
            try (PreparedStatement updateStatement = connection.prepareStatement(UPDATE_ORDER_DETAIL_SQL)) {
                updateStatement.setString(1, orderDetail.getOrderStatus().name());
                updateStatement.setBigDecimal(2, orderDetail.getTotalAmauntOrderDetail());
                updateStatement.setLong(3, orderDetail.getIdOrderDetail());
                updateStatement.executeUpdate();
                connection.commit();
            }
        } catch (SQLException e) {
            throw new DatabaseConnectionException("error in update order detail");
        }
    }

    @Override
    public void deleteOrderDetailProductById(long id) {
        try (PreparedStatement deleteProductsStatement = connection.prepareStatement(
                QUERY_DELETE_ORDER_DETAIL_PRODUCT_BY_ORDER_DETAIL_ID)) {
            deleteProductsStatement.setLong(1, id);
            deleteProductsStatement.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            throw new DatabaseConnectionException("error in delete order detail product by id");
        }
    }

    @Override
    public void deleteOrderDetail(OrderDetailDTO orderDetail) {
        try {
            connection.setAutoCommit(false);
            try (PreparedStatement deleteStatement = connection.prepareStatement(QUERY_DELETE_ORDER_DETAIL)) {
                deleteStatement.setLong(1, orderDetail.getIdOrderDetail());
                deleteStatement.executeUpdate();
                connection.commit();
            }
        } catch (SQLException e) {
            throw new DatabaseConnectionException("error in delete order detail");
        }
    }
}
