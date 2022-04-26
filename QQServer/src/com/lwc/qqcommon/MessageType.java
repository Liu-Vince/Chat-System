package com.lwc.qqcommon;

/**
 * @author 刘文长
 * @version 1.0
 */
public interface MessageType {
    //表示登录成功
    String MESSAGE_LOGIN_SUCCEED = "1";
    //表示登录失败
    String MESSAGE_LOGIN_FAIL = "2";
    //普通信息包
    String MESSAGE_COMM_MES = "3";
    //要求返回在线用户列表
    String MESSAGE_GET_ONLINE_FRIEND = "4";
    //返回在线用户列表
    String MESSAGE_RET_ONLINE_FRIEND = "5";
    //客户端请求退出
    String MESSAGE_CLIENT_EXIT = "6";
    //群发消息包
    String MESSAGE_TOALL_MES = "7";
    //文件消息
    String MESSAGE_FILE_MES = "8";
}
