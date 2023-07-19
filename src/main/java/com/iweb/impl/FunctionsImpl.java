package com.iweb.impl;

import com.iweb.Inter.Functions;
import com.iweb.dao.AddressDaoImpl;
import com.iweb.pojo.Address;
import com.iweb.pojo.Order;
import com.iweb.pojo.Product;
import com.iweb.pool.ConnectionPool;
import com.iweb.util.CommandCompare;
import com.iweb.util.ReviewCompare;
import com.iweb.util.SaleCompare;

import java.sql.Connection;
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
        products.sort(comparator);
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
            System.out.println(product.getPid() + "--" + product.getPName() + "--"+ product.getOriginalPrice()
                    + "--" +product.getPromotePrice());
        }
    }
    private void addToCart(List<Product> cart,List<Product> products){
        System.out.println("请输入你想添加的商品");
        Scanner sc = new Scanner(System.in);
        int prodIndex = sc.nextInt();
        Product product = products.get(prodIndex);
        cart.add(product);
    }
    @Override
    public void cartOperations(Integer cartId, int choice) {
        Comparator<Product> comparator = getComparator();
        List<Product> cart = shoppingMethod();
        System.out.println("请选择即将进行的操作：");
        System.out.println("1. 添加商品");
        System.out.println("2. 移除商品");
        System.out.println("3. 查看购物车");
        Scanner sc = new Scanner(System.in);
        switch (choice){
            case 1:
                HashMap<Integer,List<Product>> pagedProducts = commodityChoose(comparator);
                System.out.println("请输入你想查询的页码");
                int pageIndex = sc.nextInt();
                displayProducts(pagedProducts.get(pageIndex));
                addToCart(cart,pagedProducts.get(pageIndex));
            case 2:
                System.out.println("请输入你想删除的商品id");
                int prodIndex = sc.nextInt();
                removeProduct(cart,prodIndex);
            case 3:
                displayCart(cart);
            default:
                System.out.println("选项有误！");
        }
    }
    private void removeProduct(List<Product> cart,int prodIndex){
        Product product = cart.get(prodIndex);
        cart.remove(product);
    }
    private void displayCart(List<Product> cart){
        for(Product product:cart){
            System.out.println(product.getPid()+"--"+product.getPName()+"--"+product.getPromotePrice());
        }
    }
    @Override
    public List<Order> cartSettlement(List<Product> cart) {
        return null;
    }

    @Override
    public void viewOrder(int orderId) {

    }
    @Override
    public int manageAddress() {
        AddressDaoImpl addressDao = new AddressDaoImpl();
        LinkedList<Address> addressList = (LinkedList<Address>) addressDao.listAll();

        Scanner sc = new Scanner(System.in);
        System.out.print("请选择地址ID: ");
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

    @Override
    public void investMoney(int recharge) {

    }

    @Override
    public void customerService(String host, int port) {

    }
}
