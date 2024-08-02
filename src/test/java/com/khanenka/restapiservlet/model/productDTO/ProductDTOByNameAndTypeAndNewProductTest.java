package com.khanenka.restapiservlet.model.productDTO;

import com.khanenka.restapiservlet.model.productdto.ProductCategoryDTOByNameAndType;
import com.khanenka.restapiservlet.model.productdto.ProductDTOByNameAndTypeAndNewProduct;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class ProductDTOByNameAndTypeAndNewProductTest {

    @Test
    public void testNoArgsConstructorAndSetters() {
        ProductDTOByNameAndTypeAndNewProduct productDTO = new ProductDTOByNameAndTypeAndNewProduct();
        productDTO.setNameProduct("Product2");
        productDTO.setPriceProduct(BigDecimal.valueOf(200.00));
        ProductCategoryDTOByNameAndType productCategoryDTOByNameAndType = new ProductCategoryDTOByNameAndType();
        productCategoryDTOByNameAndType.setNameProductCategory("Category");
        productDTO.setProductCategoryDTOS(Arrays.asList(productCategoryDTOByNameAndType));
        productDTO.setNewProduct("New Product");
        assertEquals(productDTO.getNameProduct(), "Product2");
        assertEquals(productDTO.getPriceProduct(), BigDecimal.valueOf(200.00));
        assertEquals("Category", productDTO.getProductCategoryDTOS().get(0).getNameProductCategory());
        assertEquals("New Product", productDTO.getNewProduct());

    }

    @Test
    public void testToString() {
        ProductDTOByNameAndTypeAndNewProduct productDTO = new ProductDTOByNameAndTypeAndNewProduct(
                "Product3", BigDecimal.valueOf(300), null, "New Product"
        );
        String expectedString =
                "ProductDTOByNameAndTypeAndNewProduct(nameProduct=Product3, " +
                        "priceProduct=300, productCategoryDTOS=null, newProduct=New Product)";
        assertEquals(productDTO.toString(), expectedString);
    }
}
