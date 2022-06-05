package com.sorakadoao.asahiClient.request;

import com.sorakadoao.asahiClient.LocalConnectionHandler;
import com.sorakadoao.asahiClient.Main;
import org.bouncycastle.pqc.math.linearalgebra.ByteUtils;
import org.zz.gmhelper.SM4Util;

public abstract class Request {
    private static int packetIdMost = 0;
    public abstract byte getRequestType();
    private int requestId;
    public LocalConnectionHandler localConnectionHandler;
    public RequestInfo requestInfo;
    public abstract byte[] build();
    public Request(boolean isDataEnded, LocalConnectionHandler localConnectionHandler){
        this.requestId = getNextRequestId();
        this.localConnectionHandler = localConnectionHandler;
        Main.remoteSocket.requestConnectionHandlerMap.put(requestId,localConnectionHandler);
        this.requestInfo = new RequestInfo(isDataEnded,this);
    }
    private static int getNextRequestId(){
        packetIdMost++;
        return  packetIdMost;
    }
    public int getRequestId(){
        return requestId;
    }

    public byte[] buildEncryptedPacket(){
        byte[] ans = null;
        try {
            byte[] encryptedContent = SM4Util.encrypt_ECB_Padding(Main.remoteSocket.sm4key, build());
            byte[] rubbish = Main.remoteSocket.generateRubbish(encryptedContent.length + 16);
            byte[] encryptedHeader = SM4Util.encrypt_ECB_Padding(Main.remoteSocket.sm4key, requestInfo.buildRequestHeader(encryptedContent, rubbish.length));
            ans = new byte[16+ rubbish.length+ encryptedContent.length];
            System.arraycopy(encryptedHeader,0,ans,0,16);
            System.arraycopy(encryptedContent,0,ans,16,encryptedContent.length);
            System.arraycopy(rubbish,0,ans,16+encryptedContent.length,rubbish.length);
        }catch (Exception e){
            e.printStackTrace();
        }
        return ans;
    }


}
