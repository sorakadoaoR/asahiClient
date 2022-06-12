package com.sorakadoao.asahiClient.response;

import com.sorakadoao.asahiClient.Main;
import com.sorakadoao.asahiClient.MemoryStream;
import com.sorakadoao.asahiClient.Utils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;

public class ConnectResponse extends Response {
    public byte[] status;
    public ConnectResponse(ResponseInfo responseInfo, ByteArrayInputStream byteArrayInputStream){
        super(responseInfo);
        //1:status
        status = byteArrayInputStream.readAllBytes();

    }

    @Override
    public void resolve() {
        MemoryStream memoryStream = new MemoryStream(300);
        memoryStream.write(5);
        try {
            memoryStream.write(status);
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] a = memoryStream.toByteArray();
        localConnectionHandler.sendData(a);
    }
}
