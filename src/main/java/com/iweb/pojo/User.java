package com.iweb.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author jxy
 * @date
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class User {
    private int uid;
    private String userName;
    private String password;
    private String authority;
    private String phone;
    private double money;
}
