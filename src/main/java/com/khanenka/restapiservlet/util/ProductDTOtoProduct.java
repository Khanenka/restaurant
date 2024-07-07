package com.khanenka.restapiservlet.util;

import com.khanenka.restapiservlet.model.Product;
import com.khanenka.restapiservlet.model.productDTO.ProductDTO;

import java.math.BigDecimal;

public class ProductDTOtoProduct {

        //из entity в dto
        public ProductDTO mapToProductDto(Product entity){
            ProductDTO dto = new ProductDTO();

            dto.setNameProduct(entity.getNameProduct());
            dto.setPriceProduct(entity.getPriceProduct());
//            dto.setQuantityProduct(entity.getQuantityProduct());
//            dto.setAvailableProduct(entity.isAvailableProduct());
            return dto;
        }
        //из dto в entity
        public Product mapToProductEntity(ProductDTO dto){
            Product entity = new Product();
            entity.setNameProduct(dto.getNameProduct());
            entity.setPriceProduct(dto.getPriceProduct());
//            entity.setQuantityProduct(dto.getQuantityProduct());
//            entity.setAvailableProduct(dto.isAvailableProduct());

            return entity;
        }

}
//    private String nameProduct;
//    private BigDecimal priceProduct;
//    private int quantityProduct;
//    private boolean availableProduct;