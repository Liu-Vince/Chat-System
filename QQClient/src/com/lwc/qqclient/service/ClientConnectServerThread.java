package com.lwc.qqclient.service;

import com.lwc.qqcommon.Message;
import com.lwc.qqcommon.MessageType;

import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.net.Socket;

/**
 * @author 刘文长
 * @version 1.0
 */
public class ClientConnectServerThread extends Thread{
    //该线程需要持有Socket
    private Socket socket;
    public ClientConnectServerThread(Socket socket){
        this.socket = socket;
    }

    @Override
    public void run() {
        //因为Thread需要在后台和服务器通信，所以我们while循环
//        super.run();
        while (true){
            try {
                System.out.println("客户端的线程，等待从服务器读取发送的信息");
                ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
                Message message =(Message) objectInputStream.readObject();
                //判断message类型
                if (message.getMesType().equals(MessageType.MESSAGE_RET_ONLINE_FRIEND)){
                    //取出在线列表信息，并显示
                    String[] onlineUsers  = message.getContent().split(" ");
                    System.out.println("\n==========当前在线用户列表==========");
                    for (int i = 0; i < onlineUsers.length; i++) {
                        System.out.println("用户：" + onlineUsers[i]);
                    }
                } else if (message.getMesType().equals(MessageType.MESSAGE_COMM_MES)) {
                    //把从服务器端转发的消息，显示到控制台
                    System.out.println("\n" + message.getSender() + " 对你说: " + message.getContent() +
                            "\t时间:"+message.getSendTime());
                } else if (message.getMesType().equals(MessageType.MESSAGE_TOALL_MES)){
                    //显示在客户端的控制台
                    System.out.println("\n" + message.getSender() + " 对大家说: " + message.getContent());
                } else if (message.getMesType().equals(MessageType.MESSAGE_FILE_MES)) {
                    System.out.println("\n" + message.getSender() +" 给你发文件： "+message.getSrc()+"到我的电脑的"+message.getDest());

                    FileOutputStream fileOutputStream = new FileOutputStream(message.getDest());
                    fileOutputStream.write(message.getFileBytes());
                    fileOutputStream.close();
                    System.out.println("\n 保存文件成功~");
                } else {
                    System.out.println("其它类型的message");
                }
            } catch (Exception e) {
                e.printStackTrace();
//                throw new RuntimeException(e);
            }
        }
    }



    //方便得到Socket
    public Socket getSocket() {
        return socket;
    }
}
