package com.khanenka.restapiservlet.service;

import com.khanenka.restapiservlet.exception.DatabaseConnectionException;
import com.khanenka.restapiservlet.model.productdto.ProductCategoryDTOByNameAndType;
import com.khanenka.restapiservlet.model.productdto.ProductDTOByNameAndPrice;
import com.khanenka.restapiservlet.repository.implementation.ProductCategoryDAOImpl;
import com.khanenka.restapiservlet.repository.implementation.ProductDAOImpl;


import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;



public class ProductCategoryService {
     static ProductCategoryDAOImpl productCategoryDAO;
     static ProductDAOImpl productDAO;

    public ProductCategoryService(ProductCategoryDAOImpl productCategoryDAO, ProductDAOImpl productDAO) {
        this.productCategoryDAO=  productCategoryDAO;
        this.productDAO = productDAO;
    }
   public List<ProductCategoryDTOByNameAndType> getAllProductCategory(){
        try {
            return productCategoryDAO.getAllProductCategories();
        } catch (SQLException e) {
            throw new DatabaseConnectionException("Error getting all product categories");
        }
    }
    public void addProductCategory(ProductCategoryDTOByNameAndType productCategory){
        productCategoryDAO.addProductCategory(productCategory);
        if (productCategory.getProductDTOS() != null && !productCategory.getProductDTOS().isEmpty()) {
       // Подготовка запросов для вставки продуктов и связывания их с категорией

                   for (ProductDTOByNameAndPrice product : productCategory.getProductDTOS()) {
//                            productStatement.setString(1, product.getNameProduct());
//                            productStatement.setBigDecimal(2, product.getPriceProduct());
                       productCategoryDAO.addProductJoinProductCategory(product.getNameProduct(),productCategory.getNameProductCategory());

                   }

           }
    }
    public void updateProductCategory(ProductCategoryDTOByNameAndType category, String newCategory) {
        try {
            productCategoryDAO.updateProductCategory(category, newCategory);
            productCategoryDAO.deleteProductProductCategoryByName(category.getNameProductCategory());
            productCategoryDAO.addProductJoinProductCategory(category.getNameProductCategory(),newCategory);
        } catch (IllegalArgumentException e) {
            throw new DatabaseConnectionException("Error updating product category: " + e.getMessage());
        }
    }
    public void deleteProductCategory(ProductCategoryDTOByNameAndType category) {
        productCategoryDAO.deleteProductCategory(category);
        productCategoryDAO.deleteProductProductCategoryByName(category.getNameProductCategory());
    }
}
