package com.khanenka.restapiservlet.model;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * Класс Product со свойствами <b>idProduct</b>,<b>nameProduct</b>,<b>priceProduct</b>,<b>quantityProduct</b>,<b>availableProduct</b>
 *
 * @author Khanenka
 * *
 * * @version 1.0
 */

@Setter
@Getter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class Product {

    private long idProduct;
    private String nameProduct;
    private BigDecimal priceProduct;
    private int quantityProduct;
    private boolean availableProduct;
    private List<ProductCategory> productCategories;
}