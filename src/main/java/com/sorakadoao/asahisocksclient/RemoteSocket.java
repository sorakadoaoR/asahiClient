package com.sorakadoao.asahisocksclient;

import org.bouncycastle.pqc.math.linearalgebra.ByteUtils;
import org.zz.gmhelper.SM2Util;

import java.io.*;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.TimeoutException;

public class RemoteSocket implements Runnable{

    Socket socket;
    InputStream input;
    OutputStream output;
    int lastSeen;
    byte[] sm4key;
    MemoryStream memoryStream = new MemoryStream(1024);
    private LinkedList<HandlerData> queuingData = new LinkedList<>();
    @Override
    public void run() {
        try {
            try {
                socket = new Socket(Main.juc.host, Main.juc.port);
                input = socket.getInputStream();
                output = socket.getOutputStream();
            } catch (IOException e) {
                e.printStackTrace();
                closeConnection();
                return;
            }
            if(!login()) return;
            getSM4Key();

        }catch (TimeoutException e){
            closeConnection();
            return;
        }
    }

    public boolean login() throws TimeoutException{
        //Authenticate:
        //
        //Authenticate Msg Packet
        // 16:uuid | 4: authenticate Msg length |?: authenticate Msg
        //
        //
        try {
            output.write(Utils.uuidToByteArray(Main.juc.uuid));
            byte[] passwordBytes = Main.juc.password.getBytes();
            byte[] userNameBytes = Main.juc.name.getBytes();
            memoryStream.writeInt(passwordBytes.length);
            memoryStream.writeInt(userNameBytes.length);
            memoryStream.write(passwordBytes);
            memoryStream.write(userNameBytes);
            byte[] authenticateMsg = SM2Util.encrypt(Main.serverPublicKey,memoryStream.getNativeByte());
            Utils.writeInt(output,authenticateMsg.length);
            memoryStream.refresh();
            output.write(authenticateMsg);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public boolean getSM4Key(){
        // get SM4 key
        //
        // 113: Encrypted SM4 Key
        //
        byte[] encsm4k = new byte[113];
        try {
            Utils.readByteFromInput(input,encsm4k,113);
            sm4key = SM2Util.decrypt(Main.clientPrivateKey,encsm4k);
            System.out.println(ByteUtils.toHexString(sm4key));
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }


    public void closeConnection(){
        try {
            socket.close();
            Thread.currentThread().stop();
        } catch (IOException ex) {
        }
    }

    public void addToQueue(HandlerData data){
        queuingData.add(data);
    }

}
