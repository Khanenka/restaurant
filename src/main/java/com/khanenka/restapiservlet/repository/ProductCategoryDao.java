package com.khanenka.restapiservlet.repository;

import com.khanenka.restapiservlet.model.productdto.ProductCategoryDTOByNameAndType;

import java.sql.SQLException;
import java.util.List;

/**
 * Интерфейс для управления категориями продуктов.
 * Предоставляет методы для добавления, получения, обновления и удаления категорий продуктов.
 * @author Khanenka
 */
public interface ProductCategoryDao {

    /**
     * Добавляет новую категорию продукта.
     *
     * @param category объект категории продукта, который нужно добавить.
     */
    void addProductCategory(ProductCategoryDTOByNameAndType category);

    /**
     * Получает список всех категорий продуктов.
     *
     * @return список объектов категорий продуктов.
     * @throws SQLException если возникла проблема при доступе к базе данных.
     */
    List<ProductCategoryDTOByNameAndType> getAllProductCategories() throws SQLException;

    /**
     * Обновляет существующую категорию продукта.
     *
     * @param category    объект категории продукта, который нужно обновить.
     * @param newCategory новое имя или тип категории продукта.
     * @throws SQLException если возникла проблема при доступе к базе данных.
     */
    void updateProductCategory(ProductCategoryDTOByNameAndType category, String newCategory) throws SQLException;

    /**
     * Удаляет категорию продукта.
     *
     * @param category объект категории продукта, которую нужно удалить.
     */
    void deleteProductCategory(ProductCategoryDTOByNameAndType category);
}
