package com.khanenka.restapiservlet.service;


import com.khanenka.restapiservlet.exceptions.DatabaseConnectionException;
import com.khanenka.restapiservlet.model.productdto.ProductCategoryDTOByNameAndType;
import com.khanenka.restapiservlet.model.productdto.ProductDTOByNameAndPrice;
import com.khanenka.restapiservlet.repository.OrderDetailDao;
import com.khanenka.restapiservlet.repository.ProductCategoryDao;
import com.khanenka.restapiservlet.repository.ProductDao;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

public class ProductService {
    ProductDao productDAO;
    ProductCategoryDao productCategoryDAO;
    OrderDetailDao orderDetailDAO;

    public ProductService() {
    }

    public ProductService(ProductDao productDAO,
                          ProductCategoryDao productCategoryDAO, OrderDetailDao orderDetailDAO) {
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
            throw new DatabaseConnectionException("Error in add product");
        }
    }

    public void updateProduct(ProductDTOByNameAndPrice product, String updatedNameProduct) {
        productDAO.updateProductProductCategory(product);
        productDAO.updateProduct(product, updatedNameProduct);
    }

    public void deleteProduct(ProductDTOByNameAndPrice product) {
        try {
            productDAO.deleteProduct(product);
            productDAO.deleteProductCategories(product.getNameProduct());
            for (ProductCategoryDTOByNameAndType category : product.getProductCategoryDTOS())
                productCategoryDAO.deleteProductCategory(category);
        } catch (Exception e) {
            throw new DatabaseConnectionException("Error in delete product");
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
