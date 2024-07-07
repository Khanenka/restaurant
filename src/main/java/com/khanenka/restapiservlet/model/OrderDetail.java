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
 */
@Setter
@Getter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetail {
    private long idOrderDetail;
    OrderStatus orderStatus;
    List<Product> products; // (OneToMany)
    BigDecimal totalAmauntOrderDetail;

}
