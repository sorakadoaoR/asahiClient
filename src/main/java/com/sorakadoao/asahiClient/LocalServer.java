package com.sorakadoao.asahiClient;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.HashMap;
import java.util.HashSet;

public class LocalServer implements Runnable{
    ServerSocket serverSocket;
    int idMost = 0;
    public HashMap<Integer,LocalConnectionHandler> connectionMap = new HashMap<>();
    public LocalServer(int port,int backlog)throws Exception{
        serverSocket = new ServerSocket(port,backlog);

    }

    @Override
    public void run() {
        while(true){
            try {
                LocalConnectionHandler s = (new LocalConnectionHandler(serverSocket.accept(),idMost));
                idMost++;
                Thread t = new Thread(s);
                s.thread = t;
                t.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
