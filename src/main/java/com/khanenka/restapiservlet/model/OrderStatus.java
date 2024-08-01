package com.khanenka.restapiservlet.model;

import lombok.Getter;

/**
 *  ENUM OrderStatus со свойствами <b>PENDING</b>,<b>PROCESSING</b>,<b>SHIPPED</b>,<b>DELIVERED</b>,<b>CANCELLED</b>
 *
 * @author Khanenka
 * *
 * * @version 1.0
 * @version $Id: $Id
 */
@Getter
public enum OrderStatus {
    /**
     * Поле PENDING класса OrderStatus
     */
    PENDING,
    /**
     * Поле PROCESSING класса OrderStatus
     */
    PROCESSING,
    /**
     * Поле SHIPPED класса OrderStatus
     */
    SHIPPED,
    /**
     * Поле DRINK класса OrderStatus
     */
    DELIVERED,
    /**
     * Поле CANCELLED класса OrderStatus
     */
    CANCELLED
}
