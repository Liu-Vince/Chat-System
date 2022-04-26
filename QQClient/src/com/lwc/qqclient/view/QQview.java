package com.lwc.qqclient.view;

import com.lwc.qqclient.service.FileClientService;
import com.lwc.qqclient.service.MessageClientService;
import com.lwc.qqclient.service.UserClientService;
import com.lwc.qqclient.utils.Utility;

/**
 * @author 刘文长
 * @version 1.0
 * 客户端的菜单界面
 */
public class QQview {    //控制是否显示菜单
    private boolean loop = true;
    //接收用户的键盘输入
    private String key = "";
    //该类用于登录服务/注册用户
    private UserClientService userClientService = new UserClientService();
    //用于聊天
    MessageClientService messageClientService = new MessageClientService();
    //用于文件传输
    private FileClientService fileClientService = new FileClientService();

    public static void main(String[] args) {
        new QQview().mainMenu();
        System.out.println("客户端退出系统......");
    }

    //显示菜单
    private void mainMenu() {
        while (loop) {
            System.out.println("==========欢迎登录网络通信系统==========");
            System.out.println("\t\t 1 登录系统");
            System.out.println("\t\t 9 退出系统");
            System.out.println("请输入你的选择：");
            key = Utility.readString(1);

            //根据用户的输入，来处理不同的逻辑
            switch (key) {
                case "1":
                    System.out.println("请输入用户名:");
                    String userId = Utility.readString(50);
                    System.out.println("请输入密码");
                    String pwd = Utility.readString(50);
                    //到服务器验证该用户是否合法
                    //这里有很多代码，我们这里编写一个列UserClientService[用户登录/注册]

                    if (userClientService.checkUser(userId,pwd)) {
                        System.out.println("==========欢迎 (用户 " + userId + " 登录成功)==========");
                        //进入到二级菜单
                        while (loop) {
                            System.out.println("\n==========网络通信系统二级菜单(用户 " + userId + ")==========");
                            System.out.println("\t\t 1 显示在线用户列表");
                            System.out.println("\t\t 2 群发消息");
                            System.out.println("\t\t 3 私聊消息");
                            System.out.println("\t\t 4 发送文件");
                            System.out.println("\t\t 9 退出系统");
                            System.out.println("请输入你的选择：");
                            key = Utility.readString(1);
                            switch (key) {
                                case "1":
                                    //显示在线用户列表
                                    userClientService.onlineFriendList();
                                    break;
                                case "2":
                                    System.out.println("请输入想对大家说的话");
                                    String s = Utility.readString(100);
                                    messageClientService.sendMessageToAll(s, userId);
                                    break;
                                case "3":
                                    System.out.println("请输入想聊天的用户名(在线)");
                                    String getterId = Utility.readString(50);
                                    System.out.println("请输入想说的话: ");
                                    String content = Utility.readString(1000);
                                    //编写一个方法，将消息发送给服务器
                                    messageClientService.sendMessageToOne(content,userId,getterId);
                                    break;
                                case "4":
                                    System.out.println("请输入你想把文件发送给的用户");
                                    getterId = Utility.readString(50);
                                    System.out.println("请输入发送文件的路径(形式 d:\\\\xx.jpg)");
                                    String src =  Utility.readString(100);
                                    System.out.println("请输入把文件发送到对方的路径(形式 d:\\\\yy.jpg)");
                                    String dest = Utility.readString(100);
                                    fileClientService.sendFileToOne(src,dest,userId,getterId);
                                    break;
                                case "9":
                                    //调用方法，给服务器发送一个退出系统的message
                                    userClientService.logout();
                                    loop = false;
                                    break;
                                default:
                                    break;
                            }
                        }
                    } else {//登录服务器失败
                        System.out.println("==========登录失败==========");
                    }
                    break;
                case "2":
                    loop = false;
                    break;
                default:
                    break;
            }
        }
    }
}
