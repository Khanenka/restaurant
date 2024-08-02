package com.khanenka.restapiservlet.util;

public class QueryInDB {
    private QueryInDB() {
    }

    public static final String QUERY_INSERT_ORDER_DETAIL = "INSERT INTO order_detail" +
            " (order_detail_id,order_status, total_amount) VALUES (?,?,?)";
    public static final String QUERY_SELECT_ALL_ORDER_DETAIL = "SELECT DISTINCT * FROM order_detail";
    public static final String UPDATE_ORDER_DETAIL_SQL = "UPDATE order_detail set order_status = ?," +
            " total_amount = ? WHERE order_detail_id = ?";
    public static final String QUERY_DELETE_ORDER_DETAIL = "DELETE FROM order_detail WHERE order_detail_id = ?";
    public static final String QUERY_SELECT_PRODUCTS_BY_ORDER_DETAIL_ID = "SELECT p.* FROM product" +
            " p JOIN order_detail_product odp ON p.idproduct= odp.product_id WHERE odp.order_detail_id = ?";
    public static final String QUERY_INSERT_ORDER_DETAIL_PRODUCT = "INSERT INTO order_detail_product" +
            " (order_detail_id, product_id) VALUES (?,?)";
    public static final String QUERY_DELETE_ORDER_DETAIL_PRODUCT_BY_ORDER_DETAIL_ID =
            "delete from order_detail_product where order_detail_id = ?;";
    public static final String QUERY_CREATE_TABLE_ORDER_DETAIL = "CREATE TABLE if not exists order_detail" +
            " (order_detail_id  SERIAL PRIMARY KEY , order_status character varying(50) ,total_amount numeric(10,2))";
    public static final String QUERY_CREATE_TABLE_ORDER_DETAIL_PRODUCT = "create table  order_detail_product" +
            " (order_detail_id INT,  product_id INT)";
    public static final String QUERY_INSERT_PRODUCT_CATEGORY = "INSERT INTO productcategory (\"name\", \"type\")" +
            " VALUES ( ?, ?)";
    public static final String QUERY_SELECT_ALL_PRODUCT_CATEGORY = "SELECT \"name\",\"type\"" +
            " FROM productcategory";
    public static final String QUERY_JOIN_PRODUCT_PRODUCT_CATEGORY_BY_NAME = "SELECT DISTINCT p.nameproduct," +
            "p.priceproduct FROM product p JOIN product_productcategory ppc ON " +
            "p.nameproduct = ppc.nameproduct WHERE ppc.name = ?";
    public static final String QUERY_DELETE_PRODUCT_PRODUCT_CATEGORY_BY_PRODUCT_NAME = "DELETE" +
            " FROM product_productcategory WHERE \"name\" = ?";
    public static final String QUERY_DELETE_PRODUCT_PRODUCT_CATEGORY_BY_PRODUCT_CATEGORY_NAME = "DELETE" +
            " FROM product_productcategory WHERE \"name\" = ?";
    public static final String QUERY_DELETE_PRODUCT_CATEGORY = "DELETE FROM productcategory WHERE \"name\" = ?";
    public static final String QUERY_PRODUCT_CATEGORY_TABLE = "CREATE TABLE IF NOT EXISTS productcategory" +
            "(id SERIAL PRIMARY KEY , name character varying(255) ,  type character varying(255))";
    public static final String UPDATE_PRODUCT_CATEGORY_SQL = "UPDATE productcategory set name = ?, type = ?" +
            " WHERE name = ?";
    public static final String UPDATE_PRODUCT_PRODUCT_CATEGORY_BY_NAME = "UPDATE product_productcategory" +
            " set \"nameproduct\" = ? WHERE \"nameproduct\" = ?";
    public static final String QUERY_CREATE_TABLE_PRODUCT = """
            create table if not exists product (
                idproduct SERIAL PRIMARY KEY,
                nameproduct character varying(100),
                priceproduct decimal(10,2),
                quantityproduct INT,
                availableproduct boolean
            )
            """;
    public static final String QUERY_INSERT_PRODUCT = "INSERT INTO product (nameproduct, priceproduct) VALUES (?,?)";
    public static final String QUERY_SELECT_ALL_PRODUCT = "SELECT \"nameproduct\",\"priceproduct\"," +
            "\"quantityproduct\",\"availableproduct\" FROM product";
    public static final String UPDATE_PRODUCT_SQL = "UPDATE product set nameproduct = ?," +
            " priceproduct = ? WHERE nameproduct = ?;";
    public static final String QUERY_DELETE_PRODUCT = "DELETE FROM product WHERE \"nameproduct\"=?";
    static final String QUERY_PRODUCT = "INSERT INTO product (\"nameproduct\", \"priceproduct\") VALUES (?,?)";
    public static final String QUERY_INSERT_PRODUCT_PRODUCT_CATEGORY = "INSERT INTO product_productcategory" +
            " (nameproduct, name) VALUES (?, ?)";
    public static final String QUERY_INSERT_PRODUCT_WITH_ID =
            "INSERT INTO product (idproduct,nameproduct, priceproduct) VALUES (?,?,?)";
    public static final String QUERY_JOIN_PRODUCT_CATEGORY_PRODUCT_BY_NAME = "SELECT DISTINCT pc.name,pc.type" +
            " FROM productcategory pc JOIN product_productcategory ppc ON pc.name = ppc.name WHERE ppc.nameproduct = ?";
    public static final String QUERY_CREATE_PRODUCT_PRODUCT_CATEGORY_TABLE = "create table" +
            " if not exists product_productcategory (name character varying(100), nameproduct character varying(100))";
    public static final String NAME_PRODUCT = "nameProduct"; // Имя продукта
    public static final String PRICE_PRODUCT = "priceProduct"; // Цена продукта
    public static final String CATEGORY_NAME = "name";
    public static final String CATEGORY_TYPE = "type";
    public static final String ORDER_STATUS = "order_status";
    public static final String TOTAL_AMOUNT = "total_amount";
}
