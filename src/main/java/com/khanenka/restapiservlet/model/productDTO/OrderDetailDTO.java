package com.khanenka.restapiservlet.model.productDTO;

import com.khanenka.restapiservlet.model.OrderStatus;
import com.khanenka.restapiservlet.model.Product;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetailDTO {

    private long idOrderDetail;
    OrderStatus orderStatus;
    BigDecimal totalAmauntOrderDetail;
    List<ProductDTO> products; // (OneToMany)

}
