//package com.khanenka.restapiservlet.repository.impl;
//
//import com.khanenka.restapiservlet.exception.DatabaseConnectionException;
//import com.khanenka.restapiservlet.model.CategoryType;
//import com.khanenka.restapiservlet.model.productdto.ProductCategoryDTOByNameAndType;
//import com.khanenka.restapiservlet.model.productdto.ProductDTOByNameAndPrice;
//import com.khanenka.restapiservlet.repository.ProductCategoryDao;
//import com.khanenka.restapiservlet.util.DBConnection;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import java.sql.Connection;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.util.ArrayList;
//import java.util.List;
//
//import static com.khanenka.restapiservlet.repository.impl.ProductDaoImpl.*;
//import static com.khanenka.restapiservlet.repository.implementation.ProductDAOImpl.QUERY_INSERT_PRODUCT;
//import static com.khanenka.restapiservlet.repository.implementation.ProductDAOImpl.QUERY_INSERT_PRODUCT_PRODUCT_CATEGORY;
//
///**
// * Реализация DAO для работы с категориями продуктов.
// * Это класс отвечает за выполнение операций с таблицей productcategory
// * и их связями с таблицей product.
// *
// * @author Khanenka
// */
//public class ProductCategoryDaoImpl implements ProductCategoryDao {
//    // Запросы SQL для операций с таблицей productcategory
//   public static final String QUERY_INSERT_PRODUCT_CATEGORY = "INSERT INTO productcategory (\"name\", \"type\") VALUES ( ?, ?)";
//   public static final String QUERY_SELECT_ALL_PRODUCT_CATEGORY = "SELECT DISTINCT \"name\",\"type\" FROM productcategory";
//   public static final String QUERY_JOIN_PRODUCT_PRODUCT_CATEGORY_BY_NAME = "SELECT DISTINCT p.nameproduct,p.priceproduct FROM product p JOIN product_productcategory ppc ON p.nameproduct = ppc.nameproduct WHERE ppc.name = ?";
//   public static final String QUERY_DELETE_PRODUCT_PRODUCT_CATEGORY_BY_PRODUCT_NAME = "DELETE FROM product_productcategory WHERE \"nameproduct\" = ?";
//   public static final String QUERY_DELETE_PRODUCT_PRODUCT_CATEGORY_BY_PRODUCT_CATEGORY_NAME = "DELETE FROM product_productcategory WHERE \"name\" = ?";
//   public static final String QUERY_DELETE_PRODUCT_CATEGORY = "DELETE FROM productcategory WHERE \"name\" = ?";
//   public static final String QUERY_PRODUCT_CATEGORY_TABLE = "CREATE TABLE IF NOT EXISTS productcategory(id SERIAL PRIMARY KEY , name character varying(255) ,  type character varying(255))";
//   public static final String UPDATE_PRODUCT_CATEGORY_SQL = "UPDATE productcategory set name = ?, type = ? WHERE name = ?";
//
//    // Константы для имен столбцов
//    static final String CATEGORY_NAME = "name";
//    static final String CATEGORY_TYPE = "type";
//
//    // Подключение к базе данных
//    Connection connection = DBConnection.getConnection();
//    Logger logger = LoggerFactory.getLogger(ProductCategoryDaoImpl.class); // Логгер для логирования информации
//
//    /**
//     * Конструктор по умолчанию.
//     */
//    public ProductCategoryDaoImpl() {
//    }
//
//    /**
//     * Конструктор с указанием подключения к базе данных.
//     *
//     * @param connection Подключение к базе данных.
//     */
//    public ProductCategoryDaoImpl(Connection connection) {
//        this.connection = connection; // Позволяет передать подключение в конструктор
//    }
//
//    /**
//     * Добавляет новую категорию продукта и связывает её с продуктами.
//     *
//     * @param productCategory Категория продукта, которую нужно добавить.
//     * @throws DatabaseConnectionException Если произойдет ошибка при работе с базой данных.
//     * @throws IllegalArgumentException    Если продукт в списке является null.
//     */
//    @Override
//    public void addProductCategory(ProductCategoryDTOByNameAndType productCategory) {
//        try {
//            connection.setAutoCommit(false); // Отключаем автокоммит
//            // Вставка новой категории продукта
//            try (PreparedStatement categoryStatement = connection.prepareStatement(QUERY_INSERT_PRODUCT_CATEGORY)) {
//                categoryStatement.setString(1, productCategory.getNameProductCategory());
//                categoryStatement.setString(2, String.valueOf(productCategory.getTypeProductCategory()));
//                categoryStatement.executeUpdate(); // Выполняем вставку
//
//                // Проверяем, есть ли продукты, связанные с категорией
//                if (productCategory.getProductDTOS() != null && !productCategory.getProductDTOS().isEmpty()) {
//                    // Подготовка запросов для вставки продуктов и связывания их с категорией
//                    try (PreparedStatement productStatement = connection.prepareStatement(QUERY_INSERT_PRODUCT);
//                         PreparedStatement productCategoryStatement = connection.prepareStatement(QUERY_INSERT_PRODUCT_PRODUCT_CATEGORY)) {
//
//                        for (ProductDTOByNameAndPrice product : productCategory.getProductDTOS()) {
//                            // Проверка на null для продукта
//                            if (product == null) {
//                                throw new IllegalArgumentException("Product cannot be null in the product list");
//                            }
//
//                            // Вставка продукта
//                            productStatement.setString(1, product.getNameProduct());
//                            productStatement.setBigDecimal(2, product.getPriceProduct());
//
//                            // Связываем продукт с категорией
//                            productCategoryStatement.setString(1, product.getNameProduct());
//                            productCategoryStatement.setString(2, productCategory.getNameProductCategory());
//
//                            productStatement.addBatch(); // Добавляем в батч
//                            productCategoryStatement.addBatch();
//                        }
//                        // Выполняем батч
//                        productStatement.executeBatch();
//                        productCategoryStatement.executeBatch();
//                    }
//                } else {
//                    logger.info("Products nullable"); // Записываем информацию в лог
//                    connection.commit(); // Коммитим транзакцию, если продуктов нет
//                }
//                // Коммит транзакции
//                connection.commit();
//            }
//        } catch (SQLException e) {
//            throw new DatabaseConnectionException("Failed to add product category"); // Обработка исключения при ошибке SQL
//        } catch (IllegalArgumentException e) {
//            // Обработка случая, когда цена недействительна
//            throw new IllegalArgumentException(e.getMessage());
//        }
//    }
//
//    /**
//     * Возвращает список всех категорий продуктов.
//     *
//     * @return Список всех категорий продуктов.
//     * @throws SQLException Если произошла ошибка в SQL-запросе.
//     */
//    @Override
//    public List<ProductCategoryDTOByNameAndType> getAllProductCategories() throws SQLException {
//        List<ProductCategoryDTOByNameAndType> categoryList = new ArrayList<>(); // Список категорий
//
//        ResultSet resultSet;
//        try (PreparedStatement statement = connection.prepareStatement(QUERY_SELECT_ALL_PRODUCT_CATEGORY)) {
//            resultSet = statement.executeQuery(); // Выполняем запрос для получения всех категорий
//
//            while (resultSet.next()) {
//                ProductCategoryDTOByNameAndType category = new ProductCategoryDTOByNameAndType();
//                category.setNameProductCategory(resultSet.getString(CATEGORY_NAME));
//                category.setTypeProductCategory(CategoryType.valueOf(resultSet.getString(CATEGORY_TYPE)));
//
//                // Получаем продукты в данной категории
//                ResultSet productResult;
//                try (PreparedStatement productCategoryStatement = connection.prepareStatement(QUERY_JOIN_PRODUCT_PRODUCT_CATEGORY_BY_NAME)) {
//                    productCategoryStatement.setString(1, category.getNameProductCategory());
//                    productResult = productCategoryStatement.executeQuery(); // Выполняем JOIN запрос для получения продуктов
//
//                    List<ProductDTOByNameAndPrice> products = new ArrayList<>();
//                    while (productResult.next()) {
//                        ProductDTOByNameAndPrice product = new ProductDTOByNameAndPrice();
//                        product.setNameProduct(productResult.getString(NAME_PRODUCT));
//                        product.setPriceProduct(productResult.getBigDecimal(PRICE_PRODUCT));
//                        products.add(product); // Добавляем продукт в список
//                    }
//
//                    category.setProductDTOS(products); // Устанавливаем список продуктов в категорию
//                    categoryList.add(category); // Добавляем категорию в общий список
//                }
//            }
//        }
//        return categoryList; // Возвращаем список всех категорий
//    }
//
//    /**
//     * Обновляет информацию о категории продукта.
//     *
//     * @param category    Существующая категория продукта, которую нужно обновить.
//     * @param newCategory Новое имя категории продукта.
//     * @throws SQLException Если произошла ошибка в SQL-запросе.
//     */
//    @Override
//    public void updateProductCategory(ProductCategoryDTOByNameAndType category, String newCategory) throws SQLException {
//        connection.setAutoCommit(false); // Отключаем автокоммит
//        try (PreparedStatement updateStatement = connection.prepareStatement(UPDATE_PRODUCT_CATEGORY_SQL)) {
//            updateStatement.setString(1, newCategory); // Устанавливаем новое имя категории
//            updateStatement.setString(2, String.valueOf(category.getTypeProductCategory())); // Устанавливаем тип категории
//            updateStatement.setString(3, category.getNameProductCategory()); // Условия для обновления
//
//            int rowsUpdated = updateStatement.executeUpdate(); // Выполняем обновление
//
//            if (rowsUpdated > 0) {
//                // Обновляем связи продуктов с категориями
//                try (PreparedStatement deleteCategoriesStatement = connection.prepareStatement(QUERY_DELETE_PRODUCT_PRODUCT_CATEGORY_BY_PRODUCT_NAME)) {
//                    deleteCategoriesStatement.setString(1, category.getNameProductCategory());
//                    deleteCategoriesStatement.executeUpdate(); // Удаляем старые связи
//
//                        try (PreparedStatement insertCategoriesStatement = connection.prepareStatement(QUERY_INSERT_PRODUCT_PRODUCT_CATEGORY)) {
//                        insertCategoriesStatement.setString(2, newCategory); // Устанавливаем новую категорию
//                        for (ProductDTOByNameAndPrice productDTOByNameAndPrice : category.getProductDTOS()) {
//                            insertCategoriesStatement.setString(1, productDTOByNameAndPrice.getNameProduct()); // Устанавливаем имя продукта
//
//                            insertCategoriesStatement.addBatch(); // Добавляем в батч
//                        }
//                        insertCategoriesStatement.executeBatch(); // Выполняем батч
//
//                        connection.commit(); // Коммит транзакции
//                    }
//                }
//            } else {
//                throw new SQLException("Failed to update product category"); // Исключение в случае неудачного обновления
//            }
//        }
//    }
//
//    /**
//     * Удаляет категорию продукта и связывающие её продукты.
//     *
//     * @param category Категория продукта, которую нужно удалить.
//     * @throws DatabaseConnectionException Если произойдет ошибка при работе с базой данных.
//     */
//    @Override
//    public void deleteProductCategory(ProductCategoryDTOByNameAndType category) {
//        try {
//            connection.setAutoCommit(false); // Отключаем автокоммит
//            try (PreparedStatement deleteStatement = connection.prepareStatement(QUERY_DELETE_PRODUCT_CATEGORY)) {
//                deleteStatement.setString(1, category.getNameProductCategory()); // Устанавливаем имя категории для удаления
//                deleteStatement.executeUpdate(); // Выполняем удаление
//                try (PreparedStatement deleteProductCategoryStatement = connection.prepareStatement(QUERY_DELETE_PRODUCT_PRODUCT_CATEGORY_BY_PRODUCT_CATEGORY_NAME)) {
//                    deleteProductCategoryStatement.setString(1, category.getNameProductCategory());
//                    deleteProductCategoryStatement.executeUpdate(); // Удаляем связи с продуктами
//                    connection.commit(); // Коммит транзакции
//                }
//            }
//        } catch (SQLException e) {
//            throw new DatabaseConnectionException("Failed to delete product category");
//        }
//    }
//}