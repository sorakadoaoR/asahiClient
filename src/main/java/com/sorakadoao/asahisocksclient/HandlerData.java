package com.sorakadoao.asahisocksclient;

import org.bouncycastle.pqc.math.linearalgebra.ByteUtils;

public class HandlerData {
    public byte[] data;
    public int connectionId;
    public  HandlerData(byte[] data,int connectionId){
        this.connectionId = connectionId;
        this.data = data;
    }
}
