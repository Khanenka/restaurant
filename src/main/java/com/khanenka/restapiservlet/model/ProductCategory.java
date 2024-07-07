package com.khanenka.restapiservlet.model;

import lombok.*;

import java.util.List;

/**
 * Класс ProductCategory со свойствами <b>idProductCategory</b>,<b>nameProductCategory</b>,<b>typeProductCategory</b>,<b>products</b>
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
public class ProductCategory {
    private long idProductCategory;
    private String nameProductCategory;
    private CategoryType typeProductCategory;
    private List<Product> products;


    public ProductCategory(long idProductCategory, String nameProductCategory, CategoryType typeProductCategory) {
        this.idProductCategory=idProductCategory;
        this.nameProductCategory=nameProductCategory;
        this.typeProductCategory=typeProductCategory;
    }
}
