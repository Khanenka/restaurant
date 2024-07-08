package com.khanenka.restapiservlet.repository.impl;

import com.khanenka.restapiservlet.DBConnection;
import com.khanenka.restapiservlet.exception.DatabaseConnectionException;
import com.khanenka.restapiservlet.model.CategoryType;
import com.khanenka.restapiservlet.model.productDTO.ProductCategoryDTO;
import com.khanenka.restapiservlet.model.productDTO.ProductDTO;
import com.khanenka.restapiservlet.repository.ProductCategoryDao;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static com.khanenka.restapiservlet.repository.impl.ProductDaoImpl.QUERY_INSERT_PRODUCT_PRODUCT_CATEGORY;

public class ProductCategoryDaoImpl implements ProductCategoryDao {
    static final String QUERY_INSERT_PRODUCT_CATEGORY = "INSERT INTO productcategory (\"id\",\"name\", \"type\") VALUES (?, ?, ?)";
    static final String QUERY_SELECT_ALL_PRODUCT_CATEGORY = "SELECT \"id\",\"name\",\"type\" FROM productcategory";
    static final String QUERY_SELECT_PRODUCT_CATEGORY_BY_NAME = "SELECT * FROM productcategory where name = ?";
    static final String QUERY_JOIN_PRODUCT_PRODUCT_CATEGORY_BY_ID = "SELECT p.* FROM product p JOIN product_productcategory pp ON p.id = pp.product_id WHERE pp.productcategory_id = ?";
    static final String QUERY_DELETE_PRODUCT_PRODUCT_CATEGORY_BY_CATEGORY_ID = "DELETE FROM product_productcategory WHERE \"productcategory_id\" = ?";

    static final String CATEGORY_NAME = "name";
    static final String CATEGORY_TYPE = "type";

    Connection connection = DBConnection.getConnection();

    @Override
    public void addProductCategory(ProductCategoryDTO productCategory) throws Exception {

        connection = DBConnection.getConnection();

        connection.setAutoCommit(false);

        try (PreparedStatement categoryStatement = connection.prepareStatement(QUERY_INSERT_PRODUCT_CATEGORY)) {
            categoryStatement.setLong(1, productCategory.getIdProductCategory());
            categoryStatement.setString(2, productCategory.getNameProductCategory());
            categoryStatement.setString(3, String.valueOf(productCategory.getTypeProductCategory()));

            categoryStatement.executeUpdate();

            if (productCategory.getProductDTOS() != null && !productCategory.getProductDTOS().isEmpty()) {
                try (PreparedStatement productCategoryStatement = connection.prepareStatement(QUERY_INSERT_PRODUCT_PRODUCT_CATEGORY)) {
                    for (ProductDTO product : productCategory.getProductDTOS()) {
                        productCategoryStatement.setLong(1, product.getIdProduct());
                        productCategoryStatement.setLong(2, productCategory.getIdProductCategory());
                        productCategoryStatement.addBatch();
                    }
                    productCategoryStatement.executeBatch();
                }
            }

            connection.commit();
        }
    }

    @Override
    public List<ProductCategoryDTO> getAllProductCategories() throws SQLException {
        List<ProductCategoryDTO> categoryList = new ArrayList<>();
        connection = DBConnection.getConnection();

        ResultSet resultSet;
        try (PreparedStatement statement = connection.prepareStatement(QUERY_SELECT_ALL_PRODUCT_CATEGORY)) {
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                ProductCategoryDTO category = new ProductCategoryDTO();
                category.setIdProductCategory(resultSet.getLong("id"));
                category.setNameProductCategory(resultSet.getString(CATEGORY_NAME));
                category.setTypeProductCategory(CategoryType.valueOf(resultSet.getString(CATEGORY_TYPE)));

                // Get products in this category
                ResultSet productResult;
                try (PreparedStatement productCategoryStatement = connection.prepareStatement(QUERY_JOIN_PRODUCT_PRODUCT_CATEGORY_BY_ID)) {
                    productCategoryStatement.setLong(1, category.getIdProductCategory());
                    productResult = productCategoryStatement.executeQuery();

                    List<ProductDTO> products = new ArrayList<>();
                    while (productResult.next()) {
                        ProductDTO product = new ProductDTO();
                        product.setIdProduct(productResult.getLong("id"));
                        product.setNameProduct(productResult.getString("name"));
                        product.setPriceProduct(productResult.getBigDecimal("price"));
                        products.add(product);
                    }

                    category.setProductDTOS(products);
                    categoryList.add(category);
                }
            }
        }
        return categoryList;
    }
    @Override
    public void updateProductCategory(ProductCategoryDTO category) throws SQLException {
        connection = DBConnection.getConnection(); connection.setAutoCommit(false);


        try (PreparedStatement updateStatement = connection.prepareStatement("UPDATE productcategory SET \"name\" = ?, \"type\" = ? WHERE \"id\" = ?")) {
            updateStatement.setString(1, category.getNameProductCategory());
            updateStatement.setString(2, String.valueOf(category.getTypeProductCategory()));
            updateStatement.setLong(3, category.getIdProductCategory());
            int rowsUpdated = updateStatement.executeUpdate();

            if (rowsUpdated > 0) {
                connection.commit();
            } else {
                throw new SQLException("Failed to update product category");
            }
        }
    }

    @Override
    public void deleteProductCategory(ProductCategoryDTO category) throws Exception {
        connection = DBConnection.getConnection();

        PreparedStatement deleteStatement = connection.prepareStatement(QUERY_DELETE_PRODUCT_PRODUCT_CATEGORY_BY_CATEGORY_ID);
        deleteStatement.setLong(1, category.getIdProductCategory());
        deleteStatement.executeUpdate();
    }
}

