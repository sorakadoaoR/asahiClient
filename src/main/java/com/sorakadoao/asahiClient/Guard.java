package com.sorakadoao.asahiClient;


import com.sorakadoao.asahiClient.request.PendingRequest;
import com.sorakadoao.asahiClient.request.Request;

import java.util.Iterator;
import java.util.Map;

//speed tester, terminate dead connections
public class Guard implements Runnable{

    public static long totalData = 0;
    public static int speedLastSecond = 0;
    public static int speedHighest = 0;

    long lastTotalData = 0;
    int timeSinceLastSpeedTest = 0;

    @Override
    public void run() {
        try {
            while (true){
                Thread.sleep(2);
                //TODO
                Iterator<PendingRequest> it = Main.remoteSocket.requestHashSet.iterator();
                while(it.hasNext()){
                    PendingRequest pendingRequest= it.next();
                    //发送完了就开始等待服务器回复
                    if(pendingRequest.send()) {
                        it.remove();
                        Request.addToRequestMap(pendingRequest.request);
                    }
                }
                //Iterator<Map.Entry<Integer,LocalConnectionHandler>> it1 = Main.localServer.connectionMap.entrySet().iterator();
                //while (it1.hasNext()){
                //    if(it1.next().getValue().client.isClosed()) it1.remove();
                //}

            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    private void speedTest(){
        if(timeSinceLastSpeedTest<10) {
            timeSinceLastSpeedTest++;
            return;
        }
        timeSinceLastSpeedTest=0;
        speedLastSecond = (int)(totalData-lastTotalData);

    }

    public int getNextRequestLength(Request request){
        return 4096;
    }
}
