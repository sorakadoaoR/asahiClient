package com.sorakadoao.asahiClient;

public class HandlerData {
    public byte[] data;
    public int connectionId;
    public  HandlerData(byte[] data,int connectionId){
        this.connectionId = connectionId;
        this.data = data;
    }
}
