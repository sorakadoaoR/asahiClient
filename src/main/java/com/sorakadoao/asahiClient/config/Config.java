package com.sorakadoao.asahiClient.config;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.sorakadoao.asahiClient.Main;
import org.bouncycastle.pqc.math.linearalgebra.ByteUtils;
import org.zz.gmhelper.BCECUtil;

import java.io.FileReader;

public class Config {
    public static void loadUserConfig(){
        Gson gson = new Gson();
        try {
            JsonReader jr = gson.newJsonReader(new FileReader("client-config.json"));
            Main.juc= gson.fromJson(jr,JsonUserConfig.class);
            Main.serverPublicKey = BCECUtil.convertX509ToECPublicKey(ByteUtils.fromHexString(Main.juc.server_sm2_public_key));
            Main.clientPrivateKey = BCECUtil.convertPKCS8ToECPrivateKey(ByteUtils.fromHexString(Main.juc.sm2_private_key));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
