package com.sorakadoao.asahiClient;

import com.sorakadoao.asahiClient.request.ConnectRequest;
import com.sorakadoao.asahiClient.request.TcpRequest;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class LocalConnectionHandler implements Runnable{
    Socket client;
    InputStream input;
    OutputStream output;
    public long lastSeen;
    public int id;//same as requestId in server side
    public LocalConnectionHandler(Socket client,int id){
        this.client = client;
        this.id = id;
        System.out.println("New connection from "+client.getRemoteSocketAddress());

    }
    @Override
    public void run() {
        try {
            input = client.getInputStream();
            output = client.getOutputStream();

            if (input.read() != 5) {
                closeConnection();
                System.out.println("Invalid Protocol");
            }
            int nMethods = input.read();
            byte[] methods = new byte[nMethods];
            input.read(methods);
            boolean isNoRequired = false;
            for (byte b : methods) {
                if (b == 0) {
                    isNoRequired = true;
                    break;
                }
            }
            if (!isNoRequired) {
                output.write(new byte[]{5,-128});
                throw new Exception("Method not available");
            }

            output.write(new byte[]{5,0});
            System.out.println("Authenticate completed.");
            input.read();//version
            int cmd = input.read();
            input.read();//SRV
            int addressType = input.read();
            byte[] address = null;
            switch (addressType){
                case 1://ipv4
                    address = new byte[4];
                    input.read(address);
                    break;
                case 3://domainName
                    int length = input.read();
                    address = new byte[length];
                    input.read(address);
                    break;
                case 4://ipv6
                    address = new byte[16];
                    input.read(address);
                    break;
            }
            byte[] port = new byte[2];
            input.read(port);
            ConnectRequest connectRequest = new ConnectRequest(this,(byte) addressType,address,port);
            connectRequest.send();

            byte[] bytes;
            while(true){
                bytes= input.readAllBytes();

                if(bytes.length==1) {
                    System.out.println("Input stream ended.");
                    closeConnection();
                    return;
                }else{
                    System.out.println("Incoming message");
                }
                byte[] data = input.readAllBytes();
                TcpRequest tcpRequest = new TcpRequest(data,this);
                tcpRequest.send();
            }

        } catch (Exception e) {
            e.printStackTrace();
            closeConnection();
            return;
        }
    }


    //send data to user
    public void sendData(byte[] data){
        try {
            output.write(data);
        } catch (IOException e) {
            e.printStackTrace();
            closeConnection();
        }
    }

    public void closeConnection(){
        try {
            Main.localServer.connectionMap.remove(id);
            client.close();
            Thread.currentThread().stop();
        } catch (IOException ex) {
        }
    }
}
