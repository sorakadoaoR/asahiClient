package com.sorakadoao.asahiClient.request;

import com.sorakadoao.asahiClient.MemoryStream;
import com.sorakadoao.asahiClient.Utils;

import java.io.IOException;

public class RequestInfo {
    public Request request;

    public RequestInfo(Request request) {
        this.request = request;
    }

    //returns a not encrypted header
    public byte[] buildRequestHeader(byte[] encryptedData,int rubbishLength,boolean isDataEnded) throws IOException{
        //
        //write the 31-byte request header
        //4:requestId 4:EncryptedDataLength 1:requestType 1:isDataCompleted 2:rubbishLength 4:connectionId 15:RSV
        //
        MemoryStream headerStream = new MemoryStream(32);
        Utils.writeInt(headerStream,request.getRequestId());
        Utils.writeInt(headerStream,encryptedData.length);
        headerStream.write(request.getRequestType());
        headerStream.write(isDataEnded?1:0);
        Utils.write2ByteInt(headerStream,rubbishLength);
        Utils.writeInt(headerStream,request.getConnectionId());
        byte[] a = headerStream.toByteArray();
        return headerStream.toByteArray();
    }
}
