package com.iweb.pool;

import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * @author jxy
 * @date
 */
public class ConnectionPool  {
    private List<Connection> connections;
    static {
        try{
            Class.forName("com.mysql.jdbc.Driver");
        }catch (ClassNotFoundException e){
            e.printStackTrace();
        }
    }
    public ConnectionPool(int poolSize){
        connections = new ArrayList<>();
        for(int i=0;i<poolSize;i++){
            try{
                Connection connection = createConnection();
                connections.add(connection);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
    private static Connection createConnection() throws Exception{
        File f = new File("D:\\idea_work_space\\PersonalProject\\src\\" +
                "main\\java\\com\\iweb\\config.properties");
        Properties config = new Properties();
        config.load(new FileInputStream(f));
        String url = (String) config.get("url");
        String user_name = (String) config.get("userName");
        String password = (String) config.get("password");
        return DriverManager.getConnection(url,user_name,password);
    }
    public synchronized Connection getConnection(){

        while (connections.isEmpty()){
            try {
                wait();
            }catch (Exception e){

            }
        }
        Connection connection = connections.remove(0);
        return connection;
    }
    public synchronized void returnConnection(Connection connection){
        connections.add(connection);
        notifyAll();
    }

}
