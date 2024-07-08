package com.khanenka.restapiservlet.repository;

import com.khanenka.restapiservlet.model.Product;
import com.khanenka.restapiservlet.model.productDTO.ProductDTO;
import com.khanenka.restapiservlet.model.productDTO.ProductDTOByNameAndPrice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.SQLException;
import java.util.List;

public interface ProductDao {
    void createTableProduct();

    void addProduct(ProductDTO product) throws SQLException, Exception;
    List<ProductDTO> getAllProducts() throws SQLException;
    void updateProduct(ProductDTO productDTO) throws Exception;

    void deleteProduct(ProductDTO product) throws Exception;


//    ProductDTO findAll(HttpServletRequest request,HttpServletResponse);

}
