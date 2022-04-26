package com.lwc.qqclient.service;

import com.lwc.qqcommon.Message;
import com.lwc.qqcommon.MessageType;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

/**
 * @author 刘文长
 * @version 1.0
 * 完成文件传输
 */
public class FileClientService {
    public void sendFileToOne(String src,String dest, String senderId, String getterId){
        //读取src文件 封装到message
        Message message = new Message();
        message.setMesType(MessageType.MESSAGE_FILE_MES);
        message.setSender(senderId);
        message.setGetter(getterId);
        message.setSrc(src);
        message.setDest(dest);
        //需要将文件读取
        FileInputStream fileInputStream = null;
        byte[] fileBytes = new byte[(int)new File(src).length()];
        try {
            fileInputStream = new FileInputStream(src);
            fileInputStream.read(fileBytes);
            message.setFileBytes(fileBytes);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }finally {
            //关流
            if (fileInputStream !=null) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        System.out.println("\n"+senderId +" 给 "+getterId +"发送文件："+ src
        + "到对方的 "+ dest +" 目录");
        //发送
        try {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(ManageClientConnectServerThread.getClientConnectServerThread(senderId).getSocket().getOutputStream());
            objectOutputStream.writeObject(message);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
