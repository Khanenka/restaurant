package com.khanenka.restapiservlet.model.productdto;

import com.khanenka.restapiservlet.model.CategoryType;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class ProductCategoryDTOByNameAndTypeAndNewCategoryTest {
    @Test
    public void testConstructorAndGetters() {
        // Arrange
        String nameProductCategory = "Category1";
        CategoryType typeProductCategory = CategoryType.DRINK;

        ProductDTOByNameAndPrice productDto = new ProductDTOByNameAndPrice("Test Product", new BigDecimal("99.99"), null);
        List<ProductDTOByNameAndPrice> productDTOS = Arrays.asList(productDto);

        String newCategory = "New Category";

        // Act
        ProductCategoryDTOByNameAndTypeAndNewCategory productCategoryDTO =
                new ProductCategoryDTOByNameAndTypeAndNewCategory(
                        nameProductCategory,
                        typeProductCategory,
                        productDTOS,
                        newCategory
                );

        // Assert
        assertEquals(productCategoryDTO.getNameProductCategory(), nameProductCategory);
        assertEquals(productCategoryDTO.getTypeProductCategory(), typeProductCategory);
        assertEquals(productCategoryDTO.getProductDTOS(), productDTOS);
        assertEquals(productCategoryDTO.getNewCategory(), newCategory);
    }

    @Test
    public void testSetters() {
        // Arrange
        ProductCategoryDTOByNameAndTypeAndNewCategory productCategoryDTO = new ProductCategoryDTOByNameAndTypeAndNewCategory();

        // Act
        productCategoryDTO.setNameProductCategory("New Category 1");
        productCategoryDTO.setTypeProductCategory(CategoryType.FISH);
        String newCategory = "Updated New Category";
        productCategoryDTO.setNewCategory(newCategory);

        ProductDTOByNameAndPrice newProductDto = new ProductDTOByNameAndPrice("Another Product", new BigDecimal("49.99"), null);
        List<ProductDTOByNameAndPrice> newProductDTOS = Arrays.asList(newProductDto);
        productCategoryDTO.setProductDTOS(newProductDTOS);

        // Assert
        assertEquals( "New Category 1",productCategoryDTO.getNameProductCategory());
        assertEquals(CategoryType.FISH,productCategoryDTO.getTypeProductCategory() );
        assertEquals(productCategoryDTO.getNewCategory(), newCategory);
        assertEquals(productCategoryDTO.getProductDTOS(), newProductDTOS);
    }

    @Test
    public void testToString() {
        // Arrange
        ProductCategoryDTOByNameAndTypeAndNewCategory productCategoryDTO =
                new ProductCategoryDTOByNameAndTypeAndNewCategory("Category1", CategoryType.DRINK, null, "New Category");

        // Act
        String result = productCategoryDTO.toString();

        String expected = "ProductCategoryDTOByNameAndTypeAndNewCategory(nameProductCategory=Category1, typeProductCategory=DRINK, productDTOS=null, newCategory=New Category)";

        // Assert
        assertEquals(result, expected);
    }
}