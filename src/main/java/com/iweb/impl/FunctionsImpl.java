package com.iweb.impl;

import com.iweb.Inter.Functions;
import com.iweb.dao.AddressDaoImpl;
import com.iweb.pojo.Address;
import com.iweb.pojo.Order;
import com.iweb.pojo.Product;
import com.iweb.pojo.User;
import com.iweb.pool.ConnectionPool;
import com.iweb.util.CommandCompare;
import com.iweb.util.ReviewCompare;
import com.iweb.util.SaleCompare;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author jxy
 * @date
 */
public class FunctionsImpl implements Functions {
    private static final int CORE_THREADS =20;
    private static final int CONNECTIONS = 20;
    private ConnectionPool connectionPool;
    private ThreadPoolExecutor threadPool;
    public FunctionsImpl(){
        connectionPool = new ConnectionPool(CONNECTIONS);
        threadPool = new ThreadPoolExecutor(CORE_THREADS,25,60, TimeUnit.SECONDS,
                new LinkedBlockingDeque<Runnable>());
    }
    @Override
    public List<Product> shoppingMethod(){
        Comparator<Product> comparator = getComparator();
        // 对商品进行分页显示
        HashMap<Integer,List<Product>> pagedProducts = commodityChoose(comparator);
        System.out.println("请输入你想查询的页码");
        Scanner sc = new Scanner(System.in);
        int pageIndex = sc.nextInt();
        displayProducts(pagedProducts.get(pageIndex));
        // 创建一个购物车并添加商品
        List<Product> cart = new ArrayList<>();
        System.out.println("请输入你想添加到购物车的商品所在显示行数");
        addToCart(cart,pagedProducts.get(pageIndex));
        return cart;

    }
    private Comparator<Product> getComparator(){
        Comparator<Product> comparator = null;
        // 按三种方法选择排序方式
        System.out.println("请选择排序方式：");
        System.out.println("1. 按销量排序");
        System.out.println("2. 按评价数排序");
        System.out.println("3. 系统推荐排序");
        Scanner sc= new Scanner(System.in);
        int choice = sc.nextInt();
        switch (choice){
            case 1:
                comparator = new SaleCompare();
                break;
            case 2:
                comparator = new ReviewCompare();
                break;
            case 3:
                comparator = new CommandCompare();
                break;
            default:
                System.out.println("无效选项！");
        }
        return comparator;
    }
    private HashMap<Integer, List<Product>> commodityChoose(Comparator<Product> comparator) {
        HashMap<Integer,List<Product>> sortProducts = new HashMap<>();
        int pageSize = 4;

        List<Product> products = getProducts();
        Collections.sort(products,comparator);
        int totalPage = (int) Math.ceil((double) products.size() / pageSize);
        for(int i =0;i<totalPage;i++){
            int begin = i*pageSize;
            int end = Math.min(begin+pageSize,products.size());
            List<Product> pagedProducts = products.subList(begin,end);
            sortProducts.put(i+1,pagedProducts);
        }

        return sortProducts;
    }
    private List<Product> getProducts(){
        List<Product> products = new ArrayList<>();
        String sql = "select * from product";
        try(Connection connection = connectionPool.getConnection();
            PreparedStatement ps =connection.prepareStatement(sql)){
            ps.execute();
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                int pid = rs.getInt("id");
                String pName = rs.getString("name");
                double originalPrice = rs.getDouble("originalprice");
                double promotePrice = rs.getDouble("promoteprice");
                Product product = new Product(pid,pName,originalPrice,promotePrice);
                products.add(product);
            }
            rs.close();
            ps.close();
        }catch (Exception e){

        }
        return products;
    }

    private void displayProducts(List<Product> products){
        for(Product product:products){
            System.out.println(products.indexOf(product)+"--"+product.getPid() + "--" + product.getPName()
                    + "--"+ product.getOriginalPrice() + "--" +product.getPromotePrice());
        }
    }
    private void addToCart(List<Product> cart,List<Product> products){
        System.out.println("请输入你想添加的商品");
        Scanner sc = new Scanner(System.in);
        if (sc.hasNextInt()) {
            int prodIndex = sc.nextInt();
            if (prodIndex >= 0 && prodIndex < products.size()) {
                Product product = products.get(prodIndex);
                cart.add(product);
            } else {
                System.out.println("无效的索引！");
            }
        } else {
            System.out.println("无效的输入！");
        }
    }
    @Override
    public void cartOperations(List<Product> cart) {
        System.out.println("请选择即将进行的操作：");
        System.out.println("1. 添加商品");
        System.out.println("2. 查看购物车");
        System.out.println("3. 移除商品");
        Scanner sc = new Scanner(System.in);
        int choice = sc.nextInt();
        switch (choice){
            case 1:
                Comparator<Product> comparator = getComparator();
                HashMap<Integer,List<Product>> pagedProducts = commodityChoose(comparator);
                System.out.println("请输入你想查询的页码");
                int pageIndex = sc.nextInt();
                displayProducts(pagedProducts.get(pageIndex));
                addToCart(cart,pagedProducts.get(pageIndex));
                break;
            case 2:
                displayCart(cart);
                break;
            case 3:
                System.out.println("请输入你想删除的商品id");
                int prodIndex = sc.nextInt();
                removeProduct(cart,prodIndex);
                break;
            default:
                System.out.println("选项有误！");
        }
    }
    private void removeProduct(List<Product> cart,int prodIndex){
        if (prodIndex >= 0 && prodIndex < cart.size()) {
            cart.remove(prodIndex);
            System.out.println("商品已成功移除");
        } else {
            System.out.println("无效的索引！");
        }
    }
    private void displayCart(List<Product> cart){
        if (cart.isEmpty()) {
            System.out.println("购物车为空");
        } else {
            System.out.println("购物车内容：");
            for (Product product : cart) {
                System.out.println(product.getPid() + "--" + product.getPName() + "--" + product.getPromotePrice());
            }
        }
    }
    @Override
    public Order cartSettlement(List<Product> cart) {
        return null;

    }

    @Override
    public void viewOrder(int orderId) {
        //.订单查看和订单关联的订单项数据显示,包括了 购买的商品名称 购买的商品数量 以及单价和总价
        String sql =
                "SELECT o.`order_id` AS order_id, u.username, a.province_addr, a.city_addr, a.detail_addr, \n" +
                        "       p.name AS product_name,p.id AS pid,od.quantity, p.promoteprice AS unit_price, \n" +
                        "       (od.quantity * p.promoteprice) AS total_price\n" +
                        "FROM `order` o\n" +
                        "JOIN order_detail od ON o.`order_id` = od.oid\n" +
                        "JOIN product p ON od.pid = p.id\n" +
                        "JOIN address a ON o.`user_id` = a.`uid`\n" +
                        "JOIN (\n" +
                        "  SELECT id, username\n" +
                        "  FROM `user`\n" +
                        ") u ON o.`user_id` = u.id\n" +
                        "WHERE o.`order_id` =?";
        try (
                Connection c = connectionPool.getConnection();
                PreparedStatement ps = c.prepareStatement(sql);
        ) {
            //在是第一个？处加入传入的参数orderId 根据订单编号查询指定订单的相关信息
            ps.setInt(1, orderId);
            //获取查询结果
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int oid = rs.getInt("order_id");
                String username = rs.getString("username");
                String provinceAddr = rs.getString("province_addr");
                String cityAddr = rs.getString("city_addr");
                String detailAddr = rs.getString("detail_addr");
                String productName = rs.getString("product_name");
                int quantity = rs.getInt("quantity");
                double unitPrice = rs.getDouble("unit_price");
                double totalPrice = rs.getDouble("total_price");

                // 输出订单项的相关信息
                System.out.print("订单编号: " + oid+"  |"+"\t");
                System.out.print("用户名: " + username+"   |"+"\t");
                System.out.print("用户地址: " + provinceAddr+cityAddr+ detailAddr+" |"+"\t");
                System.out.print("商品名称: " + productName+"   |"+"\t");
                productPropertyAndValue(rs.getInt("pid"));
                System.out.print("购买数量: " + quantity+"  |"+"\t");
                System.out.print("单价: " + unitPrice+"   |"+"\t");
                System.out.print("总价: " + totalPrice+"  |"+"\n");
                System.out.println("-----------------------------------------------------------------------------------------------------------------");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    //根据传入的订单id找到对应的订单
    //展示商品属性以及属性值
    private void productPropertyAndValue(int pid){
        String sql ="SELECT * FROM `property` pt \n" +
                "JOIN `propertyvalue` pv ON pt.`id`= pv.`ptid`\n" +
                "WHERE pid =?";
        try(
                Connection c = connectionPool.getConnection();
                PreparedStatement ps = c.prepareStatement(sql);
        ){
            ps.setInt(1,pid);
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                System.out.print("商品属性："+rs.getString("name")+"   |" + "\t");
                System.out.print("对应属性参数: " + rs.getString("value") + "   |" + "\t");
            }
        }catch (Exception e){

        }
    }
    @Override
    public int manageAddress() {
        AddressDaoImpl addressDao = new AddressDaoImpl();
        LinkedList<Address> addressList = (LinkedList<Address>) addressDao.listAll();
        Scanner sc = new Scanner(System.in);
        System.out.print("请选择地址ID:  ");
        String choose = sc.nextLine();
        int res = Integer.parseInt(choose);
        for (Address a: addressList) {
            if(a.getAddrId()==res){
                System.out.println("获取成功");
                return res;
            }
        }
        System.out.println("获取失败");
        return manageAddress();
    }

    public void shopCart(User user,List<Product> pd,int addressId)
    {
        Double money = 0.00;
        //根据传进来的购物车集合获得所有的价格
        for (int i = 0; i <pd.size() ; i++) {
            money=money+pd.get(i).getPromotePrice();
        }
        Double SurplusMoney =user.getMoney()-money;
        //判断用户余额是否够用，不够用回到选购物车阶段
        //够用，根据用户id修改用户余额，再生成一个order订单
        if (SurplusMoney<0)
        {
            System.out.println("你的余额不足");
            //用户登录成功界面
            return ;
        }
        else {
            user.setMoney(SurplusMoney);
            String sql = "update user set money=? where id = ?";
            try (
                    Connection c = connectionPool.getConnection();
                    PreparedStatement ps = c.prepareStatement(sql)
            ) {
                ps.setDouble(1, user.getMoney());
                ps.setInt(2, user.getUid());
                ps.execute();
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println("购物完成");
            //清空购物车
            //用户生成生成一个以manageAddress选择的地址id为order订单里的addressId,User的id为user_id,系统当前时间为order_data
            // ,固定"待发货"为order_value的order订单
            Date date = new Date(System.currentTimeMillis());
            String orderValue = "待发货";
            String str1 = "INSERT INTO `order` (user_id, address_id, order_data, order_status) VALUES (?, ?, ?, ?)";
            try (Connection c = connectionPool.getConnection();
                 PreparedStatement ps = c.prepareStatement(str1)) {
                ps.setInt(1, user.getUid());
                ps.setInt(2, addressId);
                ps.setDate(3, date);
                ps.setString(4, orderValue);
                ps.execute();
            } catch (Exception e) {
                e.printStackTrace();
            }


        }


    }

    @Override
    public void investMoney(User user, double recharge) {
        String sql = "update user set money=? where username=?";
        Connection c = connectionPool.getConnection();
        try {
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setDouble(1, user.getMoney() + recharge);
            ps.setString(2, user.getUserName());
            ps.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            connectionPool.returnConnection(c);
        }
    }

    @Override
    public void customerService(String host, int port) {

    }
}
