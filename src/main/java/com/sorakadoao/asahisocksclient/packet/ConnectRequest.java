package com.sorakadoao.asahisocksclient.packet;

public class ConnectRequest extends RequestPacket{
    public byte[] address;
    public byte addressType;
    public byte[] port;
    public byte getPacketType(){
        return 0x01;
    }
    public ConnectRequest(byte addressType,byte[] address,byte[] port){
        this.address = address;
        this.addressType = addressType;
        this.port = port;
    }
    @Override
    public byte[] build() {
        return new byte[0];
    }
}
