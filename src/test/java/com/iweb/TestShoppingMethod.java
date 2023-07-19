package com.iweb;

import com.iweb.impl.FunctionsImpl;
import com.iweb.pojo.Product;

import java.util.List;

/**
 * @author jxy
 * @date
 */
public class TestShoppingMethod {
    public static void main(String[] args) {
        FunctionsImpl fi = new FunctionsImpl();
        List<Product> cart = fi.shoppingMethod();
        fi.cartOperations(cart);
    }
}
