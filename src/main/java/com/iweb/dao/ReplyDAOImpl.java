package com.iweb.dao;

import com.iweb.pojo.Reply;
import com.iweb.pool.ConnectionPool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author jxy
 * @date
 */
public class ReplyDAOImpl implements ReplyDAO {
    private static final int CORE_THREADS =20;
    private static final int CONNECTIONS = 20;
    private ConnectionPool connectionPool;
    private ThreadPoolExecutor threadPool;
    public ReplyDAOImpl(){
        connectionPool = new ConnectionPool(CONNECTIONS);
        threadPool = new ThreadPoolExecutor(CORE_THREADS,25,60, TimeUnit.SECONDS,
                new LinkedBlockingDeque<Runnable>());
    }
    @Override
    public List<Reply> list(String message) {
        Connection c = connectionPool.getConnection();
        List<Reply> replies = new ArrayList<>();
        String sql = "select * from reply where message like concat('%',?,'%')";
        try {
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setString(1,message);
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                Reply r = new Reply(
                        rs.getInt(1),
                        rs.getString(2),
                        rs.getString(3));
                replies.add(r);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            connectionPool.returnConnection(c);
        }
        return replies.isEmpty()?null:replies;

    }
}

