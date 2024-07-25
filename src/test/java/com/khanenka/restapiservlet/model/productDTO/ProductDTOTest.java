package com.khanenka.restapiservlet.model.productdto;

import com.khanenka.restapiservlet.model.CategoryType;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class ProductDTOTest {
    @Test
    public void testConstructorAndGetters() {
        // Arrange
        long idProduct = 1L;
        String nameProduct = "Test Product";
        BigDecimal priceProduct = new BigDecimal("99.99");

        ProductCategoryDTO productCategoryDTO = new ProductCategoryDTO();
        productCategoryDTO.setNameProductCategory("Category1");
        productCategoryDTO.setTypeProductCategory(CategoryType.DRINK);

        List<ProductCategoryDTO> categories = Arrays.asList(productCategoryDTO);

        // Act
        ProductDTO productDTO = new ProductDTO(idProduct, nameProduct, priceProduct, categories);

        // Assert
        assertEquals(productDTO.getIdProduct(), idProduct);
        assertEquals(productDTO.getNameProduct(), nameProduct);
        assertEquals(productDTO.getPriceProduct(), priceProduct);
        assertEquals(productDTO.getProductCategory(), categories);
    }

    @Test
    public void testSetters() {
        // Arrange
        ProductDTO productDTO = new ProductDTO();

        // Act
        productDTO.setIdProduct(2L);
        productDTO.setNameProduct("New Product");
        productDTO.setPriceProduct(new BigDecimal("49.99"));

        ProductCategoryDTO newProductCategoryDTO = new ProductCategoryDTO();
        newProductCategoryDTO.setNameProductCategory("Category 1");
        newProductCategoryDTO.setTypeProductCategory(CategoryType.FISH);
        List<ProductCategoryDTO> newCategories = Arrays.asList(newProductCategoryDTO);

        productDTO.setProductCategory(newCategories);

        // Assert
        assertEquals( 2L,productDTO.getIdProduct());
        assertEquals("New Product",productDTO.getNameProduct());
        assertEquals(productDTO.getPriceProduct(), new BigDecimal("49.99"));
        assertEquals(productDTO.getProductCategory(), newCategories);
    }

    @Test
    public void testToString() {
        // Arrange
        ProductDTO productDTO = new ProductDTO(1L, "Test Product", new BigDecimal("99.99"), null);

        // Act
        String result = productDTO.toString();

        String expected = "ProductDTO(idProduct=1, nameProduct=Test Product, priceProduct=99.99, productCategory=null)";

        // Assert
        assertEquals(result, expected);
    }
}