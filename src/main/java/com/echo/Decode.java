package com.echo;

import java.io.IOException;
import java.net.Socket;

import static java.lang.Math.min;

public class Decode {
    public static Object getPayload(Socket socket) throws IOException {
        int bufferSize = 15;
        //버퍼로 뜨기
        byte[] buf = new byte[bufferSize];
        int bufLen = socket.getInputStream().read(buf);
        //헤더사이즈 확인하기
        int headerSize = ((buf[0] & 0xFF) << 24) | ((buf[1] & 0xFF) << 16) | ((buf[2] & 0xFF) << 8) | (buf[3] & 0xFF);
        //타입 확인하기
        int bufType = buf[5];
        //페이로드 길이 확인
        int payloadSize = ((buf[6] & 0xFF) << 24) | ((buf[7] & 0xFF) << 16) | ((buf[8] & 0xFF) << 8) | (buf[9] & 0xFF);
        //페이로드 담기
        byte[] inBuf = new byte[payloadSize];
        System.arraycopy(buf, headerSize, inBuf, 0, min(bufLen - headerSize, payloadSize));
        //첫 버퍼에 다 못담은 페이로드 확인하기
        int inIdx = min(bufLen - headerSize, payloadSize);
        for (int i = 1; i < (headerSize + payloadSize) / (float) bufferSize; i++) {
            buf = new byte[bufferSize];
            bufLen = socket.getInputStream().read(buf);
            System.arraycopy(buf, 0, inBuf, inIdx, bufLen);
            inIdx += bufferSize;
        }
        Object in;
        if (bufType == 0) {
            in = Integer.parseInt(new String(inBuf));
        } else {
            in = new String(inBuf);
        }
        return in;
    }
}
