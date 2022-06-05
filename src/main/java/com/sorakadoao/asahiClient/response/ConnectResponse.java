package com.sorakadoao.asahiClient.response;

import com.sorakadoao.asahiClient.Main;
import com.sorakadoao.asahiClient.MemoryStream;
import com.sorakadoao.asahiClient.Utils;
import com.sorakadoao.asahiClient.request.ConnectRequest;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;

public class ConnectResponse extends Response {
    byte status;
    public ConnectResponse(ResponseInfo responseInfo, ByteArrayInputStream byteArrayInputStream){
        super(responseInfo);
        //1:status
        status = (byte) byteArrayInputStream.read();

    }

    @Override
    public void resolve() {
        System.out.println(status);
        InetAddress a = InetAddress.getLoopbackAddress();
        MemoryStream memoryStream = new MemoryStream(64);
        memoryStream.write(5);
        memoryStream.write(status);
        memoryStream.write(0);
        memoryStream.write((a instanceof Inet4Address)?1:4);
        try {
            memoryStream.write(a.getAddress());
            Utils.write2ByteInt(memoryStream, Main.juc.port);
        } catch (IOException e) {
            e.printStackTrace();
        }
        responseInfo.connectionHandler.sendData(memoryStream.toByteArray());
    }
}
