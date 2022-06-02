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
    public static void main(String[] args) {
        Config.loadUserConfig();
        System.out.println(ByteUtils.toHexString(clientPrivateKey.getEncoded()));
        Thread localServerThread = new Thread(new RemoteSocket());
        localServerThread.start();
    }

}
