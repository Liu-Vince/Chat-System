package com.lwc.qqserver.service;

import com.lwc.qqcommon.Message;
import com.lwc.qqcommon.MessageType;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Iterator;

/**
 * @author 刘文长
 * @version 1.0
 * 该类一个的对象和某个客户端保持通信
 */
public class ServerConnectClientThread extends Thread {
    private Socket socket;
    //连接到服务器的用户ID
    private String userId;

    public Socket getSocket() {
        return socket;
    }

    public ServerConnectClientThread(Socket socket, String userId) {
        this.socket = socket;
        this.userId = userId;
    }

    @Override
    public void run() {
        //这里线程处于run的状态，可以发送/接收消息
        while (true) {
            try {
                System.out.println("服务端和客户端" + userId + "保持通信，读取数据...");
                ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
                Message message = (Message) objectInputStream.readObject();
                if (message.getMesType().equals(MessageType.MESSAGE_GET_ONLINE_FRIEND)) {
                    //客户端拉取在线用户列表
                    System.out.println(message.getSender() + " 要在线用户列表");
                    String olineUser = ManageClientThreads.getOlineUser();
                    //返回messages
                    //构建一个message
                    Message message1 = new Message();
                    message1.setMesType(MessageType.MESSAGE_RET_ONLINE_FRIEND);
                    message1.setContent(olineUser);
                    message1.setGetter(message.getSender());
                    //返回给客户端
                    ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
                    objectOutputStream.writeObject(message1);

                } else if (message.getMesType().equals(MessageType.MESSAGE_CLIENT_EXIT)) {
                    System.out.println(message.getSender() + " 退出");
                    ManageClientThreads.removeServerConnectClientThread(message.getSender());
                    //关闭连接
                    socket.close();
                    //退出线程
                    break;
                } else if (message.getMesType().equals(MessageType.MESSAGE_COMM_MES)) {
                    //根据message获取getter id,然后在得到对应线程
                    ObjectOutputStream objectOutputStream =
                            new ObjectOutputStream(ManageClientThreads.
                                    getServerConnectClientThread(message.getGetter()).
                                    getSocket().getOutputStream());
                    objectOutputStream.writeObject(message);
                } else if (message.getMesType().equals(MessageType.MESSAGE_TOALL_MES)) {
                    //遍历管理线程的集合，把所有线程的socket得到，然后转发message
                    HashMap<String, ServerConnectClientThread> hm = ManageClientThreads.getHm();

                    Iterator<String> iterator = hm.keySet().iterator();
                    while (iterator.hasNext()) {
                        String onLineUserId = iterator.next();
                        if (!onLineUserId.equals(message.getSender())) {
                            ObjectOutputStream objectOutputStream = new ObjectOutputStream(ManageClientThreads.getServerConnectClientThread(onLineUserId).getSocket().getOutputStream());
                            objectOutputStream.writeObject(message);
                        }
                    }
                } else if (message.getMesType().equals(MessageType.MESSAGE_FILE_MES)) {
                    //根据getter id获取到对应的线程，将message对象转发
                    ObjectOutputStream objectOutputStream = new ObjectOutputStream(ManageClientThreads.getServerConnectClientThread(message.getSender()).getSocket().getOutputStream());
                    objectOutputStream.writeObject(message);

                } else {
                    System.out.println("其他类型的message,暂不处理");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
}
