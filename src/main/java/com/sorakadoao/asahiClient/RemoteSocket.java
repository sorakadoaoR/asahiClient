package com.sorakadoao.asahiClient;

import com.sorakadoao.asahiClient.request.Request;
import com.sorakadoao.asahiClient.response.Response;
import com.sorakadoao.asahiClient.response.ResponseInfo;
import org.zz.gmhelper.SM2Util;
import org.zz.gmhelper.SM4Util;

import java.io.*;
import java.net.Socket;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Random;
import java.util.concurrent.TimeoutException;

public class RemoteSocket implements Runnable{

    Socket socket;
    InputStream input;
    OutputStream output;
    int lastSeen;
    public byte[] sm4key = new byte[16];
    Random random;
    MemoryStream memoryStream = new MemoryStream(1024);
    public HashMap<Integer,LocalConnectionHandler> requestConnectionHandlerMap = new HashMap<>();
    public HashSet<Request> requestHashSet = new HashSet<>();
    public RemoteSocket(){
        random = new Random();
        Guard guard = new Guard();
        Thread thread = new Thread(guard);
        thread.start();
    }
    @Override
    public void run() {
        try {
            socket = new Socket(Main.juc.host, Main.juc.port);
            input = socket.getInputStream();
            output = socket.getOutputStream();

            if(!login() || !getSM4Key()) {
                System.out.println("Authentication Failed.");
                return;
            }
            System.out.println("Logged in.");
            while(true){
                System.out.println(114514);
                byte[] encryptedHeader= input.readNBytes(16);
                byte[] decryptedHeader = SM4Util.decrypt_ECB_Padding(sm4key,encryptedHeader);
                ResponseInfo responseInfo = new ResponseInfo(decryptedHeader);
                byte[] encryptedData = input.readNBytes(responseInfo.encryptedDataLength);
                byte[] decryptedData = SM4Util.decrypt_ECB_Padding(sm4key,encryptedData);
                Response response = Response.analyzer(responseInfo,decryptedData);
                response.resolve();
                input.skipNBytes(responseInfo.rubbishLength);
            }

        }catch (Exception e){
            if(!(e instanceof TimeoutException)) e.printStackTrace();
            closeConnection();
            return;
        }
    }

    public boolean login(){
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
            byte[] authenticateMsg = SM2Util.encrypt(Main.serverPublicKey,memoryStream.toByteArray());
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
        // 114: Encrypted SM4 Key & rubbish length
        //
        //decrypted:
        // 16: SM4key 1:rubbishLength
        //
        byte[] encsm4k = new byte[114];
        try {
            Utils.readByteFromInput(input,encsm4k,114);
            byte[] a= SM2Util.decrypt(Main.clientPrivateKey,encsm4k);
            System.arraycopy(a,0,sm4key,0,16);
            input.skipNBytes(a[16]&0xff);
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

    //send data to server
    public void sendData(byte[] data){
        try {
            output.write(data);
        } catch (IOException e) {
            e.printStackTrace();
            closeConnection();
        }
    }

    public byte[] generateRubbish(int encryptedDataLength){
        //TODO a batter rubbish generator should be made
        return  Utils.generateRandomBytes(random.nextInt(10,200),random);
    }
}
