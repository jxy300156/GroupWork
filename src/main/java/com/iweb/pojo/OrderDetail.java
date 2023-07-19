package com.iweb.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author jxy
 * @date
 */
@Data
@NoArgsConstructor
@AllArgsConstructor

public class OrderDetail {
    private int odId;
    private Order order;
    private Product product;
    private int quantity;

    public OrderDetail(Order order, Product product, int quantity) {
        this.order = order;
        this.product = product;
        this.quantity = quantity;
    }
}
