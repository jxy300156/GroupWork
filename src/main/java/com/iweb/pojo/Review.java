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
public class Review {
    private int rid;
    private User user;
    private Product product;
    private int rating;
    private String comment;

    public Review(User user, Product product, int rating, String comment) {
        this.user = user;
        this.product = product;
        this.rating = rating;
        this.comment = comment;
    }
}
