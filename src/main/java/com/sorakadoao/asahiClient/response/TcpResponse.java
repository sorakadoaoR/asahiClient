package com.sorakadoao.asahiClient.response;

public class TcpResponse extends Response{
    public byte[] data;
    TcpResponse(ResponseInfo requestInfo,byte[] decryptedData) {
        super(requestInfo);
        data = decryptedData;
    }
}
