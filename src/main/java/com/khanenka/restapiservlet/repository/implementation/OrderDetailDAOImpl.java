package com.khanenka.restapiservlet.repository.implementation;


import com.khanenka.restapiservlet.exception.DatabaseConnectionException;
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

import static com.khanenka.restapiservlet.repository.impl.OrderDetailDaoImpl.*;
import static com.khanenka.restapiservlet.repository.impl.ProductDaoImpl.QUERY_INSERT_PRODUCT;

public class OrderDetailDAOImpl implements OrderDetailDao {
    private Connection connection = DBConnection.getConnection();
    Logger logger = LoggerFactory.getLogger(OrderDetailDAOImpl.class);
    public static final String QUERY_INSERT_PRODUCT_WITH_ID = "INSERT INTO product (idproduct,nameproduct, priceproduct) VALUES (?,?,?)";

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
            connection.setAutoCommit(false); // Отключаем автоматическое подтверждение транзакций
            // Подготовка SQL-запроса для вставки деталей заказа
            try (PreparedStatement orderDetailStatement = connection.prepareStatement(QUERY_INSERT_ORDER_DETAIL)) {
                // Отключаем автоматическую фиксацию транзакции


                // Установка параметров для запроса вставки деталей заказа
                orderDetailStatement.setLong(1, orderDetail.getIdOrderDetail()); // ID деталей заказа
                orderDetailStatement.setString(2, String.valueOf(orderDetail.getOrderStatus())); // Статус заказа
                orderDetailStatement.setBigDecimal(3, orderDetail.getTotalAmauntOrderDetail()); // Общая сумма деталей заказа
                orderDetailStatement.executeUpdate();
                // Проверяем, есть ли  продукт

                // Подтверждение транзакции
                connection.commit();
            }
        } catch (SQLException e) {
            throw new DatabaseConnectionException("Failed to add order"); // Исключение при ошибке
        }
    }

    public void addOrderDetailProduct(OrderDetailDTO orderDetail) {
        try (PreparedStatement productOrderDetailStatement = connection.prepareStatement(QUERY_INSERT_ORDER_DETAIL_PRODUCT)) {
            // Проходимся по списку продуктов в деталях заказа
            for (ProductDTO product : orderDetail.getProducts()) {
                productOrderDetailStatement.setLong(1, orderDetail.getIdOrderDetail()); // Установка ID деталей заказа
                productOrderDetailStatement.setLong(2, product.getIdProduct()); // Установка ID продукта
                productOrderDetailStatement.addBatch(); // Добавление в пакет
            }
            productOrderDetailStatement.executeBatch();
        } catch (SQLException e) {
            throw new DatabaseConnectionException("Error add order detail");
        }
    }
    public void addProduct(ProductDTO product) throws SQLException{
        connection.setAutoCommit(false); // Отключаем автоматическое подтверждение транзакций
        try (PreparedStatement productStatement = connection.prepareStatement(QUERY_INSERT_PRODUCT_WITH_ID)) {
            productStatement.setLong(1,product.getIdProduct());
            productStatement.setString(2, product.getNameProduct());
            productStatement.setBigDecimal(3, product.getPriceProduct());
            productStatement.executeUpdate(); // Выполнение запроса на добавление продукта

            connection.commit(); // Подтверждение транзакции
        } catch (SQLException e) {
            connection.rollback(); // Откат транзакции в случае ошибки
            throw e;
        }

    }


    @Override
    public List<OrderDetailDTO> getAllOrderDetails() throws SQLException {
        List<OrderDetailDTO> orderDetailList = new ArrayList<>();
        ResultSet resultSet;
        // Подготавливаем запрос к базе данных для выборки всех деталей заказов
        try {
            PreparedStatement statement = connection.prepareStatement(QUERY_SELECT_ALL_ORDER_DETAIL);
            resultSet = statement.executeQuery();
            // Обрабатываем каждый результат из выборки
            while (resultSet.next()) {
                OrderDetailDTO orderDetail = new OrderDetailDTO();
                // Устанавливаем ID детали заказа
                orderDetail.setIdOrderDetail(resultSet.getLong("order_detail_id"));
                // Устанавливаем статус заказа, преобразуя строку в перечисление OrderStatus
                orderDetail.setOrderStatus(OrderStatus.valueOf(resultSet.getString(ORDER_STATUS)));
                // Устанавливаем общую сумму детали заказа
                orderDetail.setTotalAmauntOrderDetail(resultSet.getBigDecimal(TOTAL_AMOUNT));
                // Получаем продукты, связанные с данной деталью заказа
                ResultSet productResultSet = joinProductOrderDetailById(orderDetail.getIdOrderDetail());
                List<ProductDTO> products = new ArrayList<>();
                // Обрабатываем каждый результат из выборки продуктов
                while (productResultSet.next()) {
                    ProductDTO product = new ProductDTO();
                    // Устанавливаем ID продукта
                    product.setIdProduct(productResultSet.getLong("idproduct"));
                    // Устанавливаем название продукта
                    product.setNameProduct(productResultSet.getString("nameproduct"));
                    // Устанавливаем цену продукта
                    product.setPriceProduct(productResultSet.getBigDecimal("priceproduct"));
                    // Добавляем созданный продукт в список
                    products.add(product);
                }
                orderDetail.setProducts(products);
                // Добавляем деталь заказа в общий список
                orderDetailList.add(orderDetail);
            }
        } catch (SQLException | IllegalArgumentException e) {
            throw new DatabaseConnectionException("Error get all order details");
        }
        // Возвращаем список деталей заказов
        return orderDetailList;
    }
    public ResultSet joinProductOrderDetailById(Long idOrderDetail) {
        ResultSet productResultSet;
        try {
            PreparedStatement productStatement =
                     connection.prepareStatement(QUERY_SELECT_PRODUCTS_BY_ORDER_DETAIL_ID);
            productStatement.setLong(1, idOrderDetail); // Устанавливаем параметр запроса
            productResultSet = productStatement.executeQuery();
        } catch (SQLException e) {
            throw new DatabaseConnectionException("Error in joining order details with product");
        }
        return productResultSet;
    }
    @Override
    public void updateOrderDetail(OrderDetailDTO orderDetail) {
        try {
            connection.setAutoCommit(false);

            try (PreparedStatement updateStatement = connection.prepareStatement(UPDATE_ORDER_DETAIL_SQL)) {
                // Устанавливаем параметры для обновления информации о заказе
                updateStatement.setString(1, orderDetail.getOrderStatus().name()); // Статус заказа
                updateStatement.setBigDecimal(2, orderDetail.getTotalAmauntOrderDetail()); // Общая сумма заказа
                updateStatement.setLong(3, orderDetail.getIdOrderDetail()); // ID деталей заказа
                 updateStatement.executeUpdate();
                 connection.commit();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void deleteOrderDetailProductById(long id) {
        try (PreparedStatement deleteProductsStatement = connection.prepareStatement(QUERY_DELETE_ORDER_DETAIL_PRODUCT_BY_ORDER_DETAIL_ID)) {
            deleteProductsStatement.setLong(1, id); // Устанавливаем ID детали заказа
            deleteProductsStatement.executeUpdate(); // Удаляем продукты
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
        @Override
    public void deleteOrderDetail(OrderDetailDTO orderDetail){

    }
}
