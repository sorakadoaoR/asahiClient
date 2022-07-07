package com.sorakadoao.asahiClient.response;

import com.sorakadoao.asahiClient.Main;

import java.io.IOException;

public class ServerInputStreamClosedResponse extends Response{

    ServerInputStreamClosedResponse(ResponseInfo requestInfo) {
        super(requestInfo);
    }

    @Override
    public void resolve() {
        try {
            responseInfo.connectionHandler.bufferedInput.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
