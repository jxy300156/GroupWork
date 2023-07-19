package com.iweb;

import com.iweb.impl.FunctionsImpl;
import com.iweb.pojo.Address;
import com.iweb.pojo.Product;
import com.iweb.pojo.User;
import com.iweb.pool.ConnectionPool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

/**
 * @author jxy
 * @date
 */
public class TestShopCart {
    private static final int CONNECTIONS = 20;
    private ConnectionPool connectionPool;
    public TestShopCart(){
        connectionPool = new ConnectionPool(CONNECTIONS);
    }
    public static void main(String[] args) {
        TestShopCart tsc = new TestShopCart();
        FunctionsImpl fi = new FunctionsImpl();
        List<Product> cart = fi.shoppingMethod();
        List<User> users = tsc.getUsers();
        User user = users.get(0);
        int addressId = 1;
        fi.shopCart(user,cart,addressId);

    }
    public List<User> getUsers() {
        LinkedList<User> userList = new LinkedList<>();
        String sql="select * from `user`";
        Connection connection= connectionPool.getConnection();
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                int id = rs.getInt("id");
                String userName1 = rs.getString("username");
                String password1 = rs.getString("username");
                String authority1 = rs.getString("authority");
                String phone1 = rs.getString("phone");
                double money1 = rs.getDouble("money");
                User user = new User(id,userName1,password1,authority1,phone1,money1);
                userList.add(user);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            connectionPool.returnConnection(connection);
        }
        return userList;
    }

}
