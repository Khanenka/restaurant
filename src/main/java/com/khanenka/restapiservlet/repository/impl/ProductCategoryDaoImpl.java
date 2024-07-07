package com.khanenka.restapiservlet.repository.impl;

import com.khanenka.restapiservlet.DBConnection;
import com.khanenka.restapiservlet.model.CategoryType;
import com.khanenka.restapiservlet.model.productDTO.ProductCategoryDTO;
import com.khanenka.restapiservlet.repository.ProductCategoryDao;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductCategoryDaoImpl implements ProductCategoryDao {
    Connection connection;
    PreparedStatement statement;

    static final String QUERY_SELECT_ALL_PRODUCT_CATEGORY = "SELECT * FROM productcategory";

    @Override
    public void addProductCategory(HttpServletRequest req, HttpServletResponse resp) {

        try {
            connection = DBConnection.getConnection();

            statement = connection.prepareStatement("INSERT INTO productcategory(\n" +
                    "\tname, type)\n" +
                    "\tVALUES (?, ?);");
            String nameProductCategory = req.getParameter("name");
            String typeProductCategory = req.getParameter("type");


            statement.setString(1, nameProductCategory);
            statement.setString(2, typeProductCategory);
            statement.executeUpdate();
            statement.close();
        } catch (SQLException | IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<ProductCategoryDTO> getAllProductsCategory() throws RuntimeException, SQLException {
        List<ProductCategoryDTO> productCategoryDTOS = new ArrayList<>();
        connection = DBConnection.getConnection();


            statement = connection.prepareStatement(QUERY_SELECT_ALL_PRODUCT_CATEGORY);

        ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                ProductCategoryDTO productCategoryDTO = new ProductCategoryDTO();
                String nameProductCategory = resultSet.getString("name");
                String typeProductCategory = resultSet.getString("type");


                productCategoryDTO.setNameProductCategory(nameProductCategory);
                productCategoryDTO.setTypeProductCategory(CategoryType.valueOf(typeProductCategory));
                productCategoryDTOS.add(productCategoryDTO);
            }



        return productCategoryDTOS;
    }
}


