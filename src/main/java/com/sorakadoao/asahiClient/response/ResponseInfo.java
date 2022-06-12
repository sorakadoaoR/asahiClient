package com.sorakadoao.asahiClient.response;

import com.sorakadoao.asahiClient.LocalConnectionHandler;
import com.sorakadoao.asahiClient.Main;
import com.sorakadoao.asahiClient.Utils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class ResponseInfo {
    public boolean isDataEnded;
    public byte requestType;
    public int encryptedDataLength;
    public int rubbishLength;
    public int requestId;
    LocalConnectionHandler connectionHandler;

    public ResponseInfo(byte[] decryptedHeader) throws IOException, TimeoutException {
        //
        //read the 32-byte response header
        //4:requestId 4:EncryptedDataLength 1:requestType 1:isDataCompleted 2:rubbishLength 4:responseId 15:RSV
        //
        ByteArrayInputStream headerStream = new ByteArrayInputStream(decryptedHeader);
        requestId = Utils.readInt(headerStream);
        encryptedDataLength = Utils.readInt(headerStream);
        requestType = (byte) headerStream.read();
        isDataEnded = headerStream.read()==1;
        rubbishLength = Utils.read2byteInt(headerStream);
        int connectionId = Utils.readInt(headerStream);
        connectionHandler = Main.localServer.connectionMap.get(connectionId);
    }
}
