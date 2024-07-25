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
    /**
     * Поле id класса ProductCategory
     */
    private long idProductCategory;
    /**
     * Поле имя класса ProductCategory
     */
    private String nameProductCategory;
    /**
     * Поле тип категории ENUM CategoryType
     */
    private CategoryType typeProductCategory;
    /**
     * Поле List продуктов класса ProductCategory
     */
    private List<Product> products;

    /**
     * Конструктор - создание нового объекта с определенными значениями
     *
     * @param idProductCategory    - id
     * @param nameProductCategory  - имя
     * @param typeProductCategory - тип
     */
    public ProductCategory(long idProductCategory, String nameProductCategory, CategoryType typeProductCategory) {
        this.idProductCategory = idProductCategory;
        this.nameProductCategory = nameProductCategory;
        this.typeProductCategory = typeProductCategory;
    }
}
