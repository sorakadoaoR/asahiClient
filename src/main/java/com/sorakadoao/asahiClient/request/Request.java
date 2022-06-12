package com.sorakadoao.asahiClient.request;

import com.sorakadoao.asahiClient.LocalConnectionHandler;
import com.sorakadoao.asahiClient.Main;

import java.util.HashMap;


//发给服务器的请求
public abstract class Request {
    private static int packetIdMost = 0;
    public abstract byte getRequestType();
    private int requestId;
    public LocalConnectionHandler localConnectionHandler;
    public RequestInfo requestInfo;

    //等待服务器响应的请求
    private static HashMap<Integer, Request> pendingRequest = new HashMap<>();
    private static int getNextRequestId(){
        packetIdMost++;
        return  packetIdMost;
    }

    /**
     * 将请求加入到等待请求列表
     */
    public static void addToRequestMap(Request request){
        pendingRequest.put(request.requestId,request);
    }


    /** method to build the request
     * 不需要加密和包含请求头部
     * @return 未加密的数据字节数组
     */
    public abstract byte[] build();


    public Request(LocalConnectionHandler localConnectionHandler){
        this.requestId = getNextRequestId();
        this.localConnectionHandler = localConnectionHandler;
        this.requestInfo = new RequestInfo(this);
    }

    public int getRequestId(){
        return requestId;
    }

    public int getConnectionId(){
        return this.localConnectionHandler.id;
    }



    /**
     * 将数据包构建成PendingRequest并加入到待发送池中，由guard按照规则稍后发送
     */
    public final void send(){
        PendingRequest pendingRequest = new PendingRequest(this);
        synchronized (Main.remoteSocket.requestHashSet){
            Main.remoteSocket.requestHashSet.add(pendingRequest);
        }
    }
}
