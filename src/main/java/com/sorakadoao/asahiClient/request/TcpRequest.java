package com.sorakadoao.asahiClient.request;

import com.sorakadoao.asahiClient.LocalConnectionHandler;
import com.sorakadoao.asahiClient.response.Response;
import com.sorakadoao.asahiClient.response.TcpResponse;

public class TcpRequest extends Request{
    byte[] data;

    public TcpRequest(byte[] data,LocalConnectionHandler localConnectionHandler) {
        super(localConnectionHandler);
        this.data = data;
    }

    @Override
    public byte getRequestType() {
        return 2;
    }

    @Override
    public byte[] build() {
        return data;
    }

    @Override
    public void resolve(Response response) {
        TcpResponse tcpResponse= (TcpResponse)response;
        localConnectionHandler.sendData(tcpResponse.data);
    }
}
