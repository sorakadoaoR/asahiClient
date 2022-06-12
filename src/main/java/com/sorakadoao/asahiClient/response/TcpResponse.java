package com.sorakadoao.asahiClient.response;

import org.bouncycastle.pqc.math.linearalgebra.ByteUtils;
import org.zz.gmhelper.SM3Util;

public class TcpResponse extends Response{
    public byte[] data;
    TcpResponse(ResponseInfo requestInfo,byte[] decryptedData) {
        super(requestInfo);
        data = decryptedData;
    }
    @Override
    public void resolve() {
        System.out.println(responseInfo.requestId +" Down: " + ByteUtils.toHexString( SM3Util.hash(data)));
        localConnectionHandler.sendData(data);
    }
}
