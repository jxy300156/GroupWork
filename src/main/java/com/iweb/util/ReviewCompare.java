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
public class ReviewCompare implements Comparator<Product> {
    private static final int CONNECTIONS = 20;
    private ConnectionPool connectionPool;
    public ReviewCompare(){
        connectionPool = new ConnectionPool(CONNECTIONS);
    }
    @Override
    public int compare(Product o1, Product o2) {
        int reviewQuantity1 = getReviewQuantity(o1);
        int reviewQuantity2 = getReviewQuantity(o2);
        return Integer.compare(reviewQuantity1,reviewQuantity2);
    }
    private int getReviewQuantity(Product product){
        int quantity = 0;
        String sql = "SELECT avg(rating) FROM review WHERE pid = ?";
        try(Connection connection = connectionPool.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql)){
            ps.setInt(1,product.getPid());
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                quantity = rs.getInt(1);
            }
            rs.close();
            ps.close();
        }catch (Exception e){

        }
        return quantity;
    }
}
