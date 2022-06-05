package com.sorakadoao.asahiClient;

import org.bouncycastle.pqc.math.linearalgebra.ByteUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Locale;
import java.util.Random;
import java.util.concurrent.TimeoutException;

public class Utils {
    public static byte[] uuidToByteArray(String str){
        return ByteUtils.fromHexString(str.replace("-","").toLowerCase(Locale.ROOT));
    }

    public static void writeInt(OutputStream outputStream, int i) {
        try {
            outputStream.write(i>>24);
            outputStream.write((i<<8)>>24);
            outputStream.write((i<<16)>>24);
            outputStream.write((i<<24)>>24);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void write2ByteInt(OutputStream outputStream, int i)throws IOException{
            outputStream.write((i&0xff00)>>8);
            outputStream.write(i&0xff);
    }

    public static void readByteFromInput(InputStream inputStream, byte[] bytes, int length) throws IOException{
            int byteCount = 0;
            while(true){
                int nowByte = 0;
                nowByte = inputStream.read();
                if(nowByte==-1) continue;
                bytes[byteCount] = (byte)nowByte;
                byteCount++;
                if(byteCount>length-1) break;
            }
    }

    public static int readInt(InputStream inputStream) throws IOException{
        int ans = 0;
        byte[] bs = new byte[4];
        readByteFromInput(inputStream,bs,4);
        for(byte b:bs){
            ans<<=8;
            ans+=b;
        }
        return ans;
    }

    public static byte[] generateRandomBytes(int count, Random random){
        byte[] ans = new byte[count];
        random.nextBytes(ans);
        return ans;
    }

    public static int read2byteInt(InputStream inputStream) throws TimeoutException,IOException{
        return (inputStream.read()<<8) + inputStream.read();
    }
}
