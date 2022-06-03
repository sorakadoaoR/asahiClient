package com.sorakadoao.asahisocksclient;

import com.sorakadoao.asahisocksclient.config.Config;
import com.sorakadoao.asahisocksclient.config.JsonUserConfig;
import org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPrivateKey;
import org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPublicKey;
import org.bouncycastle.pqc.math.linearalgebra.ByteUtils;

public class Main {
    public static JsonUserConfig juc;
    public static BCECPublicKey serverPublicKey;
    public static BCECPrivateKey clientPrivateKey;
    public static RemoteSocket remoteSocket;
    public static LocalServer localServer;
    private static int packetId = 0;

    public static void main(String[] args) {
        Config.loadUserConfig();
        System.out.println(ByteUtils.toHexString(clientPrivateKey.getEncoded()));
        remoteSocket = new RemoteSocket();
        Thread remoteServerThread = new Thread(remoteSocket);
        remoteServerThread.start();
        Thread localServerThread = null;
        try {
            localServer = new LocalServer(juc.local_port,20);
            localServerThread = new Thread(localServer);
            localServerThread.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //SocksServer socksServer = new SocksServer();
        //socksServer.start(1716);
    }
    public static int getNextPacketId(){
        packetId++;
        return  packetId;
    }
}
