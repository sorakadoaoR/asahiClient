package com.sorakadoao.asahiClient;

import com.sorakadoao.asahiClient.config.Config;
import com.sorakadoao.asahiClient.config.JsonUserConfig;
import org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPrivateKey;
import org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPublicKey;
import org.bouncycastle.pqc.math.linearalgebra.ByteUtils;
import org.zz.gmhelper.SM4Util;

import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

public class Main {
    public static JsonUserConfig juc;
    public static BCECPublicKey serverPublicKey;
    public static BCECPrivateKey clientPrivateKey;
    public static RemoteSocket remoteSocket;
    public static LocalServer localServer;


    public static void main(String[] args) {
        //try {
        //    byte[] a = {1,2,3,4,5,6,7,8,9,10,11,12};
        //    byte[] k = SM4Util.generateKey();
        //    byte[] b = SM4Util.encrypt_ECB_Padding(k,a);
        //    System.out.println(ByteUtils.toHexString(b)+" "+b.length);
        //} catch (Exception e){}

        Config.loadUserConfig();
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
    }

}
