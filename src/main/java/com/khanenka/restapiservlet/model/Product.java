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
 * @version $Id: $Id
 */

@Setter
@Getter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class Product {
    /**
     * Поле id класса Product
     */
    private long idProduct;
    /**
     * Поле имя класса Product
     */
    private String nameProduct;
    /**
     * Поле цена класса Product
     */
    private BigDecimal priceProduct;
    /**
     * Поле количество класса Product
     */
    private int quantityProduct;
    /**
     * Поле наличие класса Product
     */
    private boolean availableProduct;
    /**
     * Поле List Category класса Product
     */
    private List<ProductCategory> productCategories;

    /**
     * Конструктор - создание нового объекта с определенными значениями
     *
     * @param idProduct    - id
     * @param nameProduct  - имя
     * @param priceProduct - цена
     */
    public Product(long idProduct, String nameProduct, BigDecimal priceProduct) {
        this.idProduct = idProduct;
        this.nameProduct = nameProduct;
        this.priceProduct = priceProduct;
    }
}
