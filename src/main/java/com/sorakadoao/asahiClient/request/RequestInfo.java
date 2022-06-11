package com.sorakadoao.asahiClient.request;

import com.sorakadoao.asahiClient.MemoryStream;
import com.sorakadoao.asahiClient.Utils;

import java.io.IOException;

public class RequestInfo {
    public Request request;
    public int encryptedDataLength;
    public int rubbishLength;
    public int requestId;

    public RequestInfo(Request request) {
        this.request = request;
    }

    //returns a not encrypted header
    public byte[] buildRequestHeader(byte[] encryptedData,int rubbishLength,boolean isDataEnded) throws IOException{
        //
        //write the 16-byte request header
        //4:requestId 4:EncryptedDataLength 1:requestType 1:isDataCompleted 2:rubbishLength 4:localConnectionId
        //
        MemoryStream headerStream = new MemoryStream(16);
        Utils.writeInt(headerStream,request.getRequestId());

        Utils.writeInt(headerStream,encryptedData.length);
        headerStream.write(request.getRequestType());
        headerStream.write(isDataEnded?1:0);
        Utils.writeInt(headerStream,request.localConnectionHandler.id);
        Utils.write2ByteInt(headerStream,rubbishLength);
        return headerStream.toByteArray();
    }
}
