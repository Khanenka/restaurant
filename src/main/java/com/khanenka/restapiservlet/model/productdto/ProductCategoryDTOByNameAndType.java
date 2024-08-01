package com.khanenka.restapiservlet.model.productdto;

import com.khanenka.restapiservlet.model.CategoryType;
import lombok.*;

import java.util.List;

/**
 * public Класс ProductCategoryDTOByNameAndType со свойствами <b>nameProductCategory</b>,<b>typeProductCategory</b>,<b>productDTOS</b>
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
public class ProductCategoryDTOByNameAndType {
    /**
     * Поле имя класса ProductCategoryDTOByNameAndType
     */
    private String nameProductCategory;
    /**
     * Поле тип является ENUM класса ProductCategoryDTOByNameAndType
     */
    private CategoryType typeProductCategory;
    /**
     * Поле List продуктов класса ProductCategoryDTOByNameAndType
     */
    private List<ProductDTOByNameAndPrice> productDTOS;
    /**
     * Поле новая категория класса ProductCategoryDTOByNameAndTypeAndNewCategory
     */
    private String newCategory;

}
