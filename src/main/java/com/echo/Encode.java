package com.echo;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;

public class Encode {
    public static void sendMassage(Object in, OutputStream out) throws IOException {
        int payloadSize = in.toString().getBytes().length;
        byte[] data = new byte[10 + payloadSize];

        int headerSize = 10;
        intToByte(data,headerSize,0);

        if (String.valueOf(in).matches("-?\\d+")) {
            data[4] = (byte) 0;
            data[5] = (byte) 0;
        } else {
            data[4] = (byte) 0;
            data[5] = (byte) 1;
        }

        intToByte(data,payloadSize,6);

        System.arraycopy(in.toString().getBytes(), 0, data, headerSize, payloadSize);

        out.write(data);
    }
    public static String getUserInput() throws IOException {
        int userInputBufSize = 1024 * 1024;
        int stdBufferSize = 8;
        byte[] stdBuf;
        int stdSize;
        String userInput;
        byte[] userInputByte = new byte[userInputBufSize];
        int userInputIdx = 0;
        do {
            stdBuf = new byte[stdBufferSize];
            stdSize = System.in.read(stdBuf);
            System.arraycopy(stdBuf, 0, userInputByte, userInputIdx, stdSize);
            userInputIdx += stdSize;
        } while (stdBuf[stdSize - 1] != 10);
        userInput = new String(userInputByte, 0, userInputIdx - 1);
        return userInput;
    }
    public static void intToByte(byte[] data,int num,int startIdx){
        ByteBuffer buffer = ByteBuffer.allocate(4);
        buffer.putInt(num);
        byte[] tempBytes = buffer.array();
        System.arraycopy(tempBytes, 0, data, startIdx, tempBytes.length);
    }
}
