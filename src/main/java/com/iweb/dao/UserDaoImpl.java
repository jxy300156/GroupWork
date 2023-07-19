package com.iweb.dao;

import com.iweb.pojo.User;
import com.iweb.pool.ConnectionPool;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.LinkedList;

public class UserDaoImpl implements Dao<User> {
    private ConnectionPool connectionPool=new ConnectionPool(20);
    @Override
    public Collection listAll() {
        LinkedList<User> userList = new LinkedList<>();
        String sql="select * from user";
        try (Connection c= connectionPool.getConnection();
             PreparedStatement p = c.prepareStatement(sql);
        ){
            ResultSet res = p.executeQuery();
            while (res.next()){
                int id = res.getInt("id");
                String username = res.getString("username");
                String password = res.getString("password");
                String phone = res.getString("phone");
                double money = res.getDouble("money");
                String authority = res.getString("authority");
                User user =  new User(id,username,password,authority,phone,money);
                userList.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userList;
    }
    @Override
    public void insert(User user) {
        String sql="insert into user(username,password,authority,phone,money) value(?,?,?,?,?)";
        try (Connection c= connectionPool.getConnection();
             PreparedStatement p = c.prepareStatement(sql)
        ){
            if(user==null||user.getUserName()==null||user.getUserName().equals("")){
                System.out.println("参数有误，请检查");
                return ;
            }
            p.setString(1,user.getUserName());
            p.setString(2,user.getPassword());
            p.setString(4,user.getPhone());
            p.setDouble(5,user.getMoney());
            p.setString(3,user.getAuthority());
            p.execute();
            System.out.println("插入成功");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void delete(User user) {

    }

    @Override
    public void update(User user) {

    }

    @Override
    public String detail(User user) {
        return "";
    }
}
