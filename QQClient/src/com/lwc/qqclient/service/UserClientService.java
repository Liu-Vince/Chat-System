package com.lwc.qqclient.service;

import com.lwc.qqcommon.Message;
import com.lwc.qqcommon.MessageType;
import com.lwc.qqcommon.User;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

/**
 * @author 刘文长
 * @version 1.0
 * 该类完成用户登录验证和用户注册
 */
public class UserClientService {
    //可能在其它地方使用user信息，所有做成成员属性
    private User u = new User();
    private Socket socket;

    //根据userId 和 pwd 到服务器验证该用户是否合法
    public boolean checkUser(String userId, String pwd) {
        boolean b = false;
        //创建User对象
        u.setUserId(userId);
        u.setPassword(pwd);
        try {

            //Socket socket = new Socket(InetAddress.getByName("127.0.0.1"), 9999);
            socket = new Socket(InetAddress.getLocalHost(), 9999);
            //得到ObjectOutputStream对象
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            objectOutputStream.writeObject(u);

            //读取从服务器回复的Message对象
            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
            Message ms = (Message) objectInputStream.readObject();
            if(ms.getMesType().equals(MessageType.MESSAGE_LOGIN_SUCCEED)){

                //创建一个和服务器端保持通信的线程 -> 创建一个类 ClientConnectServerThread
                ClientConnectServerThread clientConnectServerThread = new ClientConnectServerThread(socket);
                //启动客户端的线程
                clientConnectServerThread.start();
                //这里为了后面客户端的扩展，我们将线程放入到集合管理
                ManageClientConnectServerThread.addClientConnectServerThread(userId,clientConnectServerThread);
                b = true;
            }else {
                //如果登录失败，就不能启动和服务器通信的线程，关闭socket
                socket.close();
            }
        } catch(IOException e){
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return b;
    }

    public void onlineFriendList(){
        //发送一个Message,类型MESSAGE_GET_ONLINE_FRIEND
        Message message = new Message();
        message.setMesType(MessageType.MESSAGE_GET_ONLINE_FRIEND);
        message.setSender(u.getUserId());
        try {
            //得到当前线程的Socket 对应的ObjectOutputStream对象
            //得到输出流
            ObjectOutputStream objectOutputStream = new ObjectOutputStream
                    (ManageClientConnectServerThread.getClientConnectServerThread
                            (u.getUserId()).getSocket().getOutputStream());
            //想服务的发送一个message
            objectOutputStream.writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //编写方法，退出客户端，并给服务端发送一个退出系统的message对象
    public void logout(){
        Message message = new Message();
        message.setMesType(MessageType.MESSAGE_CLIENT_EXIT);
        message.setSender(u.getUserId());
        //发送message
        try {
//            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            ObjectOutputStream objectOutputStream =
                    new ObjectOutputStream(ManageClientConnectServerThread.getClientConnectServerThread
                            (u.getUserId()).getSocket().getOutputStream());
            objectOutputStream.writeObject(message);
            System.out.println(u.getUserId() + " 退出系统 ");
            //结束进程
            System.exit(0);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
