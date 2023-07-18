package com.iweb.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author jxy
 * @date
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Product {
    private int pid;
    private String pName;
    private double originalPrice;
    private Category category;
    private int stock;
    private Date createDate;
    private double promotePrice;
    private String subTitle;
    public Product(int pid, String pName, double originalPrice,double promotePrice) {
        this.pid = pid;
        this.pName = pName;
        this.originalPrice = originalPrice;
        this.promotePrice = promotePrice;
    }
}
