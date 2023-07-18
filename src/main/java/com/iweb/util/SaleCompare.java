package com.iweb.util;

import com.iweb.pojo.Product;
import com.iweb.pool.ConnectionPool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Comparator;

/**
 * @author jxy
 * @date
 */
public class SaleCompare implements Comparator<Product> {
    private static final int CONNECTIONS = 20;
    private ConnectionPool connectionPool;
    public SaleCompare(){
        connectionPool = new ConnectionPool(CONNECTIONS);
    }

    @Override
    public int compare(Product o1, Product o2) {
        int sale1 = getSaleCount(o1);
        int sale2 = getSaleCount(o2);
        return Integer.compare(sale1,sale2);
    }
    private int getSaleCount(Product product){
        int sales = 0;
        String sql = "SELECT COUNT(*) FROM order_detail WHERE pid = ?";
        try(Connection connection = connectionPool.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql)){
            ps.setInt(1,product.getPid());
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                sales = rs.getInt(1);
            }
            rs.close();
            ps.close();
        }catch (Exception e){

        }
        return sales;
    }
}
