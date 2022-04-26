package com.lwc.qqserver.service;

import com.lwc.qqcommon.Message;
import com.lwc.qqcommon.MessageType;
import com.lwc.qqcommon.User;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author 刘文长
 * @version 1.0
 * 这是服务器，在监听9999，等待客户端的连接，并保持通信
 */
public class QQserver {

    private ServerSocket ss =null;

    //创建一个集合，存放多个注册用户
    //使用ConcurrentHashMap,可以处理并发的集合，是线程安全的,做了线程同步处理
//    private static HashMap<String,User> validUsers = new HashMap<>();
    private static ConcurrentHashMap<String,User> validUsers = new ConcurrentHashMap<>();
    //在静态代码块，初始化validUsers
    static {
        validUsers.put("100", new User("100", "123456"));
        validUsers.put("200", new User("200", "123456"));
        validUsers.put("300", new User("300", "123456"));
        validUsers.put("至尊宝", new User("至尊宝", "123456"));
        validUsers.put("紫霞仙子", new User("紫霞仙子", "123456"));
        validUsers.put("菩提老祖", new User("菩提老祖", "123456"));

    }


    //验证用户是否有效的方法
    private boolean checkUser(String userId, String password){
        User user = validUsers.get(userId);
        if (user == null){
            return false;
        }
        if (!user.getPassword().equals(password)){
            return false;
        }
        return true;
    }

    public QQserver() {
        try {
            System.out.println("服务器在9999端口监听...");
            ss = new ServerSocket(9999);
            //当和某个客户端连接后，会一直监听
            while (true) {
                Socket socket = ss.accept();
                //得到socket关联的对象输入流
                ObjectInputStream objectInputStream =
                        new ObjectInputStream(socket.getInputStream());
                ObjectOutputStream objectOutputStream =
                        new ObjectOutputStream(socket.getOutputStream());
//                Object o = objectInputStream.readObject();
                //读取客户端发送的User对象
                User u = (User) objectInputStream.readObject();
                //创建一个Message对象，准备回复客户端
                Message message = new Message();
                //验证
                if (checkUser(u.getUserId(), u.getPassword())){
                    //登录通过
                    message.setMesType(MessageType.MESSAGE_LOGIN_SUCCEED);
                    //将message对象回复客户端
                    objectOutputStream.writeObject(message);
                    //创建一个线程，和客户端保持通信，该线程需要持有socket对象
                    ServerConnectClientThread serverConnectClientThread = new ServerConnectClientThread(socket, u.getUserId());
                    //启动线程对象，
                    serverConnectClientThread.start();
                    //放入到一个集合中,进行管理
                    ManageClientThreads.addClientThread(u.getUserId(),serverConnectClientThread);
                } else {
                    //登录失败
                    System.out.println("用户 id=" +u.getUserId() +" pwd="+ u.getPassword() +" 验证失败");
                    message.setMesType(MessageType.MESSAGE_LOGIN_FAIL);
                    objectOutputStream.writeObject(message);
                    socket.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            //如果服务器退出了while，说明服务器不在监听，因此
            try {
                ss.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
