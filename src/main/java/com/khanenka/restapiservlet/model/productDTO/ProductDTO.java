package com.khanenka.restapiservlet.model.productDTO;

import com.khanenka.restapiservlet.model.ProductCategory;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@ToString
public class ProductDTO {

    private long idProduct;
    private String nameProduct;
    private BigDecimal priceProduct;
    private List<ProductCategoryDTO> productCategory;
}
