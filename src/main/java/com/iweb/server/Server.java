package com.iweb.server;

import com.iweb.dao.ReplyDAO;
import com.iweb.dao.ReplyDAOImpl;
import com.iweb.pojo.Reply;
import lombok.SneakyThrows;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

/**
 * @author jxy
 * @date
 */
public class Server extends Thread {

    @SneakyThrows
    @Override
    public void run() {
        //如果不提供ip地址 默认使用本机的ip  也就是localhost
        //但是 服务器端的端口号需要由我们指定
        ServerSocket serverSocket = new ServerSocket(8888);
        System.out.println("人工小池为您服务:");
        //服务器端会一直阻塞在这个位置 等待客户端的接入
        //客户端在向服务器端发送请求的时候 会将客户端本身(Scoket对象)
        //发送过来
        Socket s = serverSocket.accept();
        //从客户端获取对应的输入流
        InputStream is = s.getInputStream();
        //将输入流进行二次封装 封装为数据流
        DataInputStream dis = new DataInputStream(is);
        //为了能够让服务器端向客户端发送消息 服务器端提供输出流
        OutputStream os = s.getOutputStream();
        DataOutputStream dos = new DataOutputStream(os);
        Scanner sc = new Scanner(System.in);
        ReplyDAO replyDAO = new ReplyDAOImpl();
        List<String> defaultReplies = new ArrayList<>();
        defaultReplies.add("听不懂");
        defaultReplies.add("你闭嘴");
        defaultReplies.add("我是你爹");
        defaultReplies.add("已经给你退货");
        defaultReplies.add("建议你百度，网址：www.baidu.com");
        defaultReplies.add("在的，亲");

        while(true)

        {
            String str = dis.readUTF();
            List<Reply> replies = replyDAO.list(str);
            if (replies == null) {
                Collections.shuffle(defaultReplies);
                dos.writeUTF(defaultReplies.get(0));
            } else {
                Collections.shuffle(replies);
                dos.writeUTF(replies.get(0).getResponse());
            }
        }
    }
}

