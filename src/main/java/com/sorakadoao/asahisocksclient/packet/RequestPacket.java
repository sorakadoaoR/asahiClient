package com.sorakadoao.asahisocksclient.packet;

import com.sorakadoao.asahisocksclient.Main;

public abstract class RequestPacket {
    public abstract byte getPacketType();
    public int packetId;
    public abstract byte[] build();
    protected RequestPacket(){
        this.packetId = Main.getNextPacketId();
    }

}
