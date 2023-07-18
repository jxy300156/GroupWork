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
public class CommandCompare implements Comparator<Product> {
    private static final int CONNECTIONS = 20;
    private ConnectionPool connectionPool;
    public CommandCompare(){
        connectionPool = new ConnectionPool(CONNECTIONS);
    }
    @Override
    public int compare(Product o1, Product o2) {
        int stockCount1 = getStockCount(o1);
        int stockCount2 = getStockCount(o2);
        return Integer.compare(stockCount1,stockCount2);
    }
    private int getStockCount(Product product){
        int stocks = 0;
        String sql = "SELECT stock FROM product WHERE id = ?";
        try(Connection connection = connectionPool.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql)){
            ps.setInt(1,product.getPid());
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                stocks = rs.getInt(1);
            }
            rs.close();
            ps.close();
        }catch (Exception e){

        }
        return stocks;
    }
}
