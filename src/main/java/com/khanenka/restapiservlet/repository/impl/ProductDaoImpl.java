package com.khanenka.restapiservlet.repository.impl;

import com.khanenka.restapiservlet.exception.DatabaseConnectionException;
import com.khanenka.restapiservlet.model.CategoryType;
import com.khanenka.restapiservlet.model.productdto.ProductCategoryDTOByNameAndType;
import com.khanenka.restapiservlet.model.productdto.ProductDTOByNameAndPrice;
import com.khanenka.restapiservlet.repository.ProductDao;
import com.khanenka.restapiservlet.util.DBConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static com.khanenka.restapiservlet.repository.impl.ProductCategoryDaoImpl.QUERY_PRODUCT_CATEGORY_TABLE;

/**
 * Класс ProductDaoImpl реализует интерфейс ProductDao и предоставляет
 * основные операции для работы с продуктами в базе данных.
 *  @author Khanenka
 */
public class ProductDaoImpl implements ProductDao {
    // SQL-запросы для операций с продуктами
    static final String QUERY_CREATE_TABLE_PRODUCT = "create table if not exists product (idproduct SERIAL PRIMARY KEY,\n" +
            "nameproduct character varying(100),\n" +
            "priceproduct decimal(10,2),\n" +
            "quantityproduct INT,\n" +
            "availableproduct boolean)";

    static final String QUERY_INSERT_PRODUCT = "INSERT INTO product (nameproduct, priceproduct) VALUES (?,?)";
    static final String QUERY_SELECT_ALL_PRODUCT = "SELECT \"nameproduct\",\"priceproduct\",\"quantityproduct\",\"availableproduct\" FROM product";

    static final String UPDATE_PRODUCT_SQL = "UPDATE product set \"nameproduct\" = ?, \"priceproduct\" = ? WHERE \"nameproduct\" = ?;";
    static final String QUERY_DELETE_PRODUCT = "DELETE FROM product WHERE \"nameproduct\"=?";
    static final String QUERY_PRODUCT = "INSERT INTO product (\"nameproduct\", \"priceproduct\") VALUES ( ?,?)";
    static final String QUERY_INSERT_PRODUCT_PRODUCT_CATEGORY = "INSERT INTO product_productcategory (nameproduct, name) VALUES (?, ?)";
    static final String QUERY_INSERT_PRODUCT_CATEGORY = "INSERT INTO productcategory (\"name\", \"type\") VALUES ( ?, ?)";
    static final String QUERY_JOIN_PRODUCT_PRODUCT_CATEGORY_BY_NAME = "SELECT DISTINCT pc.name,pc.type FROM productcategory pc JOIN product_productcategory ppc ON pc.name = ppc.name WHERE ppc.nameproduct = ?";
    static final String QUERY_DELETE_PRODUCT_PRODUCT_CATEGORY_BY_PRODUCT_NAME = "DELETE FROM product_productcategory WHERE \"nameproduct\" = ?";
    static final String QUERY_CREATE_PRODUCT_PRODUCT_CATEGORY_TABLE = "create table if not exists product_productcategory (name character varying(100), nameproduct character varying(100))";

    static final String NAME_PRODUCT = "nameProduct"; // Имя продукта
    static final String PRICE_PRODUCT = "priceProduct"; // Цена продукта
    Connection connection = DBConnection.getConnection(); // Подключение к базе данных

    Logger logger = LoggerFactory.getLogger(ProductDaoImpl.class); // Логирование

    /**
     * Конструктор без параметров
     */
    // Конструктор без параметров
    public ProductDaoImpl() {
    }

