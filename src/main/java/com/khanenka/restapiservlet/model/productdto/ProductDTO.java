package com.khanenka.restapiservlet.model.productdto;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * public Класс ProductDTO со свойствами <b>idProduct</b>,<b>nameProduct</b>,<b>priceProduct</b><b>productCategory</b>
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
public class ProductDTO {
    /**
     * Поле id класса ProductDTO
     */
    private long idProduct;
    /**
     * Поле имя класса ProductDTO
     */
    private String nameProduct;
    /**
     * Поле цена класса ProductDTO
     */
    private BigDecimal priceProduct;
    /**
     * Поле List продуктов класса ProductDTO
     */
    private List<ProductCategoryDTO> productCategory;

    /**
     * Конструктор - создание нового объекта с определенными значениями
     *
     * @param idProduct    - id
     * @param nameProduct  - имя
     * @param priceProduct - цена
     */
    public ProductDTO(long idProduct, String nameProduct, BigDecimal priceProduct) {
        this.idProduct = idProduct;
        this.nameProduct = nameProduct;
        this.priceProduct = priceProduct;
    }
}
