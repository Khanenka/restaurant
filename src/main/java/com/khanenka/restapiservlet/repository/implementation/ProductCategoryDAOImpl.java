package com.khanenka.restapiservlet.repository.implementation;


import com.khanenka.restapiservlet.exceptions.DatabaseConnectionException;
import com.khanenka.restapiservlet.model.CategoryType;
import com.khanenka.restapiservlet.model.productdto.ProductCategoryDTOByNameAndType;
import com.khanenka.restapiservlet.model.productdto.ProductDTOByNameAndPrice;
import com.khanenka.restapiservlet.repository.ProductCategoryDao;
import com.khanenka.restapiservlet.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static com.khanenka.restapiservlet.util.QueryInDB.*;

public class ProductCategoryDAOImpl implements ProductCategoryDao {

    private Connection connection = DBConnection.getConnection();

    public ProductCategoryDAOImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void addProductCategory(ProductCategoryDTOByNameAndType productCategory) {
        try {
            connection.setAutoCommit(false);
            try (PreparedStatement categoryStatement = connection.prepareStatement(QUERY_INSERT_PRODUCT_CATEGORY)) {
                categoryStatement.setString(1, productCategory.getNameProductCategory());
                categoryStatement.setString(2, String.valueOf(productCategory.getTypeProductCategory()));
                categoryStatement.executeUpdate(); // Выполняем вставку
                connection.commit();
            }
        } catch (SQLException e) {
            throw new DatabaseConnectionException("Failed to add product category");
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    @Override
    public List<ProductCategoryDTOByNameAndType> getAllProductCategories() {
        List<ProductCategoryDTOByNameAndType> categoryList = new ArrayList<>();
        ResultSet resultSet;
        try (PreparedStatement statement = connection.prepareStatement(QUERY_SELECT_ALL_PRODUCT_CATEGORY)) {
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                ProductCategoryDTOByNameAndType category = new ProductCategoryDTOByNameAndType();
                category.setNameProductCategory(resultSet.getString(CATEGORY_NAME));
                category.setTypeProductCategory(CategoryType.valueOf(resultSet.getString(CATEGORY_TYPE)));
                try (ResultSet productResult = joinProductProductCategoryByName(category.getNameProductCategory())) {
                    List<ProductDTOByNameAndPrice> products = new ArrayList<>();
                    while (productResult.next()) {
                        ProductDTOByNameAndPrice product = new ProductDTOByNameAndPrice();
                        product.setNameProduct(productResult.getString(NAME_PRODUCT));
                        product.setPriceProduct(productResult.getBigDecimal(PRICE_PRODUCT));
                        products.add(product);
                    }
                    category.setProductDTOS(products);
                    categoryList.add(category);
                }
            }
        } catch (SQLException e) {
            throw new DatabaseConnectionException("error in get all product categories");
        }
        return categoryList;
    }

    public ResultSet joinProductProductCategoryByName(String nameProductCategory) {
        ResultSet productResult;
        try {
            PreparedStatement productCategoryStatement =
                    connection.prepareStatement(QUERY_JOIN_PRODUCT_PRODUCT_CATEGORY_BY_NAME);
            productCategoryStatement.setString(1, nameProductCategory);
            productResult = productCategoryStatement.executeQuery();
        } catch (SQLException e) {
            throw new DatabaseConnectionException("error in joinProductProductCategoryByName");
        }
        return productResult;
    }

    @Override
    public void updateProductCategory(ProductCategoryDTOByNameAndType category, String newCategory) {
        try {
            connection.setAutoCommit(false);
            try (PreparedStatement updateStatement = connection.prepareStatement(UPDATE_PRODUCT_CATEGORY_SQL)) {
                updateStatement.setString(1, newCategory);
                updateStatement.setString(2, String.valueOf(category.getTypeProductCategory()));
                updateStatement.setString(3, category.getNameProductCategory());
                int rowsUpdated = updateStatement.executeUpdate();
                if (rowsUpdated > 0) {
                    connection.commit();
                } else {
                    throw new SQLException("Failed to update product category");
                }
            }
        } catch (SQLException e) {
            throw new DatabaseConnectionException("Failed to update product category");
        }
    }

    public void deleteProductProductCategoryByName(String categoryName) {
        try (PreparedStatement deleteCategoriesStatement =
                     connection.prepareStatement(QUERY_DELETE_PRODUCT_PRODUCT_CATEGORY_BY_PRODUCT_NAME)) {
            deleteCategoriesStatement.setString(1, categoryName);
            deleteCategoriesStatement.executeUpdate(); // Удаляем старые связи
        } catch (SQLException e) {
            throw new DatabaseConnectionException("error in delete product product category by name");
        }
    }

    @Override
    public void deleteProductCategory(ProductCategoryDTOByNameAndType category) {
        try {
            connection.setAutoCommit(false);
            try (PreparedStatement deleteStatement = connection.prepareStatement(QUERY_DELETE_PRODUCT_CATEGORY)) {
                deleteStatement.setString(1, category.getNameProductCategory());
                deleteStatement.executeUpdate();
                connection.commit();
            }
        } catch (SQLException e) {
            throw new DatabaseConnectionException("Failed to delete product category");
        }
    }

    public void addProductJoinProductCategory(String nameProduct, String productCategory) {
        try {
            connection.setAutoCommit(false);
            try (PreparedStatement productCategoryStatement = connection.prepareStatement(
                    QUERY_INSERT_PRODUCT_PRODUCT_CATEGORY)) {
                // Связываем продукт с категорией
                productCategoryStatement.setString(1, nameProduct);
                productCategoryStatement.setString(2, productCategory);
                productCategoryStatement.executeUpdate();
                connection.commit();
            }
        } catch (SQLException ex) {
            throw new DatabaseConnectionException("add product join product category");
        }
    }
}
