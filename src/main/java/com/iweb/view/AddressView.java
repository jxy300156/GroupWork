package com.iweb.view;

import com.iweb.dao.AddressDaoImpl;
import com.iweb.pojo.Address;
import com.iweb.pojo.User;

import java.util.LinkedList;
import java.util.Scanner;

/**
 * @author 79840
 *
 */
public class AddressView {
    AddressDaoImpl addressDao = new AddressDaoImpl();
    LinkedList<Address> addressList = (LinkedList<Address>) addressDao.listAll();

    public AddressView() {
        addressView();
    }
    /**
     * 选择地址方法
     * 1.输入一个地址的ID,如果此ID在地址表中存在，则返回此ID
     *
     */
    public int manageAddress() {
        Scanner sc = new Scanner(System.in);
        System.out.print("请选择地址ID: ");
        String choose = sc.nextLine();
        int res = Integer.parseInt(choose);
        for (Address a: addressList) {
            if(a.getAddrId()==res){
                System.out.println("获取成功 ");
                return res;
            }
        }
        System.out.println("获取失败");
        return manageAddress();
    }
    public void addressView(){
        Scanner sc = new Scanner(System.in);
        System.out.println("增加-1 删除-2 修改-3 查询-4 返回-5");
        System.out.print("请输入要对地址进行的操作:");
        String choose = sc.nextLine();
        switch (choose){
            case "1":
                addAddress();
                break;
            case "2":
                deleteAddress();
                break;
            case "3":
                updateAddress();
                break;
            case "4":
                flushed();
                break;
            case "5":
                addressView();
                break;
            default:
                System.out.println("输入错误");
                addressView();
        }
    }
    public void addAddress(){
        Scanner sc = new Scanner(System.in);
        System.out.print("请输入用户:");
        String uid=sc.nextLine();
        System.out.print("请输入省份:");
        String province_addr=sc.nextLine();
        System.out.print("请输入城市:");
        String city_addr =sc.nextLine();
        System.out.print("请输入详细地址:");
        String detail_addr=sc.nextLine();
        if(uid.equals("")||province_addr.equals("")||city_addr.equals("")||detail_addr.equals("")){
            addressView();
        }
        User u =new User();
        u.setUid(Integer.parseInt(uid));
        Address address = new Address(addressList.size()+1,u,province_addr,city_addr,detail_addr);
        addressDao.insert(address);
        addressView();
    }
    public void deleteAddress(){
        Scanner sc = new Scanner(System.in);
        System.out.print("请输入地址ID:");
        String rid=sc.nextLine();
        if(rid.equals("")){
            addressView();
        }
        int id=Integer.parseInt(rid);
        for (Address address: addressList) {
            if(address.getAddrId()==id){
                addressDao.delete(address);
            }
        }
        System.out.println("请重新输入");
        addressView();
    }
    public void updateAddress(){
        Scanner sc = new Scanner(System.in);
        System.out.print("请输入地址ID:");
        String rid=sc.nextLine();
        System.out.print("请输入用户:");
        String uid=sc.nextLine();
        System.out.print("请输入省份:");
        String province_addr=sc.nextLine();
        System.out.print("请输入城市:");
        String city_addr =sc.nextLine();
        System.out.print("请输入详细地址:");
        String detail_addr=sc.nextLine();
        if(rid.equals("")||uid.equals("")||province_addr.equals("")||city_addr.equals("")||detail_addr.equals("")){
            addressView();
        }
        User u =new User();
        u.setUid(Integer.parseInt(uid));
        Address address = new Address(Integer.parseInt(rid),u,province_addr,city_addr,detail_addr);
        addressDao.update(address);
        addressView();
    }
    public void flushed(){
        addressList = (LinkedList<Address>) addressDao.listAll();
        addressView();
    }
}
