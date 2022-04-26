package com.lwc.qqserver.service;


import com.lwc.qqcommon.Message;
import com.lwc.qqcommon.MessageType;
import com.lwc.utils.Utility;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

/**
 * @author 刘文长
 * @version 1.0
 */
public class SendNewsToAllService implements Runnable {

    @Override
    public void run() {
        //推送多次，使用while(true)
        while (true) {
            System.out.println("请输入服务器要推送给用户的新闻/消息(输入exit退出推送服务)");
            String news = Utility.readString(100);
            if ("exit".equals(news)){
                break;
            }
            //构建一个消息，群发消息
            Message message = new Message();
            message.setSender("服务器");
            message.setContent(news);
            message.setMesType(MessageType.MESSAGE_TOALL_MES);
            message.setSendTime(new Date().toString());
            System.out.println("服务器推送消息给所有人 说：" + news);

            //遍历当前所有的通信线程，得到socket，并发送message
            HashMap<String, ServerConnectClientThread> hm = ManageClientThreads.getHm();
            Iterator<String> iterator = hm.keySet().iterator();
            while (iterator.hasNext()) {
                String onLineUserId = iterator.next().toString();
                try {
                    ObjectOutputStream objectOutputStream = new ObjectOutputStream(hm.get(onLineUserId).getSocket().getOutputStream());
                    objectOutputStream.writeObject(message);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

            }
        }
    }
}
