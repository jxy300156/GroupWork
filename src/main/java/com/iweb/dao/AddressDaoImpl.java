package com.iweb.dao;



import com.iweb.pojo.Address;
import com.iweb.pojo.User;
import com.iweb.pool.ConnectionPool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.LinkedList;

public class AddressDaoImpl implements Dao<Address> {
    //
    private ConnectionPool connectionPool=new ConnectionPool(20);
    UserDaoImpl userDao = new UserDaoImpl();
    LinkedList<User> userList = (LinkedList<User>) userDao.listAll();
    @Override
    public Collection listAll() {
        LinkedList<Address> addressList = new LinkedList<>();
        String sql="select * from address";
        try (Connection c= connectionPool.getConnection();
             PreparedStatement p = c.prepareStatement(sql);
        ){
            ResultSet res = p.executeQuery();
            while (res.next()){
                int rid = res.getInt("rid");
                int uid = res.getInt("uid");
                String province_addr=res.getString("province_addr");
                String city_addr=res.getString("city_addr");
                String detail_addr=res.getString("detail_addr");
                for (int i = 0; i < userList.size(); i++) {
                    User u = userList.get(i);
                    if(u.getUid()==uid){
                        Address address = new Address(rid,u,province_addr,city_addr,detail_addr);
                        addressList.add(address);
                    }
                }

            }
            for (Address a: addressList) {
                System.out.println(a);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return addressList;
    }

    @Override
    public void insert(Address address) {
        String sql="insert into address(uid,province_addr,city_addr,detail_addr) value(?,?,?,?)";
        try (Connection c= connectionPool.getConnection();
             PreparedStatement p = c.prepareStatement(sql)
        ){
            if(address==null){
                System.out.println("参数有误，请检查");
                return ;
            }
            p.setInt(1,address.getUser().getUid());
            p.setString(2,address.getProvince());
            p.setString(3,address.getCity());
            p.setString(4,address.getCity());
            p.execute();
            System.out.println("地址添加成功");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(Address address) {
        String sql="delete from address where rid=?";
        try (Connection c= connectionPool.getConnection();
             PreparedStatement p = c.prepareStatement(sql)
        ){
            if(address==null){
                System.out.println("参数有误，请检查");
                return ;
            }
            p.setInt(1,address.getAddrId());
            p.execute();
            System.out.println("地址删除成功");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Address address) {
        String sql = "update address set uid=? , province_addr=? ,city_addr=? , detail_addr=? where rid=?";
        try (Connection c= connectionPool.getConnection();
             PreparedStatement p = c.prepareStatement(sql)
        ){
            p.setInt(1,address.getUser().getUid());
            p.setString(2,address.getProvince());
            p.setString(3,address.getCity());
            p.setString(4,address.getDetail());
            p.setInt(5,address.getAddrId());
            p.execute();
            System.out.println("地址修改成功");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String detail(Address address) {
        return null;
    }
}
