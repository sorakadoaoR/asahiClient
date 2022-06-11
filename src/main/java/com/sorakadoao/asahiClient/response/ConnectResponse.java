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
    public byte status;
    public ConnectResponse(ResponseInfo responseInfo, ByteArrayInputStream byteArrayInputStream){
        super(responseInfo);
        //1:status
        status = (byte) byteArrayInputStream.read();

    }
}
