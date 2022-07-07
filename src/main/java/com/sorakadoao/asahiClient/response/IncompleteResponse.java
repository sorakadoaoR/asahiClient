package com.sorakadoao.asahiClient.response;

import java.util.LinkedList;

public class IncompleteResponse extends Response{
    LinkedList<byte[]> incompleteDecryptedData = new LinkedList<>();
    public IncompleteResponse(ResponseInfo ResponseInfo,byte[] data) {
        super(ResponseInfo);
        incompleteDecryptedData.add(data);
    }
    public void append(byte[] data){
        incompleteDecryptedData.add(data);
    }
    public byte[] flush(){
        int length = 0;
        for(byte[] bytes:incompleteDecryptedData){
            length += bytes.length;
        }
        byte[] ans = new byte[length];
        int nowIndex = 0;
        for(byte[] bytes:incompleteDecryptedData){
            System.arraycopy(bytes,0,ans,nowIndex,bytes.length);
            nowIndex+= bytes.length;
        }
        return ans;
    }

    public void resolve() {
        //Nothing to do since it's incomplete
    }
}
