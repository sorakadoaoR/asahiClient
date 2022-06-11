package com.sorakadoao.asahiClient.request;

import com.sorakadoao.asahiClient.LocalConnectionHandler;
import com.sorakadoao.asahiClient.Main;
import com.sorakadoao.asahiClient.response.IncompleteResponse;
import com.sorakadoao.asahiClient.response.Response;
import com.sorakadoao.asahiClient.response.ResponseInfo;
import org.bouncycastle.pqc.math.linearalgebra.ByteUtils;
import org.zz.gmhelper.SM4Util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


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

    /**返回并等待响应的请求
     * 除了不完整的请求，其他时候shouldRemove应该为true
     * 设置为同步以防止多个请求同时到达
     * @return null或者找到的请求
     */
    public synchronized static Request getRequestByResponseInfo(ResponseInfo responseInfo){
        Request request;
        Iterator<Map.Entry<Integer,Request>> it = pendingRequest.entrySet().iterator();
        while(it.hasNext()){
            Map.Entry<Integer,Request> a = it.next();
            if(a.getKey()==responseInfo.requestId){
                request = a.getValue();
                if(responseInfo.isDataEnded) it.remove();
                return request;
            }
        }
        //return null if no request is found, which should not happen.
        return null;
    }

    /** method to build the request
     * 不需要加密和包含请求头部
     * @return 未加密的数据字节数组
     */
    public abstract byte[] build();

    /** method to resolve the request
     * 在服务器响应后会被调用
     * @param response 服务器对这个请求返回的响应
     */
    public abstract void resolve(Response response);
    public Request(LocalConnectionHandler localConnectionHandler){
        this.requestId = getNextRequestId();
        this.localConnectionHandler = localConnectionHandler;
        Main.remoteSocket.requestConnectionHandlerMap.put(requestId,localConnectionHandler);
        this.requestInfo = new RequestInfo(this);
    }

    public int getRequestId(){
        return requestId;
    }



    /**
     * 将数据包构建成PendingRequest并加入到待发送池中，由guard按照规则稍后发送
     */
    public final void send(){
        PendingRequest pendingRequest = new PendingRequest(this);
        Main.remoteSocket.requestHashSet.add(pendingRequest);
    }
}
