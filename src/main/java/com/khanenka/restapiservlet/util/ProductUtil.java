package com.khanenka.restapiservlet.util;

import com.khanenka.restapiservlet.model.Product;
import com.khanenka.restapiservlet.model.ProductCategory;
import com.khanenka.restapiservlet.model.productDTO.ProductCategoryDTO;
import com.khanenka.restapiservlet.model.productDTO.ProductDTO;

import java.util.List;
import java.util.stream.Collectors;


public class ProductUtil {

    private ProductUtil() {
    }


    public static ProductDTO convertToDTO(Product product) {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setIdProduct(product.getIdProduct());
        productDTO.setNameProduct(product.getNameProduct());
        productDTO.setPriceProduct(product.getPriceProduct());

        List<ProductCategoryDTO> productCategoryDTOS = product.getProductCategories().stream()
                .map(pc -> new ProductCategoryDTO(pc.getIdProductCategory(),pc.getNameProductCategory(), pc.getTypeProductCategory()))
                .collect(Collectors.toList());

        productDTO.setProductCategory(productCategoryDTOS);

        return productDTO;
    }

    public static Product convertToEntity(ProductDTO productDTO) {
        Product product = new Product();
        product.setIdProduct(productDTO.getIdProduct());
        product.setNameProduct(productDTO.getNameProduct());
        product.setPriceProduct(productDTO.getPriceProduct());

        List<ProductCategory> productCategories = productDTO.getProductCategory().stream()
                .map(pcDTO -> new ProductCategory(pcDTO.getIdProductCategory(),pcDTO.getNameProductCategory(), pcDTO.getTypeProductCategory()))
                .collect(Collectors.toList());

        product.setProductCategories(productCategories);

        return product;
    }
}

