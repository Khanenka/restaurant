package com.khanenka.restapiservlet.model.productdto;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * public Класс ProductDTOByNameAndPrice со свойствами <b>nameProduct</b>,<b>priceProduct</b>,<b>productCategoryDTOS</b>
 *
 * @author Khanenka
 * *
 * * @version 1.0
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTOByNameAndPrice {
    /**
     * Поле имя класса ProductDTOByNameAndPrice
     */
    private String nameProduct;
    /**
     * Поле цена класса ProductDTOByNameAndPrice
     */
    private BigDecimal priceProduct;
    /**
     * Поле List продуктов класса ProductDTOByNameAndPrice
     */
    private List<ProductCategoryDTOByNameAndType> productCategoryDTOS;
    private String newProduct;


}
