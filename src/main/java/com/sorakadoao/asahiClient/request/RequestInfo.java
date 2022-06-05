package com.sorakadoao.asahiClient.request;

import com.sorakadoao.asahiClient.MemoryStream;
import com.sorakadoao.asahiClient.Utils;

import java.io.IOException;

public class RequestInfo {
    public Request request;
    public boolean isDataEnded;
    public int encryptedDataLength;
    public int rubbishLength;
    public int requestId;

    public RequestInfo(boolean isDataEnded,Request request) {
        this.request = request;
        this.isDataEnded = isDataEnded;
    }

    //returns a not encrypted header
    public byte[] buildRequestHeader(byte[] encryptedData,int rubbishLength) throws IOException{
        //
        //write the 16-byte request header
        //4:requestId 4:EncryptedDataLength 1:requestType 1:isDataCompleted 2:rubbishLength 4:RSV
        //
        MemoryStream headerStream = new MemoryStream(16);
        Utils.writeInt(headerStream,request.getRequestId());

        Utils.writeInt(headerStream,encryptedData.length);
        headerStream.write(request.getRequestType());
        headerStream.write(isDataEnded?1:0);
        Utils.write2ByteInt(headerStream,rubbishLength);
        return headerStream.toByteArray();
    }
}
