package com.lwc.qqclient.service;

import com.lwc.qqcommon.Message;
import com.lwc.qqcommon.MessageType;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Date;

/**
 * @author 刘文长
 * @version 1.0
 * 该类提供和消息相关的服务方法
 */
public class MessageClientService {

    /**
     *
     * @param content 内容
     * @param senderId 发送者
     */

    public void sendMessageToAll(String content, String senderId){
//构建messages
        Message message = new Message();
        //群发聊天消息
        message.setMesType(MessageType.MESSAGE_TOALL_MES);
        message.setSender(senderId);
        message.setContent(content);
        //发送时间
        message.setSendTime(new Date().toString());
        System.out.println("你对 所有人 说 " + content);
        //发送给服务器
        try {
            ObjectOutputStream objectOutputStream =
                    new ObjectOutputStream(ManageClientConnectServerThread.
                            getClientConnectServerThread(senderId).getSocket().getOutputStream());
            objectOutputStream.writeObject(message);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     *
     * @param content 内容
     * @param senderId 发送用户id
     * @param getterId 接收用户id
     */
    public void sendMessageToOne(String content, String senderId, String getterId){
        //构建messages
        Message message = new Message();
        //普通聊天消息
        message.setMesType(MessageType.MESSAGE_COMM_MES);
        message.setSender(senderId);
        message.setGetter(getterId);
        message.setContent(content);
        //发送时间
        message.setSendTime(new Date().toString());
        System.out.println("你对 " +getterId +" 说 " + content);
        //发送给服务器
        try {
            ObjectOutputStream objectOutputStream =
                    new ObjectOutputStream(ManageClientConnectServerThread.
                            getClientConnectServerThread(senderId).getSocket().getOutputStream());
            objectOutputStream.writeObject(message);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
