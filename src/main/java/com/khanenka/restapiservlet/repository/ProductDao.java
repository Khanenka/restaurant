package com.khanenka.restapiservlet.repository;

import com.khanenka.restapiservlet.model.productdto.ProductDTO;
import com.khanenka.restapiservlet.model.productdto.ProductDTOByNameAndPrice;

import java.sql.SQLException;
import java.util.List;

/**
 * Интерфейс для управления продуктами в системе.
 * Предоставляет методы для создания и управления таблицами продуктов и категорий,
 * а также для добавления, получения, обновления и удаления продуктов.
 * @author Khanenka
 */
public interface ProductDao {

    /**
     * Создает таблицу для хранения информации о продуктах.
     */
    void createTableProduct();

    /**
     * Создает связь между продуктами и категориями продуктов.
     */
    void createProductProductCategory();

    /**
     * Создает таблицу категорий продуктов.
     */
    void createProductCategoryTable();

    /**
     * Добавляет новый продукт в систему.
     *
     * @param productDTOByNameAndPrice объект, содержащий информацию о продукте,
     *                                  включая название и цену.
     */
    void addProduct(ProductDTOByNameAndPrice productDTOByNameAndPrice) throws SQLException;
    /**
     * Добавляет новый продукт в бд по id.
     *
     * @param product объект, содержащий информацию о продукте,
     *                                  включая название и цену.
     */
    void addProductById(ProductDTO product);
    /**
     * Получает список всех продуктов из системы.
     *
     * @return список объектов ProductDTOByNameAndPrice, представляющих все продукты.
     */
    List<ProductDTOByNameAndPrice> getAllProducts();

    /**
     * Обновляет информацию о существующем продукте.
     *
     * @param productDTOByNameAndPrice объект, содержащий обновленную информацию о продукте.
     * @param updateNameProduct название продукта, который нужно обновить.
     */
    void updateProduct(ProductDTOByNameAndPrice productDTOByNameAndPrice, String updateNameProduct);
    /**
     * Обновляет информацию о существующей категории в продукте.
     * @param productDTOByNameAndPrice объект, содержащий обновленную информацию о продукте.
     * @param productDTOByNameAndPrice название категории продукта, который нужно обновить.
     */
    void updateProductProductCategory(ProductDTOByNameAndPrice productDTOByNameAndPrice);
    /**
     * Удаляет продукт из системы.
     *
     * @param productDTOByNameAndPrice объект, содержащий информацию о продукте, который нужно удалить.
     */
    void deleteProduct(ProductDTOByNameAndPrice productDTOByNameAndPrice);
    /**
     * Удаляет продукт из системы.
     *
     * @param nameProduct имя продукта, который нужно удалить.
     */
    void deleteProductCategories(String nameProduct) throws SQLException;
}
