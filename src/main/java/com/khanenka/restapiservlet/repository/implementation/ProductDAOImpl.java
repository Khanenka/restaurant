package com.khanenka.restapiservlet.repository.implementation;


import com.khanenka.restapiservlet.exceptions.DatabaseConnectionException;
import com.khanenka.restapiservlet.model.CategoryType;
import com.khanenka.restapiservlet.model.productdto.ProductCategoryDTOByNameAndType;
import com.khanenka.restapiservlet.model.productdto.ProductDTO;
import com.khanenka.restapiservlet.model.productdto.ProductDTOByNameAndPrice;
import com.khanenka.restapiservlet.repository.ProductDao;
import com.khanenka.restapiservlet.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static com.khanenka.restapiservlet.util.QueryInDB.*;


public class ProductDAOImpl implements ProductDao {
    private Connection connection = DBConnection.getConnection();

    public ProductDAOImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void createTableProduct() {
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(QUERY_CREATE_TABLE_PRODUCT);
        } catch (SQLException e) {
            throw new DatabaseConnectionException("Failed to create product" + e);
        }
    }

    @Override
    public void createProductProductCategory() {
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(QUERY_CREATE_PRODUCT_PRODUCT_CATEGORY_TABLE);
        } catch (SQLException e) {
            throw new DatabaseConnectionException("Failed to create product product category");
        }
    }

    @Override
    public void createProductCategoryTable() {
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(QUERY_PRODUCT_CATEGORY_TABLE);
        } catch (SQLException e) {
            throw new DatabaseConnectionException("error in create table");
        }
    }

    @Override
    public void addProduct(ProductDTOByNameAndPrice product) throws SQLException {
        connection.setAutoCommit(false);
        try (PreparedStatement productStatement = connection.prepareStatement(QUERY_INSERT_PRODUCT)) {
            productStatement.setString(1, product.getNameProduct());
            productStatement.setBigDecimal(2, product.getPriceProduct());
            productStatement.executeUpdate();
            addProductCategories(product);
            connection.commit();
        } catch (SQLException e) {
            connection.rollback();
            throw e;
        }
    }

    @Override
    public void addProductById(ProductDTO product) {
        try
                (PreparedStatement productStatement = connection.prepareStatement(QUERY_INSERT_PRODUCT_WITH_ID)) {
            productStatement.setLong(1, product.getIdProduct());
            productStatement.setString(2, product.getNameProduct());
            productStatement.setBigDecimal(3, product.getPriceProduct());
            productStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseConnectionException("Error while adding product");
        }
    }

    private void addProductJoinProductCategory(String nameProduct, String productCategory) {
        try (PreparedStatement statement = connection.prepareStatement(QUERY_INSERT_PRODUCT_PRODUCT_CATEGORY)) {
            statement.setString(1, nameProduct);
            statement.setString(2, productCategory);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseConnectionException("Error in join product and product category");
        }
    }

    private void addProductCategories(ProductDTOByNameAndPrice product) throws SQLException {
        if (product.getProductCategoryDTOS() != null && !product.getProductCategoryDTOS().isEmpty()) {
            try (PreparedStatement categoryStatement = connection.prepareStatement(QUERY_INSERT_PRODUCT_CATEGORY)) {
                for (ProductCategoryDTOByNameAndType cat : product.getProductCategoryDTOS()) {
                    categoryStatement.setString(1, cat.getNameProductCategory());
                    categoryStatement.setString(2, String.valueOf(cat.getTypeProductCategory()));
                    addProductJoinProductCategory(product.getNameProduct(), cat.getNameProductCategory());
                    categoryStatement.addBatch();
                }
                categoryStatement.executeBatch();
            }
        }
    }

    @Override
    public List<ProductDTOByNameAndPrice> getAllProducts() {
        List<ProductDTOByNameAndPrice> productList = new ArrayList<>();
        ResultSet resultSet;
        try (PreparedStatement statement = connection.prepareStatement(QUERY_SELECT_ALL_PRODUCT)) {
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                ProductDTOByNameAndPrice product = new ProductDTOByNameAndPrice();
                product.setNameProduct(resultSet.getString(NAME_PRODUCT));
                product.setPriceProduct(resultSet.getBigDecimal(PRICE_PRODUCT));
                List<ProductCategoryDTOByNameAndType> productCategories =
                        getProductCategories(product.getNameProduct());
                product.setProductCategoryDTOS(productCategories);
                productList.add(product);
            }
        } catch (SQLException e) {
            throw new DatabaseConnectionException("error in get all products");
        }
        return productList;
    }

    public List<ProductCategoryDTOByNameAndType> getProductCategories(String productName) {
        List<ProductCategoryDTOByNameAndType> productCategories = new ArrayList<>();
        try (PreparedStatement productCategoryStatement =
                     connection.prepareStatement(QUERY_JOIN_PRODUCT_CATEGORY_PRODUCT_BY_NAME)) {
            productCategoryStatement.setString(1, productName);
            ResultSet categoryResult = productCategoryStatement.executeQuery();
            while (categoryResult.next()) {
                ProductCategoryDTOByNameAndType category = new ProductCategoryDTOByNameAndType();
                category.setNameProductCategory(categoryResult.getString("name"));
                category.setTypeProductCategory(CategoryType.valueOf(categoryResult.getString("type")));
                productCategories.add(category);
            }
        } catch (SQLException e) {
            throw new DatabaseConnectionException("error in getProductCategories");
        }
        return productCategories;
    }

    @Override
    public void updateProduct(ProductDTOByNameAndPrice product, String updatedNameProduct) {
        try
                (PreparedStatement updateStatement = connection.prepareStatement(UPDATE_PRODUCT_SQL)) {
            updateStatement.setString(1, updatedNameProduct);
            updateStatement.setBigDecimal(2, product.getPriceProduct());
            updateStatement.setString(3, product.getNameProduct());
            updateStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseConnectionException("Database connection error");
        }
    }

    @Override
    public void updateProductProductCategory(ProductDTOByNameAndPrice productDTOByNameAndPrice) {
        try {
            connection.setAutoCommit(false);
            try
                    (PreparedStatement updateStatement = connection.prepareStatement(
                            UPDATE_PRODUCT_PRODUCT_CATEGORY_BY_NAME)) {
                updateStatement.setString(1, productDTOByNameAndPrice.getNewProduct());
                updateStatement.setString(2, productDTOByNameAndPrice.getNameProduct());
                updateStatement.executeUpdate();
                connection.commit();
            }
        } catch (SQLException e) {
            throw new DatabaseConnectionException("error ind update product product_category");
        }
    }

    @Override
    public void deleteProduct(ProductDTOByNameAndPrice product) {
        try {
            connection.setAutoCommit(false);
            try (PreparedStatement deleteStatement = connection.prepareStatement(QUERY_DELETE_PRODUCT)) {
                deleteStatement.setString(1, product.getNameProduct());
                deleteStatement.executeUpdate();
            }
            connection.commit();
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException rollbackEx) {
                throw new DatabaseConnectionException("Не удалось откатить транзакцию");
            }
            throw new DatabaseConnectionException("Не удалось удалить продукт");
        }
    }

    public void deleteProductCategories(String productName) throws SQLException {
        try (PreparedStatement deleteStatement =
                     connection.prepareStatement(QUERY_DELETE_PRODUCT_PRODUCT_CATEGORY_BY_PRODUCT_NAME)) {
            deleteStatement.setString(1, productName);
            deleteStatement.executeUpdate();
        }
    }
}
