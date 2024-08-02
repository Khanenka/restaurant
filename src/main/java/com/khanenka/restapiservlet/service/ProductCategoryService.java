package com.khanenka.restapiservlet.service;

import com.khanenka.restapiservlet.exceptions.DatabaseConnectionException;
import com.khanenka.restapiservlet.model.productdto.ProductCategoryDTOByNameAndType;
import com.khanenka.restapiservlet.model.productdto.ProductDTOByNameAndPrice;
import com.khanenka.restapiservlet.repository.implementation.ProductCategoryDAOImpl;
import com.khanenka.restapiservlet.repository.implementation.ProductDAOImpl;

import java.sql.SQLException;
import java.util.List;


public class ProductCategoryService {
    ProductCategoryDAOImpl productCategoryDAO;
    ProductDAOImpl productDAO;

    public ProductCategoryService() {
    }

    public ProductCategoryService(ProductCategoryDAOImpl productCategoryDAO, ProductDAOImpl productDAO) {
        this.productCategoryDAO = productCategoryDAO;
        this.productDAO = productDAO;
    }

    public List<ProductCategoryDTOByNameAndType> getAllProductCategory() {
        return productCategoryDAO.getAllProductCategories();
    }

    public void addProductCategory(ProductCategoryDTOByNameAndType productCategory) throws SQLException {
        productCategoryDAO.addProductCategory(productCategory);
        if (productCategory.getProductDTOS() != null && !productCategory.getProductDTOS().isEmpty()) {
            // Подготовка запросов для вставки продуктов и связывания их с категорией
            for (ProductDTOByNameAndPrice product : productCategory.getProductDTOS()) {
                productCategoryDAO.addProductJoinProductCategory(product.getNameProduct(),
                        productCategory.getNameProductCategory());
                productDAO.addProduct(product);
            }
        }
    }

    public void updateProductCategory(ProductCategoryDTOByNameAndType category, String newCategory) {
        try {
            productCategoryDAO.updateProductCategory(category, newCategory);
            productCategoryDAO.deleteProductProductCategoryByName(category.getNameProductCategory());
            productCategoryDAO.addProductJoinProductCategory(category.getNameProductCategory(), newCategory);
        } catch (IllegalArgumentException e) {
            throw new DatabaseConnectionException("Error updating product category: " + e.getMessage());
        }
    }

    public void deleteProductCategory(ProductCategoryDTOByNameAndType category) {
        productCategoryDAO.deleteProductCategory(category);
        productCategoryDAO.deleteProductProductCategoryByName(category.getNameProductCategory());
        for (ProductDTOByNameAndPrice product : category.getProductDTOS())
            productDAO.deleteProduct(product);
    }
}
