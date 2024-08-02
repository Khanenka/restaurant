package com.khanenka.restapiservlet.model.productDTO;

import com.khanenka.restapiservlet.model.CategoryType;
import com.khanenka.restapiservlet.model.productdto.ProductCategoryDTO;
import com.khanenka.restapiservlet.model.productdto.ProductDTO;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class ProductCategoryDTOTest {

    @Test
    public void testConstructorAndGetters() {
        // Arrange
        long idProductCategory = 1L;
        String nameProductCategory = "Category1";
        CategoryType typeProductCategory = CategoryType.DRINK;
        ProductDTO productDto = new ProductDTO(1L, "Test Product", new BigDecimal("99.99"));
        List<ProductDTO> productDTOS = Arrays.asList(productDto);
        ProductCategoryDTO productCategoryDTO =
                new ProductCategoryDTO(idProductCategory, nameProductCategory, typeProductCategory, productDTOS);
        assertEquals(productCategoryDTO.getIdProductCategory(), idProductCategory);
        assertEquals(productCategoryDTO.getNameProductCategory(), nameProductCategory);
        assertEquals(productCategoryDTO.getTypeProductCategory(), typeProductCategory);
        assertEquals(productCategoryDTO.getProductDTOS(), productDTOS);
    }

    @Test
    public void testSetters() {
        ProductCategoryDTO productCategoryDTO = new ProductCategoryDTO();
        long newId = 2L;
        productCategoryDTO.setIdProductCategory(newId);
        productCategoryDTO.setNameProductCategory("New Category 1");
        productCategoryDTO.setTypeProductCategory(CategoryType.FISH);
        ProductDTO newProductDto = new ProductDTO(
                1L, "Another Product", new BigDecimal("49.99"));
        List<ProductDTO> newProductDTOS = Arrays.asList(newProductDto);
        productCategoryDTO.setProductDTOS(newProductDTOS);
        assertEquals(productCategoryDTO.getIdProductCategory(), newId);
        assertEquals("New Category 1", productCategoryDTO.getNameProductCategory());
        assertEquals(CategoryType.FISH, productCategoryDTO.getTypeProductCategory());
        assertEquals(productCategoryDTO.getProductDTOS(), newProductDTOS);
    }

    @Test
    public void testToString() {
        long idProductCategory = 1L;
        ProductCategoryDTO productCategoryDTO =
                new ProductCategoryDTO(
                        idProductCategory, "Category1", CategoryType.DRINK, null);
        String result = productCategoryDTO.toString();
        String expected = "ProductCategoryDTO(idProductCategory=1," +
                " nameProductCategory=Category1, typeProductCategory=DRINK, productDTOS=null)";
        assertEquals(result, expected);
    }
}
