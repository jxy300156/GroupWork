package com.iweb.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author jxy
 * @date
 */
@AllArgsConstructor
@Data
@NoArgsConstructor
public class Order {
    private int oid;
    private User user;
    private Address address;
    private Date oDate;
}