    /**
     * Конструктор с параметром подключения
     * @param connection
     */
    // Конструктор с параметром подключения
    public ProductDaoImpl(Connection connection) {
        this.connection = connection;
    }
    /**
     * Создает таблицу продуктов в базе данных, если она не существует.
     *
     * @throws DatabaseConnectionException если не удалось создать таблицу продуктов.
     */
    @Override
    public void createTableProduct() {
        try (Statement statement = connection.createStatement()) {
            // Создание таблицы продуктов, если она не существует
            statement.executeUpdate(QUERY_CREATE_TABLE_PRODUCT);
            logger.info("Product table created successfully"); // Успешное сообщение в лог
        } catch (SQLException e) {
            throw new DatabaseConnectionException("Failed to create product" + e); // Исключение при ошибке
        }
    }
    /**
     * Создает таблицу категорий продуктов в базе данных.
     *
     * @throws DatabaseConnectionException если не удалось создать таблицу категорий продуктов.
     */
    @Override
    public void createProductCategoryTable() {
        try (Statement statement = connection.createStatement()) {
            // Создание таблицы категорий продуктов
            statement.executeUpdate(QUERY_PRODUCT_CATEGORY_TABLE);
            logger.info("Product category table created successfully"); // Успешное сообщение в лог
        } catch (SQLException e) {
            logger.info("Error creating product category table: "); // Сообщение об ошибке
        }
    }
    /**
     * Создает связь между продуктами и категориями продуктов в базе данных.
     *
     * @throws DatabaseConnectionException если не удалось создать таблицу "продукт-категория продукта".
     */
    @Override
    public void createProductProductCategory() {
        try (Statement statement = connection.createStatement()) {
            // Создание таблицы "продукт-категория продукта"
            statement.executeUpdate(QUERY_CREATE_PRODUCT_PRODUCT_CATEGORY_TABLE);
            logger.info("Product with Product Category table created successfully"); // Успешное сообщение в лог
        } catch (SQLException e) {
            throw new DatabaseConnectionException("Failed to create product product category"); // Исключение при ошибке
        }
    }
    /**
     * Добавляет новый продукт в базу данных.
     *
     * @param product продукт с именем и ценой.
     *
     * @throws DatabaseConnectionException если не удалось добавить продукт.
     * @throws IllegalArgumentException если цена продукта не положительна.
     */
    @Override
    public void addProduct(ProductDTOByNameAndPrice product) {
        try {
            connection.setAutoCommit(false); // Отключаем автоматическое подтверждение транзакций
            try (PreparedStatement productStatement = connection.prepareStatement(QUERY_PRODUCT)) {
                productStatement.setString(1, product.getNameProduct());
                // Проверка цены на положительность
                if (product.getPriceProduct().compareTo(BigDecimal.ZERO) <= 0) {
                    throw new IllegalArgumentException("Price should be greater than zero"); // Исключение при невалидной цене
                }
                productStatement.setBigDecimal(2, product.getPriceProduct());
                productStatement.executeUpdate(); // Выполнение запроса на добавление продукта

                // Проверяем, есть ли категории продуктов
                if (product.getProductCategoryDTOS() != null && !product.getProductCategoryDTOS().isEmpty()) {
                    try (PreparedStatement categoryStatement = connection.prepareStatement(QUERY_INSERT_PRODUCT_CATEGORY);
                         PreparedStatement productProductCategoryStatement = connection.prepareStatement(QUERY_INSERT_PRODUCT_PRODUCT_CATEGORY)) {
                        // Добавляем категории продуктов
                        for (ProductCategoryDTOByNameAndType cat : product.getProductCategoryDTOS()) {
                            categoryStatement.setString(1, cat.getNameProductCategory());
                            categoryStatement.setString(2, String.valueOf(cat.getTypeProductCategory()));

                            productProductCategoryStatement.setString(1, product.getNameProduct());
                            productProductCategoryStatement.setString(2, cat.getNameProductCategory());

                            categoryStatement.addBatch(); // Добавление в пакет
                            productProductCategoryStatement.addBatch(); // Добавление в пакет
                        }
                        // Выполнение пакетов
                        categoryStatement.executeBatch();
                        productProductCategoryStatement.executeBatch();
                    }
                } else {
                    logger.info("Product category null"); // Сообщение, если категорий нет
                    connection.commit(); // Подтверждение транзакции
                }
                // Подтверждение транзакции
                connection.commit();
            }
        } catch (SQLException e) {
            throw new DatabaseConnectionException("Failed to add product"); // Исключение при ошибке
        } catch (IllegalArgumentException e) {
            // Обработка случая, когда цена невалидна
            throw new IllegalArgumentException(e.getMessage());
        }
    }
    /**
     * Получает список всех продуктов из базы данных.
     *
     * @return список DTO продуктов.
     *
     * @throws DatabaseConnectionException если не удалось получить все продукты.
     */
    @Override
    public List<ProductDTOByNameAndPrice> getAllProducts() {
        List<ProductDTOByNameAndPrice> productList = new ArrayList<>(); // Список для хранения продуктов
        ResultSet resultSet;
        try (PreparedStatement statement = connection.prepareStatement(QUERY_SELECT_ALL_PRODUCT)) {
            resultSet = statement.executeQuery(); // Выполнение запроса на получение всех продуктов
            while (resultSet.next()) {
                ProductDTOByNameAndPrice product = new ProductDTOByNameAndPrice();
                product.setNameProduct(resultSet.getString(NAME_PRODUCT));
                product.setPriceProduct(resultSet.getBigDecimal(PRICE_PRODUCT));
                ResultSet categoryResult;
                try (PreparedStatement productCategoryStatement = connection.prepareStatement(QUERY_JOIN_PRODUCT_PRODUCT_CATEGORY_BY_NAME)) {
                    productCategoryStatement.setString(1, product.getNameProduct());
                    categoryResult = productCategoryStatement.executeQuery(); // Получение категорий для каждого продукта
                    List<ProductCategoryDTOByNameAndType> productCategories = new ArrayList<>();
                    while (categoryResult.next()) {
                        ProductCategoryDTOByNameAndType category = new ProductCategoryDTOByNameAndType();
                        category.setNameProductCategory(categoryResult.getString("name"));
                        category.setTypeProductCategory(CategoryType.valueOf((categoryResult.getString("type")))); // Преобразование типа
                        productCategories.add(category); // Добавление категории в список
                    }
                    product.setProductCategoryDTOS(productCategories); // Установка категорий в продукт
                    productList.add(product); // Добавление продукта в общий список
                }
            }
        } catch (SQLException e) {
            throw new DatabaseConnectionException("Failed to get all products"); // Исключение при ошибке
        }
        return productList; // Возврат списка всех продуктов
    }
    /**
     * Обновляет информацию о продукте.
     *
     * @param product продукт с новой информацией.
     * @param updateNameProduct новое имя продукта.
     *
     * @throws DatabaseConnectionException если не удалось обновить продукт.
     */
    @Override
    public void updateProduct(ProductDTOByNameAndPrice product, String updateNameProduct) throws DatabaseConnectionException {
        try {
            // Устанавливаем транзакцию в режим отключения автокоммита
            connection.setAutoCommit(false);
            try (PreparedStatement updateStatement = connection.prepareStatement(UPDATE_PRODUCT_SQL)) {
                // Устанавливаем новое имя продукта
                updateStatement.setString(1, updateNameProduct);

                // Проверяем, что цена продукта больше нуля
                if (product.getPriceProduct().compareTo(BigDecimal.ZERO) <= 0) {
                    throw new IllegalArgumentException("Цена должна быть больше нуля");
                }

                // Устанавливаем новую цену продукта
                updateStatement.setBigDecimal(2, product.getPriceProduct());
                // Устанавливаем имя продукта, который мы обновляем
                updateStatement.setString(3, product.getNameProduct());

                // Выполняем обновление и получаем количество обновленных строк
                int rowsUpdated = updateStatement.executeUpdate();
                if (rowsUpdated > 0) {
                    // Если обновление прошло успешно, удаляем категории продукта
                    try (PreparedStatement deleteCategoriesStatement = connection.prepareStatement(QUERY_DELETE_PRODUCT_PRODUCT_CATEGORY_BY_PRODUCT_NAME)) {
                        deleteCategoriesStatement.setString(1, product.getNameProduct());
                        deleteCategoriesStatement.executeUpdate();

                        // Добавляем новые категории продукта
                        try (PreparedStatement insertCategoriesStatement = connection.prepareStatement(QUERY_INSERT_PRODUCT_PRODUCT_CATEGORY)) {
                            insertCategoriesStatement.setString(2, updateNameProduct); // Задаем новое имя продукта для категорий
                            for (ProductCategoryDTOByNameAndType category : product.getProductCategoryDTOS()) {
                                insertCategoriesStatement.setString(1, category.getNameProductCategory());
                                insertCategoriesStatement.addBatch();
                            }
                            // Выполняем пакетное добавление категорий продуктов
                            insertCategoriesStatement.executeBatch();
                            // Подтверждаем транзакцию
                            connection.commit();
                        }
                    }
                } else {
                    // Если обновление не произошло, выбрасываем исключение
                    throw new SQLException("Не удалось обновить продукт");
                }
            }
        } catch (SQLException e) {
            // В случае ошибки подключения к базе данных выбрасываем собственное исключение
            throw new DatabaseConnectionException("Не удалось обновить продукт");
        }
    }
    /**
     * Удаляет  продукт и связывающие её категории.
     *
     * @param product  продукт, который нужно удалить.
     * @throws DatabaseConnectionException Если произойдет ошибка при работе с базой данных.
     */
    @Override
    public void deleteProduct(ProductDTOByNameAndPrice product) {
        try {
            // Устанавливаем транзакцию в режим отключения автокоммита
            connection.setAutoCommit(false);
            try (PreparedStatement deleteStatement = connection.prepareStatement(QUERY_DELETE_PRODUCT)) {
                // Устанавливаем имя продукта для удаления
                deleteStatement.setString(1, product.getNameProduct());
                // Выполняем удаление продукта
                deleteStatement.executeUpdate();

                // Удаляем категории, связанные с продуктом
                try (PreparedStatement deleteProductCategoryStatement = connection.prepareStatement(QUERY_DELETE_PRODUCT_PRODUCT_CATEGORY_BY_PRODUCT_NAME)) {
                    deleteProductCategoryStatement.setString(1, product.getNameProduct());
                    deleteProductCategoryStatement.executeUpdate();
                    // Подтверждаем транзакцию
                    connection.commit();
                }
            }
        } catch (SQLException e) {
            // В случае ошибки подключения к базе данных выбрасываем собственное исключение
            throw new DatabaseConnectionException("Не удалось удалить продукт");
        }
    }
}
