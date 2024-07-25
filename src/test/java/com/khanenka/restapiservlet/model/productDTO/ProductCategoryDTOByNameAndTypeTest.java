package com.khanenka.restapiservlet.model.productdto;

import com.khanenka.restapiservlet.model.CategoryType;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class ProductCategoryDTOByNameAndTypeTest {
    @Test
    public void testConstructorAndGetters() {
        // Arrange
        String nameProductCategory = "Category1";
        CategoryType typeProductCategory = CategoryType.DRINK;

        ProductDTOByNameAndPrice productDto = new ProductDTOByNameAndPrice("Test Product", new BigDecimal("99.99"), null);
        List<ProductDTOByNameAndPrice> productDTOS = Arrays.asList(productDto);

        // Act
        ProductCategoryDTOByNameAndType productCategoryDTO =
                new ProductCategoryDTOByNameAndType(
                        nameProductCategory,
                        typeProductCategory,
                        productDTOS
                );

        // Assert
        assertEquals(productCategoryDTO.getNameProductCategory(), nameProductCategory);
        assertEquals(productCategoryDTO.getTypeProductCategory(), typeProductCategory);
        assertEquals(productCategoryDTO.getProductDTOS(), productDTOS);
    }

    @Test
    public void testSetters() {
        // Arrange
        ProductCategoryDTOByNameAndType productCategoryDTO = new ProductCategoryDTOByNameAndType();

        // Act
        productCategoryDTO.setNameProductCategory("New Category 1");
        productCategoryDTO.setTypeProductCategory(CategoryType.FISH);

        ProductDTOByNameAndPrice newProductDto = new ProductDTOByNameAndPrice("Another Product", new BigDecimal("49.99"), null);
        List<ProductDTOByNameAndPrice> newProductDTOS = Arrays.asList(newProductDto);
        productCategoryDTO.setProductDTOS(newProductDTOS);

        // Assert
        assertEquals("New Category 1",productCategoryDTO.getNameProductCategory() );
        assertEquals( CategoryType.FISH,productCategoryDTO.getTypeProductCategory());
        assertEquals(productCategoryDTO.getProductDTOS(), newProductDTOS);
    }

    @Test
    public void testToString() {
        // Arrange
        ProductDTOByNameAndPrice productDto = new ProductDTOByNameAndPrice("Test Product", new BigDecimal("99.99"), null);
        List<ProductDTOByNameAndPrice> productDtOS = Arrays.asList(productDto);

        ProductCategoryDTOByNameAndType productCategoryDTO =
                new ProductCategoryDTOByNameAndType("Category1", CategoryType.DRINK, productDtOS);

        // Act
        String result = productCategoryDTO.toString();

        // To verify the format of toString, adjust according to your own `toString()` implementation
        String expected = "ProductCategoryDTOByNameAndType(nameProductCategory=" + productCategoryDTO.getNameProductCategory() + ", typeProductCategory=" + productCategoryDTO.getTypeProductCategory() + ", productDTOS=" + productCategoryDTO.getProductDTOS() + ")";

        // Assert
        assertEquals(result, expected);
    }
}