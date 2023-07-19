package com.iweb.Inter;

import com.iweb.pojo.Order;
import com.iweb.pojo.Product;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

/**
 * @author jxy
 * @date
 */
public interface Functions {

    /**提供选项，让用户选择按照销量或者评价数或者系统推荐的方式定义比较器
     * 对商品数据进行排序之后查询并分页显示，选购商品后，将商品添加到购物车中
     * @return 返回一个一Integer类型的购物车编号作为key，Product类型的集合作为购物车value的hashmap
     */
    List<Product> shoppingMethod();

    /**输入一个购物车的编号id和一个选项对购物车进行增删改操作
     * @param cart 作为需要被进行操作的购物车传入
     */
    void cartOperations(List<Product> cart);


    /**通过传入一个Product的集合作为购物车，实现对购物车的结算
     * @param cart 作为一个Product类型的集合传入，
     * @return
     */
    Order cartSettlement(List<Product> cart);

    /** 实现输入订单编号对订单信息进行查看，详细显示订单的各项数据
     *  如果未找到数据则输出“订单信息不存在”
     * @param orderId 作为查找订单数据的索引传入
     */
    void viewOrder(int orderId);

    /** 实现对收获地址的管理，通过输入选项，按选项来对地址增删改
     * @param
     */
    int manageAddress();

    /** 实现对账户中的余额进行充值，使用SQL语句对用户信息中的money字段做修改
     * @param recharge 作为修改的资金数值传入
     */
    void investMoney(int recharge);

    /** 使用BIO和socket编程 实现与智能客服通信 方法实现中选择调用两个方法
     * 一个实现服务器端的ServerSocket通信，另一个实现客户端的Socket通信
     * @param host 作为客户端通信时的服务器地址
     * @param port 作为客户端与服务器端通信的端口号
     */
    void customerService(String host,int port);
}
