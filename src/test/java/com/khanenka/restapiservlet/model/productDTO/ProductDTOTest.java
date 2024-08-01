package com.khanenka.restapiservlet.model.productDTO;

import com.khanenka.restapiservlet.model.CategoryType;
import com.khanenka.restapiservlet.model.productdto.ProductCategoryDTO;
import com.khanenka.restapiservlet.model.productdto.ProductDTO;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class ProductDTOTest {
    @Test
    public void testConstructorAndGetters() {
        long idProduct = 1L;
        String nameProduct = "Test Product";
        BigDecimal priceProduct = new BigDecimal("99.99");
        ProductCategoryDTO productCategoryDTO = new ProductCategoryDTO();
        productCategoryDTO.setNameProductCategory("Category1");
        productCategoryDTO.setTypeProductCategory(CategoryType.DRINK);
        List<ProductCategoryDTO> categories = Arrays.asList(productCategoryDTO);
        ProductDTO productDTO = new ProductDTO(idProduct, nameProduct, priceProduct, categories);
        assertEquals(productDTO.getIdProduct(), idProduct);
        assertEquals(productDTO.getNameProduct(), nameProduct);
        assertEquals(productDTO.getPriceProduct(), priceProduct);
        assertEquals(productDTO.getProductCategory(), categories);
    }

    @Test
    public void testSetters() {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setIdProduct(2L);
        productDTO.setNameProduct("New Product");
        productDTO.setPriceProduct(new BigDecimal("49.99"));
        ProductCategoryDTO newProductCategoryDTO = new ProductCategoryDTO();
        newProductCategoryDTO.setNameProductCategory("Category 1");
        newProductCategoryDTO.setTypeProductCategory(CategoryType.FISH);
        List<ProductCategoryDTO> newCategories = Arrays.asList(newProductCategoryDTO);
        productDTO.setProductCategory(newCategories);
        assertEquals( 2L,productDTO.getIdProduct());
        assertEquals("New Product",productDTO.getNameProduct());
        assertEquals(productDTO.getPriceProduct(), new BigDecimal("49.99"));
        assertEquals(productDTO.getProductCategory(), newCategories);
    }

    @Test
    public void testToString() {
        ProductDTO productDTO = new ProductDTO(1L, "Test Product", new BigDecimal("99.99"), null);
        String result = productDTO.toString();
        String expected = "ProductDTO(idProduct=1, nameProduct=Test Product, priceProduct=99.99, productCategory=null)";
        assertEquals(result, expected);
    }
}