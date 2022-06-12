package com.sorakadoao.asahiClient.response;

import com.sorakadoao.asahiClient.LocalConnectionHandler;
import com.sorakadoao.asahiClient.Main;
import org.bouncycastle.pqc.math.linearalgebra.ByteUtils;
import org.zz.gmhelper.SM3Util;

import java.io.ByteArrayInputStream;
import java.util.HashMap;

//服务器发来的响应
public abstract class Response {
    public ResponseInfo responseInfo;
    protected LocalConnectionHandler localConnectionHandler;
    private static HashMap<Integer,IncompleteResponse> incompleteResponseMap = new HashMap<>();


    Response(ResponseInfo requestInfo){
        this.responseInfo = requestInfo;
        this.localConnectionHandler = responseInfo.connectionHandler;
    }

    /** method to resolve the request
     * 在服务器响应后会被调用
     */
    public abstract void resolve();



    public static Response analyzer(ResponseInfo requestInfo,byte[] decryptedData){
        //merge coming request with existing incomplete request
        IncompleteResponse incompleteResponse = incompleteResponseMap.get(requestInfo.requestId);
        if(!requestInfo.isDataEnded){
            System.out.println(requestInfo.requestId +" (Partial)Down: " +  ByteUtils.toHexString(SM3Util.hash(decryptedData)));
            if(incompleteResponse ==null) {
                incompleteResponse = new IncompleteResponse(requestInfo, decryptedData);
                incompleteResponseMap.put(requestInfo.requestId, incompleteResponse);
            }else{
                incompleteResponse.append(decryptedData);
            }
            return incompleteResponse;
        }else if(incompleteResponse != null){
            incompleteResponse.append(decryptedData);
            decryptedData = incompleteResponse.flush();
            incompleteResponseMap.remove(requestInfo.requestId);
            System.out.println(requestInfo.requestId +" (Complete)Down: " +  ByteUtils.toHexString(SM3Util.hash(decryptedData)));
        }else{
            System.out.println(requestInfo.requestId +" (Once)Down: " +  ByteUtils.toHexString(SM3Util.hash(decryptedData)));
        }


        Response request = switch (requestInfo.requestType) {
            case 1 -> new ConnectResponse(requestInfo, new ByteArrayInputStream(decryptedData));
            case 2 -> new TcpResponse(requestInfo, decryptedData);
            default -> null;
        };
        return request;
    }
}
