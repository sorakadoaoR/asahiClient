package com.sorakadoao.asahiClient.request;

import com.sorakadoao.asahiClient.LocalConnectionHandler;
import com.sorakadoao.asahiClient.MemoryStream;

import java.io.IOException;

public class ConnectRequest extends Request {
    public byte[] address;
    public byte addressType;
    public byte[] port;
    public byte getRequestType(){
        return 1;
    }
    public ConnectRequest(LocalConnectionHandler localConnectionHandler, byte addressType, byte[] address, byte[] port){
        super(true,localConnectionHandler);
        this.address = address;
        this.addressType = addressType;
        this.port = port;
    }
    @Override
    public byte[] build() {
        MemoryStream memoryStream = new MemoryStream(1024);
        //1:addressType ?:address 2:port
        memoryStream.write(addressType);
        byte[] ans = null;
        try {
            switch (addressType) {
                case 1, 4 ->//ipv4or6
                        memoryStream.write(address);
                case 3 -> {//domainName
                    memoryStream.write(address.length);
                    memoryStream.write(address);
                }
            }
            memoryStream.write(port);
            ans = memoryStream.toByteArray();
        }catch (IOException e){
            e.printStackTrace();
        }
        return ans;
    }
}
