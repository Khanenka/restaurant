package com.khanenka.restapiservlet.service;

import com.khanenka.restapiservlet.exception.DatabaseConnectionException;
import com.khanenka.restapiservlet.model.productdto.ProductDTOByNameAndPrice;
import com.khanenka.restapiservlet.repository.implementation.OrderDetailDAOImpl;
import com.khanenka.restapiservlet.repository.implementation.ProductCategoryDAOImpl;
import com.khanenka.restapiservlet.repository.implementation.ProductDAOImpl;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

public class ProductService {
    static ProductDAOImpl productDAO;
    static ProductCategoryDAOImpl productCategoryDAO;
    static OrderDetailDAOImpl orderDetailDAO;


    public ProductService(ProductDAOImpl productDAO,ProductCategoryDAOImpl productCategoryDAO,OrderDetailDAOImpl orderDetailDAO) {
        this.productDAO = productDAO;
        this.productCategoryDAO = productCategoryDAO;
        this.orderDetailDAO = orderDetailDAO;

    }

    public List<ProductDTOByNameAndPrice> getAllProducts() {
        return productDAO.getAllProducts();
    }

    public void addProduct(ProductDTOByNameAndPrice product) {
        validateProduct(product);
        try {
            productDAO.addProduct(product);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void updateProduct(ProductDTOByNameAndPrice product, String updatedNameProduct) {
        try {
            productDAO.updateProduct(product, updatedNameProduct);
        } catch (IllegalArgumentException e) {
            throw new DatabaseConnectionException("Error updating product: " + e.getMessage());
        }
    }
    public void deleteProduct(ProductDTOByNameAndPrice product) {
        try {
            productDAO.deleteProduct(product);
        } catch (DatabaseConnectionException e) {
            // Здесь вы можете логировать исключение или выполнять другую обработку
            throw new DatabaseConnectionException("Error when delete product: " + e.getMessage());
        }
    }
    private void validateProduct(ProductDTOByNameAndPrice product) {
        if (product.getNameProduct() == null || product.getNameProduct().isEmpty()) {
            throw new IllegalArgumentException("Product name cannot be null or empty");
        }
        if (product.getPriceProduct() == null || product.getPriceProduct().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Price should be greater than zero");
        }
    }
}
