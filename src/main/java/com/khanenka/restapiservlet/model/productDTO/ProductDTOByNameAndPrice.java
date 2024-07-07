package com.khanenka.restapiservlet.model.productDTO;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

@Getter
@Setter
@ToString
public class ProductDTOByNameAndPrice {
    private long idProduct;
    private String nameProduct;
    private BigDecimal priceProduct;
}
