package com.lwc.qqclient.service;

import java.util.HashMap;

/**
 * @author 刘文长
 * @version 1.0
 * 该类管理客户端连接到服务器的线程类
 */
public class ManageClientConnectServerThread {
    //我们把多个线程放入到一个HashMap集合,key就是用户id，value就是线程
    private static HashMap<String, ClientConnectServerThread> hm = new HashMap<>();
    //将某个线程加入到集合
    public static void addClientConnectServerThread(String userId, ClientConnectServerThread clientConnectServerThread){
        hm.put(userId, clientConnectServerThread);
    }
    //通过userId 可以得到对应线程
    public static ClientConnectServerThread getClientConnectServerThread(String userId){
        return hm.get(userId);
    }
}
