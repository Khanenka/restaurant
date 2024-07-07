package com.khanenka.restapiservlet.repository;

import com.khanenka.restapiservlet.model.productDTO.ProductCategoryDTO;
import com.khanenka.restapiservlet.model.productDTO.ProductDTO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.SQLException;
import java.util.List;

public interface ProductCategoryDao {


    void addProductCategory(HttpServletRequest req, HttpServletResponse resp);
    List<ProductCategoryDTO> getAllProductsCategory() throws RuntimeException, SQLException;
}
