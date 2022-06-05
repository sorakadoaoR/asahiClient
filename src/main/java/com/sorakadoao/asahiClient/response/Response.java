package com.sorakadoao.asahiClient.response;

import java.io.ByteArrayInputStream;
import java.util.HashMap;

public abstract class Response {
    public ResponseInfo responseInfo;
    private static HashMap<Integer,IncompleteResponse> incompleteResponseMap = new HashMap<>();

    Response(ResponseInfo requestInfo){
        this.responseInfo = requestInfo;
    }

    //method to resolve the request
    public abstract void resolve();

    public static Response analyzer(ResponseInfo requestInfo,byte[] decryptedData){
        //merge coming request with existing incomplete request
        IncompleteResponse incompleteResponse = incompleteResponseMap.get(requestInfo.requestId);
        if(!requestInfo.isDataEnded){

            if(incompleteResponse ==null) {
                incompleteResponse = new IncompleteResponse(requestInfo, decryptedData);
                incompleteResponseMap.put(requestInfo.requestId, incompleteResponse);
            }else{
                incompleteResponse.append(decryptedData);
            }
            return incompleteResponse;
        }else if(incompleteResponse != null){
            incompleteResponse.append(decryptedData);
            decryptedData = incompleteResponse.flush();
            incompleteResponseMap.remove(requestInfo.requestId);
        }


        Response request = switch (requestInfo.requestType) {
            case 1 -> new ConnectResponse(requestInfo, new ByteArrayInputStream(decryptedData));
            //case 2 -> new TcpResponse(requestInfo, decryptedData);
            default -> null;
        };
        return request;
    }
}
