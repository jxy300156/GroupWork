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
public class Address {
    private int addrId;
    private User user;
    private String province;
    private String city;
    private String detail;
}
