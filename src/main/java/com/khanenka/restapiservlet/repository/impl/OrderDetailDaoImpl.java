//package com.khanenka.restapiservlet.repository.impl;
//
//import com.khanenka.restapiservlet.exception.DatabaseConnectionException;
//import com.khanenka.restapiservlet.model.OrderStatus;
//import com.khanenka.restapiservlet.model.productdto.OrderDetailDTO;
//import com.khanenka.restapiservlet.model.productdto.ProductDTO;
//import com.khanenka.restapiservlet.repository.OrderDetailDao;
//import com.khanenka.restapiservlet.util.DBConnection;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import java.sql.*;
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * Реализация интерфейса {@link OrderDetailDao} для работы с деталями заказа в базе данных.
// * <p>
// * Данный класс предоставляет методы для добавления, обновления, удаления и получения деталей заказов и связанных с ними продуктов.
// *
// * <p>Класс использует JDBC для взаимодействия с базой данных и включает в себя
// * следующие основные SQL запросы:</p>
// * <ul>
// *   <li>INSERT, UPDATE, DELETE для таблицы {@code order_detail}</li>
// *   <li>INSERT, DELETE для связующей таблицы {@code order_detail_product}</li>
// *   <li>SELECT для получения данных о деталях заказа и связанных продуктах</li>
// * </ul>
// *
// * @author Khanenka
// */
//public class OrderDetailDaoImpl implements OrderDetailDao {
//    // Константы SQL запросов
//    public static final String QUERY_INSERT_ORDER_DETAIL = "INSERT INTO order_detail (order_detail_id,order_status, total_amount) VALUES (?,?,?)";
//    public static final String QUERY_SELECT_ALL_ORDER_DETAIL = "SELECT DISTINCT * FROM order_detail";
//    public static final String UPDATE_ORDER_DETAIL_SQL = "UPDATE order_detail set order_status = ?, total_amount = ? WHERE order_detail_id = ?";
//    public static final String QUERY_DELETE_ORDER_DETAIL = "DELETE FROM order_detail WHERE order_detail_id = ?";
//    public static final String QUERY_SELECT_PRODUCTS_BY_ORDER_DETAIL_ID = "SELECT p.* FROM product p JOIN order_detail_product odp ON p.idproduct= odp.product_id WHERE odp.order_detail_id = ?";
//    public static final String QUERY_INSERT_ORDER_DETAIL_PRODUCT = "INSERT INTO order_detail_product (order_detail_id, product_id) VALUES (?,?)";
//    public static final String QUERY_DELETE_ORDER_DETAIL_PRODUCT_BY_ORDER_DETAIL_ID = "DELETE FROM order_detail_product WHERE order_detail_id = ?";
//    public static final String QUERY_CREATE_TABLE_ORDER_DETAIL = "CREATE TABLE if not exists order_detail (order_detail_id  INT , order_status character varying(50) ,total_amount numeric(10,2))";
//    public static final String QUERY_CREATE_TABLE_ORDER_DETAIL_PRODUCT = "create table  order_detail_product (order_detail_id INT,  product_id INT)";
//
//    public static final String ORDER_STATUS = "order_status";
//    public static final String TOTAL_AMOUNT = "total_amount";
//
//    Connection connection = DBConnection.getConnection();
//    Logger logger = LoggerFactory.getLogger(OrderDetailDaoImpl.class);
//
//    /**
//     * Конструктор по умолчанию.
//     */
//    public OrderDetailDaoImpl() {
//
//    }
//
//    /**
//     * Конструктор, позволяющий установить соединение с базой данных.
//     *
//     * @param connection соединение с базой данных
//     */
//    public OrderDetailDaoImpl(Connection connection) {
//        this.connection = connection;
//    }
//
//    /**
//     * Создает таблицу для деталей заказов в базе данных.
//     * <p>
//     * Если таблица уже существует, она не будет создаваться повторно.
//     */
//    @Override
//    public void createOrderDetailTable() {
//
//
//        try
//                (Statement statement = connection.createStatement()) {
//            statement.executeUpdate(QUERY_CREATE_TABLE_ORDER_DETAIL);
//            logger.info("Order Detail table created successfully");
//        } catch (SQLException e) {
//            logger.info("Error creating order detail table: ");
//        }
//    }
//
//    /**
//     * Создает таблицу для связи деталей заказов с продуктами в базе данных.
//     * <p>
//     * Если таблица уже существует, она не будет создаваться повторно.
//     */
//    @Override
//    public void createOrderDetailProductTable() {
//
//        try
//                (Statement statement = connection.createStatement()) {
//            statement.executeUpdate(QUERY_CREATE_TABLE_ORDER_DETAIL_PRODUCT);
//            logger.info("Order Detail With Product table created successfully");
//        } catch (SQLException e) {
//            logger.info("Error creating order detail with product table: ");
//        }
//    }
//
//    /**
//     * Добавляет детали заказа в базу данных.
//     *
//     * @param orderDetail объект DTO, содержащий информацию о деталях заказа, которые нужно сохранить
//     * @throws DatabaseConnectionException если возникла ошибка при сохранении данных в базу данных
//     */
//    @Override
//    public void addOrderDetail(OrderDetailDTO orderDetail) {
//        try {
//            connection.setAutoCommit(false); // Отключаем автоматическое подтверждение транзакций
//            // Подготовка SQL-запроса для вставки деталей заказа
//            try (PreparedStatement orderDetailStatement = connection.prepareStatement(QUERY_INSERT_ORDER_DETAIL)) {
//                // Отключаем автоматическую фиксацию транзакции
//
//
//                // Установка параметров для запроса вставки деталей заказа
//                orderDetailStatement.setLong(1, orderDetail.getIdOrderDetail()); // ID деталей заказа
//                orderDetailStatement.setString(2, String.valueOf(orderDetail.getOrderStatus())); // Статус заказа
//                orderDetailStatement.setBigDecimal(3, orderDetail.getTotalAmauntOrderDetail()); // Общая сумма деталей заказа
//
//                // Проверяем, есть ли  продукт
//                if (orderDetail.getProducts() != null && !orderDetail.getProducts().isEmpty()) {
//                    // Подготовка SQL-запроса для вставки продуктов в детали заказа
//                    try (PreparedStatement productOrderDetailStatement = connection.prepareStatement(QUERY_INSERT_ORDER_DETAIL_PRODUCT)) {
//                        // Проходимся по списку продуктов в деталях заказа
//                        for (ProductDTO product : orderDetail.getProducts()) {
//                            productOrderDetailStatement.setLong(1, orderDetail.getIdOrderDetail()); // Установка ID деталей заказа
//                            productOrderDetailStatement.setLong(2, product.getIdProduct()); // Установка ID продукта
//                            productOrderDetailStatement.addBatch(); // Добавление в пакет
//                        }
//
//                        // Выполнение запроса для вставки деталей заказа
//                        orderDetailStatement.executeUpdate();
//                        // Выполнение пакетной вставки для всех продуктов
//                        productOrderDetailStatement.executeBatch();
//                    }
//                } else {
//                    logger.info("Product  null"); // Сообщение, если продуктов нет
//                    connection.commit(); // Подтверждение транзакции
//                }
//                // Подтверждение транзакции
//                connection.commit();
//            }
//        } catch (SQLException e) {
//            throw new DatabaseConnectionException("Failed to add order"); // Исключение при ошибке
//        }
//    }
//
//
//    /**
//     * Получает все детали заказов из базы данных.
//     *
//     * @return список объектов {@link OrderDetailDTO} с деталями заказов
//     */
//    @Override
//    public List<OrderDetailDTO> getAllOrderDetails() {
//        // Создаем список для хранения деталей заказов
//        List<OrderDetailDTO> orderDetailList = new ArrayList<>();
//        ResultSet resultSet;
//        // Подготавливаем запрос к базе данных для выборки всех деталей заказов
//        try (PreparedStatement statement = connection.prepareStatement(QUERY_SELECT_ALL_ORDER_DETAIL)) {
//            resultSet = statement.executeQuery();
//            // Обрабатываем каждый результат из выборки
//            while (resultSet.next()) {
//                OrderDetailDTO orderDetail = new OrderDetailDTO();
//                // Устанавливаем ID детали заказа
//                orderDetail.setIdOrderDetail(resultSet.getLong("order_detail_id"));
//                // Устанавливаем статус заказа, преобразуя строку в перечисление OrderStatus
//                orderDetail.setOrderStatus(OrderStatus.valueOf(resultSet.getString(ORDER_STATUS)));
//                // Устанавливаем общую сумму детали заказа
//                orderDetail.setTotalAmauntOrderDetail(resultSet.getBigDecimal(TOTAL_AMOUNT));
//                // Получаем продукты, связанные с данной деталью заказа
//                ResultSet productResultSet;
//                // Подготавливаем запрос к базе данных для выборки продуктов по ID детали заказа
//                try (PreparedStatement productStatement = connection.prepareStatement(QUERY_SELECT_PRODUCTS_BY_ORDER_DETAIL_ID)) {
//                    productStatement.setLong(1, orderDetail.getIdOrderDetail()); // Устанавливаем параметр запроса
//                    productResultSet = productStatement.executeQuery();
//
//                    // Создаем список для хранения продуктов
//                    List<ProductDTO> products = new ArrayList<>();
//                    // Обрабатываем каждый результат из выборки продуктов
//                    while (productResultSet.next()) {
//                        ProductDTO product = new ProductDTO();
//                        // Устанавливаем ID продукта
//                        product.setIdProduct(productResultSet.getLong("idproduct"));
//                        // Устанавливаем название продукта
//                        product.setNameProduct(productResultSet.getString("nameproduct"));
//                        // Устанавливаем цену продукта
//                        product.setPriceProduct(productResultSet.getBigDecimal("priceproduct"));
//                        // Добавляем созданный продукт в список
//                        products.add(product);
//                    }
//
//                    // Устанавливаем список продуктов в деталь заказа
//                    orderDetail.setProducts(products);
//                    // Добавляем деталь заказа в общий список
//                    orderDetailList.add(orderDetail);
//                }
//            }
//
//        } catch (SQLException e) {
//            throw new DatabaseConnectionException("Failed to get all order details"); // Исключение при ошибке
//        }
//        // Возвращаем список деталей заказов
//        return orderDetailList;
//    }
//
//    /**
//     * Обновляет информацию о деталях заказа в базе данных.
//     *
//     * @param orderDetail объект DTO, содержащий обновленные данные о деталях заказа
//     * @throws SQLException если произошла ошибка при выполнении SQL-запроса
//     */
//    @Override
//    public void updateOrderDetail(OrderDetailDTO orderDetail) throws SQLException {
//        // Устанавливаем режим автоматической фиксации транзакций в false
//        connection.setAutoCommit(false);
//
//        try (PreparedStatement updateStatement = connection.prepareStatement(UPDATE_ORDER_DETAIL_SQL)) {
//            // Устанавливаем параметры для обновления информации о заказе
//            updateStatement.setString(1, orderDetail.getOrderStatus().name()); // Статус заказа
//            updateStatement.setBigDecimal(2, orderDetail.getTotalAmauntOrderDetail()); // Общая сумма заказа
//            updateStatement.setLong(3, orderDetail.getIdOrderDetail()); // ID деталей заказа
//
//            // Выполняем обновление и получаем количество обновлённых строк
//            int rowsUpdated = updateStatement.executeUpdate();
//
//            // Если обновление успешно (строки обновлены)
//            if (rowsUpdated > 0) {
//                // Подготавливаем SQL запрос для удаления ранее связанных продуктов по ID детали заказа
//                try (PreparedStatement deleteProductsStatement = connection.prepareStatement(QUERY_DELETE_ORDER_DETAIL_PRODUCT_BY_ORDER_DETAIL_ID)) {
//                    deleteProductsStatement.setLong(1, orderDetail.getIdOrderDetail()); // Устанавливаем ID детали заказа
//                    deleteProductsStatement.executeUpdate(); // Удаляем продукты
//
//                    // Подготавливаем SQL запрос для вставки новых продуктов
//                    try (PreparedStatement insertProductsStatement = connection.prepareStatement(QUERY_INSERT_ORDER_DETAIL_PRODUCT)) {
//                        for (ProductDTO product : orderDetail.getProducts()) { // Проходим по каждому продукту в заказе
//                            insertProductsStatement.setLong(1, orderDetail.getIdOrderDetail()); // Устанавливаем ID детали заказа
//                            insertProductsStatement.setLong(2, product.getIdProduct()); // Устанавливаем ID продукта
//                            insertProductsStatement.addBatch(); // Добавляем к пакету
//                        }
//                        insertProductsStatement.executeBatch(); // Выполняем пакет вставок
//                        connection.commit(); // Фиксируем транзакцию
//                    }
//                }
//            } else {
//                // Если обновление не удалось, выбрасываем исключение
//                throw new SQLException("Failed to update order detail");
//            }
//        }
//    }
//
//    /**
//     * Удаляет детали заказа из базы данных.
//     *
//     * @param orderDetail объект DTO, содержащий данные о деталях заказа для удаления
//     * @throws DatabaseConnectionException если возникла ошибка при удалении данных из базы данных
//     */
//    @Override
//    public void deleteOrderDetail(OrderDetailDTO orderDetail) {
//        try {
//            // Устанавливаем режим авто-коммита в false для управления транзакциями вручную
//            connection.setAutoCommit(false);
//
//            // Подготовка SQL-запроса для удаления записи из таблицы OrderDetail
//            try (PreparedStatement deleteStatement = connection.prepareStatement(QUERY_DELETE_ORDER_DETAIL)) {
//                // Установка значения параметра запроса (ID детали заказа)
//                deleteStatement.setLong(1, orderDetail.getIdOrderDetail());
//                // Выполнение обновления (удаление записи)
//                deleteStatement.executeUpdate();
//
//                // Подготовка SQL-запроса для удаления связанных продуктов по ID детали заказа
//                try (PreparedStatement deleteOrderDetailProductStatement = connection.prepareStatement(QUERY_DELETE_ORDER_DETAIL_PRODUCT_BY_ORDER_DETAIL_ID)) {
//                    // Установка значения параметра запроса (ID детали заказа)
//                    deleteOrderDetailProductStatement.setLong(1, orderDetail.getIdOrderDetail());
//                    // Выполнение обновления (удаление связанных продуктов)
//                    deleteOrderDetailProductStatement.executeUpdate();
//
//                    // Подтверждение транзакции
//                    connection.commit();
//                }
//            }
//        } catch (SQLException e) {
//            // В случае ошибки, откатить транзакцию и выбросить кастомное исключение
//            throw new DatabaseConnectionException(e.getMessage());
//        }
//    }
//}