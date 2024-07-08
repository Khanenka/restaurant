package com.khanenka.restapiservlet.model.productDTO;

import com.khanenka.restapiservlet.model.CategoryType;
import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ProductCategoryDTO {

    private long idProductCategory;
    private String nameProductCategory;
    private CategoryType typeProductCategory;
    private List<ProductDTO> productDTOS;


    public ProductCategoryDTO(long idProductCategory, String nameProductCategory, CategoryType typeProductCategory) {
        this.idProductCategory=idProductCategory;
        this.nameProductCategory=nameProductCategory;
        this.typeProductCategory=typeProductCategory;
    }
}

