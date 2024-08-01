package com.khanenka.restapiservlet.model.productdto;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * public Класс ProductDTOByNameAndTypeAndNewProduct со свойствами <b>nameProduct</b>,<b>priceProduct</b>,<b>productCategoryDTOS</b>,<b>newProduct</b>
 *
 * @author Khanenka
 * *
 * * @version 1.0
 * @version $Id: $Id
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTOByNameAndTypeAndNewProduct {
    /**
     * Поле имя класса ProductDTOByNameAndTypeAndNewProduct
     */
    private String nameProduct;
    /**
     * Поле цена класса ProductDTOByNameAndTypeAndNewProduct
     */
    private BigDecimal priceProduct;
    /**
     * Поле List продуктов класса ProductDTOByNameAndTypeAndNewProduct
     */
    private List<ProductCategoryDTOByNameAndType> productCategoryDTOS;
    /**
     * Поле новый продукт класса ProductDTOByNameAndTypeAndNewProduct
     */
    private String newProduct;
}

