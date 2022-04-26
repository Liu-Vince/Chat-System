package com.lwc.qqcommon;

import java.io.Serializable;

/**
 * @author 刘文长
 * @version 1.0
 * 表示客户端和服务器端通信时的消息对象
 */
public class Message implements Serializable {
    private static final long serialVersionUID = 1L;
    //发送者
    private String sender;
    //接收者
    private String getter;
    //消息内容
    private String content;
    //发送时间
    private String sendTime;
    //消息类型  在接口定义消息类型
    private String mesType;

    //进行扩展 和文件相关的成员
    private byte[] fileBytes;
    private int fileLen =  0;
    //文件传输到的位置
    private String dest;
    //源文件路径
    private String src;

    public byte[] getFileBytes() {
        return fileBytes;
    }

    public void setFileBytes(byte[] fileBytes) {
        this.fileBytes = fileBytes;
    }

    public int getFileLen() {
        return fileLen;
    }

    public void setFileLen(int fileLen) {
        this.fileLen = fileLen;
    }

    public String getDest() {
        return dest;
    }

    public void setDest(String dest) {
        this.dest = dest;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getGetter() {
        return getter;
    }

    public void setGetter(String getter) {
        this.getter = getter;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSendTime() {
        return sendTime;
    }

    public void setSendTime(String sendTime) {
        this.sendTime = sendTime;
    }

    public String getMesType() {
        return mesType;
    }

    public void setMesType(String mesType) {
        this.mesType = mesType;
    }
}
