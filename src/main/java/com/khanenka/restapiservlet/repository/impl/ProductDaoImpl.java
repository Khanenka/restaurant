package com.khanenka.restapiservlet.repository.impl;

import com.khanenka.restapiservlet.DBConnection;
import com.khanenka.restapiservlet.exception.DatabaseConnectionException;
import com.khanenka.restapiservlet.model.CategoryType;
import com.khanenka.restapiservlet.model.Product;
import com.khanenka.restapiservlet.model.ProductCategory;
import com.khanenka.restapiservlet.model.productDTO.ProductCategoryDTO;
import com.khanenka.restapiservlet.model.productDTO.ProductDTO;
import com.khanenka.restapiservlet.model.productDTO.ProductDTOByNameAndPrice;
import com.khanenka.restapiservlet.repository.ProductDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class ProductDaoImpl implements ProductDao {
    static final String QUERY_INSERT_PRODUCT = "INSERT INTO product (nameProduct,priceProduct, quantityProduct, availableProduct) VALUES ( ?,?,?,?)";
    static final String QUERY_SELECT_ALL_PRODUCT = "SELECT \"idProduct\",\"nameProduct\",\"priceProduct\",\"quantityProduct\",\"availableProduct\" FROM product";

    static final String UPDATE_PRODUCT_SQL = "UPDATE product set \"nameProduct\" = ?, \"priceProduct\" = ? WHERE \"idProduct\" = ?;";

    static final String QUERY_DELETE_PRODUCT = "DELETE FROM product WHERE \"idProduct\"=?";
    static final String QUERY_SELECT_PRODUCT_BY_NAME = "SELECT * FROM product where nameProduct = ?;";
    static final String QUERY_PRODUCT = "INSERT INTO public.product (\"idProduct\", \"nameProduct\", \"priceProduct\", \"quantityProduct\", \"availableProduct\") VALUES (?, ?, ?, ?, ?)";
    static final String QUERY_INSERT_PRODUCT_PRODUCT_CATEGORY = "INSERT INTO product_productcategory (product_id, productcategory_id) VALUES (?, ?)";
    static final String QUERY_INSERT_PRODUCT_CATEGORY = "INSERT INTO productcategory (id,name, type) VALUES (?,?, ?)";
    static final String QUERY_JOIN_PRODUCT_PRODUCT_CATEGORY_BY_ID = "SELECT pc.* FROM productcategory pc JOIN product_productcategory ppc ON pc.id = ppc.productcategory_id WHERE ppc.product_id = ?";
    static final String QUERY_DELETE_PRODUCT_PRODUCT_CATEGORY_BY_PRODUCT_ID = "DELETE FROM \"product_productcategory\" WHERE \"product_id\" = ?";

    static final String NAME_PRODUCT = "nameProduct";
    static final String PRICE_PRODUCT = "priceProduct";
    Connection connection = DBConnection.getConnection();


    Logger logger = LoggerFactory.getLogger(ProductDaoImpl.class);

    @Override
    public void addProduct(ProductDTO product) throws Exception {

        connection = DBConnection.getConnection();


        connection.setAutoCommit(false);

        try (PreparedStatement productStatement = connection.prepareStatement(QUERY_PRODUCT)) {
            productStatement.setLong(1, product.getIdProduct());
            productStatement.setString(2, product.getNameProduct());
            productStatement.setBigDecimal(3, product.getPriceProduct());
//            productStatement.setInt(4, product.getQuantityProduct());
//            productStatement.setBoolean(5, product.isAvailableProduct());
            System.out.println(product.toString());
            productStatement.executeUpdate();


            try (PreparedStatement categoryStatement = connection.prepareStatement(QUERY_INSERT_PRODUCT_CATEGORY)) {
                try (PreparedStatement productProductCategoryStatement = connection.prepareStatement(QUERY_INSERT_PRODUCT_PRODUCT_CATEGORY)) {

                    for (ProductCategory cat : product.getProductCategory()) {
                        categoryStatement.setLong(1, cat.getIdProductCategory());
                        categoryStatement.setString(2, cat.getNameProductCategory());
                        categoryStatement.setString(3, String.valueOf(cat.getTypeProductCategory()));
                        categoryStatement.addBatch();


                        productProductCategoryStatement.setLong(1, product.getIdProduct());
                        productProductCategoryStatement.setLong(2, cat.getIdProductCategory());

                        productProductCategoryStatement.addBatch();
                    }

                    categoryStatement.executeBatch();

                    productProductCategoryStatement.executeBatch();

                    connection.commit();
                }
            }
        } catch (SQLException e) {
            throw new DatabaseConnectionException("Failed to connect to the database");
        }
    }


    public List<ProductDTO> getAllProducts() throws SQLException {
        List<ProductDTO> productList = new ArrayList<>();
        connection = DBConnection.getConnection();


        ResultSet resultSet;
        try (PreparedStatement statement = connection.prepareStatement(QUERY_SELECT_ALL_PRODUCT)) {
            resultSet = statement.executeQuery();


            while (resultSet.next()) {
                ProductDTO product = new ProductDTO();
                product.setIdProduct(resultSet.getLong("idProduct"));
                product.setNameProduct(resultSet.getString(NAME_PRODUCT));
                product.setPriceProduct(resultSet.getBigDecimal(PRICE_PRODUCT));

                // Get product categories
                ResultSet categoryResult;
                try (PreparedStatement productCategoryStatement = connection.prepareStatement(QUERY_JOIN_PRODUCT_PRODUCT_CATEGORY_BY_ID)) {
                    productCategoryStatement.setLong(1, product.getIdProduct());
                    categoryResult = productCategoryStatement.executeQuery();


                    List<ProductCategory> productCategories = new ArrayList<>();
                    while (categoryResult.next()) {
                        ProductCategory category = new ProductCategory();
                        category.setNameProductCategory(categoryResult.getString("name"));
                        category.setTypeProductCategory(CategoryType.valueOf(categoryResult.getString("type")));
                        productCategories.add(category);
                    }

                    product.setProductCategory(productCategories);
                    productList.add(product);
                }
            }
        }
        return productList;
    }


    @Override
    public void updateProduct(ProductDTO product) throws SQLException {
        connection = DBConnection.getConnection();
        connection.setAutoCommit(false);


        try (PreparedStatement updateStatement = connection.prepareStatement(UPDATE_PRODUCT_SQL)) {
            updateStatement.setString(1, product.getNameProduct());
            updateStatement.setBigDecimal(2, product.getPriceProduct());
//            updateStatement.setInt(3, product.getQuantityProduct());
//            updateStatement.setBoolean(4, product.isAvailableProduct());
            updateStatement.setLong(3, product.getIdProduct()); // where clause
            System.out.println(product.getNameProduct());
            System.out.println(product.getPriceProduct());
            int rowsUpdated = updateStatement.executeUpdate();

            if (rowsUpdated > 0) {
                // Update product categories
                try (PreparedStatement deleteCategoriesStatement = connection.prepareStatement(QUERY_DELETE_PRODUCT_PRODUCT_CATEGORY_BY_PRODUCT_ID)) {
                    deleteCategoriesStatement.setLong(1, product.getIdProduct());
                    deleteCategoriesStatement.executeUpdate();

                    try (PreparedStatement insertCategoriesStatement = connection.prepareStatement(QUERY_INSERT_PRODUCT_PRODUCT_CATEGORY)) {
                        for (ProductCategory category : product.getProductCategory()) {
                            insertCategoriesStatement.setLong(1, product.getIdProduct());
                            insertCategoriesStatement.setLong(2, category.getIdProductCategory());
                            insertCategoriesStatement.addBatch();
                        }
                        insertCategoriesStatement.executeBatch();


                        connection.commit();
                    }
                }
            } else {
                throw new SQLException("Failed to update product");
            }
        }
    }


    @Override
    public void deleteProduct(ProductDTO product) throws DatabaseConnectionException {
        connection = DBConnection.getConnection();


        try {
            connection.setAutoCommit(false);


            try (PreparedStatement deleteStatement = connection.prepareStatement(QUERY_DELETE_PRODUCT)) {
                deleteStatement.setLong(1, product.getIdProduct());
                deleteStatement.executeUpdate();

                try (PreparedStatement deleteProductCategoryStatement = connection.prepareStatement(QUERY_DELETE_PRODUCT_PRODUCT_CATEGORY_BY_PRODUCT_ID)) {
                    deleteProductCategoryStatement.setLong(1, product.getIdProduct());
                    deleteProductCategoryStatement.executeUpdate();

                    connection.commit();
                }
            }
        } catch (SQLException e) {
            throw new DatabaseConnectionException("Failed to connect to the database");
        }
    }




}

