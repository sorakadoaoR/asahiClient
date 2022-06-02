package com.sorakadoao.asahisocksclient;

import java.io.ByteArrayOutputStream;

public class MemoryStream extends ByteArrayOutputStream {
    public byte[] getNativeByte(){
        return buf;
    }
    public void writeInt(int i){
        write(i>>24);
        write((i<<8)>>24);
        write((i<<16)>>24);
        write((i<<24)>>24);
    }

    public void refresh(){
        reset();
        int size = buf.length;
        buf = new byte[size];
    }
    public MemoryStream(int size){
        super(size);
    }
    public MemoryStream(){
        super();
    }
}
