package com.khanenka.restapiservlet.model;

import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class OrderStatusTest {
    @Test
    public void testOrderStatusValues() {
        OrderStatus[] expectedStatuses = {
                OrderStatus.PENDING,
                OrderStatus.PROCESSING,
                OrderStatus.SHIPPED,
                OrderStatus.DELIVERED,
                OrderStatus.CANCELLED
        };

        assertArrayEquals(expectedStatuses, OrderStatus.values());
    }

    @Test
    public void testOrderStatusCount() {
        assertEquals(5, OrderStatus.values().length);
    }

    @Test
    public void testOrderStatusEnumNames() {
        String[] expectedNames = {
                "PENDING",
                "PROCESSING",
                "SHIPPED",
                "DELIVERED",
                "CANCELLED"
        };

        String[] actualNames = new String[OrderStatus.values().length];
        for (int i = 0; i < OrderStatus.values().length; i++) {
            actualNames[i] = OrderStatus.values()[i].name();
        }

        assertArrayEquals(expectedNames, actualNames);
    }

    @Test
    public void testOrderStatusOrdinal() {
        assertEquals(0, OrderStatus.PENDING.ordinal());
        assertEquals(1, OrderStatus.PROCESSING.ordinal());
        assertEquals(2, OrderStatus.SHIPPED.ordinal());
        assertEquals(3, OrderStatus.DELIVERED.ordinal());
        assertEquals(4, OrderStatus.CANCELLED.ordinal());
    }
}