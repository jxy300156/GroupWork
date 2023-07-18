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
}
