package com.iweb.client;

import lombok.SneakyThrows;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Scanner;

/**
 * @author jxy
 * @date
 */
public class Client extends Thread{
    @SneakyThrows
    @Override
    public void run() {
        Socket s = new Socket("127.0.0.1",8888);
        //提供一个输出流 用于写入数据
        //写入的数据后续会被服务器端接受 服务器端是通过输入流读取的
        OutputStream os = s.getOutputStream();
        //为了方便写入字符 对os二次封装
        DataOutputStream dos = new DataOutputStream(os);
        InputStream is = s.getInputStream();
        DataInputStream dis = new DataInputStream(is);
        Scanner sc = new Scanner(System.in);
        while (true){
            String input =  sc.nextLine();
            dos.writeUTF(input);
            String output = dis.readUTF();
            System.out.println(output);
        }
    }


}