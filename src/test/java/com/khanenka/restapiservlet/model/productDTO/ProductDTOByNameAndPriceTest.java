//package com.khanenka.restapiservlet.model.productdto;
//
//import com.khanenka.restapiservlet.model.CategoryType;
//import org.junit.Test;
//
//import java.math.BigDecimal;
//import java.util.Arrays;
//import java.util.List;
//
//import static org.junit.Assert.assertEquals;
//
//public class ProductDTOByNameAndPriceTest {
//    @Test
//    public void testConstructorAndGetters() {
//        // Arrange
//        String nameProduct = "Test Product";
//        BigDecimal priceProduct = new BigDecimal("99.99");
//        ProductCategoryDTOByNameAndType productCategoryDTOByNameAndType = new ProductCategoryDTOByNameAndType();
//        productCategoryDTOByNameAndType.setNameProductCategory("Category1");
//        productCategoryDTOByNameAndType.setTypeProductCategory(CategoryType.DRINK);
//
//        List<ProductCategoryDTOByNameAndType> categories = Arrays.asList(productCategoryDTOByNameAndType);
//
//        // Act
//        ProductDTOByNameAndPrice productDTO = new ProductDTOByNameAndPrice(nameProduct, priceProduct, categories);
//
//        // Assert
//        assertEquals(productDTO.getNameProduct(), nameProduct);
//        assertEquals(productDTO.getPriceProduct(), priceProduct);
//        assertEquals(productDTO.getProductCategoryDTOS(), categories);
//    }
//
//    @Test
//    public void testSetters() {
//        // Arrange
//        ProductDTOByNameAndPrice productDTO = new ProductDTOByNameAndPrice();
//
//        // Act
//        productDTO.setNameProduct("New Product");
//        productDTO.setPriceProduct(new BigDecimal("49.99"));
//        ProductCategoryDTOByNameAndType productCategoryDTOByNameAndType = new ProductCategoryDTOByNameAndType();
//        productCategoryDTOByNameAndType.setNameProductCategory("Category 1");
//        productCategoryDTOByNameAndType.setTypeProductCategory(CategoryType.FISH);
//        List<ProductCategoryDTOByNameAndType> newCategories = Arrays.asList(productCategoryDTOByNameAndType);
//        productDTO.setProductCategoryDTOS(newCategories);
//
//        // Assert
//        assertEquals("New Product",productDTO.getNameProduct() );
//        assertEquals(productDTO.getPriceProduct(), new BigDecimal("49.99"));
//        assertEquals(productDTO.getProductCategoryDTOS(), newCategories);
//    }
//
//    @Test
//    public void testToString() {
//        // Arrange
//        ProductDTOByNameAndPrice productDTO = new ProductDTOByNameAndPrice("Test Product", new BigDecimal("99.99"), null);
//
//        // Act
//        String result = productDTO.toString();
//
//        String actual = "ProductDTOByNameAndPrice(nameProduct=" + productDTO.getNameProduct() + ", priceProduct=" + productDTO.getPriceProduct() + ", productCategoryDTOS=" + productDTO.getProductCategoryDTOS() + ")";
//        // Assert
//        assertEquals(result, actual);
//    }
//}