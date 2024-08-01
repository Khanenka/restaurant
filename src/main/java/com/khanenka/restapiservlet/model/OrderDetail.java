package com.khanenka.restapiservlet.model;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * public Класс OrderDetail со свойствами <b>idOrderDetail</b>,<b>orderStatus</b>,<b>products</b>,<b>totalAmauntOrderDetail</b>
 *
 * @author Khanenka
 * *
 * * @version 1.0
 * @version $Id: $Id
 */
@Setter
@Getter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetail {
    /**
     * Поле id класса OrderDetail
     */
    private long idOrderDetail;
    /**
     * Поле статус является ENUM класса OrderStatus
     */
    private OrderStatus orderStatus;
    /**
     * Поле totalAmauntOrderDetail класса OrderDetail
     */
    private BigDecimal totalAmauntOrderDetail;
    /**
     * Поле List продуктов класса OrderDetail
     */
    private List<Product> products; // (OneToMany)


}
