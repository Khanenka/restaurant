package com.khanenka.restapiservlet.model.productdto;

import com.khanenka.restapiservlet.model.OrderStatus;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * public Класс OrderDetailDTO со свойствами <b>idOrderDetail</b>,<b>orderStatus</b>,<b>products</b>,<b>totalAmauntOrderDetail</b>
 *
 * @author Khanenka
 * *
 * * @version 1.0
 * @version $Id: $Id
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetailDTO {
    /**
     * Поле id класса OrderDetail
     */
    private long idOrderDetail;
    /**
     * Поле статус является ENUM класса OrderDetail
     */
    private OrderStatus orderStatus;
    /**
     * Поле totalAmaunt класса OrderDetail
     */
    private BigDecimal totalAmauntOrderDetail;
    /**
     * Поле List продуктов класса OrderDetail
     */
    private List<ProductDTO> products; // (OneToMany)
}
