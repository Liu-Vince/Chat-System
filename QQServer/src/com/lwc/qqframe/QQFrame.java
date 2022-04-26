package com.lwc.qqframe;

import com.lwc.qqserver.service.QQserver;

/**
 * @author 刘文长
 * @version 1.0
 * 该类创建一个QQServer，启动后台服务
 */
public class QQFrame {
    public static void main(String[] args) {
        new QQserver();
    }
}
