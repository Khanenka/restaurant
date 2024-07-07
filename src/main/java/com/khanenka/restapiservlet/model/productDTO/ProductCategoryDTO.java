package com.khanenka.restapiservlet.model.productDTO;

import com.khanenka.restapiservlet.model.CategoryType;
import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ProductCategoryDTO {

    private long idProductCategory;
    private String nameProductCategory;
    private CategoryType typeProductCategory;


}

