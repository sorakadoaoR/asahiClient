package com.sorakadoao.asahiClient.request;

import com.sorakadoao.asahiClient.LocalConnectionHandler;
import com.sorakadoao.asahiClient.response.Response;
import com.sorakadoao.asahiClient.response.TcpResponse;
import org.bouncycastle.pqc.math.linearalgebra.ByteUtils;
import org.zz.gmhelper.SM3Util;

public class TcpRequest extends Request{
    byte[] data;

    public TcpRequest(byte[] data,LocalConnectionHandler localConnectionHandler) {
        super(localConnectionHandler);
        this.data = data;
        System.out.println(localConnectionHandler.id +" Up: " +  ByteUtils.toHexString(SM3Util.hash(data)));
    }

    @Override
    public byte getRequestType() {
        return 2;
    }

    @Override
    public byte[] build() {
        return data;
    }


}
