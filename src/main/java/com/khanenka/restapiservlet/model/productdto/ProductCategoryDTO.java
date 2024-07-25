package com.khanenka.restapiservlet.model.productdto;

import com.khanenka.restapiservlet.model.CategoryType;
import lombok.*;

import java.util.List;

/**
 * public Класс ProductCategoryDTO со свойствами <b>idProductCategory</b>,<b>nameProductCategory</b>,<b>typeProductCategory</b>,<b>productDTOS</b>
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
public class ProductCategoryDTO {
    /**
     * Поле id класса ProductCategoryDTO
     */
    private long idProductCategory;
    /**
     * Поле name класса ProductCategoryDTO
     */
    private String nameProductCategory;
    /**
     * Поле type является ENUM класса ProductCategoryDTO
     */
    private CategoryType typeProductCategory;
    /**
     * Поле List product класса ProductCategoryDTO
     */
    private List<ProductDTO> productDTOS;

    /**
     * Конструктор - создание нового объекта с определенными значениями
     *
     * @param idProductCategory   - id
     * @param nameProductCategory - имя
     * @param typeProductCategory - имя
     */
    public ProductCategoryDTO(long idProductCategory, String nameProductCategory, CategoryType typeProductCategory) {
        this.idProductCategory = idProductCategory;
        this.nameProductCategory = nameProductCategory;
        this.typeProductCategory = typeProductCategory;
    }
}

