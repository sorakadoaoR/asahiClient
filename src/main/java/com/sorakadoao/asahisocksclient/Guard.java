package com.sorakadoao.asahisocksclient;


import java.util.Iterator;
import java.util.LinkedList;
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
                Thread.sleep(100);
                Iterator<Map.Entry<Integer,LocalConnectionHandler>> it1 = Main.localServer.connectionMap.entrySet().iterator();
                while (it1.hasNext()){
                    if(it1.next().getValue().client.isClosed()) it1.remove();
                }

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
}
