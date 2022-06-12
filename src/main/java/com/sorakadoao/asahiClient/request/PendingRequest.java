package com.sorakadoao.asahiClient.request;

import com.sorakadoao.asahiClient.Main;
import org.bouncycastle.pqc.math.linearalgebra.ByteUtils;
import org.zz.gmhelper.SM4Util;

import java.io.ByteArrayInputStream;

/**
 * 已加入请求池的请求数据包
 */
public class PendingRequest {
    public Request request;
    ByteArrayInputStream requestStream;

    public  PendingRequest(Request request){
        this.request = request;
        requestStream = new ByteArrayInputStream(request.build());
    }

    /**
     * 构建最终发送的数据包
     * @return 包含头部和尾部垃圾数据的加密包
     */
    private byte[] buildPartialPacket(){
        byte[] ans = null;
        try {
            //TODO
            byte[] rubbish = Main.remoteSocket.generateRubbish(114514);
            int length = Main.guard.getNextRequestLength(request);
            byte[] encryptedContent = SM4Util.encrypt_ECB_Padding(Main.remoteSocket.sm4key, requestStream.readNBytes((length- rubbish.length)&0x7fffff00 ));
            byte[] encryptedHeader = SM4Util.encrypt_ECB_Padding(Main.remoteSocket.sm4key, request.requestInfo.buildRequestHeader(encryptedContent, rubbish.length, requestStream.available()==0));
            ans = new byte[32+ rubbish.length+ encryptedContent.length];
            System.arraycopy(encryptedHeader,0,ans,0,32);
            System.arraycopy(encryptedContent,0,ans,32,encryptedContent.length);
            System.arraycopy(rubbish,0,ans,32+encryptedContent.length,rubbish.length);
        }catch (Exception e){
            e.printStackTrace();
        }
        return ans;
    }

    /**
     * 将下一份数据包发送到服务端
     * @return 标志着包是否已经发完
     */
    public boolean send(){
        Main.remoteSocket.sendData(buildPartialPacket());
        return requestStream.available()==0;
    }
}
